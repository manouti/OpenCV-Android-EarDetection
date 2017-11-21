package com.manouti.eardetection;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.manouti.eardetection.view.CameraView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final Scalar EAR_RECT_COLOR = new Scalar(0, 255, 0, 255);

    private CameraView mCameraView;
    private CascadeClassifier mEarDetector;
    // https://github.com/opencv/opencv/blob/master/samples/android/face-detection/src/org/opencv/samples/facedetect/DetectionBasedTracker.java
//    private EarDetector mNativeDetector;

    private BaseLoaderCallback mBaseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case SUCCESS:
                    Log.i(TAG, "OpenCV loaded successfully");

                    // Load native library after(!) OpenCV initialization
//                    System.loadLibrary("detection_based_tracker");

                    try {
                        // load cascade file from application resources
                        InputStream is = getResources().openRawResource(R.raw.left_2000_haar_24);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        File cascadeFile = new File(cascadeDir, "40_left_2000_haar_24.xml");
                        FileOutputStream os = new FileOutputStream(cascadeFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();

                        mEarDetector = new CascadeClassifier(cascadeFile.getAbsolutePath());
                        if (mEarDetector.empty()) {
                            Log.e(TAG, "Failed to load cascade classifier");
                            mEarDetector = null;
                        } else {
                            Log.i(TAG, "Loaded cascade classifier from " + cascadeFile.getAbsolutePath());
                        }

//                        mNativeDetector = new EarDetector(cascadeFile.getAbsolutePath(), 0);

                        if(!cascadeDir.delete()) {
                            Log.d(TAG, "Cascade file was not deleted.");
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
                    }

                    mCameraView.enableView();
                    mCameraView.turnOnTheFlash();
                    break;
                default:
                    super.onManagerConnected(status);
            }
        }
    };

    private Mat mRgba;
    private Mat mGray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCameraView = findViewById(R.id.java_camera_view);
        mCameraView.setVisibility(View.VISIBLE);
        mCameraView.setCvCameraViewListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mCameraView != null) {
            mCameraView.turnOffTheFlash();
            mCameraView.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCameraView != null) {
            mCameraView.turnOffTheFlash();
            mCameraView.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(OpenCVLoader.initDebug()) {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mBaseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        } else {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_3_0, this, mBaseLoaderCallback);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mGray = new Mat(height, width, CvType.CV_8UC1);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
        mGray.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();

        Mat mRgbaT = mRgba.t();
        Core.flip(mRgba.t(), mRgbaT, 1);
        Imgproc.resize(mRgbaT, mRgbaT, mRgba.size());

        mGray = inputFrame.gray();

        MatOfRect ears = new MatOfRect();

//        mNativeDetector.detect(mGray, ears);

        mEarDetector.detectMultiScale(mGray, ears, 1.2, 3, 2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                new Size(30, 30), new Size());

        Rect[] earsArray = ears.toArray();
        for (Rect ear : earsArray) {
            Imgproc.rectangle(mRgbaT, ear.tl(), ear.br(), EAR_RECT_COLOR, 3);
        }

        return mRgbaT;
    }
}

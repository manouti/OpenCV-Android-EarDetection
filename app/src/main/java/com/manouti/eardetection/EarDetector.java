package com.manouti.eardetection;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;

/**
 * Created by manouti on 11/21/2017.
 */

public class EarDetector {

//    EarDetector(String cascadeName, int minEarSize) {
//        mNativeObj = nativeCreateObject(cascadeName, minEarSize);
//    }
//
//    public void start() {
//        nativeStart(mNativeObj);
//    }
//
//    public void stop() {
//        nativeStop(mNativeObj);
//    }
//
//    public void setMinFaceSize(int size) {
//        nativeSetFaceSize(mNativeObj, size);
//    }
//
//    public void detect(Mat imageGray, MatOfRect ears) {
//        nativeEarDetect(mNativeObj, imageGray.getNativeObjAddr(), ears.getNativeObjAddr());
//    }
//
//    public void release() {
//        nativeDestroyObject(mNativeObj);
//        mNativeObj = 0;
//    }
//
//    private long mNativeObj = 0;
//
//    private static native long nativeCreateObject(String cascadeName, int minEarSize);
//    private static native void nativeDestroyObject(long thiz);
//    private static native void nativeStart(long thiz);
//    private static native void nativeStop(long thiz);
//    private static native void nativeSetFaceSize(long thiz, int size);
//    private static native void nativeEarDetect(long thiz, long inputImage, long faces);

    public static native void earDetection(long rgbaRef);

}

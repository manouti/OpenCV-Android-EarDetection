#include "com_manouti_eardetection_EarDetection.h"

JNIEXPORT void JNICALL Java_com_manouti_eardetection_EarDetection_earDetection
  (JNIEnv *, jclass, jlong rgbaRef) {
  Mat& frame = *(Mat*) rgbaRef;

  earDetect(frame);
  }

void earDetect(Mat& frame) {
  String left_ear_cascade_name = "40_left_2000_haar_24.xml";
  CascadeClassifier left_ear_cascade;

  if( !left_ear_cascade.load(left_ear_cascade_name )) {
     printf("--(!) Error loading\n");
     return;
  }

  std::vector<Rect> ears;
    Mat frame_gray;

    cvtColor( frame, frame_gray, CV_BGR2GRAY );
    equalizeHist( frame_gray, frame_gray );

    //-- Detect left ear
    left_ear_cascade.detectMultiScale( frame_gray, ears, 1.2, 3, 0|CV_HAAR_SCALE_IMAGE, Size(30, 30) );

    for( size_t i = 0; i < ears.size(); i++ )
    {
      Point center( ears[i].x + ears[i].width*0.5, ears[i].y + ears[i].height*0.5 );
      ellipse( frame, center, Size( ears[i].width*0.5, ears[i].height*0.5), 0, 0, 360, Scalar( 255, 0, 255 ), 4, 8, 0 );
    }
}
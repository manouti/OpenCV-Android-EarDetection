LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

#opencv
OPENCVROOT:= C:/Users/manouti/Downloads/OpenCV-android-sdk
OPENCV_CAMERA_MODULES:=on
OPENCV_INSTALL_MODULES:=on
OPENCV_LIB_TYPE:=SHARED
include ${OPENCVROOT}/sdk/native/jni/OpenCV.mk

LOCAL_SRC_FILES := com_manouti_eardetection_EarDetection.cpp

LOCAL_LDLIBS += -llog
LOCAL_MODULE := ear_detector


include $(BUILD_SHARED_LIBRARY)


	###OLD
#OPENCV_ANDROID_SDK=C:/Users/manouti/Downloads/OpenCV-android-sdk

##OPENCV_INSTALL_MODULES:=off
##OPENCV_LIB_TYPE:=SHARED
#ifdef OPENCV_ANDROID_SDK
#  ifneq ("","$(wildcard $(OPENCV_ANDROID_SDK)/OpenCV.mk)")
#    include ${OPENCV_ANDROID_SDK}/OpenCV.mk
#  else
#    include ${OPENCV_ANDROID_SDK}/sdk/native/jni/OpenCV.mk
#  endif
#else
#  include ../../sdk/native/jni/OpenCV.mk
#endif
#
#LOCAL_SRC_FILES  := com_manouti_eardetection_EarDetection.cpp
#LOCAL_C_INCLUDES += $(LOCAL_PATH)
#LOCAL_LDLIBS     += -llog -ldl
#
#LOCAL_MODULE     := ear_detector
#
#include $(BUILD_SHARED_LIBRARY)
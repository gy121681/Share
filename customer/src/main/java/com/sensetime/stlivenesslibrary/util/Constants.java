package com.sensetime.stlivenesslibrary.util;

import android.view.animation.Animation;

public class Constants {
	public static final int PREVIEW_WIDTH = 640;
	public static final int PREVIEW_HEIGHT = 480;
	public static final int DETECT_BLINK = 0;
	public static final int DETECT_NOTHING = 9;
	public static final String LOG_RESULT_JSON_EXCEPTION_STRING = "RESULT_LOG_RESULT_JSON_EXCEPTION";
	public static final String OS = "os";
	public static final String SYSVERSION = "sysversion";
	public static final String DEVICE = "device";
	public static final String UUID = "uuid";
	public static final String TIME = "time";
	public static final String AUTH = "auth";
	public static final String RECT = "rect";
	public static final String LOG = "log";
	public static final String DATA = "data";
	public static final String USERID = "userid";
	public static final String PLAN = "plan";
	public static final String SEQUENCE = "sequence";
	public static final String NUM = "num";
	public static final String OUTTYPE = "outType";
	public static final String BLINK = "BLINK";
	public static final String NOD = "NOD";
	public static final String MOUTH = "MOUTH";
	public static final String YAW = "YAW";
	public static final String ERROR = "error";
	public static final String RESULT = "result";
	public static final String INTERNALERROR = "INTERNALERROR";
	public static final String RESULT_OK = "OK";
	public static final String RESULT_CANCEL_BY_USER = "CANCEL_BY_USER";
	public static final String SDK_VERSION = "sdk_version";
	public static final String SYS_VERSION = "sys_version";
	public static final String USED = "used";
	public static final String TIMESTAMP = "timestamp";
	public static final String SCORE = "score";
	public static final String STATE = "state";
	public static final String POINT = "point";
	public static final int RELATIVE_TO_SELF = Animation.RELATIVE_TO_SELF;
	public static final int RELATIVE_TO_PARENT = Animation.RELATIVE_TO_PARENT;
	public static final int DEFAULT_ANIMATION_CONFIG = -1;

	public static final int LIVENESS_SUCCESS = 0x86243331;
	public static final int LIVENESS_TRACKING_MISSED = 0x86243332;
	public static final int LIVENESS_TIME_OUT = 0x86243333;
	public static final int TRACK_FACE_OUTOF_BOUND = 0x86243334;
	public static final int TRACK_FACE_TOO_CLOSE = 0x86243335;
	public static final int TRACK_FACE_TOO_FAR = 0x86243336;
	public static final int TRACK_PASSED = 0x86243337;
	public static final int DETECT_FACE_OUTOF_BOUND = 0x86243338;
	public static final int DETECT_FACE_TOO_CLOSE = 0x86243339;
	public static final int DETECT_FACE_TOO_FAR = 0x86243340;
	public static final int TRACK_PREPARE_START = 0x86243341;
	public static final int FIRST_LIVENESS_TRACKING_MISSED = 0x86243342;
}

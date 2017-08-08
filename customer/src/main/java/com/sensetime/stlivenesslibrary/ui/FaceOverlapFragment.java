package com.sensetime.stlivenesslibrary.ui;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.sensetime.stlivenesslibrary.LivenessDetector;
import com.sensetime.stlivenesslibrary.LivenessDetector.DetectStatus;
import com.sensetime.stlivenesslibrary.LivenessDetector.Motion;
import com.sensetime.stlivenesslibrary.LivenessDetector.Status;
import com.sensetime.stlivenesslibrary.STFinanceJNI;
import com.sensetime.stlivenesslibrary.STFinanceJNI.CVFinanceFrame;
import com.sensetime.stlivenesslibrary.Utils;
import com.sensetime.stlivenesslibrary.util.Constants;
import com.sensetime.stlivenesslibrary.util.DataController;
import com.sensetime.stlivenesslibrary.util.LivenessUtils;

/**
 * 
 * @author MatrixCV
 * 
 *         实时人脸检测接口调用示例
 * 
 */
public class FaceOverlapFragment extends CameraOverlapFragment {
	private static final String TAG = "FaceOverlapFragment";
	private static final boolean DEBUG_PREVIEW = false;
	private OnLivenessCallBack mListener;
	private boolean mIsKilled = false;
	public boolean mPaused = true;
	private boolean mNV21DataIsReady = false;
	private byte mNv21[];
	private byte[] mTmp;
	private Motion[] mDetectList;
	private boolean mLiveResult[];
	private int mStatus = 0;
	public LivenessDetector mDetector = null;
	private Status status = null;
	private long mStartTime;
	private int mFrameCount = 0;
	Bundle bundle = null;
	private final static int mSystemRootStateUnknow = -1;
	private final static int mSystemRootStateDisable = 0;
	private final static int mSystemRootStateEnable = 1;
	private static int systemRootState = mSystemRootStateUnknow;
	public boolean createHandleSuccess = false;
	public boolean pauseDetect = true;
	private int livenessConfig;
	private int outputType;
	private int complexity;
	private ExecutorService executor;
	private boolean trackDone = false;
	Rect rect;
	private int screenWidth;
	private int screenHeight;
	private float leftRate = 0.15f;
	private float topRate = 0.15f;
	private float rightRate = 0.85f;
	private float bottomRate = 0.85f;
	private static final int trackConfig = STFinanceJNI.WRAPPER_COMPLEXITY_EASY
			| STFinanceJNI.WRAPPER_OUTPUT_TYPE_SINGLE_IMAGE;
	boolean firstPassedStatus = false;
	long firstTrackPassedTime = 0l;
	private Motion[] mTrackList;
	private boolean trackerStartSuccess = false;
	private boolean detectorStartSuccess = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		mDetectList = DataController.setDetectActionOrder(getActivity().getIntent().getStringExtra(
						LivenessActivity.EXTRA_MOTION_SEQUENCE));
		if (mDetectList.length > 0) {
			mLiveResult = new boolean[mDetectList.length];
			for (int i = 0; i < mDetectList.length; i++) {
				mLiveResult[i] = false;
			}
		}
		if (getActivity().getIntent().getStringExtra(LivenessActivity.COMPLEXITY).equalsIgnoreCase("easy")) {
			complexity = STFinanceJNI.WRAPPER_COMPLEXITY_EASY;
		} else if (getActivity().getIntent().getStringExtra(LivenessActivity.COMPLEXITY).equalsIgnoreCase("normal")) {
			complexity = STFinanceJNI.WRAPPER_COMPLEXITY_NORMAL;
		} else if (getActivity().getIntent().getStringExtra(LivenessActivity.COMPLEXITY).equalsIgnoreCase("hard")) {
			complexity = STFinanceJNI.WRAPPER_COMPLEXITY_HARD;
		} else if (getActivity().getIntent().getStringExtra(LivenessActivity.COMPLEXITY).equalsIgnoreCase("hell")) {
			complexity = STFinanceJNI.WRAPPER_COMPLEXITY_HELL;
		} else {
			complexity = STFinanceJNI.WRAPPER_COMPLEXITY_NORMAL;
		}
		if (getActivity().getIntent().getStringExtra(LivenessActivity.OUTPUT_TYPE).equalsIgnoreCase("singleImg")) {
			outputType = STFinanceJNI.WRAPPER_OUTPUT_TYPE_SINGLE_IMAGE;
		} else if (getActivity().getIntent().getStringExtra(LivenessActivity.OUTPUT_TYPE).equalsIgnoreCase("multiImg")) {
			outputType = STFinanceJNI.WRAPPER_OUTPUT_TYPE_MULTI_IMAGE;
		} else {
			outputType = STFinanceJNI.WRAPPER_OUTPUT_TYPE_MULTI_IMAGE;
		}
		livenessConfig = outputType | STFinanceJNI.WRAPPER_LOG_LEVEL_ONLY_EXTERN | complexity;
		initStateAndPreviewCallBack();
		screenWidth = Utils.getScreenWidth(getActivity());
		screenHeight = Utils.getScreenHeight(getActivity());
		rect = new Rect();
		rect.top = (int) (screenHeight * leftRate);
		rect.left = (int) (screenWidth * topRate);
		rect.right = (int) (screenWidth * rightRate);
		rect.bottom = (int) (screenHeight * bottomRate);
		status = new Status();
		mTrackList = new Motion[1];
		mTrackList[0] = Motion.BLINK;
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		mIsKilled = false;
		if (executor == null) {
			executor = Executors.newSingleThreadExecutor();
		}
		executor.execute(new Runnable() {
			@Override
			public void run() {
				while (!mIsKilled) {
					try {
						Thread.sleep(2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (!mNV21DataIsReady || mPaused) {
						continue;
					}
					synchronized (mNv21) {
						if (mNv21 != null && mTmp != null
								&& mTmp.length >= mNv21.length) {
							System.arraycopy(mNv21, 0, mTmp, 0, mNv21.length);
						}
						mNV21DataIsReady = false;
					}
					if (!mPaused) {
						if (!trackDone) {
							startTrack();
						}
						if (trackDone) {
							startLivenessIfNeed();
							if (pauseDetect) {
								trackFaces();
							}
						}
					}
				}
			}
		});
	}
	private void startTrack() {
		if(mDetector == null) {
			mDetector = new LivenessDetector(getActivity());
			if(mDetector.createHandle()){
				trackerStartSuccess = mDetector.start(trackConfig);
			} else {
				onErrorHappen(LivenessActivity.RESULT_CREATE_HANDLE_ERROR);
			}
		}
		long currentTime = System.currentTimeMillis();
		if(trackerStartSuccess) {
			status = mDetector.detect(mTmp, Constants.PREVIEW_WIDTH, Constants.PREVIEW_HEIGHT, mCameraInfo.orientation, Motion.BLINK, rect);
		}
		if(status.getTrackStatus() == LivenessDetector.TrackStatus.FACE_OUTOF_BOUND.getValue()) {
			firstPassedStatus = false;
			mListener.onLivenessDetect(Constants.TRACK_FACE_OUTOF_BOUND, -1);
		} else if(status.getTrackStatus() == LivenessDetector.TrackStatus.FACE_TOO_CLOSE.getValue()) {
			firstPassedStatus = false;
			mListener.onLivenessDetect(Constants.TRACK_FACE_TOO_CLOSE, -1);
		} else if(status.getTrackStatus() == LivenessDetector.TrackStatus.FACE_TOO_FAR.getValue()) {
			firstPassedStatus = false;
			mListener.onLivenessDetect(Constants.TRACK_FACE_TOO_FAR, -1);
		} else if(status.getTrackStatus() == LivenessDetector.TrackStatus.PASSED.getValue()) {
			if(!firstPassedStatus) {
				firstPassedStatus = true;
				firstTrackPassedTime = System.currentTimeMillis();
				mListener.onLivenessDetect(Constants.TRACK_PREPARE_START, -1);
			}
			if((currentTime - firstTrackPassedTime) < 1000) {
				mListener.onLivenessDetect(Constants.TRACK_PREPARE_START, -1);
			} else {
				trackDone = true;
				mListener.onLivenessDetect(Constants.TRACK_PASSED, -1);
				mDetector.end();
				detectorStartSuccess = false;
			}
		}
	}

	private void startAndSetStaticInfo() {
		if (mDetector != null) {
			createHandleSuccess = true;
			if (mDetector != null) {
				detectorStartSuccess = mDetector.start(livenessConfig);
				if (createHandleSuccess) {
					setWrapperStaticInfo();
				}
			}
		}
	}

	private void trackFaces() {
		if (null == mDetector) {
			startLivenessIfNeed();
		}
		if(null != mDetector) {
			if(!detectorStartSuccess) {
				startAndSetStaticInfo();
			}
		}
		if (mDetector != null) {
			try {
				if (mStatus < mDetectList.length) {
					if (detectorStartSuccess) {
						status = mDetector.detect(mTmp,
								Constants.PREVIEW_WIDTH,
								Constants.PREVIEW_HEIGHT,
								mCameraInfo.orientation, mDetectList[mStatus], rect);
					}
				}
			} catch (Exception e) {
				status.setDetectStatus(DetectStatus.INTERNAL_ERROR.getValue());
				e.printStackTrace();
			}
		}
		if (mStatus < mDetectList.length) {
			if (status.getDetectStatus() == DetectStatus.TRACKING_MISSED
					.getValue()) {
				if (mStatus > 0) {
					if (null != mDetector) {
						mDetector.end();
						pauseDetect = false;
						boolean saveFileSuccess = LivenessUtils.saveFile(getLivenessResult(), LivenessActivity.EXTRA_RESULT_PATH + LivenessActivity.LIVENESS_FILE_NAME);
						if(!saveFileSuccess) {
							onErrorHappen(getActivity().RESULT_FIRST_USER);
						}
						if (null != mListener) {
							mListener
									.onLivenessDetect(
											Constants.LIVENESS_TRACKING_MISSED,
											mStatus);
						}
						mDetector.destroy();
						try {
							mDetector.finalize();
						} catch (Throwable e) {
							e.printStackTrace();
						}
						mDetector = null;
					}
				} else {
					resetStatus(true);
					pauseDetect = true;
					restartTrack();
				}
			}
		}
		if (status.getDetectStatus() == DetectStatus.PASSED.getValue()
				 && status.isPassed()) {
			if (mStatus < mDetectList.length) {
				mLiveResult[mStatus] = true;
				mStatus++;
				mDetector.detect(mTmp,
						Constants.PREVIEW_WIDTH,
						Constants.PREVIEW_HEIGHT,
						mCameraInfo.orientation, Motion.NONE, rect);
				if (mStatus == mDetectList.length) {
					mPaused = true;
					synchronized (this) {
						if (null != mDetector) {
							mDetector.end();
						}
					}
					if (null != mListener) {
						mListener.onLivenessDetect(
								Constants.LIVENESS_SUCCESS, mStatus);
					}
				} else {
					restartDetect(true);
				}
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		releaseLiveness();
		stopDetectThread();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	public byte[] getLivenessResult() {
		try {
			return mDetector.getLivenessResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public CVFinanceFrame[] getImageResult() {
		try {
			return mDetector.getImageResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * Return whether the system is root or not.
	 */
	public static boolean isRootSystem() {
		systemRootState = mSystemRootStateDisable;
		File f = null;
		final String kSuSearchPaths[] = { "/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/" };
		try {
			for (int i = 0; i < kSuSearchPaths.length; i++) {
				f = new File(kSuSearchPaths[i] + "su");
				if (f != null && f.exists()) {
					systemRootState = mSystemRootStateEnable;
				}
			}
		} catch (Exception e) {
		}
		if (systemRootState == mSystemRootStateEnable) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * set the WrapperStaticInfo here.
	 */
	public void setWrapperStaticInfo() {
		try {
			mDetector.setStaticInfo(LivenessDetector.WrapperStaticInfo.DEVICE.getValue(), android.os.Build.MODEL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			mDetector.setStaticInfo(LivenessDetector.WrapperStaticInfo.OS.getValue(), "Android");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			mDetector.setStaticInfo(LivenessDetector.WrapperStaticInfo.SDK_VERSION.getValue(),
							LivenessDetector.getSDKVersion());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			mDetector.setStaticInfo(LivenessDetector.WrapperStaticInfo.SYS_VERSION.getValue(),
							android.os.Build.VERSION.RELEASE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			mDetector.setStaticInfo(LivenessDetector.WrapperStaticInfo.ROOT.getValue(), String.valueOf(isRootSystem()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			mDetector.setStaticInfo(LivenessDetector.WrapperStaticInfo.CUSTOMER.getValue(),  getActivity().getApplicationContext().getPackageName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initStateAndPreviewCallBack() {
		mStatus = 0;
		mNv21 = new byte[Constants.PREVIEW_WIDTH * Constants.PREVIEW_HEIGHT * 3 / 2];
		mTmp = new byte[Constants.PREVIEW_WIDTH * Constants.PREVIEW_HEIGHT * 3 / 2];
		this.setPreviewCallback(new PreviewCallback() {
			@Override
			public void onPreviewFrame(byte[] data, Camera camera) {
				if (DEBUG_PREVIEW) {
					debugFps();
				}
				if (!mPaused) {
					synchronized (mNv21) {
						if (data != null && mNv21 != null && mNv21.length >= data.length) {
							System.arraycopy(data, 0, mNv21, 0, data.length);
							mNV21DataIsReady = true;
						}
					}
				}
			}
		});
	}

	public void startLivenessIfNeed() {
		if (mDetector == null) {
			try {
				mDetector = new LivenessDetector(getActivity());
				if (mDetector.createHandle()) {
					createHandleSuccess = true;
					if (mDetector != null) {
						detectorStartSuccess = mDetector.start(livenessConfig);
						/*
						 * set the WrapperStaticInfo here.
						 */
						if (createHandleSuccess) {
							setWrapperStaticInfo();
						}
					}
				} else {
					createHandleSuccess = false;
					onErrorHappen(LivenessActivity.RESULT_CREATE_HANDLE_ERROR);
				}
			} catch (Throwable e) {
				createHandleSuccess = false;
				onErrorHappen(LivenessActivity.RESULT_CREATE_HANDLE_ERROR);
			}
		}
	}

	private void releaseLiveness() {
		if (mDetector != null) {
			mDetector = null;
			createHandleSuccess = false;
		}
	}

	private void stopDetectThread() {
		mIsKilled = true;
		createHandleSuccess = false;
		executor.shutdown();
		try {
			executor.awaitTermination(100, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executor = null;
	}

	void restartDetect(boolean bRestartTime) {
		if (bRestartTime && null != mListener) {
			mListener.onLivenessDetect(mDetectList[mStatus].getValue(), mStatus);
		}
	}

	public void restartTrack() {
		if (null != mDetector) {
			mDetector.end();
			boolean saveFileSuccess = LivenessUtils.saveFile(getLivenessResult(), LivenessActivity.EXTRA_RESULT_PATH + LivenessActivity.LIVENESS_FILE_NAME);
			if(!saveFileSuccess) {
				onErrorHappen(getActivity().RESULT_FIRST_USER);
			}
			mDetector.destroy();
			mDetector = null;
		}
		firstPassedStatus = false;
		trackDone = false;
		mListener.onLivenessDetect(Constants.FIRST_LIVENESS_TRACKING_MISSED, -1);
	}

	public void resetStatus(boolean fAlert) {
		boolean bRestartTime = fAlert;
		if (mStatus > 0) {
			bRestartTime = true;
		}
		resetLivenessResult();
		mStatus = 0;
		restartDetect(bRestartTime);
	}

	private void resetLivenessResult() {
		int count = mLiveResult.length;
		for (int i = 0; i < count; i++) {
			mLiveResult[i] = false;
		}
	}

	public void registerLivenessDetectCallback(OnLivenessCallBack callback) {
		mListener = callback;
	}

	public interface OnLivenessCallBack {
		public void onLivenessDetect(int value, int status);
	}

	public void stopLiveness() {
		mPaused = true;
	}

	public void startLiveness() {
		mPaused = false;
	}

	private void debugFps() {
		if (mFrameCount == 0) {
			mStartTime = System.currentTimeMillis();
		}
		mFrameCount++;
		long testTime = System.currentTimeMillis() - mStartTime;
		if (testTime > 1000) {
			Log.i(TAG, "onPreviewFrame FPS = " + mFrameCount);
			Toast.makeText(getActivity(), "FPS: " + mFrameCount, Toast.LENGTH_SHORT).show();
			mFrameCount = 0;
		}
	}

}

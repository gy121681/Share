

package com.sensetime.stlivenesslibrary.ui;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.easeui.R;
import com.sensetime.stlivenesslibrary.LivenessDetector;
import com.sensetime.stlivenesslibrary.LivenessDetector.Motion;
import com.sensetime.stlivenesslibrary.STFinanceJNI.CVFinanceFrame;
import com.sensetime.stlivenesslibrary.util.Constants;
import com.sensetime.stlivenesslibrary.util.DataController;
import com.sensetime.stlivenesslibrary.util.LivenessUtils;
import com.sensetime.stlivenesslibrary.view.ActionPageAdapter;
import com.sensetime.stlivenesslibrary.view.CircleTimeView;
import com.sensetime.stlivenesslibrary.view.FixedSpeedScroller;
import com.sensetime.stlivenesslibrary.view.MyAlertDialog;
import com.sensetime.stlivenesslibrary.view.TimeViewContoller;
import com.sensetime.stlivenesslibrary.view.TimeViewContoller.CallBack;

public class LivenessActivity extends Activity {

	/**
	 * 加载库文件出现错误
	 */
	public static final int RESULT_CREATE_HANDLE_ERROR = 1001;

	/**
	 * 无法访问摄像头，没有权限或摄像头被占用
	 */
	public static final int RESULT_CAMERA_ERROR_NOPRERMISSION_OR_USED = 2;

	/**
	 * 内部错误
	 */
	public static final int RESULT_INTERNAL_ERROR = 3;

	/**
	 * 传入保存结果的文件路径
	 */
	public static String EXTRA_RESULT_PATH = "com.sensetime.liveness.resultPath";

	/**
	 * 传入活体检测的动作序列
	 */
	public static final String EXTRA_MOTION_SEQUENCE = "com.sensetime.liveness.motionSequence";

	/**
	 * 获取info信息
	 */
	public static final String EXTRA_INFO = "com.sensetime.liveness.info";

	/**
	 */
	public static final String SOUND_NOTICE = "soundNotice";

	/**
	 * 单图或多图开关
	 */
	public static final String OUTPUT_TYPE_CONFIG = "output_type_config";

	public static String OUTPUT_TYPE = "singleImg";

	/**
	 * 难易程度设置
	 */
	public static String COMPLEXITY = "normal";

	private boolean mIsBackground = false;

	private static final int CURRENT_ANIMATION = -1;
	private Context mContext;
	private MyAlertDialog mDialog;
	private CircleTimeView mTimeView;
	private boolean mIsStart = false, soundNoticeOrNot = true;
	private FaceOverlapFragment mFragment;
	private RelativeLayout trackNotice, noticeRelativeLayout;
	private TimeViewContoller mTimeViewContoller;
	private Bundle bundle;
	private TextView trackNoticeText;
	private MediaPlayer mediaPlayer = null;
	private String[] mDetectList = null;
	private ImageButton soundPlayBtn, returnBtn;
	private ViewGroup viewGroup;
	private View animFrame;
	private int currentDetectStep = 0;
	private SensorManager sm = null;
	public static final String LIVENESS_FILE_NAME = "livenessResult";
	private int groupLen;

	int[] defaultImageResource = { R.drawable.linkface_pic_one, R.drawable.linkface_pic_two,
			R.drawable.linkface_pic_three, R.drawable.linkface_pic_four, R.drawable.linkface_pic_five,
			R.drawable.linkface_pic_six, R.drawable.linkface_pic_seven, R.drawable.linkface_pic_eight,
			R.drawable.linkface_pic_nine, R.drawable.linkface_pic_ten };
	int[] imageResource = { R.drawable.linkface_pic_one, R.drawable.linkface_pic_two, R.drawable.linkface_pic_three,
			R.drawable.linkface_pic_four, R.drawable.linkface_pic_five, R.drawable.linkface_pic_six,
			R.drawable.linkface_pic_seven, R.drawable.linkface_pic_eight, R.drawable.linkface_pic_nine,
			R.drawable.linkface_pic_ten };
	int[] imageResourceSolid = { R.drawable.linkface_pic_onesolid, R.drawable.linkface_pic_twosolid,
			R.drawable.linkface_pic_threesolid, R.drawable.linkface_pic_foursolid, R.drawable.linkface_pic_fivesolid,
			R.drawable.linkface_pic_sixsolid, R.drawable.linkface_pic_sevensolid, R.drawable.linkface_pic_eightsolid,
			R.drawable.linkface_pic_ninesolid, R.drawable.linkface_pic_tensolid };

	// use ViewPager implements animation
	private ViewPager viewPager;
	private ActionPageAdapter pageAdapter;
	private boolean pop = false;

	private FaceOverlapFragment.OnLivenessCallBack listenerBack = new FaceOverlapFragment.OnLivenessCallBack() {
		@Override
		public void onLivenessDetect(final int value, final int status) {
			onLivenessDetectCallBack(value, status);
		}
	};
	AudioManager audioManager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.linkface_activity_liveness);
		soundPlayBtn = (ImageButton) findViewById(R.id.linkface_sound_play_btn);
		mContext = this;
		bundle = getIntent().getExtras();
		audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		mDetectList = DataController.getDetectActionOrder(bundle.getString(LivenessActivity.EXTRA_MOTION_SEQUENCE));
		EXTRA_RESULT_PATH = bundle.getString(LivenessActivity.EXTRA_RESULT_PATH);
		DataController.deleteFiles(EXTRA_RESULT_PATH);
		OUTPUT_TYPE = bundle.getString(LivenessActivity.OUTPUT_TYPE);
		soundNoticeOrNot = bundle.getBoolean(LivenessActivity.SOUND_NOTICE);
		if (soundNoticeOrNot) {
			soundPlayBtn.setBackgroundResource(R.drawable.linkface_icon_voice);
		} else {
			soundPlayBtn.setBackgroundResource(R.drawable.linkface_icon_novoice);
		}
		soundPlayBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (soundNoticeOrNot) {
					if (mediaPlayer != null && mediaPlayer.isPlaying()) {
						mediaPlayer.stop();
						mediaPlayer.reset();
						mediaPlayer.release();
						mediaPlayer = null;
					}
					soundPlayBtn.setBackgroundResource(R.drawable.linkface_icon_novoice);
					soundNoticeOrNot = false;
				} else {
					soundPlayBtn.setBackgroundResource(R.drawable.linkface_icon_voice);
					soundNoticeOrNot = true;
					playSoundNotice(currentDetectStep);
				}
			}
		});
		returnBtn = (ImageButton) findViewById(R.id.linkface_return_btn);
		returnBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		viewGroup = (ViewGroup) findViewById(R.id.viewGroup);
		if (mDetectList.length >= 1 && mDetectList.length <= imageResource.length) {
			groupLen = mDetectList.length;
		} else if (mDetectList.length > imageResource.length) {
			groupLen = imageResource.length;
		}
		for (int i = 0; i < groupLen; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
							ViewGroup.LayoutParams.MATCH_PARENT));
			imageView.setImageResource(imageResource[i]);
			viewGroup.addView(imageView);
		}
		animFrame = findViewById(R.id.anim_frame);
		animFrame.setVisibility(View.INVISIBLE);
		trackNotice = (RelativeLayout)findViewById(R.id.track_notice_layout);
		noticeRelativeLayout = (RelativeLayout)findViewById(R.id.notice_relative_layout);
		noticeRelativeLayout.setVisibility(View.INVISIBLE);
		trackNoticeText = (TextView)findViewById(R.id.track_notice_text);
		initView();
		mIsStart = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		mIsBackground = true;
		stopAndRelease();
		sm.unregisterListener(sensorEventListener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		sm.registerListener(sensorEventListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
		sm.registerListener(sensorEventListener, sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_UI);
		sm.registerListener(sensorEventListener, sm.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_UI);
		sm.registerListener(sensorEventListener, sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);
		mIsBackground = false;
		if(pop){
			showDialog(getStringWithID(R.string.failed_liveness_title), getStringWithID(R.string.keep_on_the_stage));
		}
		playSoundNotice(currentDetectStep);
	}

	@Override
	protected void onStop() {
		super.onStop();
		pop = mIsStart;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mDialog != null) {
			mDialog.dismiss();
		}
	}

	private void initView() {
		mTimeView = (CircleTimeView) findViewById(R.id.time_view);
		mTimeViewContoller = new TimeViewContoller(mTimeView);
		mFragment = (FaceOverlapFragment) getFragmentManager().findFragmentById(R.id.overlapFragment);
		mFragment.registerLivenessDetectCallback(listenerBack);
		startTrack();
	}

	private void startTrack() {
		setLivenessState(false);
	}

	private void startLivenessDetect() {
		animFrame.setVisibility(View.VISIBLE);
		initViewPager();
		mIsStart = true;
		setLivenessState(false);
		mFragment.resetStatus(true);
	}

	private String getStringWithID(int id) {
		return getResources().getString(id);
	}

	private void startAnimation(int animation) {
		if (animation != CURRENT_ANIMATION) {
			if (isDialogShowing()) {
				return;
			}
		}
		mTimeViewContoller.start();
		mTimeViewContoller.setCallBack(new CallBack() {
			@Override
			public void onTimeEnd() {
				mFragment.registerLivenessDetectCallback(null);
				if (null != mFragment.mDetector) {
					mFragment.mDetector.end();
					mFragment.pauseDetect = false;
					boolean saveFileSuccess = LivenessUtils.saveFile(mFragment.getLivenessResult(), EXTRA_RESULT_PATH + LIVENESS_FILE_NAME);
					if(!saveFileSuccess) {
						onErrorHappen(RESULT_FIRST_USER);
					}
					mFragment.mDetector.destroy();
					try {
						mFragment.mDetector.finalize();
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mFragment.mDetector = null;
				}
				stopAndRelease();
				showDialog(getStringWithID(R.string.time_out_dialog_title),
								getStringWithID(R.string.time_out_dialog_msg));

			}
		});
	}

	private void setLivenessState(boolean pause) {
		if (null == mFragment) {
			return;
		}
		if (pause) {
			mFragment.stopLiveness();
		} else {
			mFragment.startLiveness();
		}
	}

	private void onLivenessDetectCallBack(final int value, final int status) {
		mTimeViewContoller.stop();

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (!mIsStart) {
					return;
				}
				currentDetectStep = status + 1;
				if (value == Motion.BLINK.getValue()) {
					updateUi(R.string.note_wink, R.drawable.linkface_blink,
							status + 1);
				} else if (value == Motion.MOUTH.getValue()) {
					updateUi(R.string.note_mouth, R.drawable.linkface_mouth,
							status + 1);
				} else if (value == Motion.NOD.getValue()) {
					updateUi(R.string.note_nod, R.drawable.linkface_nod,
							status + 1);
				} else if (value == Motion.YAW.getValue()) {
					updateUi(R.string.note_shakehead, R.drawable.linkface_yaw,
							status + 1);
				} else if (value == Constants.LIVENESS_SUCCESS) {
					stopAndRelease();
					synchronized (this) {
						onDetectSuccess(mFragment.getImageResult(),
								mFragment.getLivenessResult());
					}
				} else if (value == Constants.LIVENESS_TRACKING_MISSED) {
					stopAndRelease();
					showDialog(
							getStringWithID(R.string.track_missed_dialog_title),
							getStringWithID(R.string.track_missed_dialog_msg));
				} else if (value == Constants.LIVENESS_TIME_OUT) {
					stopAndRelease();
					showDialog(getStringWithID(R.string.time_out_dialog_title),
							getStringWithID(R.string.time_out_dialog_msg));
				} else if (value == Constants.TRACK_FACE_OUTOF_BOUND) {
					trackNoticeText.setText(getResources().getString(
							R.string.face_out_of_bound_note));
				} else if (value == Constants.TRACK_FACE_TOO_CLOSE) {
					trackNoticeText.setText(getResources().getString(
							R.string.face_too_close_note));
				} else if (value == Constants.TRACK_FACE_TOO_FAR) {
					trackNoticeText.setText(getResources().getString(
							R.string.face_too_far_note));
				} else if(value == Constants.TRACK_PREPARE_START) {
					trackNoticeText.setText(getResources().getString(
							R.string.start_detect_note));
				} else if (value == Constants.TRACK_PASSED) {
					removeTrackUI();
				} else if (value == Constants.DETECT_FACE_OUTOF_BOUND) {
					stopAndRelease();
					showDialog(
							getStringWithID(R.string.track_missed_dialog_title),
							getStringWithID(R.string.track_missed_dialog_msg));
				} else if(value == Constants.DETECT_FACE_TOO_CLOSE) {
					stopAndRelease();
					showDialog(
							getStringWithID(R.string.track_missed_dialog_title),
							getStringWithID(R.string.track_missed_dialog_msg));
				} else if(value == Constants.DETECT_FACE_TOO_FAR) {
					stopAndRelease();
					showDialog(
							getStringWithID(R.string.track_missed_dialog_title),
							getStringWithID(R.string.track_missed_dialog_msg));
				} else if(value == Constants.FIRST_LIVENESS_TRACKING_MISSED) {
					stopAndRelease();
					showTrackUI();
				}
			}
		});
	}

	public void showTrackUI(){
		trackNotice.setVisibility(View.VISIBLE);
        noticeRelativeLayout.setVisibility(View.INVISIBLE);
	}

	private void removeTrackUI() {
		trackNotice.setVisibility(View.INVISIBLE);
        noticeRelativeLayout.setVisibility(View.VISIBLE);
        startLivenessDetect();
	}

	private void updateUi(int stringId, int animationId, int number) {
		if (animationId != 0) {
			startAnimation(animationId);
		}		
		// Use restartPlaySoundNotece instead of playSoundNotice
		restartPlaySoundNotice(number);
		changeAction(number - 1, true);
		if (number - 1 >= 0 && number - 1 < imageResource.length) {

			((ImageView) viewGroup.getChildAt(number - 1)).setImageResource(imageResourceSolid[number - 1]);
		}
	}

	private void playSoundNotice(int step) {
		if (step > 0) {
			stopAndRelease();
			if (mDetectList[step - 1].equalsIgnoreCase(getString(R.string.blink))) {
				if (soundNoticeOrNot) {
					setMediaSource("linkface_notice_blink.mp3", false);
				}
			} else if (mDetectList[step - 1].equalsIgnoreCase(getString(R.string.nod))) {
				if (soundNoticeOrNot) {
					setMediaSource("linkface_notice_nod.mp3", false);
				}
			} else if (mDetectList[step - 1].equalsIgnoreCase(getString(R.string.mouth))) {
				if (soundNoticeOrNot) {
					setMediaSource("linkface_notice_mouth.mp3", false);
				}
			} else if (mDetectList[step - 1].equalsIgnoreCase(getString(R.string.yaw))) {
				if (soundNoticeOrNot) {
					setMediaSource("linkface_notice_yaw.mp3", false);
				}
			}
		}
	}

	public void onDetectSuccess(CVFinanceFrame[] imageResult,
			byte[] livenessResult) {
		if (mFragment.mDetector != null) {
			mFragment.mDetector.destroy();
			mFragment.mDetector = null;
		}
		File livenessFolder = new File(EXTRA_RESULT_PATH);
		if (!livenessFolder.exists()) {
			livenessFolder.mkdirs();
		}
		Intent intent = new Intent();
		if (imageResult != null) {
			for (int i = 0; i < imageResult.length; i++) {
				String filePath = EXTRA_RESULT_PATH + "image" + i + ".jpg";
				boolean saveFileSuccess = LivenessUtils.saveFile(
						imageResult[i].image, filePath);
				if (!saveFileSuccess) {
					onErrorHappen(RESULT_FIRST_USER);
				}
			}
		}
		String resultPath = EXTRA_RESULT_PATH + LIVENESS_FILE_NAME;
		boolean saveFileSuccess = LivenessUtils.saveFile(livenessResult,
				resultPath);
		if (!saveFileSuccess) {
			onErrorHappen(RESULT_FIRST_USER);
		} else {
			setResult(RESULT_OK, intent);
			audioManager.abandonAudioFocus(afChangeListener);
			finish();
		}
	}

	private void showDialog(String title, String msg) {

		if (isDialogShowing()) {
			return;
		}

		// reuse ImageView saved instead of new ImageView object
		if (mDetectList.length > 0 && mDetectList.length <= defaultImageResource.length) {
			for (int i = 0; i < mDetectList.length; i++) {
				((ImageView) viewGroup.getChildAt(i)).setImageResource(defaultImageResource[i]);
			}
		} else if (mDetectList.length > defaultImageResource.length) {
			for (int i = 0; i < defaultImageResource.length; i++) {
				((ImageView) viewGroup.getChildAt(i)).setImageResource(defaultImageResource[i]);
			}
		}

		setLivenessState(true);
		mTimeViewContoller.hide();
		if (mDialog != null) {
			mDialog.dismiss();
		}
		mDialog = new MyAlertDialog(mContext)
						.builder()
						.setCancelable(false)
						.setMsg(msg)
						.setTitle(title)
						.setNegativeButton(getStringWithID(R.string.cancel), new android.view.View.OnClickListener() {
							@Override
							public void onClick(View arg0) {
								onErrorHappen(RESULT_CANCELED);
							}
						})
						.setPositiveButton(getStringWithID(R.string.restart_preview),
										new android.view.View.OnClickListener() {
											@Override
											public void onClick(View arg0) {
												mFragment.registerLivenessDetectCallback(listenerBack);
												mDialog.dismiss();
												mFragment.pauseDetect = true;
												restartAnimationAndLiveness();
											}
										});
		if(!mIsBackground){	
			mDialog.show();			
		}
	}

	private void restartAnimationAndLiveness() {
		if (viewPager != null) {
			viewPager.setCurrentItem(0, false);
		}
		setLivenessState(false);
		trackNotice.setVisibility(View.VISIBLE);
        noticeRelativeLayout.setVisibility(View.INVISIBLE);
        mFragment.restartTrack();
	}

	// don't call stopAndRelease(mediaPlayer),just change mediaPlayer's source
	private void restartPlaySoundNotice(int step) {
		if (step > 0) {
			if (mDetectList[step - 1].equalsIgnoreCase(getString(R.string.blink))) {
				if (soundNoticeOrNot) {
					setMediaSource("linkface_notice_blink.mp3", true);
				}
			} else if (mDetectList[step - 1].equalsIgnoreCase(getString(R.string.nod))) {
				if (soundNoticeOrNot) {
					setMediaSource("linkface_notice_nod.mp3", true);
				}
			} else if (mDetectList[step - 1].equalsIgnoreCase(getString(R.string.mouth))) {
				if (soundNoticeOrNot) {
					setMediaSource("linkface_notice_mouth.mp3", true);
				}
			} else if (mDetectList[step - 1].equalsIgnoreCase(getString(R.string.yaw))) {
				if (soundNoticeOrNot) {
					setMediaSource("linkface_notice_yaw.mp3", true);
				}
			}
		}
	}

	private boolean isDialogShowing() {
		if (mDialog == null || !mDialog.isShowing()) {
			return false;
		} else if (mDialog != null && mDialog.isShowing()) {
			return true;
		} else {
			return false;
		}
	}

	public void onErrorHappen(int resultCode) {
		setResult(resultCode);
		audioManager.abandonAudioFocus(afChangeListener);
		finish();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString(Constants.RESULT, getResources().getString(R.string.result_cancel));
		intent.putExtras(bundle);
		setResult(RESULT_CANCELED, intent);
		audioManager.abandonAudioFocus(afChangeListener);
		finish();
	}

	private void restartPrepareAndPlay() {
		mediaPlayer.prepareAsync();
		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				if (mIsBackground) {
					// Do not play when activity in background.
					return;
				}

				mediaPlayer.start();
				mediaPlayer.setLooping(true);
			}
		});

	}

	private void prepareAndPlay() {
		mediaPlayer.prepareAsync();
		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				if (isDialogShowing()) {
					if (mediaPlayer != null && mediaPlayer.isPlaying())
						mediaPlayer.stop();
				} else {
					if (mIsBackground) {
						// Do not play when activity in background.
						return;
					}

					mediaPlayer.start();
					mediaPlayer.setLooping(true);
				}
			}
		});
	}

	public void stopAndRelease() {

		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			mediaPlayer.reset();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	private void setMediaSource(String mediaName, boolean restart) {
		if (mDialog != null && mDialog.isShowing()) {
			// Ignore play media when dialog is showing.
			return;
		}

		AssetManager am = this.getAssets();
		AssetFileDescriptor fileDescriptor = null;
		try {
			fileDescriptor = am.openFd(mediaName);
			if (restart) {
				if (mediaPlayer != null && mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
					mediaPlayer.reset();
				} else {
					stopAndRelease();
					mediaPlayer = new MediaPlayer();
				}
				mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
						fileDescriptor.getStartOffset(),
						fileDescriptor.getLength());
				// Request audio focus for playback
				int result = audioManager.requestAudioFocus(afChangeListener,
						AudioManager.STREAM_MUSIC,
						AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE);
					restartPrepareAndPlay();
			} else {
				stopAndRelease();
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
						fileDescriptor.getStartOffset(),
						fileDescriptor.getLength());

				prepareAndPlay();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// initialize ViewPager Play
	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.action_group);

		// return true prevent touch event
		viewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		pageAdapter = new ActionPageAdapter(this, mDetectList);
		viewPager.setAdapter(pageAdapter);

		// FixedSpeedScroller control change time
		try {
			Field mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			FixedSpeedScroller fScroller = new FixedSpeedScroller(viewPager.getContext());
			mScroller.set(viewPager, fScroller);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

	}

	// control ViewPager
	private void changeAction(int num, boolean action) {
		if (num < mDetectList.length)
			viewPager.setCurrentItem(num, action);
	}

	AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
		@Override
		public void onAudioFocusChange(int focusChange) {
		}
	};

	SensorEventListener sensorEventListener = new SensorEventListener() {

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			if (!mFragment.mPaused && mFragment.mDetector != null
					&& mFragment.createHandleSuccess) {
				if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
					try {
						// add the MAGNETIC_FIELD info
						mFragment.mDetector
								.addSequentialInfo(
										LivenessDetector.WrapperSequentialInfo.MAGNETIC_FIELD
												.getValue(), event.values[0]
												+ " " + event.values[1] + " "
												+ event.values[2] + " ");
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
					try {
						// add the ACCLERATION info
						mFragment.mDetector
								.addSequentialInfo(
										LivenessDetector.WrapperSequentialInfo.ACCLERATION
												.getValue(), event.values[0]
												+ " " + event.values[1] + " "
												+ event.values[2] + " ");
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
					try {
						// add the ROTATION_RATE info
						mFragment.mDetector
								.addSequentialInfo(
										LivenessDetector.WrapperSequentialInfo.ROTATION_RATE
												.getValue(), event.values[0]
												+ " " + event.values[1] + " "
												+ event.values[2] + " ");
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
					try {
						// add the GRAVITY info
						mFragment.mDetector.addSequentialInfo(
								LivenessDetector.WrapperSequentialInfo.GRAVITY
										.getValue(), event.values[0] + " "
										+ event.values[1] + " "
										+ event.values[2] + " ");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	};
}


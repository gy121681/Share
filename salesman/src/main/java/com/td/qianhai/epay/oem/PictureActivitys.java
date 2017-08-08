package com.td.qianhai.epay.oem;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.views.BitmapUtil;
import com.td.qianhai.epay.oem.views.DragImageView;

public class PictureActivitys
// s extends Activity {

		// /** Called when the activity is first created. */
		//
		// private ImageView v1;
		//
		// private int widthOrg, heightOrg, w;
		//
		// float scale = 1;
		//
		// float scaleWidth, scaleHeight;
		//
		// Bitmap OrgMap, newMap;
		//
		// BitmapDrawable bmd;
		//
		// Matrix matrix = new Matrix();
		//
		// private TextView backs , content;
		//
		// @Override
		// public void onCreate(Bundle savedInstanceState) {
		//
		// super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// setContentView(R.layout.activity_picture);
		//
		// v1 = (ImageView) findViewById(R.id.image011);
		// backs = (TextView) findViewById(R.id.bt_title_left);
		// content = (TextView) findViewById(R.id.tv_title_contre);
		// content.setText("资质证书");
		// backs.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		//
		// finish();
		//
		// }
		// });
		//
		// OrgMap =
		// BitmapFactory.decodeResource(getResources(),R.drawable.qualifications);
		//
		// // Bitmap bgBitmap = Bitmap.createBitmap(OrgMap.getWidth(),
		// OrgMap.getHeight(), Config.ARGB_8888);
		//
		// widthOrg = OrgMap.getWidth();
		//
		// heightOrg = OrgMap.getHeight();
		//
		// // 确定图片初始缩放比例,一般以适应屏幕宽度为准
		//
		// w = 320;
		//
		// scale = (float) w / widthOrg;
		// matrix = new Matrix();
		//
		// matrix.postScale(scale, scale);
		//
		// newMap = Bitmap.createBitmap(OrgMap, 0, 0, widthOrg, heightOrg,
		// matrix,
		// true);
		//
		// bmd = new BitmapDrawable(newMap);
		// v1.setImageDrawable(bmd);
		//
		// }
		//
		// @Override
		// public boolean onKeyDown(int keyCode, KeyEvent event) {
		// // 放大图片
		// if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
		//
		// { // 设置放大尺寸,若大于2倍,则停止放大,另外,小于1200是为了防止因图片太大造成内存泄露
		// if (scale < 2.0 && w < 1200) {
		//
		// scale += 0.1;
		//
		// matrix.reset(); // 重置矩阵
		//
		// matrix.postScale(scale, scale); // 设置矩阵属性
		//
		// newMap = Bitmap.createBitmap(OrgMap, 0, 0, widthOrg, heightOrg,
		// matrix, true);
		//
		// bmd = new BitmapDrawable(newMap);
		//
		// v1.setImageDrawable(bmd);
		//
		// w = newMap.getWidth();
		//
		// int h = newMap.getHeight();
		//
		// Log.i("ta==========", w + " " + h);
		//
		// }
		//
		// return true;
		//
		// }
		//
		// if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
		//
		// {
		//
		// if (scale > 0.1)
		//
		// {
		//
		// scale -= 0.05;
		//
		// matrix.reset(); // 重置矩阵
		//
		// matrix.postScale(scale, scale); // 设置矩阵属性
		//
		// // 重新绘图
		//
		// newMap = Bitmap.createBitmap(OrgMap, 0, 0, widthOrg, heightOrg,
		// matrix, true);
		//
		// // 转化为drawable图像，使其可以显示在imageview中
		//
		// bmd = new BitmapDrawable(newMap);
		//
		// v1.setImageDrawable(bmd);
		//
		// w = newMap.getWidth();
		//
		// int h = newMap.getHeight();
		//
		// Log.i("ta==========", w + " " + h);
		//
		// }
		//
		// return true;
		//
		// }
		//
		// else
		//
		// return super.onKeyDown(keyCode, event);
		// }
		// }

		extends BaseActivity {
	private TextView backs, content;
	private int window_width, window_height;// �ؼ����
	private DragImageView dragImageView;// �Զ���ؼ�
	private int state_height;// ״̬���ĸ߶�

	private ViewTreeObserver viewTreeObserver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture);
		/** ��ȡ��Ҋ����߶� **/
		AppContext.getInstance().addActivity(this);
		WindowManager manager = getWindowManager();
		window_width = manager.getDefaultDisplay().getWidth();
		window_height = manager.getDefaultDisplay().getHeight();

		backs = (TextView) findViewById(R.id.bt_title_left);
		content = (TextView) findViewById(R.id.tv_title_contre);
		content.setText("资质证书");
		backs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				finish();
			}
		});
		dragImageView = (DragImageView) findViewById(R.id.div_main);
//		Bitmap bmp = BitmapUtil.ReadBitmapById(this, R.drawable.qualifications,
//				window_width, window_height);
		// ����ͼƬ
//		dragImageView.setImageBitmap(bmp);
		dragImageView.setmActivity(this);// ע��Activity.
		/** ����״̬���߶� **/
		viewTreeObserver = dragImageView.getViewTreeObserver();
		viewTreeObserver
				.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						if (state_height == 0) {
							// ��ȡ״�����߶�
							Rect frame = new Rect();
							getWindow().getDecorView()
									.getWindowVisibleDisplayFrame(frame);
							state_height = frame.top;
							dragImageView.setScreen_H(window_height
									- state_height);
							dragImageView.setScreen_W(window_width);
						}

					}
				});

	}

	/**
	 * ��ȡ������Դ��ͼƬ
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap ReadBitmapById(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// ��ȡ��ԴͼƬ
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

}
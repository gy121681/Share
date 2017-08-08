package com.td.qianhai.epay.oem.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.views.dialog.interfaces.DialogListener;

/***
 * 电子签名
 * 
 * @author liangge
 * 
 */
public class UploadWriteDialog extends Dialog {

	private Context context;
	private LayoutParams p;
	private DialogListener dialogListener;
	private int height, width;
	private TextView tv_content;

	public UploadWriteDialog(Context context, int height, int width,
			DialogListener dialogListener) {
		super(context);
		this.context = context;
		this.dialogListener = dialogListener;
		this.height = height;
		this.width = width;
	}

	static final int BACKGROUND_COLOR = Color.WHITE;

	static final int BRUSH_COLOR = Color.BLACK;

	PaintView mView;

	/** The index of the current color to use. */
	int mColorIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.upload_sign_dialog);
		tv_content = (TextView) findViewById(R.id.tv_title_contre);
		tv_content.setText("电子签名");
		p = getWindow().getAttributes(); // ��ȡ�Ի���ǰ�Ĳ���ֵ
		p.height = height;// (int) (d.getHeight() * 0.4); //�߶�����Ϊ��Ļ��0.4
		p.width = width;// (int) (d.getWidth() * 0.6); //�������Ϊ��Ļ��0.6
		getWindow().setAttributes(p); // ������Ч

		mView = new PaintView(context);
		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.tablet_view);
		frameLayout.addView(mView);
		mView.requestFocus();
		Button btnClear = (Button) findViewById(R.id.btn_upload_dialog_clear);
		btnClear.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mView.clear();
			}
		});

		Button btnOk = (Button) findViewById(R.id.btn_upload_dialog_ok);
		btnOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					dialogListener.refreshActivity(mView.getCachebBitmap());
					UploadWriteDialog.this.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

//		Button btnCancel = (Button) findViewById(R.id.btn_upload_dialog_cancel);
//		btnCancel.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				cancel();
//			}
//		});
	}

	/**
	 * This view implements the drawing canvas.
	 * 
	 * It handles all of the input events and drawing functions.
	 */
	class PaintView extends View {
		private Paint paint;
		private Canvas cacheCanvas;
		private Bitmap cachebBitmap;
		private Path path;

		public Bitmap getCachebBitmap() {
			return cachebBitmap;
		}

		public PaintView(Context context) {
			super(context);
			init();
		}

		private void init() {
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStrokeWidth(3);
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(Color.BLACK);
			path = new Path();
			cachebBitmap = Bitmap.createBitmap(p.width, (int) (p.height),
					Config.ARGB_8888);
			cacheCanvas = new Canvas(cachebBitmap);
			cacheCanvas.drawColor(Color.WHITE);
		}

		public void clear() {
			if (cacheCanvas != null) {

				paint.setColor(BACKGROUND_COLOR);
				cacheCanvas.drawPaint(paint);
				paint.setColor(Color.BLACK);
				cacheCanvas.drawColor(Color.WHITE);
				invalidate();
			}
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// canvas.drawColor(BRUSH_COLOR);
			canvas.drawBitmap(cachebBitmap, 0, 0, null);
			canvas.drawPath(path, paint);
		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {

			int curW = cachebBitmap != null ? cachebBitmap.getWidth() : 0;
			int curH = cachebBitmap != null ? cachebBitmap.getHeight() : 0;
			if (curW >= w && curH >= h) {
				return;
			}

			if (curW < w)
				curW = w;
			if (curH < h)
				curH = h;

			Bitmap newBitmap = Bitmap.createBitmap(curW, curH,
					Bitmap.Config.ARGB_8888);
			Canvas newCanvas = new Canvas();
			newCanvas.setBitmap(newBitmap);
			if (cachebBitmap != null) {
				newCanvas.drawBitmap(cachebBitmap, 0, 0, null);
			}
			cachebBitmap = newBitmap;
			cacheCanvas = newCanvas;
		}

		private float cur_x, cur_y;

		@Override
		public boolean onTouchEvent(MotionEvent event) {

			float x = event.getX();
			float y = event.getY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				cur_x = x;
				cur_y = y;
				path.moveTo(cur_x, cur_y);
				break;
			}

			case MotionEvent.ACTION_MOVE: {
				path.quadTo(cur_x, cur_y, x, y);
				cur_x = x;
				cur_y = y;
				break;
			}

			case MotionEvent.ACTION_UP: {
				cacheCanvas.drawPath(path, paint);
				path.reset();
				break;
			}
			}

			invalidate();

			return true;
		}
	}

}

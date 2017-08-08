package com.shareshenghuo.app.shop.widget.dialog;

import android.content.Context;
import android.widget.ImageView;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.util.BitmapTool;

public class QRCodeWindow extends CommonDialog {
	
	private ImageView ivQR;
	private String content;

	public QRCodeWindow(Context context, String content) {
		super(context, R.layout.dlg_qr_code, 280, 280);
		this.content = content;
	}

	@Override
	public void initDlgView() {
		ivQR = getView(R.id.ivQR);
		ivQR.setImageBitmap(BitmapTool.createQRCodeBitmap(content,400));
	}
}

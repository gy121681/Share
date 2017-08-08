package com.shareshenghuo.app.user.activity.realname;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shareshenghuo.app.user.BaseTopActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.event.RealNameVerifyCompleteEvent;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.response.SubmitIdCardInformationResponse;
import com.shareshenghuo.app.user.networkapi.Request;
import com.shareshenghuo.app.user.networkapi.Callback;
import com.shareshenghuo.app.user.networkapi.CallbackObject;
import com.shareshenghuo.app.user.util.BitmapTool;
import com.shareshenghuo.app.user.util.FileUploadUtils;
import com.shareshenghuo.app.user.util.FileUtil;
import com.shareshenghuo.app.user.util.ImageUtil;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.UrlUtils;
import com.shareshenghuo.app.user.widget.dialog.PickPhotoWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

/**
 * 实名认证，上传身份证界面
 * Created by Snow on 2017/7/19.
 */

public class RealnameStepIDCardActivity extends BaseTopActivity {

    private ImageView mImgPhoto1;//身份证正面
    private ImageView mImgPhoto2;//身份证反面
    private TextView mTvTip1;//正面提示
    private  TextView mTvTip2;//反面提示
    private  ImageView mImgDelete1;//正面删除按钮
    private ImageView mImgDelete2;//反面删除按钮
    private EditText mEdtName;
    private EditText mEdtCardNO;


    private String mImgPathFront;//下面路径
    private String mImgPathBack;//反面路径

    private OnActivityResultListener mResultListener;

    private Callback<String> mUploadFront = new Callback<String>() {
        @Override
        public void onSuccess(String data) {
            mImgPathFront = UrlUtils.getUrl(data);
            refreshUI();
        }

        @Override
        public void onFailure(String msg) {
            mImgPathFront = "";
            refreshUI();
        }

        @Override
        public void onNetError(int code, String msg) {
            toast("网络异常");
            mImgPathFront = "";
            refreshUI();
        }
    };

    private Callback<String> mUploadBack = new Callback<String>() {
        @Override
        public void onSuccess(String data) {
            mImgPathBack = UrlUtils.getUrl(data);
            refreshUI();
        }

        @Override
        public void onFailure(String msg) {
            mImgPathBack = "";
            refreshUI();
        }

        @Override
        public void onNetError(int code, String msg) {
            toast("网络异常");
            Log.d("", msg);
        }
    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_realname_step_idcard);
        EventBus.getDefault().register(this);
        initTopBar("实名认证");
        initData();
        initView();
        setEvent();
    }

    private void initData(){

    }

    private void initView(){
        mImgPhoto1 = getView(R.id.img_idcard_1);
        mImgPhoto2 = getView(R.id.img_idcard_2);
        mTvTip1 = getView(R.id.tv_idcard_tip_1);
        mTvTip2 = getView(R.id.tv_idcard_tip_2);
        mImgDelete1 = getView(R.id.img_idcard_delete_1);
        mImgDelete2 = getView(R.id.img_idcard_delete_2);
        mEdtName = getView(R.id.edt_name);
        mEdtCardNO = getView(R.id.edt_idcard);
        refreshUI();
    }

    private void setEvent(){
        //下面拍照
        mImgPhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mImgPathFront)) {
                    new PickPhotoWindow(RealnameStepIDCardActivity.this).showAtBottom();
                    // 拍照回调
                    setActivityResultListener(new OnActivityResultListener() {
                        @Override
                        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                            if (resultCode == Activity.RESULT_OK) {
                                File file = getPhotoFile(requestCode, resultCode, data);
                                if (file != null) {
                                    uploadFile(file, mUploadFront);
                                }
                            }
                        }
                    });
                }
            }
        });
        //背面拍照
        mImgPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mImgPathBack)) {
                    new PickPhotoWindow(RealnameStepIDCardActivity.this).showAtBottom();
                    setActivityResultListener(new OnActivityResultListener() {
                        @Override
                        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                            if (resultCode == Activity.RESULT_OK) {
                                File file = getPhotoFile(requestCode, resultCode, data);
                                if (file != null) {
                                    uploadFile(file, mUploadBack);
                                }
                            }
                        }
                    });
                }
            }
        });
        //删除按钮
        mImgDelete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImgPathFront = "";
                refreshUI();
            }
        });
        //
        mImgDelete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImgPathBack = "";
                refreshUI();
            }
        });
        getView(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitIDCardInfo();
            }
        });
    }

    /**
     * 获取图片
     * @param requestCode
     * @param resultCode
     * @param data
     * @return
     */
    private File getPhotoFile(int requestCode, int resultCode, Intent data) {
        File file = null;
        if (requestCode == PickPhotoWindow.REQUEST_PICK_LOCAL) {
            String path = FileUtil.getPath(RealnameStepIDCardActivity.this, data.getData());
            if (path == null) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap bmp = extras.getParcelable("data");
                    if (bmp != null) {
                        file = BitmapTool.Bitmap2File(RealnameStepIDCardActivity.this, bmp);
                    }
                }
            } else {
                file = new File(path);
            }
        } else if (requestCode == PickPhotoWindow.REQUEST_TAKE_CAMERA) {
            ImageUtil.saveBitmap2file(PickPhotoWindow.mImagePaths,768,1280);
            file = new File(PickPhotoWindow.mImagePaths);
        }
        return file;
    }

    /**
     * 上传图片
     */
    private void uploadFile(File file, Callback callback) {
        FileUploadUtils.upPhoto(RealnameStepIDCardActivity.this, file, callback);
    }

    private void refreshUI(){
        setViewVisiable(mTvTip1, TextUtils.isEmpty(mImgPathFront));
        setViewVisiable(mTvTip2, TextUtils.isEmpty(mImgPathBack));
        setViewVisiable(mImgDelete1, !TextUtils.isEmpty(mImgPathFront));
        setViewVisiable(mImgDelete2, !TextUtils.isEmpty(mImgPathBack));
        Glide.with(RealnameStepIDCardActivity.this).load(mImgPathFront).into(mImgPhoto1);
        Glide.with(RealnameStepIDCardActivity.this).load(mImgPathBack).into(mImgPhoto2);
    }

    private void setViewVisiable(View view, boolean visiable) {
        if (view == null) {
            return;
        }
        view.setVisibility(visiable ? View.VISIBLE : View.GONE);
    }

    public void setActivityResultListener(OnActivityResultListener l) {
        this.mResultListener = l;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mResultListener != null) {
            mResultListener.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
        mResultListener = null;
    }

    public interface OnActivityResultListener{
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    private void commitIDCardInfo(){
        if (TextUtils.isEmpty(getName()) || TextUtils.isEmpty(getCardNO()) ||
                TextUtils.isEmpty(mImgPathFront) || TextUtils.isEmpty(mImgPathBack)) {
            toast("资料未填写完整！");
            return;
        }
        ProgressDialogUtil.showProgressDlg(this, "");
        Request.getServiceSubmitIdCardInformation(UserInfoManager.getUserId(this) + "",
                "1", getName(), getCardNO(), mImgPathFront, mImgPathBack, new CallbackObject<SubmitIdCardInformationResponse>() {
                    @Override
                    public void onSuccess(SubmitIdCardInformationResponse data) {
                        ProgressDialogUtil.dismissProgressDlg();
                        if (TextUtils.equals("000000", data.RSPCOD)) {
                            startActivity(new Intent(RealnameStepIDCardActivity.this, RealnameStepSetpwdActivity.class));
                        } else if (TextUtils.equals("000001", data.RSPCOD)) {
//                            toast("实名认证已通过");
                        } else if (TextUtils.equals("000002", data.RSPCOD)) {
//                            toast("此身份证已经认证,请更换");
                        } else if (TextUtils.equals("000003", data.RSPCOD)) {
//                            toast("身份证信息验证失败");
                        }
                        toast(data.RSPMSG);
                    }

                    @Override
                    public void onFailure(String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
                        toast(msg);
                    }

                    @Override
                    public void onNetError(int code, String msg) {
                        ProgressDialogUtil.dismissProgressDlg();
                        toast("网络异常");
                    }
                });
    }

    private String getName(){
        return mEdtName.getText().toString().trim();
    }

    private String getCardNO(){
        return mEdtCardNO.getText().toString().trim();
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onEvent(RealNameVerifyCompleteEvent event) {
        finish();
    }

}


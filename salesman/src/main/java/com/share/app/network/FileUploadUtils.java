package com.share.app.network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.share.app.entity.response.Constans;
import com.share.app.utils.ProgressDialogUtil;
import com.share.app.utils.T;
import com.td.qianhai.epay.oem.beans.FileUploadResponse;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.BitmapTool;
import com.td.qianhai.epay.oem.mail.utils.FileUtil;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.mail.utils.PictureUtil;
import com.td.qianhai.epay.oem.views.dialog.PickPhotoWindow;

import java.io.File;

/**
 * Created by Snow on 2017/7/20.
 */

public class FileUploadUtils {

    /**
     * 处理拍照返回数据
     * @param context
     * @param requestCode
     * @param resultCode
     * @param data
     * @return 照片图片
     */
    public static File handleTakePhotoResult(Context context, int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == PickPhotoWindow.REQUEST_PICK_LOCAL || requestCode == PickPhotoWindow.REQUEST_TAKE_CAMERA) {
                return getPhotoFile(context, data);
            }
        }
        return null;
    }

    public static File getPhotoFile(Context context, Intent data) {
        String path = FileUtil.getPath((Activity) context, data.getData());
        if (path == null) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap bmp = extras.getParcelable("data");
                if (bmp != null) {
                    return BitmapTool.Bitmap2File(context, bmp);
                }
            }
        } else {
            return new File(path);
        }
        return null;
    }

    public static void upPhoto(final Context context, File file, final Callback<String> callback) {
        try {
            // 压缩图片
            String compressPath = PictureUtil.compressImage(context, file.getPath(), file.getName(), 65);
            file = new File(compressPath);
        } catch(Exception e) {
            e.printStackTrace();
        }

        ProgressDialogUtil.showProgressDlg(context, "上传图片");
        RequestParams params = new RequestParams();
        params.addBodyParameter("business_type", MyCacheUtil.getshared(context).getString(Constans.Login.USERID, ""));
        params.addBodyParameter("1", file);
        new HttpUtils().send(HttpRequest.HttpMethod.POST, HttpUrls.URL_UPLOAD_FILE, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException e, String msg) {
                ProgressDialogUtil.dismissProgressDlg();
                T.showNetworkError(context);
                if (callback != null) {
                    callback.onNetError(e.getExceptionCode(), msg);
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> resp) {
                ProgressDialogUtil.dismissProgressDlg();
                if (callback == null) return;
                if(resp.statusCode==200 && resp.result!=null) {
                    FileUploadResponse bean = new Gson().fromJson(resp.result, FileUploadResponse.class);
                    if(Api.SUCCEED == bean.result_code) {
                        T.showShort(context, "上传成功");
                        callback.onSuccess(bean.data.get(0));
                    } else {
                        T.showShort(context, bean.result_desc);
                        callback.onFailure(bean.result_desc);
                    }
                } else{
                    callback.onFailure("上传失败");
                }
            }
        });
    }

}

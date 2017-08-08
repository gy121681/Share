//package com.shareshenghuo.app.user.util;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Environment;
//import android.util.Log;
//
//public class UpDate {
//	public Context context;
//	 // 下载应用的进度条
//    private ProgressDialog progressDialog;
//    private String apk_path;
//    private String name ;
//    private String FILE_PATH = Environment.getExternalStorageDirectory() +"/share/";
//    private String FILE_NAME =FILE_PATH+ "ermagy";
//    // 准备安装新版本应用标记
//    private static final int INSTALL_TOKEN = 1;
//	public  UpDate(Context context,String apk_path, String name){
//		Log.e("", ""+apk_path+" - - -- "+name);
//		this.context = context;
//		this.apk_path = apk_path;
//		this.name = name;
//		File dir = new File(Environment.getExternalStorageDirectory()+"/share/");
//		if (!dir.exists()){
//			dir.mkdirs();
//		}
//		showDownloadDialog();
//	}
//	
//	 /**
//     * 显示下载进度对话框
//     */
//    public void showDownloadDialog() {
//
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setTitle("正在下载...");
//        progressDialog.setCanceledOnTouchOutside(true);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        new downloadAsyncTask().execute();
//    }
//
//	
//	 /**
//     * 下载新版本应用
//     */
//    private class downloadAsyncTask extends AsyncTask<Void, Integer, Integer> {
//
//        @Override
//        protected void onPreExecute() {
////            Log.e(TAG, "执行至--onPreExecute");
//            progressDialog.show();
//        }
//
//        @Override
//        protected Integer doInBackground(Void... params) {
//
////            Log.e(TAG, "执行至--doInBackground");
//
//            URL url;
//            HttpURLConnection connection = null;
//            InputStream in = null;
//            FileOutputStream out = null;
//            try {
//                url = new URL(apk_path);
//                connection = (HttpURLConnection) url.openConnection();
//
//                in = connection.getInputStream();
//                long fileLength = connection.getContentLength();
//                File file_path = new File(FILE_PATH);
//                if (!file_path.exists()) {
//                    file_path.mkdir();
//                }
//
//                out = new FileOutputStream(new File(FILE_NAME+name+".apk"));//为指定的文件路径创建文件输出流
//                byte[] buffer = new byte[1024 * 1024];
//                int len = 0;
//                long readLength = 0;
//
////                Log.e(TAG, "执行至--readLength = 0");
//
//                while ((len = in.read(buffer)) != -1) {
//
//                    out.write(buffer, 0, len);//从buffer的第0位开始读取len长度的字节到输出流
//                    readLength += len;
//
//                    int curProgress = (int) (((float) readLength / fileLength) * 100);
//
////                    Log.e(TAG, "当前下载进度：" + curProgress);
//
//                    publishProgress(curProgress);
//
//                    if (readLength >= fileLength) {
//
////                        Log.e(TAG, "执行至--readLength >= fileLength");
//                        break;
//                    }
//                }
//
//                out.flush();
//                return INSTALL_TOKEN;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (out != null) {
//                    try {
//                        out.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                if (in != null) {
//                    try {
//                        in.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                if (connection != null) {
//                    connection.disconnect();
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//
////            Log.e(TAG, "异步更新进度接收到的值：" + values[0]);
//            progressDialog.setProgress(values[0]);
//        }
//
//        @Override
//        protected void onPostExecute(Integer integer) {
//
//            progressDialog.dismiss();//关闭进度条
//            //安装应用
//            installApp();
//        }
//    }
//
//    /**
//     * 安装新版本应用
//     */
//    private void installApp() {
//        File appFile = new File(FILE_NAME+name+".apk");
//        if (!appFile.exists()) {
//            return;
//        }
//        // 跳转到新版本应用安装页面
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.parse("file://" + appFile.toString()), "application/vnd.android.package-archive");
//        context.startActivity(intent);
//    }
//    
//
//}

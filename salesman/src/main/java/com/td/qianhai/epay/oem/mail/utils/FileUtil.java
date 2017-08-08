package com.td.qianhai.epay.oem.mail.utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

public class FileUtil {

    public static String getPath(Activity context, Uri uri) {
        // just some safety built in 
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = context.managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

    public static void setphotopath(Activity activity,String filepath){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(filepath));
        intent.setData(uri);
        activity.sendBroadcast(intent);//
    }

    public static boolean createFile(File file) throws IOException {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return true;
        }
        if (file.getParentFile().exists()) {
            return file.createNewFile();
        } else {
            if (file.getParentFile().mkdirs()) {
                return file.createNewFile();
            } else {
                return false;
            }
        }
    }
}

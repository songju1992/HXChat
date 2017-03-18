package com.app.song.hxchat.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by song on 2017/3/15 20:56
 */


public class ToastUtils {

    private static Toast sToast;

    public static void showToast(Context context, String msg){
        if (sToast==null){
            sToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        }
        //如果这个Toast已经在显示了，那么这里会立即修改文本
        sToast.setText(msg);
        sToast.show();
    }
}

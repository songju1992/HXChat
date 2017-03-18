package com.app.song.hxchat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by song on 2017/3/15 18:50
 */


public class BaseActivity extends AppCompatActivity{
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog=new ProgressDialog(this);
    }
    public void showDialog(String msg){
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }
    public void hideDialog(){
        mProgressDialog.hide();
    }


    public void startActivity(Class clazz,boolean isFinish){
        startActivity(clazz,isFinish,null);
    }

    public void startActivity(Class clazz,boolean isFinish,String contact){
        Intent intent=new Intent(this,clazz);
        if(contact!=null){
            intent.putExtra("username",contact);
        }
        startActivity(intent);
        if(isFinish){
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressDialog.dismiss();
    }
}

package com.app.song.hxchat.presenter;

import android.util.Log;

import com.app.song.hxchat.model.User;
import com.app.song.hxchat.utils.ThreadUtils;
import com.app.song.hxchat.view.RegisterActivity;
import com.app.song.hxchat.view.RegisterView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by song on 2017/3/15 19:39
 */


public class RegisterPresenterIml implements RegisterPresenter{
    private RegisterView mRegisterView;
    public RegisterPresenterIml(RegisterView registerView) {
        mRegisterView=registerView;
    }

    @Override
    public void regist(final String username, final String pwd) {
        /**
         * 1. 先注册Bmob云数据库
         * 2. 如果Bmob成功了再去注册环信平台
         * 3. 如果Bmob成功了，环信失败了，则再去把Bmob上的数据给删除掉
         */

        User user=new User();
        user.setUsername(username);
        user.setPassword(pwd);

        user.signUp(new SaveListener<User>() {
            @Override
            public void done(final User user, BmobException e) {
                //Bmob中的回调方法都是在主线程中被调用的,但是通过异常 e 来判断请求成功或者失败
                if(e==null){//请求成功
                    Log.d("AAAAAAAAA","注册BOM成功");
                    //注册到Bmob后端云成功，再去注册到环信。
                    ThreadUtils.runOnSubThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().createAccount(username,pwd);//环信的注册是一个同步方法，此处需要开启子线程
                                Log.d("AAAAAAAAA","环信注册成功");
                                //注册成功
                                ThreadUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mRegisterView.onRegister(username,pwd,true,null);//在此需要回调到主线程，所以runOnMainThread
                                    }
                                });
                            } catch (final HyphenateException e1) {
                                e1.printStackTrace();
                                //将Bmob上注册的user给删除掉
                                user.delete();
                                Log.d("AAAAAAAAA","环信注册失败");
                                //环信注册失败了
                                ThreadUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mRegisterView.onRegister(username,pwd,false,e1.toString());
                                    }
                                });
                            }

                        }
                    });
                }else{
                    //失败了，将结果告诉Activity
                    mRegisterView.onRegister(username,pwd,false,e.getMessage());
                }

            }
        });
    }
}

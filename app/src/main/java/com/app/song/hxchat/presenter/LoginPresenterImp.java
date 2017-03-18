package com.app.song.hxchat.presenter;

import android.util.Log;

import com.app.song.hxchat.utils.ThreadUtils;
import com.app.song.hxchat.view.LoginActivity;
import com.app.song.hxchat.view.LoginView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * Created by song on 2017/3/15 23:19
 */


public class LoginPresenterImp implements LoginPresenter{
    private LoginView mLoginView;
    public LoginPresenterImp(LoginView loginView) {
        mLoginView=loginView;
    }
    @Override
    public void isLogin(final String username, final String pwd) {
        //环信目前（3.5.x）的所有回调方法都是在子线程中回调的
        EMClient.getInstance().login(username, pwd, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.d("AAAAAAAAA","登录环信成功");
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
//                        Log.d("main", "登录聊天服务器成功！");
                        mLoginView.loginSuccessed(username,pwd,true,null);
                    }
                });
            }

            @Override
            public void onError(int i, final String s) {
                Log.d("AAAAAAAAA","环信登录失败"+s);
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mLoginView.loginSuccessed(username,pwd,false,s);
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
}

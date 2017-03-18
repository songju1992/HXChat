package com.app.song.hxchat.presenter;

import com.app.song.hxchat.view.SplashActivity;
import com.app.song.hxchat.view.SplashView;
import com.hyphenate.chat.EMClient;

/**
 * Created by song on 2017/3/15 18:40
 */


public class SplashPresenterImp implements SplashPresenter{
    private SplashView mSplashView;

    public SplashPresenterImp(SplashView splashView) {
        mSplashView = splashView;
    }

    @Override
    public void checkLogined() {
        if (EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected()) {
            //已经登录过了
            mSplashView.onCheckedLogin(true);
        } else {
            //还未登录
            mSplashView.onCheckedLogin(false);
        }

    }
    }

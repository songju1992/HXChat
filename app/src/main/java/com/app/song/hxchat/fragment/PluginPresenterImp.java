package com.app.song.hxchat.fragment;

import com.app.song.hxchat.utils.ThreadUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * Created by song on 2017/3/18 12:19
 */


public class PluginPresenterImp implements PluginPresenter{
    private PluginView pluginView;

    public PluginPresenterImp(PluginView pluginView) {
        this.pluginView=pluginView;
    }


    @Override
    public void loginOut() {
        /**
         * 参数1：true代表解除绑定，不再推送消息
         */
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        pluginView.login_out(EMClient.getInstance().getCurrentUser(),true,null);
                    }
                });
            }

            @Override
            public void onError(int i, final String s) {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        pluginView.login_out(EMClient.getInstance().getCurrentUser(),false,s);
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
}

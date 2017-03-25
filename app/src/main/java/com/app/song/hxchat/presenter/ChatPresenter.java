package com.app.song.hxchat.presenter;

/**
 * Created by song on 2017/3/25 23:50
 */


public interface ChatPresenter {
    void initChat(String contact);

    void updateData(String username);

    void sendMessage(String username, String msg);
}

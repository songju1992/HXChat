package com.app.song.hxchat.view;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by song on 2017/3/25 23:50
 */


public interface ChatView {
    void onInit(List<EMMessage> emMessageList);

    void onUpdate(int size);
}

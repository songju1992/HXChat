package com.app.song.hxchat.fragment;

import com.hyphenate.chat.EMConversation;

import java.util.List;

/**
 * Created by song on 2017/3/18 19:00
 */


public interface ConversationView{
    void onInitConversation(List<EMConversation> emConversationList);
}

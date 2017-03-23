package com.app.song.hxchat.fragment;

/**
 * Created by song on 2017/3/18 19:01
 */


public class ConversationPresenterImp implements ConversationPresenter{
    private ConversationView conversationView;

    public ConversationPresenterImp(ConversationView conversationView) {
        this.conversationView=conversationView;
    }

    @Override
    public void initConversation() {

    }
}

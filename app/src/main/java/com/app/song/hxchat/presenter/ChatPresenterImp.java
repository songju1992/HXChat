package com.app.song.hxchat.presenter;

import com.app.song.hxchat.utils.ThreadUtils;
import com.app.song.hxchat.view.ChatActivity;
import com.app.song.hxchat.view.ChatView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by song on 2017/3/25 23:50
 */


public class ChatPresenterImp implements ChatPresenter{
    private ChatView chatView;
    private List<EMMessage> mEMMessageList = new ArrayList<>();
    public ChatPresenterImp(ChatView chatView) {
        this.chatView=chatView;
    }

    @Override
    public void initChat(String contact) {
        /**
         * 1. 如果曾经跟contact有聊天过，那么获取最多最近的20条聊天记录，然后展示到View层
         * 2. 如果没有聊天过，返回一个空的List
         */
        updateChatData(contact);
        chatView.onInit(mEMMessageList);

    }

    private void updateChatData(String contact) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(contact);
        if(conversation!=null){
            //曾经聊过天
            //获取20条聊天记录，然后展示到view层
            //获取最后一条消息
            EMMessage lastMessage = conversation.getLastMessage();
            //获取最后一条前19条数据
            int count = 19;
            if (mEMMessageList.size()>=19){
                count = mEMMessageList.size();
            }
            List<EMMessage> emMessages = conversation.loadMoreMsgFromDB(lastMessage.getMsgId(), count);
            mEMMessageList.clear();
            mEMMessageList.add(lastMessage);
            mEMMessageList.addAll(emMessages);
            Collections.reverse(mEMMessageList);
        }else{
            //从未聊过天
            mEMMessageList.clear();
        }
    }

    @Override
    public void updateData(String username) {
        updateChatData(username);
        chatView.onUpdate(mEMMessageList.size());
    }

    /**
     * 发送消息的逻辑
     * @param username
     * @param msg
     */
    @Override
    public void sendMessage(String username, String msg) {
        EMMessage emMessage = EMMessage.createTxtSendMessage(msg,username);
        emMessage.setStatus(EMMessage.Status.INPROGRESS);
        mEMMessageList.add(emMessage);
        chatView.onUpdate(mEMMessageList.size());//更新消息
        emMessage.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        chatView.onUpdate(mEMMessageList.size());
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        chatView.onUpdate(mEMMessageList.size());
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        EMClient.getInstance().chatManager().sendMessage(emMessage);
    }
}

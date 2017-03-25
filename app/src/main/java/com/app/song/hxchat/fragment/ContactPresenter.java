package com.app.song.hxchat.fragment;

/**
 * Created by song on 2017/3/19 13:25
 */


public interface ContactPresenter {
    void initContacts();//获取联系人

    void updateContacts();//更新联系人

    void deleteContact(String contact);//删除联系人
}

package com.app.song.hxchat.fragment;

import android.util.Log;

import com.app.song.hxchat.db.DBUtils;
import com.app.song.hxchat.utils.ThreadUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by song on 2017/3/19 13:26
 */


public class ContactPresenterImp implements ContactPresenter{
    private ContactView contactView;
    private List<String> contactList = new ArrayList<>();
    public ContactPresenterImp(ContactView contactView) {
        this.contactView=contactView;
    }

    @Override
    public void initContacts() {
        /**
         * 1.首先访问本地缓存联系人
         * 2.开启子线程去环信获取当前用户的联系人
         * 3.更新本地缓存，刷新UI
         */
        String currentUser= EMClient.getInstance().getCurrentUser();
        //TODO 从本地数据库获取联系人
        List<String> contacts= DBUtils.getContacts(currentUser);
        Log.d("AAAAAAAAAAAAA", String.valueOf(contacts));
        contactList.clear();
        contactList.addAll(contacts);
        contactView.onInitContacts(contactList);
        updateContactsFromServer(currentUser);//从环信获取联系人

    }

    private void updateContactsFromServer(final String currentUser) {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                List<String>contactFromServer= null;
                try {
                    contactFromServer = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    Log.d("AAAAAAAAAAAAA", String.valueOf(contactFromServer));

                    //排序
                    Collections.sort(contactFromServer, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return o1.compareTo(o2);
                        }
                    });
                    //更新本地缓存
                    DBUtils.updateContacts(currentUser,contactFromServer);
                    contactList.clear();
                    contactList.addAll(contactFromServer);
                    //通知UI更新主界面
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            contactView.updateContacts(true,null);
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            contactView.updateContacts(false,e.getMessage());
                        }
                    });
                }
            }
        });
    }

    @Override
    public void updateContacts() {
        updateContactsFromServer(EMClient.getInstance().getCurrentUser());
    }

    @Override
    public void deleteContact(final String contact) {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(contact);
                    afterDelete(contact, true,null);
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    afterDelete(contact,false,e.toString());
                }

            }
        });
    }

    private void afterDelete(final String contact, final boolean success, final String msg) {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                contactView.onDelete(contact, success, msg);
            }
        });
    }
}

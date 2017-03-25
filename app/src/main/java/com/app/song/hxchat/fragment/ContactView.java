package com.app.song.hxchat.fragment;

import java.util.List;

/**
 * Created by song on 2017/3/19 13:26
 */


public interface ContactView {
    void onInitContacts(List<String> contacts);

    void updateContacts(boolean success,String msg);

    void onDelete(String contact,boolean success,String msg);
}

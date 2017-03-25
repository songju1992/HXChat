package com.app.song.hxchat.view;

import com.app.song.hxchat.model.User;

import java.util.List;

/**
 * Created by song on 2017/3/25 21:44
 */


public interface AddFriendView {
    void onAddResult(String username, boolean success, String msg);
    void onSearchResult(List<User> userList, List<String> contactsList, boolean success, String msg);

}

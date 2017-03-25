package com.app.song.hxchat.presenter;

import android.util.Log;

import com.app.song.hxchat.db.DBUtils;
import com.app.song.hxchat.model.User;
import com.app.song.hxchat.utils.ThreadUtils;
import com.app.song.hxchat.view.AddFriendActivity;
import com.app.song.hxchat.view.AddFriendView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by song on 2017/3/25 21:44
 */


public class AddFriendPresenterImp implements AddFriendPresenter{

    private AddFriendView addFriendView;
    public AddFriendPresenterImp(AddFriendView addFriendView) {
        this.addFriendView=addFriendView;
    }
    @Override
    public void searchFriend(String keyword) {
        final String currentUser = EMClient.getInstance().getCurrentUser();
        BmobQuery<User> query = new BmobQuery<User>();
        /**
         * 参数1：数据库字段的名称
         */
        query.addWhereStartsWith("username", keyword)
                .addWhereNotEqualTo("username", currentUser)
                .findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if (e == null && list != null && list.size() > 0) {
                            Log.d("BBBBBBBBBBBB", String.valueOf(list));
                            List<String> contacts = DBUtils.getContacts(currentUser);
                            Log.d("BBBBBBBBBBBB", String.valueOf(contacts));
                            //获取到数据
                            addFriendView.onSearchResult(list, contacts, true, null);
                        } else {
                            //没有找到数据
                            if (e == null) {
                                addFriendView.onSearchResult(null, null, false, "没有找到对应的用户。");
                            } else {
                                addFriendView.onSearchResult(null, null, false, e.getMessage());
                            }
                        }
                    }
                });

    }

    @Override
    public void addFriend(final String username) {
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().addContact(username,"想加您为好友，一起写代码！");
                    //添加成功（仅仅代表这个请求发送成功了，对方有没有同意是另外一会儿事儿）
                    onAddResult(username,true,null);
                } catch (HyphenateException e) {
                    //添加失败
                    e.printStackTrace();
                    onAddResult(username,false,e.getMessage());

                }
            }
        });
    }

    private void onAddResult(final String username, final boolean success, final String msg) {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                addFriendView.onAddResult(username, success, msg);
            }
        });
    }
}

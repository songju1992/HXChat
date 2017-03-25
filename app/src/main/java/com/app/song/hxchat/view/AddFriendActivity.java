package com.app.song.hxchat.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.app.song.hxchat.BaseActivity;
import com.app.song.hxchat.R;
import com.app.song.hxchat.adapter.AddFriendAdapter;
import com.app.song.hxchat.model.User;
import com.app.song.hxchat.presenter.AddFriendPresenter;
import com.app.song.hxchat.presenter.AddFriendPresenterImp;
import com.app.song.hxchat.utils.ToastUtils;

import java.util.List;

public class AddFriendActivity extends BaseActivity implements SearchView.OnQueryTextListener,AddFriendView, AddFriendAdapter.OnAddFriendClickListener {
    private Toolbar toolbar;
    private ImageView iv_nodata;
    private RecyclerView recyclerView;

    private SearchView mSearchView;
    private AddFriendPresenter addFriendPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        initView();
        addFriendPresenter=new AddFriendPresenterImp(this);
    }

    private void initView() {
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        iv_nodata= (ImageView) findViewById(R.id.iv_nodata);
        toolbar.setTitle("搜好友");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_friend_menu,menu);
        /**
         * 初始化菜单中的SearchView
         */
        MenuItem menuItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) menuItem.getActionView();
        /**
         * 在SearchView中添加提示
         */
        mSearchView.setQueryHint("搜好友");
        mSearchView.setOnQueryTextListener(this);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (TextUtils.isEmpty(query)){
            ToastUtils.showToast(this,"请输入用户名再搜索！");
            return false;
        }
        addFriendPresenter.searchFriend(query);
        //隐藏软键盘
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mSearchView.getWindowToken(),0);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!TextUtils.isEmpty(newText )){
            ToastUtils.showToast(this,newText);
        }
        return true;
    }

    @Override
    public void onAddResult(String username, boolean success, String msg) {
        Snackbar.make(toolbar,"添加好友"+username+(success?"成功":"失败:"+msg),Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void onSearchResult(List<User> userList, List<String> contactsList, boolean success, String msg) {
        if (success){
            iv_nodata.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            AddFriendAdapter addFriendAdapter = new AddFriendAdapter(userList, contactsList);
            addFriendAdapter.setOnAddFriendClickListener(this);
            recyclerView.setAdapter(addFriendAdapter);
        }else {
            iv_nodata.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
    //添加好友
    @Override
    public void onAddClick(String username) {
        addFriendPresenter.addFriend(username);
    }
}

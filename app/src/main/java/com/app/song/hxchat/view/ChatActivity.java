package com.app.song.hxchat.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.song.hxchat.BaseActivity;
import com.app.song.hxchat.R;
import com.app.song.hxchat.adapter.ChatAdapter;
import com.app.song.hxchat.presenter.ChatPresenter;
import com.app.song.hxchat.presenter.ChatPresenterImp;
import com.app.song.hxchat.utils.ToastUtils;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class ChatActivity extends BaseActivity implements TextWatcher, ChatView, View.OnClickListener {
    private Toolbar toolbar;
    private TextView tv_title;
    private RecyclerView recyclerView;
    private EditText et_msg;
    private Button btn_send;
    private String mUsername;

    private ChatPresenter chatPresenter;
    private ChatAdapter chatAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        chatPresenter=new ChatPresenterImp(this);
        chatPresenter.initChat(mUsername);
        EventBus.getDefault().register(this);
    }

    private void initView() {
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        tv_title= (TextView) findViewById(R.id.tv_title);
        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        et_msg= (EditText) findViewById(R.id.et_msg);
        btn_send= (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        //聊天对象
        mUsername = intent.getStringExtra("username");
        Log.d("BBBBB",mUsername);
        if (TextUtils.isEmpty(mUsername)){
            ToastUtils.showToast(this,"跟鬼聊呀，请携带username参数！");
            finish();
            return;
        }
        tv_title.setText("与"+ mUsername +"聊天中");

        et_msg.addTextChangedListener(this);
        String msg = et_msg.getText().toString();
        if (TextUtils.isEmpty(msg)){
            btn_send.setEnabled(false);
        }else {
            btn_send.setEnabled(true);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length()==0){
            btn_send.setEnabled(false);
        }else{
            btn_send.setEnabled(true);
        }
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
    public void onInit(List<EMMessage> emMessageList) {
        //初始化Recyleview
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(emMessageList);
        recyclerView.setAdapter(chatAdapter);
        if (emMessageList.size()!=0){
            recyclerView.scrollToPosition(emMessageList.size()-1);
        }
    }

    /**
     * 更新聊天消息
     * @param size
     */
    @Override
    public void onUpdate(int size) {
        chatAdapter.notifyDataSetChanged();
        if(size!=0){
            recyclerView.smoothScrollToPosition(size-1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage emMessage){
        //当收到新消息时候
        /**
         * 1.判断当前消息是否正在聊天用户给自己发的
         * 2.如果是，让ChatPresenter去更新数据
         */
        String from = emMessage.getFrom();
        if(from.equals(mUsername)){
        chatPresenter.updateData(mUsername);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 发送消息
     * @param v
     */
    @Override
    public void onClick(View v) {
        String text = et_msg.getText().toString();
        chatPresenter.sendMessage(mUsername,text);
        et_msg.getText().clear();

    }
}

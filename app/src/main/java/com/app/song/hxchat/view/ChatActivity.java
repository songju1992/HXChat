package com.app.song.hxchat.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.song.hxchat.BaseActivity;
import com.app.song.hxchat.R;
import com.app.song.hxchat.utils.ToastUtils;

public class ChatActivity extends BaseActivity implements TextWatcher {
    private Toolbar toolbar;
    private TextView tv_title;
    private RecyclerView recyclerView;
    private EditText et_msg;
    private Button btn_send;
    private String mUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
    }

    private void initView() {
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        tv_title= (TextView) findViewById(R.id.tv_title);
        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        et_msg= (EditText) findViewById(R.id.et_msg);
        btn_send= (Button) findViewById(R.id.btn_send);
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
}

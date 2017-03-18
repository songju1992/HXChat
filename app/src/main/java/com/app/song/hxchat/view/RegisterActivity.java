package com.app.song.hxchat.view;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.song.hxchat.BaseActivity;
import com.app.song.hxchat.R;
import com.app.song.hxchat.presenter.RegisterPresenter;
import com.app.song.hxchat.presenter.RegisterPresenterIml;
import com.app.song.hxchat.utils.SharePreferencesHelper;
import com.app.song.hxchat.utils.StringUtils;
import com.app.song.hxchat.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by song on 2017/3/15 19:32
 */


public class RegisterActivity extends BaseActivity implements RegisterView, View.OnClickListener, TextView.OnEditorActionListener {
    private EditText et_username;
    private EditText et_pwd;
    private Button btnRegist;
    private TextInputLayout til_pwd;
    private TextInputLayout til_username;
    private RegisterPresenter mRegisterPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //沉浸式状态栏
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0以上的手机
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        initView();
        mRegisterPresenter=new RegisterPresenterIml(this);
    }

    private void initView() {
        et_username= (EditText) findViewById(R.id.et_username);
        et_pwd= (EditText) findViewById(R.id.et_pwd);
        til_pwd= (TextInputLayout) findViewById(R.id.til_pwd);
        til_username= (TextInputLayout) findViewById(R.id.til_username);
        btnRegist= (Button) findViewById(R.id.btn_regist);
        btnRegist.setOnClickListener(this);
        et_pwd.setOnEditorActionListener(this);
    }

    @Override
    public void onClick(View view) {
        regist();
    }
    private void regist() {
        String username = et_username.getText().toString().trim();
        String pwd = et_pwd.getText().toString().trim();

        if (!StringUtils.checkUsername(username)){
            til_username.setErrorEnabled(true);
            til_username.setError("用户名不合法");
            et_username.requestFocus(View.FOCUS_RIGHT);
            return;
        }else {
            til_username.setErrorEnabled(false);
        }
        if (!StringUtils.checkPwd(pwd)){
            til_pwd.setErrorEnabled(true);
            til_pwd.setError("密码不合法");
            et_pwd.requestFocus(View.FOCUS_RIGHT);
            return;
        }else{
            til_pwd.setErrorEnabled(false);
        }
        showDialog("正在注册...");
        mRegisterPresenter.regist(username,pwd);
    }

    @Override
    public void onRegister(String username, String pwd, boolean isSuccess, String msg) {
        hideDialog();
        if(isSuccess){
            /**
             * 将注册成功的数据保存到本地
             *
             * 跳转到登录界面
             */
            SharePreferencesHelper.getInstance(this).putString("username",username);
            SharePreferencesHelper.getInstance(this).putString("pwd",pwd);

            startActivity(LoginActivity.class,true);

        }else{
            //注册失败显示Toast
            ToastUtils.showToast(this,msg);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
        if (v.getId() == R.id.et_pwd) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                regist();
                return true;
            }
        }
        return false;
    }
}

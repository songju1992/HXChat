package com.app.song.hxchat.view;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.song.hxchat.BaseActivity;
import com.app.song.hxchat.MainActivity;
import com.app.song.hxchat.R;
import com.app.song.hxchat.presenter.LoginPresenter;
import com.app.song.hxchat.presenter.LoginPresenterImp;
import com.app.song.hxchat.utils.SharePreferencesHelper;
import com.app.song.hxchat.utils.StringUtils;
import com.app.song.hxchat.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by song on 2017/3/15 21:55
 */


public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginView, TextView.OnEditorActionListener {

    private static final int REQUEST_SDCARD = 1;
    private TextView newuser;
    private EditText et_username;
    private EditText et_pwd;
    private Button btn_login;
    private TextInputLayout til_username;
    private TextInputLayout til_pwd;

    private LoginPresenter mLoginPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginPresenter=new LoginPresenterImp(this);
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
    }

    private void initView() {
        newuser= (TextView) findViewById(R.id.tv_newuser);
        et_pwd= (EditText) findViewById(R.id.et_pwd);
        et_username= (EditText) findViewById(R.id.et_username);
        btn_login= (Button) findViewById(R.id.btn_login);
        til_username= (TextInputLayout) findViewById(R.id.til_username);
        til_pwd= (TextInputLayout) findViewById(R.id.til_pwd);

        newuser.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        et_pwd.setOnEditorActionListener(this);//绑定回调键盘的监听
    }

    /**
     * 当再次start到这个Activity的时候，接收新的Intent对象
     * 调用的前提是启动该模式是SingleTask,或者是singleTop但是他们得在最上面才有效
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        et_username.setText(SharePreferencesHelper.getInstance(this).getString("username"));
        et_pwd.setText(SharePreferencesHelper.getInstance(this).getString("pwd"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                //登录，登录成功进入到主界面
                login();
                break;
            case R.id.tv_newuser:
                startActivity(RegisterActivity.class,false);
                break;
        }
    }

    private void login() {
        String username=et_username.getText().toString().trim();
        String pwd=et_pwd.getText().toString().trim();

        if(!StringUtils.checkUsername(username)){
            til_username.setErrorEnabled(true);
            til_username.setError("用户名不合法");

            et_username.requestFocus(View.FOCUS_RIGHT);

            return;
        }else{
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
        /**
         * 1. 动态申请权限
         */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PermissionChecker.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_SDCARD);
            return;
        }

        showDialog("正在玩命登录中...");
        mLoginPresenter.isLogin(username,pwd);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_SDCARD){
            if (grantResults[0]==PermissionChecker.PERMISSION_GRANTED){
                //被授权了
                login();
            }else{
                ToastUtils.showToast(this,"没有给予该应用权限，不让你用了");
            }
        }
    }

    @Override
    public void loginSuccessed(String username, String pwd, boolean isLoginSuccess, String msg) {
        hideDialog();
        if(isLoginSuccess){
            /**
             * 保存用户
             * 跳转到主界面
             */
            SharePreferencesHelper.getInstance(this).putString("username",username);
            SharePreferencesHelper.getInstance(this).putString("pwd",pwd);
            startActivity(MainActivity.class,true);
        }else{
            ToastUtils.showToast(this,msg);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(v.getId()==R.id.et_pwd){
            if(actionId== EditorInfo.IME_ACTION_DONE){
                login();
            }
        }
        return false;
    }
}

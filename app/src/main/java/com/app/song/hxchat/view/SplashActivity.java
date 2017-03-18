package com.app.song.hxchat.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.app.song.hxchat.BaseActivity;
import com.app.song.hxchat.MainActivity;
import com.app.song.hxchat.R;
import com.app.song.hxchat.presenter.SplashPresenter;
import com.app.song.hxchat.presenter.SplashPresenterImp;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity implements SplashView{

    @BindView(R.id.iv_splash)
    ImageView ivSplash;
    private static final long DURATION = 2000;
    private SplashPresenter mSplashPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
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
        ButterKnife.bind(this);
        mSplashPresenter = new SplashPresenterImp(this);
        mSplashPresenter.checkLogined();
    }

    /**
     * 1.判断是否已经登录
     * 2.如果登录过直接进入MainActivity
     * 3.否则闪屏2s （渐变动画） 进入主页面
     */
    @Override
    public void onCheckedLogin(boolean isLogined) {
        if (isLogined){
            startActivity(MainActivity.class,true);
        }else {
//            否则闪屏2秒后（渐变动画），进入LoginActivity
            ObjectAnimator alpha = ObjectAnimator.ofFloat(ivSplash, "alpha", 0, 1).setDuration(DURATION);
            alpha.start();
            alpha.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    startActivity(LoginActivity.class,true);
                }
            });

        }
    }
}

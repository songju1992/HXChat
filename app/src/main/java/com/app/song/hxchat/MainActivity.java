package com.app.song.hxchat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.app.song.hxchat.utils.FragmentFactory;
import com.app.song.hxchat.utils.ToastUtils;
import com.app.song.hxchat.view.LoginActivity;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

public class MainActivity extends BaseActivity {
    private Toolbar mToolbar;
    private BottomNavigationBar bottom_navigation_bar;
    private TextView tv_title;
    private int[] titleId={R.string.conversation,R.string.contact,R.string.plugin};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initToobar();
        initBottomNavigation();
        initFirstFragment();
    }

    private void initView() {
        mToolbar= (Toolbar) findViewById(R.id.toolbar);
        bottom_navigation_bar= (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        tv_title= (TextView) findViewById(R.id.tv_title);
    }

    private void initBottomNavigation() {
        BottomNavigationItem conversationItem = new BottomNavigationItem(R.mipmap.conversation_selected_2,"消息");
        //        conversationItem.setActiveColor(getResources().getColor(R.color.btn_normal));//选中的颜色
//        conversationItem.setInActiveColor(getColoretResources().getColor(R.color.inActive));//没选中的颜色
        bottom_navigation_bar.addItem(conversationItem);

        BottomNavigationItem contactItem = new BottomNavigationItem(R.mipmap.contact_selected_2,"联系人");
//        contactItem.setActiveColor(getResources().getColor(R.color.btn_normal));//选中的颜色
//        contactItem.setInActiveColor(getResources().getColor(R.color.inActive));//没选中的颜色
        bottom_navigation_bar.addItem(contactItem);

        BottomNavigationItem pluginItem = new BottomNavigationItem(R.mipmap.plugin_selected_2,"动态");
//        pluginItem.setActiveColor(getResources().getColor(R.color.btn_normal));//选中的颜色
//        pluginItem.setInActiveColor(getResources().getColor(R.color.inActive));//没选中的颜色
        bottom_navigation_bar.addItem(pluginItem);

        bottom_navigation_bar.setActiveColor(R.color.btn_normal);
        bottom_navigation_bar.setInActiveColor(R.color.inActive);

        bottom_navigation_bar.initialise();
        bottom_navigation_bar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                /**
                 * 先判断当前Fragment是否被添加到了MainActivity中
                 * 如果添加了则直接显示即可
                 * 如果没有添加则添加，然后显示
                 */

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                BaseFragment fragment = FragmentFactory.getFragment(position);
                if (!fragment.isAdded()){
                    transaction.add(R.id.fl_content,fragment,""+position);
                }
                transaction.show(fragment).commit();

                tv_title.setText(titleId[position]);
            }

            @Override
            public void onTabUnselected(int position) {
                getSupportFragmentManager().beginTransaction().hide(FragmentFactory.getFragment(position)).commit();
            }

            @Override
            public void onTabReselected(int position) {

            }
        });

    }

    private void initToobar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tv_title.setText(titleId[0]);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initFirstFragment() {
        /**
         * 如果这个Activity中已经有老（就是Activity保存的历史的状态，又恢复了）的Fragment，先全部移除
         */
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        for(int i=0;i<titleId.length;i++){
            Fragment fragment = supportFragmentManager.findFragmentByTag(i + "");
            if (fragment!=null){
                fragmentTransaction.remove(fragment);
            }
        }
        fragmentTransaction.commit();

//        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, FragmentFactory.getFragment(0),"0").commit();

        tv_title.setText(R.string.conversation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuBuilder builder = (MenuBuilder) menu;
        builder.setOptionalIconsVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_friend:
//                startActivity(AddFriendActivity.class,false);
                break;
            case R.id.menu_scan:
                ToastUtils.showToast(this,"分享好友");

                break;
            case R.id.menu_about:
                ToastUtils.showToast(this,"关于我们");
                break;
            case android.R.id.home:
                finish();
                break;

        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}

package com.app.song.hxchat;

import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import com.app.song.hxchat.db.DBUtils;
import com.app.song.hxchat.event.OnContactUpdateEvent;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;

import static com.hyphenate.chat.EMClient.TAG;

/**
 * Created by song on 2017/3/15 17:47
 */

//application的生命周期跟进程的生命周期一致
public class HXApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        initHX();//初始化环信
        initBom();//集成BOM后端云
        initDB();
    }

    private void initDB() {
        DBUtils.initDB(this);
    }

    private void initBom() {
        Bmob.initialize(this,"3133593ce45603385971cb9edcad9cf0");
    }

    private void initHX() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        options.setAutoLogin(true);
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null ||!processAppName.equalsIgnoreCase(this.getPackageName())) {
            Log.e(TAG, "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        //初始化
        EMClient.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
        //添加通讯录监听
        initContactListener();
        //添加消息监听
        initMessageListener();
    }

    private void initMessageListener() {
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                //收到消息
                if(list!=null&&list.size()>0){
                    Log.d(TAG,"messgae"+list.get(0).getBody());
                    EventBus.getDefault().post(list.get(0));
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageRead(List<EMMessage> list) {
                //收到已读回执
            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {
                //收到已送达回执
            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        });
    }


    private void initContactListener() {
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(String s) {
                //好友请求被同意
                //发出通知让ContactFragment更新UI
                EventBus.getDefault().post(new OnContactUpdateEvent(s,true));
            }

            @Override
            public void onContactDeleted(String s) {
                //好友被删除的回调
                EventBus.getDefault().post(new OnContactUpdateEvent(s, false));
                Log.d(TAG, "onContactDeleted: " + s);
            }

            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友的邀请
                Log.d(TAG, "onContactInvited: " + username + "/" + reason);
                //同意或者拒绝
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(username);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFriendRequestAccepted(String s) {
                //添加了联系人回调
                EventBus.getDefault().post(new OnContactUpdateEvent(s,true));

            }

            @Override
            public void onFriendRequestDeclined(String s) {
                //好友请求被拒绝的回调
            }
        });
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}

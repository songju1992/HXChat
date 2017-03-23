package com.app.song.hxchat.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.song.hxchat.MainActivity;
import com.app.song.hxchat.R;
import com.app.song.hxchat.BaseFragment;
import com.app.song.hxchat.utils.ToastUtils;
import com.app.song.hxchat.view.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 * 动态fragment
 */
public class PluginFragment extends BaseFragment implements PluginView{

    private PluginPresenter mPluginPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plugin, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPluginPresenter=new PluginPresenterImp(this);
    }

    @Override
    public void login_out(String username,boolean loginOutSuccessed, String msg) {
        if(loginOutSuccessed){
            //退出成功
            MainActivity activity = (MainActivity)getActivity();
            activity.startActivity(LoginActivity.class,true);
        }else{
            ToastUtils.showToast(getContext(),msg);
        }
    }
}

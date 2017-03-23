package com.app.song.hxchat.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.song.hxchat.R;
import com.app.song.hxchat.BaseFragment;
import com.hyphenate.chat.EMConversation;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 *
 * 会话fragment
 */
public class ConversationFragment extends BaseFragment implements ConversationView, View.OnClickListener {
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private ConversationPresenter conversationPresenter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conversation, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerView);
        floatingActionButton= (FloatingActionButton) view.findViewById(R.id.fab);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//设置布局管理器
        floatingActionButton.setOnClickListener(this);
        conversationPresenter=new ConversationPresenterImp(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onInitConversation(List<EMConversation> emConversationList) {

    }
}

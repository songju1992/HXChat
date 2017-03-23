package com.app.song.hxchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.song.hxchat.R;

/**
 * Created by song on 2017/3/18 18:35
 */


public class ConversationAdapter extends RecyclerView.Adapter{
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_conversation,parent,false);
        ConversationViewHolder conversationViewHolder=new ConversationViewHolder(view);
        return conversationViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder {
        TextView mTvUsername;
        TextView mTvTime;
        TextView mTvMsg;
        TextView mTvUnread;
        public ConversationViewHolder(View itemView) {
            super(itemView);
            mTvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mTvMsg = (TextView) itemView.findViewById(R.id.tv_msg);
            mTvUnread = (TextView) itemView.findViewById(R.id.tv_unread);
        }
    }
}

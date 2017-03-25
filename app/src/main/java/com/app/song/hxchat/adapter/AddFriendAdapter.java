package com.app.song.hxchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.song.hxchat.R;
import com.app.song.hxchat.model.User;

import java.util.List;

/**
 * Created by song on 2017/3/25 22:06
 */


public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.AddFriendViewHolder>{
    private List<User> mUserList;
    private List<String> mContactsList;

    public AddFriendAdapter(List<User> userList,List<String> contacts) {
        mUserList = userList;
        mContactsList = contacts;
    }

    @Override
    public AddFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_search, parent, false);
        AddFriendViewHolder addFriendViewHolder = new AddFriendViewHolder(view);
        return addFriendViewHolder;
    }

    @Override
    public void onBindViewHolder(AddFriendViewHolder holder, int position) {
        User user = mUserList.get(position);
        final String username = user.getUsername();
        holder.mTvUsername.setText(username);
        holder.mTvTime.setText(user.getCreatedAt());
        //判断当前username是不是已经在好友列表中了
        if (mContactsList.contains(username)){
            holder.mBtnAdd.setText("已经是好友");
            holder.mBtnAdd.setEnabled(false);
        }else{
            holder.mBtnAdd.setText("添加");
            holder.mBtnAdd.setEnabled(true);
        }
        holder.mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnAddFriendClickListener!=null){
                    mOnAddFriendClickListener.onAddClick(username);
                }
            }
        });
    }

    private OnAddFriendClickListener mOnAddFriendClickListener;
    public interface OnAddFriendClickListener{
        void onAddClick(String username);
    }
    public void setOnAddFriendClickListener(OnAddFriendClickListener addFriendClickListener){
        this.mOnAddFriendClickListener = addFriendClickListener;
    }




    @Override
    public int getItemCount() {
        return mUserList == null ? 0 : mUserList.size();
    }

    class AddFriendViewHolder extends RecyclerView.ViewHolder {

        TextView mTvUsername;
        TextView mTvTime;
        Button mBtnAdd;

        public AddFriendViewHolder(View itemView) {
            super(itemView);
            mTvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mBtnAdd = (Button) itemView.findViewById(R.id.btn_add);
        }
    }

}

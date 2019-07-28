package com.prashantdhiman.chatie.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prashantdhiman.chatie.R;
import com.prashantdhiman.chatie.activities.ChatActivity;
import com.prashantdhiman.chatie.models.ChatObject;
import com.prashantdhiman.chatie.models.UserObject;

import java.util.ArrayList;

public class UserChatAdapter extends RecyclerView.Adapter<UserChatAdapter.ViewHolder> {
    Context context;
    ArrayList<ChatObject> userChatList;

    public UserChatAdapter(Context context, ArrayList<ChatObject> userChatList) {
        this.context = context;
        this.userChatList = userChatList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(layoutParams);

        return new UserChatAdapter.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mChatTitle.setText(userChatList.get(position).getChatId());

        holder.mUserChatLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),ChatActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("chatId",userChatList.get(holder.getAdapterPosition()).getChatId());
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userChatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mChatTitle;
        private LinearLayout mUserChatLinearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mChatTitle=itemView.findViewById(R.id.chatTitle);
            mUserChatLinearLayout=itemView.findViewById(R.id.userChatLinearLayout);
        }
    }
}

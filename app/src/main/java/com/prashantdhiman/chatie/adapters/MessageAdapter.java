package com.prashantdhiman.chatie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prashantdhiman.chatie.R;
import com.prashantdhiman.chatie.models.MessageObject;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    Context context;
    ArrayList<MessageObject> messageList;

    public MessageAdapter(Context context, ArrayList<MessageObject> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(layoutParams);

        return new MessageAdapter.ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mMessageTextView.setText(messageList.get(position).getMessage());
        holder.mSenderTextView.setText(messageList.get(position).getSenderId());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mMessageTextView,mSenderTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mMessageTextView=itemView.findViewById(R.id.messageTextView);
            mSenderTextView=itemView.findViewById(R.id.senderTextView);
        }
    }
}

package com.prashantdhiman.chatie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prashantdhiman.chatie.R;
import com.prashantdhiman.chatie.models.UserObject;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    Context context;
    ArrayList<UserObject> userList;

    public UserListAdapter(Context context, ArrayList<UserObject> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(layoutParams);
        return new ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mNameTextView.setText(userList.get(position).getName());
        holder.mPhoneTextView.setText(userList.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mNameTextView,mPhoneTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mNameTextView=itemView.findViewById(R.id.nameTextView);
            mPhoneTextView=itemView.findViewById(R.id.phoneTextView);
        }
    }
}

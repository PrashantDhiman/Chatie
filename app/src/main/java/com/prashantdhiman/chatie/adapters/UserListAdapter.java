package com.prashantdhiman.chatie.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.mNameTextView.setText(userList.get(position).getName());
        holder.mPhoneTextView.setText(userList.get(position).getPhone());

        holder.mUserListLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String key= FirebaseDatabase.getInstance().getReference()
                        .child("chat")
                        .push()
                        .getKey();

                //for current user
                DatabaseReference mDb1=FirebaseDatabase.getInstance().getReference()
                        .child("user")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("chat")
                        .child(key);

                mDb1.child("sender").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid()).isSuccessful();  // 1 for sender
                mDb1.child("receiver").setValue(userList.get(position).getUId());                       // 0 for receiver

                //for user selected from contacts
                DatabaseReference mDb2=FirebaseDatabase.getInstance().getReference()
                        .child("user")
                        .child(userList.get(position).getUId())
                        .child("chat")
                        .child(key);

                mDb2.child("sender").setValue(userList.get(position).getUId());
                mDb2.child("receiver").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

                ((Activity)view.getContext()).finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mNameTextView,mPhoneTextView;
        private LinearLayout mUserListLinearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mNameTextView=itemView.findViewById(R.id.nameTextView);
            mPhoneTextView=itemView.findViewById(R.id.phoneTextView);
            mUserListLinearLayout=itemView.findViewById(R.id.itemUserLinearLayout);
        }
    }
}

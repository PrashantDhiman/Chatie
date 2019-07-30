package com.prashantdhiman.chatie.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.mMessageTextView.setText(messageList.get(position).getMessage());
        holder.mSenderNameTextView.setText(messageList.get(position).getSenderName());



        holder.mMesaageLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                AlertDialog.Builder deleteAlertDialog=new AlertDialog.Builder(view.getContext());
                deleteAlertDialog
                        .setTitle("Delete?")
                        .setMessage("Delete this message?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase.getInstance().getReference()
                                        .child("chat")
                                        .child(messageList.get(position).getChatId())
                                        .child(messageList.get(position).getMessageId())
                                        .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                ((Activity)view.getContext()).finish();
                                                view.getContext().startActivity(((Activity)view.getContext()).getIntent());

                                                Toast.makeText(view.getContext(),"Deleted Successfully",Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(view.getContext(),"Unable to delete",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout mMesaageLinearLayout;
        private TextView mMessageTextView,mSenderNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mMesaageLinearLayout=itemView.findViewById(R.id.messageLinearLayout);

            mMessageTextView=itemView.findViewById(R.id.messageTextView);
            mSenderNameTextView=itemView.findViewById(R.id.senderNameTextView);
        }
    }
}

package com.prashantdhiman.chatie.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prashantdhiman.chatie.R;
import com.prashantdhiman.chatie.adapters.MessageAdapter;
import com.prashantdhiman.chatie.models.MessageObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private EditText mMessageEditText;
    private Button mSendButton;

    private RecyclerView mMessageRecyclerView;
    private RecyclerView.Adapter mMessageAdapter;

    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar mChatActivityProgressBar;
    private RelativeLayout mChatActivityLayout;

    private ArrayList<MessageObject> messageList;

    private String chatId,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mMessageEditText=findViewById(R.id.messageEditText);
        mSendButton=findViewById(R.id.sendButton);
        mChatActivityProgressBar=findViewById(R.id.chatActivityProgressBar);
        mChatActivityLayout=findViewById(R.id.chatActivityLayout);

        messageList =new ArrayList<>();
        initializeMessageRecyclerView();

        Intent intent=getIntent();
        chatId=intent.getStringExtra("chatId");
        name=intent.getStringExtra("name");

        mChatActivityProgressBar.setVisibility(View.VISIBLE);
        fetchChatMessages();

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    public void fetchChatMessages() {

        //Log.i("insidefetchchat","reached");
        DatabaseReference messageDb=FirebaseDatabase.getInstance().getReference().child("chat").child(chatId);

        messageDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    mChatActivityProgressBar.setVisibility(View.GONE);

                    Snackbar.make(mChatActivityLayout,"Say hello to "+ name,Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        messageDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    //Log.i("insideif","reached");
                    String text="";
                    String senderId="";
                    String senderName="";

                    if(dataSnapshot.child("text").getValue()!=null)
                        text=dataSnapshot.child("text").getValue().toString();
                    if(dataSnapshot.child("senderId").getValue()!=null)
                        senderId=dataSnapshot.child("senderId").getValue().toString();
                    if(dataSnapshot.child("senderId").getValue()!=null){
                        if(dataSnapshot.child("senderName").child("displayName").exists()){
                            senderName=dataSnapshot.child("senderName").child("displayName").getValue().toString();
                        }else{
                            senderName=dataSnapshot.child("senderName").getValue().toString();
                        }
                    }

                    MessageObject messageObject=new MessageObject(chatId,dataSnapshot.getKey(),senderId,senderName,text);
                    messageList.add(messageObject);
                    mLinearLayoutManager.scrollToPosition(messageList.size()-1);
                    mMessageAdapter.notifyDataSetChanged();

                    mChatActivityProgressBar.setVisibility(View.GONE);
                }else{
                    Log.i("insideelse","reached");
                    mChatActivityProgressBar.setVisibility(View.GONE);

                    Snackbar.make(mChatActivityLayout,"Say hello to "+ name,Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage() {
        if(!mMessageEditText.getText().toString().isEmpty()){

            DatabaseReference newMessageDb=FirebaseDatabase.getInstance().getReference().child("chat").child(chatId).push();

            Map messageMap=new HashMap<>();
            messageMap.put("text",mMessageEditText.getText().toString());
            messageMap.put("senderId", FirebaseAuth.getInstance().getCurrentUser().getUid());
            messageMap.put("senderName",FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            newMessageDb.updateChildren(messageMap);
        }
        mMessageEditText.setText(null);
    }

    private void initializeMessageRecyclerView() {
        mMessageRecyclerView=findViewById(R.id.chatActivityRecyclerView);
        mMessageRecyclerView.setNestedScrollingEnabled(false);
        mMessageRecyclerView.setHasFixedSize(false);
        mLinearLayoutManager=new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageAdapter=new MessageAdapter(getApplicationContext(), messageList);
        mMessageRecyclerView.setAdapter(mMessageAdapter);
    }
}

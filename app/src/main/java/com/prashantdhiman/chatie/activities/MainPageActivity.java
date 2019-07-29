package com.prashantdhiman.chatie.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prashantdhiman.chatie.R;
import com.prashantdhiman.chatie.adapters.UserChatAdapter;
import com.prashantdhiman.chatie.adapters.UserListAdapter;
import com.prashantdhiman.chatie.models.ChatObject;

import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity {

    FloatingActionButton mFindUsersButton;

    private RecyclerView mUserChatRecyclerView;
    private RecyclerView.Adapter mUserChatAdapter;

    ArrayList<ChatObject> userChatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        mFindUsersButton=findViewById(R.id.findUsersButton);

        userChatList=new ArrayList<>();

        getPermissions();


        initialiseUserChatRecyclerView();

        getUserChatList();

        mFindUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FindUserActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mUserChatAdapter.notifyDataSetChanged();
    }

    private void getUserChatList() {
        userChatList.clear();
        mUserChatAdapter.notifyDataSetChanged();

        DatabaseReference userChatDB= FirebaseDatabase.getInstance().getReference()
                .child("user")
                .child(FirebaseAuth.getInstance().getUid())
                .child("chat");

        final DatabaseReference userDB= FirebaseDatabase.getInstance().getReference()
                .child("user");

        userChatDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                if(dataSnapshot1.exists()){

                    for(final DataSnapshot childSnapshot:dataSnapshot1.getChildren()){

                        Log.i("childrencount",String.valueOf(dataSnapshot1.getChildrenCount()));

                        String idOfOtherPerson=childSnapshot.child("receiver").getValue().toString();

                        userDB.child(idOfOtherPerson).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                if(dataSnapshot2.exists()){
                                    String name=dataSnapshot2.child("name").getValue().toString();

                                    ChatObject chatObject=new ChatObject(childSnapshot.getKey(),name);
                                    Log.i("added","added");
                                    userChatList.add(chatObject);
                                    mUserChatAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        /*Log.i("idofother",idOfOtherPerson);
                        Log.i("nameofother",nameOfOtherPerson);*/



                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initialiseUserChatRecyclerView() {
        mUserChatRecyclerView=findViewById(R.id.userChatRecyclerView);
        mUserChatRecyclerView.setNestedScrollingEnabled(false);
        mUserChatRecyclerView.setHasFixedSize(false);
        mUserChatRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
        mUserChatAdapter=new UserChatAdapter(getApplicationContext(), userChatList);
        mUserChatRecyclerView.setAdapter(mUserChatAdapter);
    }

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS},1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logoutButton){
            FirebaseAuth.getInstance().signOut();

            Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

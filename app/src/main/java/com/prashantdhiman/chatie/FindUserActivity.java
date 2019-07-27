package com.prashantdhiman.chatie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.prashantdhiman.chatie.adapters.UserListAdapter;
import com.prashantdhiman.chatie.models.UserObject;

import java.util.ArrayList;

public class FindUserActivity extends AppCompatActivity {

    private RecyclerView mUserListRecyclerView;
    private RecyclerView.Adapter mUserListAdapter;

    ArrayList<UserObject> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);

        contactList=new ArrayList<>();

        initializeUserListRecyclerView();
        getContactList();
    }

    private void getContactList(){
        Cursor phones=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while(phones.moveToNext()){
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            UserObject userObject=new UserObject(name,phone);
            contactList.add(userObject);
            mUserListAdapter.notifyDataSetChanged();
        }
    }

    private void initializeUserListRecyclerView() {
        mUserListRecyclerView=findViewById(R.id.userListRecyclerView);
        mUserListRecyclerView.setNestedScrollingEnabled(false);
        mUserListRecyclerView.setHasFixedSize(false);
        mUserListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
        mUserListAdapter=new UserListAdapter(getApplicationContext(), contactList);
        mUserListRecyclerView.setAdapter(mUserListAdapter);
    }
}

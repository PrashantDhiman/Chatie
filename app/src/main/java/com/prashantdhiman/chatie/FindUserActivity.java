package com.prashantdhiman.chatie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.prashantdhiman.chatie.adapters.UserListAdapter;
import com.prashantdhiman.chatie.models.UserObject;

import java.util.ArrayList;

public class FindUserActivity extends AppCompatActivity {

    private RecyclerView mUserListRecyclerView;
    private RecyclerView.Adapter mUserListAdapter;

    ArrayList<UserObject> contactList, userList;  //userList contains only those contacts which are using Chatie

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);

        contactList=new ArrayList<>();
        userList=new ArrayList<>();

        initializeUserListRecyclerView();
        generateContactList();
    }

    private void generateContactList(){
        Cursor phonesCursor=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while(phonesCursor.moveToNext()){
            String name=phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone=phonesCursor.getString(phonesCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            phone=phone.replace(" ","");  //remove spaces,-,() symbols from phone no.
            phone=phone.replace("-","");
            phone=phone.replace("(","");
            phone=phone.replace(")","");

            String isoPrefix=getCountryIso();
            if(!String.valueOf(phone.charAt(0)).equals("+"))
                phone=isoPrefix+phone;

            UserObject contactObject=new UserObject(name,phone);
            contactList.add(contactObject);

            generateUserListUsingContactList(contactObject);  //userList contains only those contacts which are using Chatie
        }
        phonesCursor.close();
    }

    private void generateUserListUsingContactList(UserObject contactObject) {


        DatabaseReference userDB= FirebaseDatabase.getInstance().getReference().child("user");
        Query query=userDB.orderByChild("phone").equalTo(contactObject.getPhone());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String name="";
                    String phone="";

                    for(DataSnapshot childSnapshot:dataSnapshot.getChildren()){
                        if(childSnapshot.child("name").getValue()!=null)
                            name=childSnapshot.child("phone").getValue().toString();
                        if(childSnapshot.child("phone").getValue().toString()!=null)
                            phone=childSnapshot.child("phone").getValue().toString();

                        UserObject userObject= new UserObject(name,phone);
                        userList.add(userObject);
                        mUserListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private String getCountryIso(){
        String iso="";

        TelephonyManager telephonyManager=(TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        if(telephonyManager.getNetworkCountryIso()!=null)
            if(!telephonyManager.getNetworkCountryIso().toString().equals(""))
                iso=telephonyManager.getNetworkCountryIso().toString();

        return CountryToPhonePrefix.getPhone(iso);
    }

    private void initializeUserListRecyclerView() {
        mUserListRecyclerView=findViewById(R.id.userListRecyclerView);
        mUserListRecyclerView.setNestedScrollingEnabled(false);
        mUserListRecyclerView.setHasFixedSize(false);
        mUserListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
        mUserListAdapter=new UserListAdapter(getApplicationContext(), userList);
        mUserListRecyclerView.setAdapter(mUserListAdapter);
    }
}

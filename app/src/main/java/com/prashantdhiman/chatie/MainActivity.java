package com.prashantdhiman.chatie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText mPhoneNoEditText,mCodeEditText;
    private Button mVerifyButton;
    private LinearLayout mMainActivityLinearLayout;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        logInUser();

        mPhoneNoEditText=findViewById(R.id.phoneNoEditText);
        mCodeEditText=findViewById(R.id.codeEditText);
        mVerifyButton=findViewById(R.id.verifyButton);
        mMainActivityLinearLayout=findViewById(R.id.mainActivityLinearLayout);

        mVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mPhoneNoEditText.getText().toString().isEmpty()){
                    if(mVerificationId!=null){            // either this method will be used to login in
                        verifyPhoneNoWithCode();          // which code will be used
                    }
                    startPhoneNoVerification();           //or this method will be used in which
                }                                         //google will authorise automatically without sending code
            }
        });

        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                mVerificationId=s;
                mVerifyButton.setText("Verify");
            }
        };

    }

    private void verifyPhoneNoWithCode() {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(mVerificationId,mCodeEditText.getText().toString());
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    logInUser();
                }else{
                    Snackbar.make(mMainActivityLinearLayout,"Invalid Credentials",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void logInUser() {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            startActivity(new Intent(getApplicationContext(),MainPageActivity.class));
            finish();
        }
    }

    private void startPhoneNoVerification() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(mPhoneNoEditText.getText().toString(),60, TimeUnit.SECONDS,this,mCallbacks);
    }
}
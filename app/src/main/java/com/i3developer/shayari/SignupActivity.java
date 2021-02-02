package com.i3developer.shayari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

public class SignupActivity extends AppCompatActivity {

    private TextInputEditText phoneEdt,nameEdt,otpEdt;
    private Button submitBtn,otpSubmitBtn;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private FrameLayout otpFrame,credentialFrame;
    private BSFProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // To Display custom Action Bar
        setUpAppBar();
        allInitializations(); // All Initialization should be placed inside this method

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(nameEdt.getText())) {
                    showToast(getApplicationContext(),"नाम आवश्यक है");
                }
                else if(TextUtils.isEmpty(phoneEdt.getText()) || phoneEdt.length() != 10) {
                    showToast(getApplicationContext(),"10 अंकों का मोबाइल नंबर आवश्यक है");
                } else {
                    sendVerificationCode("+91"+phoneEdt.getText());
                }
            }
        });
        otpSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(otpEdt.getText())) {
                    showToast(getApplicationContext(),"कृपया ओटीपी दर्ज करें");
                } else {
                    verifyCode(otpEdt.getText().toString());
                }
            }
        });
    }

    private void setUpAppBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.app_bar_layout);
        //Change the Title of Action Bar
        TextView appBarTitle = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_title);
        TextView appBarLeft = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_left);
        TextView appBarRight = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_right);
        appBarTitle.setText("रजिस्टर करें");
        appBarLeft.setText("Back");
        appBarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void allInitializations() {
        phoneEdt = findViewById(R.id.signup_phone);
        nameEdt = findViewById(R.id.signup_name);
        submitBtn = findViewById(R.id.signup_submit);
        mAuth = FirebaseAuth.getInstance();
        otpFrame = findViewById(R.id.signup_otp_frame);
        credentialFrame = findViewById(R.id.signup_credential_frame);
        otpSubmitBtn = findViewById(R.id.signup_submit_otp);
        otpEdt = findViewById(R.id.signup_otp_field);
        progressDialog = new BSFProgressDialog();
    }

    private void sendVerificationCode(String phoneNumber) {
        progressDialog.show(getSupportFragmentManager(),progressDialog.getTag());
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void verifyCode(String otp) {
        progressDialog.show(getSupportFragmentManager(),progressDialog.getTag());
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,otp);
        signInWithPhoneCredential(credential);
    }

    private void signInWithPhoneCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, mAuth.getUid(), Toast.LENGTH_LONG).show();
                } else {
                    showToast(getApplicationContext(),"विफल कृपया दोबारा प्रयास करें");
                }
            }
        });
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code != null) {
                otpEdt.setText(code);
            }
            progressDialog.dismiss();

        }
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            showToast(getApplicationContext(),"ओटीपी सत्यापन विफल हो गया है"+e.getLocalizedMessage());
            progressDialog.dismiss();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
            progressDialog.dismiss();
            credentialFrame.setVisibility(View.GONE);
            otpFrame.setVisibility(View.VISIBLE);
        }
    };
    private void showToast(Context context, String text) {
        ContextThemeWrapper themeWrapper = new ContextThemeWrapper(context,R.style.CustomAlertTheme);
        Toast toast = Toast.makeText(themeWrapper,"",Toast.LENGTH_SHORT);
        toast.setText(text);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

}
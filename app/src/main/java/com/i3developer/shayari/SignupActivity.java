package com.i3developer.shayari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

public class SignupActivity extends AppCompatActivity {


    private TextInputEditText phoneEdt, nameEdt, otpEdt;
    private Button submitBtn, otpSubmitBtn;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private FrameLayout otpFrame, credentialFrame;
    private BSFProgressDialog progressDialog;
    private DatabaseReference referenceDb;
    private TextView otpMessage;
    private SignInButton googleSignInBtn;
    private GoogleSignInClient googleSignInClient;
    private int RC_SIGN_IN = 2323;
    long millisInFuture = 60000;// 60 sec
    long countDownInterval = 1000;// 1 sec
    public TextView textViewTimer;
    private PhoneAuthProvider.ForceResendingToken mResendToken; //resend token

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
                if (TextUtils.isEmpty(nameEdt.getText())) {
                    showToast(getApplicationContext(), "नाम आवश्यक है");
                } else if (TextUtils.isEmpty(phoneEdt.getText()) || phoneEdt.length() != 10) {
                    showToast(getApplicationContext(), "10 अंकों का मोबाइल नंबर आवश्यक है");
                } else {
                    sendVerificationCode("+91" + phoneEdt.getText());
                }
            }
        });
        otpSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(otpEdt.getText())) {
                    showToast(getApplicationContext(), "कृपया ओटीपी दर्ज करें");
                } else {
                    verifyCode(otpEdt.getText().toString());
                }
            }
        });

        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        //textViewTimer click for sending resend otp.
        textViewTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCodeAgian("+91" + phoneEdt.getText());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("Google SignIn ", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken(), task);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Google SignIn Failed", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken, Task<GoogleSignInAccount> googleSignInAccountTask) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Google SignIn Success", "signInWithCredential:success");
                            try {
                                GoogleSignInAccount account = googleSignInAccountTask.getResult(ApiException.class);
                                User user = new User(account.getDisplayName(), account.getEmail(),"",account.getPhotoUrl().toString());
                                updateSignedInUser(user);
                            } catch (ApiException e) {
                                e.printStackTrace();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Google SignIn Failed", "signInWithCredential:failure", task.getException());
                        }

                        // ...
                    }
                });
    }

    private void updateSignedInUser(User user) {
        String UId = mAuth.getUid();
        if (UId != null) {
            referenceDb.child(UId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    showToast(getApplicationContext(), "साइन इन सफल रहा");
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast(getApplicationContext(), "सत्यापन विफल हो गया है कृपया पुनः प्रयास करें");
                        }
                    });
        }
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
        appBarTitle.setText("साइन इन");
        appBarLeft.setText("BACK");
        appBarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void allInitializations() {
        textViewTimer = findViewById(R.id.signup_textView_timer);//textView for countdown and resend otp
        phoneEdt = findViewById(R.id.signup_phone);
        nameEdt = findViewById(R.id.signup_name);
        submitBtn = findViewById(R.id.signup_submit);
        mAuth = FirebaseAuth.getInstance();
        otpFrame = findViewById(R.id.signup_otp_frame);
        credentialFrame = findViewById(R.id.signup_credential_frame);
        otpSubmitBtn = findViewById(R.id.signup_submit_otp);
        otpEdt = findViewById(R.id.signup_otp_field);
        progressDialog = new BSFProgressDialog();
        referenceDb = FirebaseDatabase.getInstance().getReference("Users");
        otpMessage = findViewById(R.id.signup_otp_msg);
        googleSignInBtn = findViewById(R.id.signup_google_sign_in_button);
        googleSignInBtn.setSize(SignInButton.SIZE_WIDE);
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
    }

    private void sendVerificationCode(String phoneNumber) {
        startCountDownTimer();
        progressDialog.show(getSupportFragmentManager(), progressDialog.getTag());
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // sending otp again
    private void sendVerificationCodeAgian(String phoneNubmber) {
        startCountDownTimer();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth).
                setPhoneNumber(phoneNubmber)
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)               // Activity (for callback binding)
                .setCallbacks(mCallbacks)   // OnVerificationStateChangedCallbacks
                .setForceResendingToken(mResendToken) //resend otp
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyCode(String otp) {
        progressDialog.show(getSupportFragmentManager(), progressDialog.getTag());
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
        signInWithPhoneCredential(credential);
    }

    private void signInWithPhoneCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    // add name to firebase database
                    User user = new User(nameEdt.getText().toString());
                    updateSignedInUser(user);
                } else {
                    showToast(getApplicationContext(), "विफल कृपया दोबारा प्रयास करें");
                }
            }
        });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                otpEdt.setText(code);
            }
            progressDialog.dismiss();

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            showToast(getApplicationContext(), "ओटीपी सत्यापन विफल हो गया है");
            progressDialog.dismiss();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
            mResendToken = forceResendingToken;
            progressDialog.dismiss();
            credentialFrame.setVisibility(View.GONE);
            otpFrame.setVisibility(View.VISIBLE);
            otpMessage.setText("+91" + phoneEdt.getText().toString() + " पर प्राप्त ओटीपी दर्ज करें");
        }
    };

    // startCountDownTimer() for countDown
    private void startCountDownTimer() {
        textViewTimer.setClickable(false);
        CountDownTimer countDownTimer = new CountDownTimer(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long l) {
                if ((l / 1000) < 10) {
                    textViewTimer.setText("बचा हुआ समय: " + "00:0" + l / 1000);
                } else {
                    textViewTimer.setText("बचा हुआ समय: " + "00:" + l / 1000);
                }
            }

            @Override
            public void onFinish() {
                textViewTimer.setText("ओटीपी पुनः भेजें");
                textViewTimer.setClickable(true);
            }
        }.start();
    }

    private void showToast(Context context, String text) {
        ContextThemeWrapper themeWrapper = new ContextThemeWrapper(context, R.style.CustomAlertTheme);
        Toast toast = Toast.makeText(themeWrapper, "", Toast.LENGTH_SHORT);
        toast.setText(text);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
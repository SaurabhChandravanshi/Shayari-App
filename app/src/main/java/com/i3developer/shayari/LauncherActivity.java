package com.i3developer.shayari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Locale;

import static com.i3developer.shayari.SharedPref.APP_LANG;
import static com.i3developer.shayari.SharedPref.SHARED_PREF;

public class LauncherActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        String appLang = sharedPreferences.getString(APP_LANG, null);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LauncherActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }, 1000);

        if(mAuth.getCurrentUser() == null) {
            signInAnonymously();
        }
    }

    private void createAnonymousAccountWithReferrerInfo(String referrerUid) {
        FirebaseAuth.getInstance()
                .signInAnonymously()
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Keep track of the referrer in the RTDB. Database calls
                        // will depend on the structure of your app's RTDB.
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference userRecord =
                                FirebaseDatabase.getInstance().getReference()
                                        .child("users")
                                        .child(user.getUid());
                        userRecord.child("referred_by").setValue(referrerUid);
                    }
                });
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference userRecord =
                                FirebaseDatabase.getInstance().getReference()
                                        .child("users")
                                        .child(user.getUid());
                        userRecord.child("name").setValue("Anonymous");
                    }
                });
    }

    private void changeAppLang(String localeName) {
        if (localeName.equalsIgnoreCase(""))
            return;
        Locale myLocale = new Locale(localeName);//Set Selected Locale
        Locale.setDefault(myLocale);//set new locale as default
        Configuration config = new Configuration();//get Configuration
        config.locale = myLocale;//set config locale as selected locale
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());//Update the config
    }
}
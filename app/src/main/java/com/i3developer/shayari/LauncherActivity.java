package com.i3developer.shayari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import java.util.Locale;

import static com.i3developer.shayari.SharedPref.APP_LANG;
import static com.i3developer.shayari.SharedPref.SHARED_PREF;

public class LauncherActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        sharedPreferences = getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE);
        String appLang = sharedPreferences.getString(APP_LANG,null);
        if(appLang == null) {
            startActivity(new Intent(LauncherActivity.this,AppLangActivity.class));
            finish();
        }
        else {
            changeAppLang(appLang);
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(LauncherActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }
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
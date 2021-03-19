package com.i3developer.shayari;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Locale;

import static com.i3developer.shayari.SharedPref.APP_LANG;
import static com.i3developer.shayari.SharedPref.SHARED_PREF;

public class AppLangActivity extends AppCompatActivity {

    private RadioGroup langRadios;
    private Locale myLocale;
    private SharedPreferences sharedPreferences;
    private TextView appBarTitle;
    private Button continueBtn;
    private String langSelected = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lang);
        // To Display custom Action Bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.app_bar_layout);
        //Change the Title of Action Bar
        appBarTitle = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_title);
        ImageView appBarLeft = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_left);
        appBarTitle.setText(R.string.app_lang);
        appBarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        langRadios = findViewById(R.id.app_lang_radios);
        continueBtn = findViewById(R.id.app_lang_continue);
        setupRadios();
        updateLang();

        langRadios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.app_lang_hi)
                    langSelected = "hi";
                else if (checkedId == R.id.app_lang_en)
                    langSelected = "en";
                setLocale(langSelected);
                updateLang();
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale(langSelected);
                Intent intent = new Intent(AppLangActivity.this,LauncherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setLocale(String localeName) {
        if (localeName.equalsIgnoreCase(""))
            return;
        Locale myLocale = new Locale(localeName);//Set Selected Locale
        Locale.setDefault(myLocale);//set new locale as default
        Configuration config = new Configuration();//get Configuration
        config.locale = myLocale;//set config locale as selected locale
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());//Update the config
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(APP_LANG,localeName);
        editor.apply();
        updateLang();
    }

    private void updateLang() {
        continueBtn.setText(R.string.lang_continue);
        appBarTitle.setText(R.string.app_lang);
    }
    private void setupRadios() {
        String appLang = sharedPreferences.getString(APP_LANG,null);
        if(appLang != null) {
            if(appLang.equals("hi")) {
                langRadios.check(R.id.app_lang_hi);
            }
        }
    }
}
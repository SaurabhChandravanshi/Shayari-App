package com.i3developer.shayari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.tabs.TabLayout;

public class QuoteActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ImageView backBtn;
    private TextView appBarTitle;
    private String CATEGORY,CATEGORY_NAME;
    private AdView adView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);
        init();
        displayTextShayari();
        displayBannerAds();
        loadInterstitialAd();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    displayTextShayari();
                }
                else if (tab.getPosition() == 1) {
                    displayImageShayari();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void displayBannerAds() {
        adView = findViewById(R.id.quote_banner_ad1);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,getResources().getString(R.string.shayari_interstitial_ad),adRequest,new InterstitialAdLoadCallback(){
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd  = null;
            }
        });
    }


    private void displayImageShayari() {
        ImageQuoteFragment fragment = new ImageQuoteFragment(CATEGORY,CATEGORY_NAME);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.quote_frame,fragment).commit();
    }

    private void displayTextShayari() {
        TextShayariFragment fragment = new TextShayariFragment(CATEGORY,CATEGORY_NAME);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.quote_frame,fragment).commit();
    }


    @Override
    public void onBackPressed() {
        if(mInterstitialAd != null) {
            mInterstitialAd.show(QuoteActivity.this);
        }
        super.onBackPressed();
    }

    private void init() {
        tabLayout = findViewById(R.id.quote_tabLayout);
        appBarTitle = findViewById(R.id.quote_toolbar_title);
        backBtn = findViewById(R.id.quote_toolbar_left);
        CATEGORY = getIntent().getStringExtra("category");
        CATEGORY_NAME = getIntent().getStringExtra("name");
        appBarTitle.setText(CATEGORY_NAME);
        addTabs();
    }

    private void addTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Text Based"));
        tabLayout.addTab(tabLayout.newTab().setText("Image Based"));
    }
}
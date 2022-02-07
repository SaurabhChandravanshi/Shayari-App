package com.i3developer.shayari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Map;

import im.crisp.client.ChatActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {


    private FrameLayout appUpdateFrame;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private NativeAd mNativeAd;
    private NativeAd mNativeAdExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        allInitialization();
        refreshAd();
        refreshAdForExitDialog();
        getAppUpdate();
        displayHomeFragment();
        setupNavigationMenuFont();
        navigationView.setNavigationItemSelectedListener(this);
        // To Display custom Action Bar

        Toolbar toolbar = findViewById(R.id.main_app_bar_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });



    }

    private void getAppUpdate() {
        FirebaseFirestore firestore  = FirebaseFirestore.getInstance();
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(),0);
            String versionName = packageInfo.versionName;
            firestore.collection("appUpdate").whereGreaterThan("appVersion",versionName).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(queryDocumentSnapshots.size()>0) {
                                appUpdateFrame.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.CustomAlertTheme));
            builder.setTitle("Confirmation");
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_exit_dialog,null);
            if (mNativeAdExit != null) {
                builder.setView(view);
                showNativeAdOnExitDialog(view);
            }
            builder.setMessage("Are you sure you wants to Exit ?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MainActivity.super.onBackPressed();
                }
            })
                    .setNegativeButton("NO",null)
                    .setNeutralButton("RATE US", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName()));
                            startActivity(Intent.createChooser(intent,"Open With"));
                        }
                    });
            builder.show();

        }
    }

    private void setupNavigationMenuFont() {
        Menu menu = navigationView.getMenu();
        for (int i=0;i<menu.size();i++) {
            MenuItem menuItem = menu.getItem(i);
            SubMenu subMenu = menuItem.getSubMenu();
            if(subMenu!=null && subMenu.size() > 0) {
                for (int j=0;j<subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            applyFontToMenuItem(menuItem);
        }
    }


    private void applyFontToMenuItem(MenuItem menuItem) {
        Typeface typeface = ResourcesCompat.getFont(this,R.font.sans_regular);
        SpannableString sString = new SpannableString(menuItem.getTitle());
        sString.setSpan(new CustomTypefaceSpan("",typeface),0,sString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        menuItem.setTitle(sString);
    }

    private void allInitialization() {
        appUpdateFrame = findViewById(R.id.main_app_update_frame);
        drawerLayout = findViewById(R.id.main_drawer_layout);
        navigationView = findViewById(R.id.main_nav);
    }

    private void displayHomeFragment() {
        HomeFragment fragment = new HomeFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main_frame, fragment).commit();
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_nav_fb:
                launchChromeTab("https://facebook.com/i3Developer");
                break;
            case R.id.main_nav_ig:
                launchChromeTab("https://instagram.com/i3Developer");
                break;
            case R.id.main_nav_twitter:
                launchChromeTab("https://twitter.com/i3Developer");
                break;
            case R.id.main_nav_share:
                shareApp(null);
                break;
            case R.id.main_nav_rate:
                launchChromeTab("https://play.google.com/store/apps/details?id=com.i3developer.shayari");
                break;
            case R.id.main_nav_more_apps:
                launchChromeTab("https://play.google.com/store/apps/dev?id=9018600825061407450");
                break;
            case R.id.main_nav_help:
                Intent crispIntent = new Intent(this, ChatActivity.class);
                startActivity(crispIntent);
                break;
            case R.id.main_nav_app_update:
                launchChromeTab("https://play.google.com/store/apps/details?id=com.i3developer.shayari");
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    public void shareApp(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,"Hindi Shayari App");
        intent.putExtra(Intent.EXTRA_TEXT,"Hindi Shayari App - Find unique collections of Hindi Shayari\nDownload the app Now\n"+
                "https://play.google.com/store/apps/details?id=com.i3developer.shayari");
        startActivity(Intent.createChooser(intent,"Share with"));
    }
    private void launchChromeTab(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        customTabsIntent.launchUrl(getApplicationContext(), Uri.parse(url));
    }
    public void showShayari(View view) {
        String[] tags = view.getTag().toString().split(",");
        Intent intent = new Intent(MainActivity.this,ShayariActivity.class);
        intent.putExtra("category",tags[0]);
        intent.putExtra("name",tags[1]);
        startActivity(intent);
    }
    public void openPoetList(View view) {
        startActivity(new Intent(MainActivity.this,PoetListActivity.class));
    }
    public void openLeadersActivity(View view) {
        startActivity(new Intent(MainActivity.this,LeadersActivity.class));
    }
    public void openCategory(View view) {
        startActivity(new Intent(MainActivity.this,CategoryActivity.class));
    }
    public void openBestWishesCat(View view) {
        startActivity(new Intent(MainActivity.this,WishesCatActivity.class));
    }




    public void updateApp(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=com.i3developer.shayari"));
        startActivity(intent);
    }

    public void updateLater(View view) {
        appUpdateFrame.setVisibility(View.GONE);
    }


    // Advertisement coding block.
    private void populateNativeAdView(com.google.android.gms.ads.nativead.NativeAd nativeAd, com.google.android.gms.ads.nativead.NativeAdView adView) {
        adView.setMediaView(adView.findViewById(R.id.ad_media));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        Typeface tfRegular = ResourcesCompat.getFont(this,R.font.sans_regular);
        if(nativeAd.getHeadline() != null)
            ((TextView)adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView)adView.getHeadlineView()).setTypeface(tfRegular);
        if (nativeAd.getMediaContent() != null)
            adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            ((TextView) adView.getBodyView()).setTypeface(tfRegular,Typeface.BOLD);
        }
        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            ((Button) adView.getCallToActionView()).setTypeface(tfRegular);
        }
        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }
        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
            ((TextView) adView.getPriceView()).setTypeface(tfRegular);
        }
        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }
        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {

            ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }
        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
            ((TextView) adView.getAdvertiserView()).setTypeface(tfRegular);
        }
        adView.setNativeAd(nativeAd);
    }
    private void refreshAd() {
        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.native_ad_unit));
        builder.forNativeAd(new com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(@NonNull com.google.android.gms.ads.nativead.NativeAd nativeAd) {
                if (mNativeAd != null) {
                    mNativeAd.destroy();
                }
                mNativeAd = nativeAd;
                FrameLayout frameLayout = findViewById(R.id.main_native_ad_frame);
                com.google.android.gms.ads.nativead.NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.native_ad, null);
                populateNativeAdView(nativeAd, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
                CardView adCard = findViewById(R.id.main_ad_card);
                adCard.setVisibility(View.VISIBLE);
            }
        });
        NativeAdOptions adOptions = new NativeAdOptions.Builder().build();
        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void refreshAdForExitDialog() {
        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.native_ad_unit));
        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                if (mNativeAdExit != null) {
                    mNativeAdExit.destroy();
                }
                mNativeAdExit = nativeAd;
            }
        });
        NativeAdOptions adOptions = new NativeAdOptions.Builder().build();
        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }
    private void showNativeAdOnExitDialog(View view) {
        FrameLayout frameLayout = view.findViewById(R.id.custom_exit_native_ad_frame);
        NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.native_ad, null);
        populateNativeAdView(mNativeAdExit, adView);
        frameLayout.removeAllViews();
        frameLayout.addView(adView);
        CardView adCard = view.findViewById(R.id.custom_exit_ad_card);
        adCard.setVisibility(View.VISIBLE);
    }

    // Advertisement coding ended here ///
}
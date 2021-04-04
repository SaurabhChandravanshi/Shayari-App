package com.i3developer.shayari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {


    private FrameLayout appUpdateFrame;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadBannerAds();
        allInitialization();
        getAppUpdate();
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavListener);
        displayHomeFragment();
        setupNavigationMenuFont();
        navigationView.setNavigationItemSelectedListener(this);
        // To Display custom Action Bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.app_bar_main);
        //Change the Title of Action Bar
        ImageView appBarLeft = getSupportActionBar().getCustomView()
                .findViewById(R.id.main_app_bar_left);
        appBarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                for (String adapterClass : statusMap.keySet()) {
                    AdapterStatus status = statusMap.get(adapterClass);
                }
                // Start loading ads here...
            }
        });

    }

    private void getAppUpdate() {
        FirebaseFirestore firestore  = FirebaseFirestore.getInstance();
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(),0);
            String versionName = packageInfo.versionName;
            firestore.collection("appUpdate").whereNotEqualTo("appVersion",versionName).get()
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
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Confirmation");
            builder.setMessage("Do you want to exit from app?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.super.onBackPressed();
                }
            });
            builder.setNegativeButton("NO", null);
            builder.setNeutralButton("RATE US", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    launchChromeTab("https://play.google.com/store/apps/details?id=com.i3developer.shayari");
                }
            });
            builder.create().show();

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
        bottomNavigationView = findViewById(R.id.main_bottom_navigation);
    }

    private void displayHomeFragment() {
        HomeFragment fragment = new HomeFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main_frame, fragment).commit();
    }
    private void displayStatusFragment() {
        StatusFragment fragment = new StatusFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main_frame, fragment).commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.main_bottom_home:
                    displayHomeFragment();
                    break;
                case R.id.main_bottom_status:
                    displayStatusFragment();
            }
            return true;
        }
    };

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
                startActivity(new Intent(MainActivity.this,HelpActivity.class));
                break;
            case R.id.main_nav_app_update:
                launchChromeTab("https://play.google.com/store/apps/details?id=com.i3developer.shayari");

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
        startActivity(intent);
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
    public void openBestWishesCat(View view) {
        startActivity(new Intent(MainActivity.this,WishesCatActivity.class));
    }
    public void openCategory(View view) {
        startActivity(new Intent(MainActivity.this,CategoryActivity.class));
    }
    public void openReferral(View view) {
        startActivity(new Intent(MainActivity.this,ReferralActivity.class));
    }

    public void imageShayari(View view) {
        startActivity(new Intent(MainActivity.this,IMGCategoryActivity.class));
    }

    private void loadBannerAds() {
        AdView adView1  = findViewById(R.id.main_banner_ad1);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView1.loadAd(adRequest);
    }

    public void updateApp(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=com.i3developer.shayari"));
        startActivity(intent);
    }

    public void updateLater(View view) {
        appUpdateFrame.setVisibility(View.GONE);
    }
}
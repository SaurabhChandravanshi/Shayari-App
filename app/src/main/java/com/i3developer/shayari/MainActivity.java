package com.i3developer.shayari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
import android.widget.TextView;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {


    private FrameLayout frameLayout;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allInitialization();
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


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
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
        frameLayout = findViewById(R.id.main_frame);
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
                startActivity(new Intent(MainActivity.this,HelpActivity.class));
                break;
            case R.id.main_nav_app_lang:
                startActivity(new Intent(MainActivity.this,AppLangActivity.class));
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
}
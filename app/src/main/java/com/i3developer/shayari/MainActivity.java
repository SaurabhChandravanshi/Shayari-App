package com.i3developer.shayari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity  {


    private FrameLayout frameLayout;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allInitialization();
        bottomNavigationView.setOnNavigationItemSelectedListener(listener);
        displayHomeFragment();

        // To Display custom Action Bar
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
        appBarTitle.setText("शायरी बुक");
        appBarLeft.setText("Exit");
        appBarRight.setText("Share");
        appBarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        appBarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"Download Shayari Book App");
                intent.putExtra(Intent.EXTRA_TEXT,"Download Shayari Book App\n"+
                        "https://play.google.com/store/apps/details?id=com.i3developer.shayari");
                startActivity(intent);
            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if(item.getItemId() == R.id.main_bottom_nav_home)
                displayHomeFragment();
            else if (item.getItemId() == R.id.main_bottom_nav_public)
                displayPublicFragment();
            else if (item.getItemId()==R.id.main_bottom_nav_menu)
                displayMenuFragment();
            return true;
        }
    };

    private void allInitialization() {
        frameLayout = findViewById(R.id.main_frame);
        bottomNavigationView = findViewById(R.id.main_bottom_nav);
    }

    private void displayHomeFragment() {
        HomeFragment fragment = new HomeFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main_frame, fragment).commit();
    }
    private void displayPublicFragment() {
        PublicFragment fragment = new PublicFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("single_post",false);
        fragment.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main_frame, fragment).commit();
    }
    private void displayMenuFragment() {
        MenuFragment fragment = new MenuFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main_frame, fragment).commit();
    }
    public void openSignIn(View view) {
        startActivity(new Intent(MainActivity.this,SignupActivity.class));
    }
}
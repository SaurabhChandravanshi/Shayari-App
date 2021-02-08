package com.i3developer.shayari;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CategoryActivity extends AppCompatActivity {

    private CardView loveShayari,attitudeShayari,motivationalShayari;
    private CardView friendShipShayari,sadShayari,funnyShayari;
    private CardView birthdayWishes,twoLineShayari,boysAttitudeShayari;
    private CardView gmShayari,gnShayari,sherOShayari;
    private CardView kumarVishwasShayari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        allInitializations();

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
        appBarTitle.setText("शायरी संग्रह");
        appBarLeft.setText("Back");
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
        loveShayari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this,ShayariActivity.class);
                intent.putExtra("category","love_shayari");
                intent.putExtra("name","Love Shayari");
                startActivity(intent);
            }
        });
        attitudeShayari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this,ShayariActivity.class);
                intent.putExtra("category","attitude_shayari");
                intent.putExtra("name","Attitude Shayari");
                startActivity(intent);
            }
        });
        motivationalShayari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this,ShayariActivity.class);
                intent.putExtra("category","motivational_shayari");
                intent.putExtra("name","Motivational");
                startActivity(intent);
            }
        });
        friendShipShayari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this,ShayariActivity.class);
                intent.putExtra("category","friendship_shayari");
                intent.putExtra("name","Friendship");
                startActivity(intent);
            }
        });
        sadShayari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this,ShayariActivity.class);
                intent.putExtra("category","sad_shayari");
                intent.putExtra("name","Sad Shayari");
                startActivity(intent);
            }
        });
        funnyShayari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this,ShayariActivity.class);
                intent.putExtra("category","funny_shayari");
                intent.putExtra("name","Funny Shayari");
                startActivity(intent);
            }
        });
        birthdayWishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this,ShayariActivity.class);
                intent.putExtra("category","birthday_wishes_shayari");
                intent.putExtra("name","Birthday Wishes");
                startActivity(intent);
            }
        });
        twoLineShayari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this,ShayariActivity.class);
                intent.putExtra("category","two_line_shayari");
                intent.putExtra("name","2 Line Shayari");
                startActivity(intent);
            }
        });
        boysAttitudeShayari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this,ShayariActivity.class);
                intent.putExtra("category","boys_attitude_shayari");
                intent.putExtra("name","Boys' Attitude");
                startActivity(intent);
            }
        });
        gmShayari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this,ShayariActivity.class);
                intent.putExtra("category","good_morning_shayari");
                intent.putExtra("name","Good Morning");
                startActivity(intent);
            }
        });
        gnShayari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this,ShayariActivity.class);
                intent.putExtra("category","good_night_shayari");
                intent.putExtra("name","Good Night");
                startActivity(intent);
            }
        });
        sherOShayari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this,ShayariActivity.class);
                intent.putExtra("category","sher_o_shayari");
                intent.putExtra("name","Sher-o-Shayari");
                startActivity(intent);
            }
        });
        kumarVishwasShayari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this,ShayariActivity.class);
                intent.putExtra("category","kumar_vishwas_shayari");
                intent.putExtra("name","Kumar Vishwas");
                startActivity(intent);
            }
        });
    }

    private void allInitializations() {
        loveShayari = findViewById(R.id.category_love);
        attitudeShayari = findViewById(R.id.category_attitude);
        motivationalShayari = findViewById(R.id.category_motivational);
        friendShipShayari = findViewById(R.id.category_friendship);
        sadShayari = findViewById(R.id.category_sad);
        funnyShayari = findViewById(R.id.category_funny);
        birthdayWishes = findViewById(R.id.category_birthday_wishes);
        twoLineShayari = findViewById(R.id.category_2_line_shayari);
        boysAttitudeShayari = findViewById(R.id.category_boys_attitude);
        gmShayari = findViewById(R.id.category_good_morning);
        gnShayari = findViewById(R.id.category_good_night);
        sherOShayari = findViewById(R.id.category_sher_o_shayari);
        kumarVishwasShayari = findViewById(R.id.category_kumar_vishwas_shayari);
    }
}
package com.i3developer.shayari;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class WishesCatActivity extends AppCompatActivity {

    private CardView birthdayWishes,holiWishes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishes_cat);
        allInitializations();

        // To Display custom Action Bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.app_bar_layout);
        //Change the Title of Action Bar
        TextView appBarTitle = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_title);
        ImageView appBarLeft = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_left);
        appBarTitle.setText(R.string.wishes_cat);
        appBarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        birthdayWishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WishesCatActivity.this,ShayariActivity.class);
                intent.putExtra("category","birthday_wishes_shayari");
                intent.putExtra("name","Birthday Wishes in Hindi");
                startActivity(intent);
            }
        });
        holiWishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WishesCatActivity.this,ShayariActivity.class);
                intent.putExtra("category","holi_wishes");
                intent.putExtra("name","Holi Wishes in Hindi");
                startActivity(intent);
            }
        });
    }

    private void allInitializations() {
        birthdayWishes = findViewById(R.id.category_birthday_wishes);
        holiWishes = findViewById(R.id.category_holi_wishes);
    }
}
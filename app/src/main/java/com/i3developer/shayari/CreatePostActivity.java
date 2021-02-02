package com.i3developer.shayari;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreatePostActivity extends AppCompatActivity {

    private int CURRENT_BACKGROUND = 1;
    private int MAX_COLOR = 10;
    private FloatingActionButton bgFab;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        setUpAppBar();
        allInitializations();
        bgFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeBackground(getApplicationContext(),cardView);
            }
        });
    }

    private void allInitializations() {
        bgFab = findViewById(R.id.create_post_bg_fab);
        cardView = findViewById(R.id.create_post_card);
    }

    private void setUpAppBar() {
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
        appBarTitle.setText("नई पोस्ट");
        appBarLeft.setText("CANCEL");
        appBarLeft.setTextColor(getResources().getColor(R.color.colorRed));
        appBarRight.setText("POST");
        appBarRight.setTextColor(getResources().getColor(R.color.color6));
        appBarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void changeBackground(Context context, CardView view) {
        if(CURRENT_BACKGROUND == MAX_COLOR) {
            CURRENT_BACKGROUND = 1;
        } else {
            CURRENT_BACKGROUND++;
        }
        switch (CURRENT_BACKGROUND) {
            case 1:
                view.setCardBackgroundColor(context.getResources().getColor(R.color.color1));
                break;
            case 2:
                view.setCardBackgroundColor(context.getResources().getColor(R.color.color2));
                break;
            case 3:
                view.setCardBackgroundColor(context.getResources().getColor(R.color.color3));
                break;
            case 4:
                view.setCardBackgroundColor(context.getResources().getColor(R.color.color4));
                break;
            case 5:
                view.setCardBackgroundColor(context.getResources().getColor(R.color.color5));
                break;
            case 6:
                view.setCardBackgroundColor(context.getResources().getColor(R.color.color6));
                break;
            case 7:
                view.setCardBackgroundColor(context.getResources().getColor(R.color.color7));
                break;
            case 8:
                view.setCardBackgroundColor(context.getResources().getColor(R.color.color8));
                break;
            case 9:
                view.setCardBackgroundColor(context.getResources().getColor(R.color.color9));
                break;
            case 10:
                view.setCardBackgroundColor(context.getResources().getColor(R.color.color10));
                break;
            default:
                view.setCardBackgroundColor(context.getResources().getColor(R.color.colorRed));
        }
    }
}
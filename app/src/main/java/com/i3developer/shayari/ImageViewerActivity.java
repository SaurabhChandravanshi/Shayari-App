package com.i3developer.shayari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ImageViewerActivity extends AppCompatActivity {

    private String IMAGE_PATH;
    private ImageView imageView;
    private Button waBtn,fbBtn,shareBtn;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        loadBannerAds();
        loadInterstitialAd();

        imageView = findViewById(R.id.image_viewer_image);
        IMAGE_PATH = getIntent().getStringExtra("imagePath");
        waBtn = findViewById(R.id.image_viewer_wa_btn);
        fbBtn = findViewById(R.id.image_viewer_fb_btn);
        shareBtn = findViewById(R.id.image_viewer_share_btn);

        //Get Firebase Storage Reference to load Image
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReferenceFromUrl(IMAGE_PATH);
        // Load image into ImageView
        GlideApp.with(this).load(reference).into(imageView);

        waBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImageToCache(getApplicationContext(),viewToBitmap(imageView));
                shareImageViaWA(getApplicationContext());
            }
        });
        fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImageToCache(getApplicationContext(),viewToBitmap(imageView));
                shareImageViaFB(getApplicationContext());
            }
        });
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImageToCache(getApplicationContext(),viewToBitmap(imageView));
                shareImage(getApplicationContext());
            }
        });
    }


    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
    private void saveImageToCache(Context context, Bitmap bitmap) {
        try {
            File cacheFile = new File(context.getCacheDir(),"images");
            cacheFile.mkdir();
            FileOutputStream output = new FileOutputStream(cacheFile+"/image.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void shareImage(Context context) {
        File imagePath = new File(context.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(context, "com.i3developer.shayari.fileprovider", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT,"Download Shayari Book app for more\nhttps://shayariapp.page.link/install");
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }
    }
    private void shareImageViaWA(Context context) {
        File imagePath = new File(context.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(context, "com.i3developer.shayari.fileprovider", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT,"Download Shayari Book app for more\nhttps://shayariapp.page.link/install");
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.setPackage("com.whatsapp");
            try {
                startActivity(Intent.createChooser(shareIntent, "Choose an app"));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "WhatsApp not installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void shareImageViaFB(Context context) {
        File imagePath = new File(context.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(context, "com.i3developer.shayari.fileprovider", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT,"Download Shayari Book app for more\nhttps://shayariapp.page.link/install");
            shareIntent.setPackage("com.facebook.katana");
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                startActivity(Intent.createChooser(shareIntent, "Choose an app"));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "WhatsApp not installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadBannerAds() {
        AdView adView1  = findViewById(R.id.image_viewer_banner_ad1);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView1.loadAd(adRequest);
    }

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        com.google.android.gms.ads.interstitial.InterstitialAd.load(this,getResources().getString(R.string.shayari_interstitial_ad),adRequest,new InterstitialAdLoadCallback(){
            @Override
            public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd  = null;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(mInterstitialAd != null) {
            mInterstitialAd.show(ImageViewerActivity.this);
        }
        super.onBackPressed();
    }
}
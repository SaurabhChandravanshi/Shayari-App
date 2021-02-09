package com.i3developer.shayari;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CreatePostActivity extends AppCompatActivity {

    private int CURRENT_BACKGROUND = 1;
    private int MAX_COLOR = 10;
    private FloatingActionButton bgFab;
    private CardView cardView;
    private EditText cardContent;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        loadInterstitialAd();
        setUpAppBar();
        allInitializations();
        cardContent.requestFocus();
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
        cardContent = findViewById(R.id.create_post_content);
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
        appBarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(cardContent.getText())) {
                    showToast(getApplicationContext(),"कृपया कुछ टेक्स्ट बॉक्स में दर्ज करें");
                } else {
                    identifyLanguage(cardContent.getText().toString());
                }
            }
        });
    }

    private void identifyLanguage(String text) {
        LanguageIdentifier language = LanguageIdentification.getClient();
        language.identifyLanguage(text).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(@Nullable String languageCode) {
                if(languageCode.equals("hi") || languageCode.equals("hi-Latn")) {
                    LocalNotification notification = new LocalNotification(getApplicationContext());
                    notification.setTitle("Uploading Post...");
                    notification.setMessage("We will notify when uploading complete.");
                    notification.showLocalNotification();
                    cardContent.setCursorVisible(false);
                    uploadImageToCloud(cardView);
                    showInterstitialAd();
                    finish();
                } else {
                    showToast(getApplicationContext(),"कृपया हिंदी में लिखें");
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void showInterstitialAd() {
        if(mInterstitialAd != null) {
            mInterstitialAd.show(CreatePostActivity.this);
        }
    }

    private void insertToFirestore(String imagePath) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firestoreRef = FirebaseFirestore.getInstance();
        String postId = UUID.randomUUID().toString();
        String dynamicLink =  createDynamicLink(postId);
        PublicPost post = new PublicPost(postId,mAuth.getUid(),imagePath,dynamicLink,new HashMap<>(),new ArrayList<>());
        firestoreRef.collection("posts").document(postId).set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Firestore Success","Post added to Firestore");
                LocalNotification notification = new LocalNotification(getApplicationContext());
                notification.setTitle("Posted Successfully");
                notification.setMessage("Congratulations!! your Post was Posted Successfully.");
                notification.showLocalNotification();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Firestore error","Error adding post",e);
                LocalNotification notification = new LocalNotification(getApplicationContext());
                notification.setTitle("Failed to Upload");
                notification.setMessage("Upload Failed Please Try Again Later or Contact Us in case of Multiple failure.");
                notification.showLocalNotification();
            }
        });
    }

    private String createDynamicLink(String postId) {
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://i3developer.com/sb/p?id="+postId))
                .setDomainUriPrefix("https://shayariapp.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink();
        Uri dynamicLinkUri = dynamicLink.getUri();
        return dynamicLinkUri.toString();
    }

    private void uploadImageToCloud(View view) {
        FirebaseStorage reference = FirebaseStorage.getInstance();
        String randomImageName = UUID.randomUUID().toString()+".png";
        StorageReference storageRef = reference.getReference().child("posts").child(randomImageName);
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        byte[] data = output.toByteArray();
        // Upload Image Image.
        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                LocalNotification notification = new LocalNotification(getApplicationContext());
                notification.setTitle("Failed to Upload");
                notification.setMessage("Upload Failed Please Try Again Later or Contact Us in case of Multiple failure.");
                notification.showLocalNotification();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                insertToFirestore(taskSnapshot.getMetadata().getPath());
            }
        });
    }

    private void showToast(Context context, String text) {
        ContextThemeWrapper themeWrapper = new ContextThemeWrapper(context,R.style.CustomAlertTheme);
        Toast toast = Toast.makeText(themeWrapper,"",Toast.LENGTH_SHORT);
        toast.setText(text);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
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

    @Override
    public void onBackPressed() {
        if(mInterstitialAd != null) {
            mInterstitialAd.show(CreatePostActivity.this);
        }
        super.onBackPressed();
    }
    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,getResources().getString(R.string.create_post_interstitial_ad),adRequest,new InterstitialAdLoadCallback(){
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
}
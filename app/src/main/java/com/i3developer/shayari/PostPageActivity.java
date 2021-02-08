package com.i3developer.shayari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class PostPageActivity extends AppCompatActivity {

    private String POST_ID;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_page);
        setUpAppBar();

        if(mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this,SignupActivity.class));
            finish();
        }

        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
                .addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            assert deepLink != null;
                            if(deepLink.getBooleanQueryParameter("id", false)) {
                                POST_ID = deepLink.getQueryParameter("id");
                                displayPost();
                            }
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                startActivity(new Intent(PostPageActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    private void displayPost() {
     PublicFragment fragment = new PublicFragment();
     Bundle bundle = new Bundle();
     bundle.putBoolean("single_post",true);
     bundle.putString("post_id",POST_ID);
     fragment.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.post_page_frame,fragment).commit();
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
        appBarTitle.setText("Post");
        appBarLeft.setText("Back");
        appBarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
package com.i3developer.shayari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class ReferralActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView referralTitle,terms,expireDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral);
        init();
        if (mAuth.getCurrentUser() == null || mAuth.getCurrentUser().isAnonymous()) {
            startActivity(new Intent(ReferralActivity.this,SignupActivity.class));
            finish();
        } else {
            getReferralContest();
        }
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        referralTitle = findViewById(R.id.referral_title);
        terms = findViewById(R.id.referral_terms);
        expireDate = findViewById(R.id.referral_expire_date);
    }

    private void getReferralContest() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("referralOffers").document("referralFirst").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ReferralData data = documentSnapshot.toObject(ReferralData.class);
                        updateUI(data);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ReferralActivity.this, "Failed to get Referral Data Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI(ReferralData data) {
        referralTitle.setText(data.getTitle());
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        expireDate.setText("Offer expires on "+sdf.format(data.getExpireDate().toDate()));

        Timestamp timestampNow = Timestamp.now();

        for (int i=0;i<data.getTerms().size();i++) {
            String htmlLi = "<li>"+data.getTerms().get(i)+"</li><br>";
            terms.append(HtmlCompat.fromHtml(htmlLi,HtmlCompat.FROM_HTML_MODE_COMPACT));
        }
    }

    public void viewReferralList(View view) {
        ReferralListFragment referralList = new ReferralListFragment();
        referralList.show(getSupportFragmentManager(),referralList.getTag());
    }

    public void contactUs(View view) {
        startActivity(new Intent(ReferralActivity.this,HelpActivity.class));
    }

    public void shareReferralLink(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        String link = "https://shayariapp.page.link?invitedby=" + uid;
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(link))
                .setDomainUriPrefix("https://shayariapp.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder(getApplicationContext().getPackageName())
                                .build())
                .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
                .addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
                    @Override
                    public void onSuccess(ShortDynamicLink shortDynamicLink) {
                        String mInvitationUrl = shortDynamicLink.getShortLink().toString();
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Shayari App Referral");
                        shareIntent.putExtra(Intent.EXTRA_TEXT,"Download Shayari Book app and participate in refer and earn\n"+
                                mInvitationUrl);
                        startActivity(shareIntent);
                    }
                });
    }
}
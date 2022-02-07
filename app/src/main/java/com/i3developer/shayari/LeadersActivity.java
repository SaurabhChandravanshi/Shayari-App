package com.i3developer.shayari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;

public class LeadersActivity extends AppCompatActivity {

    private ImageView vivekanandImage;
    private ImageView mahatamaGandhiImage;
    private ImageView sardarImage,bhagatSinghImages;
    private ImageView tagoreImage,azadImage;
    private ImageView subhashImage,nehruImage;
    private ImageView kalamImage,indiraGandhiImage;
    private NativeAd mNativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaders);
        refreshAd();
        // To Display custom Action Bar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.app_bar_layout);
        //Change the Title of Action Bar
        TextView appBarTitle = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_title);
        ImageView appBarLeft = getSupportActionBar().getCustomView()
                .findViewById(R.id.app_bar_left);
        appBarTitle.setText(R.string.leaders);
        appBarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        allInitialization();
        loadLeaderImages();
    }

    public void showShayari(View view) {
        String[] tags = view.getTag().toString().split(",");
        Intent intent = new Intent(LeadersActivity.this,ShayariActivity.class);
        intent.putExtra("category",tags[0]);
        intent.putExtra("name",tags[1]);
        startActivity(intent);
    }

    private void loadLeaderImages() {
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/vivekanand.png")).circleCrop().into(vivekanandImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/mahatama_gandhi.jpg")).circleCrop().into(mahatamaGandhiImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/sardar.jpg")).circleCrop().into(sardarImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/bhagat_singh.jpg")).circleCrop().into(bhagatSinghImages);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/tagore.jpg")).circleCrop().into(tagoreImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/azaad.jpg")).circleCrop().into(azadImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/chandra_bose.png")).circleCrop().into(subhashImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/nehru.jpg")).circleCrop().into(nehruImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/kalam.jpg")).circleCrop().into(kalamImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/indira_gandhi.jpg")).circleCrop().into(indiraGandhiImage);
    }
    private void allInitialization() {
        vivekanandImage = findViewById(R.id.leaders_vivekanand_image);
        mahatamaGandhiImage = findViewById(R.id.leaders_mahatama_gandhi);
        sardarImage = findViewById(R.id.leaders_patel_image);
        bhagatSinghImages = findViewById(R.id.leaders_bhagat_singh_image);
        tagoreImage = findViewById(R.id.leaders_tagore_image);
        azadImage = findViewById(R.id.leaders_azad_image);
        subhashImage = findViewById(R.id.leaders_subhash_image);
        nehruImage = findViewById(R.id.leaders_nehru_image);
        kalamImage = findViewById(R.id.leaders_kalam_image);
        indiraGandhiImage = findViewById(R.id.leaders_indira_gandhi_image);
    }

    private void populateNativeAdView(com.google.android.gms.ads.nativead.NativeAd nativeAd, com.google.android.gms.ads.nativead.NativeAdView adView) {
        adView.setMediaView(adView.findViewById(R.id.ad_media));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        Typeface tfRegular = ResourcesCompat.getFont(this,R.font.sans_regular);
        if(nativeAd.getHeadline() != null)
            ((TextView)adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView)adView.getHeadlineView()).setTypeface(tfRegular);
        if (nativeAd.getMediaContent() != null)
            adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            ((TextView) adView.getBodyView()).setTypeface(tfRegular,Typeface.BOLD);
        }
        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            ((Button) adView.getCallToActionView()).setTypeface(tfRegular);
        }
        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }
        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
            ((TextView) adView.getPriceView()).setTypeface(tfRegular);
        }
        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }
        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {

            ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }
        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
            ((TextView) adView.getAdvertiserView()).setTypeface(tfRegular);
        }
        adView.setNativeAd(nativeAd);
    }
    private void refreshAd() {
        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.native_ad_unit));
        builder.forNativeAd(new com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(@NonNull com.google.android.gms.ads.nativead.NativeAd nativeAd) {
                if (mNativeAd != null) {
                    mNativeAd.destroy();
                }
                mNativeAd = nativeAd;
                FrameLayout frameLayout = findViewById(R.id.adFrame);
                com.google.android.gms.ads.nativead.NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.native_ad, null);
                populateNativeAdView(nativeAd, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
                CardView adCard = findViewById(R.id.adCard);
                adCard.setVisibility(View.VISIBLE);
            }
        });
        NativeAdOptions adOptions = new NativeAdOptions.Builder().build();
        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }
}
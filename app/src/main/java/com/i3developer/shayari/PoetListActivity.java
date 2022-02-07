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

public class PoetListActivity extends AppCompatActivity {

    private ImageView gulzarImage,ghalibImage,rahatIndoriImage,kumarVishwasImage,faizAhmadFaizImage,bashirBadrImage,anamikaJainImage;
    private ImageView mirTaqiMirImage,bakshNasikhImage,allamaIqbalImage,javedAkhterImage,jaunEliaImage,ahmadFarazImage;
    private ImageView akbarAllahabadiImage,parveenShakirImage,nidaFazliImage,adaJafriImage,majroohSultanpuriImage;
    private NativeAd mNativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poet_list);
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
        appBarTitle.setText(R.string.poet_coll);
        appBarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        allInitializations();
        loadPoetImages();
    }

    private void loadPoetImages() {
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/gulzar_image.jpg")).circleCrop().into(gulzarImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/ghalib_image.png")).circleCrop().into(ghalibImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/rahat_indori_image.jpg")).circleCrop().into(rahatIndoriImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/kumar_vishwash_image.jpg")).circleCrop().into(kumarVishwasImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/faiz-ahmad-faiz.png")).circleCrop().into(faizAhmadFaizImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/bashir-badr-image.jpg")).circleCrop().into(bashirBadrImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/anamika-jain.jpg")).circleCrop().into(anamikaJainImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/baksh_nasikh.jpg")).circleCrop().into(bakshNasikhImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/mir-taqi-mir.jpg")).circleCrop().into(mirTaqiMirImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/allama-iqbal.png")).circleCrop().into(allamaIqbalImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/akbar_allahabadi.jpg")).circleCrop().into(akbarAllahabadiImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/parveen_shakir.jpg")).circleCrop().into(parveenShakirImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/nida-fazli.png")).circleCrop().into(nidaFazliImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/javed_akhter.jpg")).circleCrop().into(javedAkhterImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/ada_Jafarey.jpg")).circleCrop().into(adaJafriImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/majrooh_sultanpuri.jpg")).circleCrop().into(majroohSultanpuriImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/jaun_elia.jpg")).circleCrop().into(jaunEliaImage);
        Glide.with(getApplicationContext()).load(Uri.parse("file:///android_asset/ahmad_faraz.jpg")).circleCrop().into(ahmadFarazImage);
    }
    public void showShayari(View view) {
        String[] tags = view.getTag().toString().split(",");
        Intent intent = new Intent(PoetListActivity.this,ShayariActivity.class);
        intent.putExtra("category",tags[0]);
        intent.putExtra("name",tags[1]);
        startActivity(intent);
    }
    private void allInitializations() {
        gulzarImage = findViewById(R.id.poet_gulzar_image);
        ghalibImage = findViewById(R.id.poet_ghalib_image);
        rahatIndoriImage = findViewById(R.id.poet_rahat_indori_image);
        kumarVishwasImage = findViewById(R.id.poet_kumar_vishwas_image);
        faizAhmadFaizImage = findViewById(R.id.poet_faiz_ahmad_faiz_image);
        bashirBadrImage = findViewById(R.id.poet_bashir_badr_image);
        anamikaJainImage = findViewById(R.id.poet_anamika_jain_image);
        bakshNasikhImage = findViewById(R.id.poet_baksh_nasikh_image);
        mirTaqiMirImage = findViewById(R.id.poet_mir_taqi_mir_image);
        allamaIqbalImage = findViewById(R.id.poet_allama_iqbal_image);
        akbarAllahabadiImage = findViewById(R.id.poet_akbar_allahabadi_image);
        parveenShakirImage = findViewById(R.id.poet_parveen_shakir_image);
        nidaFazliImage = findViewById(R.id.poet_nida_fazil_image);
        javedAkhterImage = findViewById(R.id.poet_javed_akhter_image);
        adaJafriImage = findViewById(R.id.poet_ada_jafri_image);
        majroohSultanpuriImage = findViewById(R.id.poet_majrooh_sultanpuri_image);
        jaunEliaImage = findViewById(R.id.poet_jaun_elia_image);
        ahmadFarazImage = findViewById(R.id.poet_ahmad_faraz_image);
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
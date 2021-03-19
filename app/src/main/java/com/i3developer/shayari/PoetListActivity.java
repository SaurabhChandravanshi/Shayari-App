package com.i3developer.shayari;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class PoetListActivity extends AppCompatActivity {

    private ImageView gulzarImage,ghalibImage,rahatIndoriImage,kumarVishwasImage,faizAhmadFaizImage,bashirBadrImage,anamikaJainImage;
    private ImageView mirTaqiMirImage,bakshNasikhImage,allamaIqbalImage;
    private ImageView akbarAllahabadiImage,parveenShakirImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poet_list);
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
    }
}
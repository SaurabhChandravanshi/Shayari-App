package com.i3developer.shayari;

import android.app.Application;
import android.util.Log;

import com.facebook.ads.AdSettings;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Map;

import im.crisp.client.Crisp;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AdSettings.setDataProcessingOptions(new String[]{},0,0);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                for (String adapterClass : statusMap.keySet()) {
                    AdapterStatus status = statusMap.get(adapterClass);
                    Log.d("MyApp", String.format(
                            "Adapter name: %s, Description: %s, Latency: %d, State: %s",
                            adapterClass, status.getDescription(), status.getLatency(),status.getInitializationState()));
                }

                // Start loading ads here...
            }
        });

        Crisp.configure(getApplicationContext(),getResources().getString(R.string.crisp_website_id));
    }
}

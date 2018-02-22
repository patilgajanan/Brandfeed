package com.appsplanet.brandfeed.Acitvity;

import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.appsplanet.brandfeed.Backgroundservice.NetworkChangeReceiver;
import com.appsplanet.brandfeed.R;

import static com.appsplanet.brandfeed.Config.Constants.CONNECTIVITY_ACTION;

public class Demo extends AppCompatActivity {

    IntentFilter intentFilter;
    NetworkChangeReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }


}

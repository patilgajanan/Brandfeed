package com.appsplanet.brandfeed.Backgroundservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.appsplanet.brandfeed.Database.DatabaseHandler;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private DatabaseHandler dbhandler;
    private Context mcContext;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        //dbhandler = new DatabaseHandler(mcContext);
        String status = NetworkUtil.getConnectivityStatusString(context);
       // Toast.makeText(context, status, Toast.LENGTH_LONG).show();


    }


}
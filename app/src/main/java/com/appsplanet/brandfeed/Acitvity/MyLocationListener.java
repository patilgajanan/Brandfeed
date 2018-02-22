package com.appsplanet.brandfeed.Acitvity;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by dell on 2/12/2018.
 */

class MyLocationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location loc) {
        loc.getLatitude();
        loc.getLongitude();
        String Text = "My current location is: " + "Latitude = "
                + loc.getLatitude() + "Longitude = " + loc.getLongitude();
        Log.d("TAG", "Starting..");
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}/* End of Class MyLocationListener */

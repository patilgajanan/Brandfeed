package com.appsplanet.brandfeed.Acitvity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.appsplanet.brandfeed.Backgroundservice.NetworkChangeReceiver;
import com.appsplanet.brandfeed.Config.Functions;
import com.appsplanet.brandfeed.Config.IpConfig;
import com.appsplanet.brandfeed.Config.StringConfig;
import com.appsplanet.brandfeed.Database.DatabaseHandler;
import com.appsplanet.brandfeed.Database.SharedPreferencesDatabase;
import com.appsplanet.brandfeed.R;
import com.appsplanet.brandfeed.Utils.JsonObjectRequestWithHeader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.appsplanet.brandfeed.Config.Constants.CONNECTIVITY_ACTION;

public class MainActivity extends AppCompatActivity {
    private ImageView iv_deploy, iv_logo;

    IntentFilter intentFilter;
    NetworkChangeReceiver receiver;
    private LinearLayout ll_main_deployement, ll_main_recee;
    private CardView card_main;
    private TextView tv_deployment, tv_recee;
    int PERMISSION_ALL = 1;
    private android.support.v7.widget.Toolbar toolbar_main;
    private RequestQueue requestQueue;
    private DatabaseHandler dbDatabaseHandler;
    private SharedPreferencesDatabase sharedPreferencesDatabase;
    String[] PERMISSIONS = {Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.LOCATION_HARDWARE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA};

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferencesDatabase = new SharedPreferencesDatabase(MainActivity.this);
        sharedPreferencesDatabase.createDatabase();
        toolbar_main = findViewById(R.id.toolbar_main);
        tv_deployment = findViewById(R.id.tv_deployment);
        tv_recee = findViewById(R.id.tv_recee);
        setSupportActionBar(toolbar_main);
        toolbar_main.setTitleTextColor(Color.BLACK);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        requestQueue = Volley.newRequestQueue(this);
        dbDatabaseHandler = new DatabaseHandler(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        if (Functions.isNetworkAvailable(MainActivity.this)) {
            String mobileno = sharedPreferencesDatabase.getData(StringConfig.LOGIN_MOBILE);
            boolean checkdb = dbDatabaseHandler.checkDb("master");
            if (checkdb == true) {
                Toast.makeText(this, "Data Is available in Local Db", Toast.LENGTH_SHORT).show();
            } else {
                getServerData(mobileno);
                Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
            }


        }
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }


        tv_recee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ReceeStore.class));


            }
        });

        tv_deployment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Deployment.class));


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                //Toast.makeText(getApplicationContext(), "Item 1 Selected", Toast.LENGTH_LONG).show();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;

    }

    public void getServerData(final String mobile) {
        // btnVisiblity(false);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contactNumber", mobile);


            JsonObjectRequestWithHeader jsonObjReqforshop = new JsonObjectRequestWithHeader(Request.Method.POST, IpConfig.ADDRESS_FOR_GET_DATA_FROM_SERVER + "", jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getBoolean("result")) {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject parentObject = data.getJSONObject(i);
                                String id = parentObject.getString("id");
                                String shopCode = parentObject.getString("shopCode");
                                String address = parentObject.getString("address");
                                String shop_id = parentObject.getString("id");
                                String shopOwner = parentObject.getString("shopOwner");
                                String shopName = parentObject.getString("shopName");
                                String autoAddress = parentObject.getString("autoAddress");
                                String shopPhoto = parentObject.getString("shopPhoto");
                                String formPhoto = parentObject.getString("formPhoto");
                                String height = parentObject.getString("height");
                                String width = parentObject.getString("width");
                                String cityId = parentObject.getString("cityId");
                                String divisionId = parentObject.getString("divisionId");
                                String region = parentObject.getString("region");
                                String assignedUserId = parentObject.getString("assignedUserId");
                                String status = parentObject.getString("status");
                                String submitedOn = parentObject.getString("submitedOn");
                                String createdOn = parentObject.getString("createdOn");
                                String cluster = parentObject.getString("cluster");
                                String latlon = parentObject.getString("latLong");
                                String userId = parentObject.getString("userId");
                                if (status.equals("0")) {
                                    dbDatabaseHandler.addDeploymentLocalorserver(shopCode, id, shopName, address, autoAddress, null, null, region, height, width, cityId, divisionId, cluster, latlon, userId, assignedUserId, status, submitedOn, createdOn, "0");

                                }

                            }

                        } else if (response.getString("result").equals("false")) {

                            Toast.makeText(MainActivity.this, response.getString("response").toString(), Toast.LENGTH_SHORT).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    String message = null;
                    if (volleyError instanceof NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                        Toast.makeText(MainActivity.this, "message", Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof ServerError) {
                        message = "The server could not be found. Please try again after some time!!";
                        Toast.makeText(MainActivity.this, "message", Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                        Toast.makeText(MainActivity.this, "message", Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof ParseError) {
                        message = "Parsing error! Please try again after some time!!";
                        Toast.makeText(MainActivity.this, "message", Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                        Toast.makeText(MainActivity.this, "message", Toast.LENGTH_SHORT).show();
                    } else if (volleyError instanceof TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection.";
                        Toast.makeText(MainActivity.this, "message", Toast.LENGTH_SHORT).show();
                    }

                }

            });
            requestQueue.add(jsonObjReqforshop);
            // AppController.getInstance().addToRequestQueue(jsonObjReq, "getdetails");

        } catch (Exception ex) {

        }
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



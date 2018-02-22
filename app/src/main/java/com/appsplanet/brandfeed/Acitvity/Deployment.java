package com.appsplanet.brandfeed.Acitvity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.BuildConfig;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import com.appsplanet.brandfeed.Config.Functions;
import com.appsplanet.brandfeed.Config.IpConfig;
import com.appsplanet.brandfeed.Config.StringConfig;
import com.appsplanet.brandfeed.Database.DatabaseHandler;
import com.appsplanet.brandfeed.Database.SharedPreferencesDatabase;

import com.appsplanet.brandfeed.Model.ImageModel;
import com.appsplanet.brandfeed.Model.RestInterface;
import com.appsplanet.brandfeed.R;
import com.appsplanet.brandfeed.Utils.JsonObjectRequestWithHeader;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.kbeanie.multipicker.api.CacheLocation;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Deployment extends AppCompatActivity implements ImagePickerCallback, AdapterView.OnItemSelectedListener {

    private Toolbar toolbar_deployment_scan;
    private TextView tv_latitude, tv_longitude, tv_address, tv_latitude_form, tv_longitude_form, tv_address_form, tv_shopname;
    private Button btn_submit_deployment;
    private DatabaseHandler databaseHandler;
    private ProgressBar pb_deployment, pb_deployment_form, pb_deployment_photo;
    private Bitmap bp;
    private ImageView iv_dealerboard, iv_form, iv_temp;
    private byte[] photo;
    private SharedPreferencesDatabase sharedPreferencesDatabase;
    private String str_dealerboard = "";
    String lattitude, longitude, timestamp, str_location;
    private static final String TAG = Deployment.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private FusedLocationProviderClient mFusedLocationClient;
    protected Location mLastLocation;
    private String str_shop_image_response, str_form_image_response, str_image_type;
    private CoordinatorLayout coordinatorLayout_deployment;
    private String ShopCode;
    private String mlatlongcombined, shop_iD, userid, assigned_userid;
    private TextView ac_tv_select_store;
    private boolean flag = false;
    private RequestQueue requestQueue;
    private EditText et_dealerboard_height, et_dealerboard_width;
    public static final int RequestPermissionCode = 1;
    File photoFile = null;
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    private String uri;
    private String mimeType;
    private String str_height_dealerboard = "", str_width_dealerboard = "";
    private CameraImagePicker cameraPicker;
    private String pickerPath;
    private Spinner ac_sp_select_store;
    private CardView card_view_dealerboard, card_view_form;
    private LinearLayout ll_btnsubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deployment);
        sharedPreferencesDatabase = new SharedPreferencesDatabase(Deployment.this);
        sharedPreferencesDatabase.createDatabase();
        //   AndroidNetworking.initialize(getApplicationContext(), okHttpClient);
        Functions.Camera_library();
        ac_sp_select_store = findViewById(R.id.ac_sp_select_store);
        ac_sp_select_store.setOnItemSelectedListener(this);
        card_view_form = findViewById(R.id.card_view_form);
        card_view_dealerboard = findViewById(R.id.card_view_dealerboard);
        ll_btnsubmit = findViewById(R.id.ll_btnsubmit);
        tv_shopname = findViewById(R.id.tv_shopname);
        // Loading spinner data from database
        loadSpinnerData();
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            Functions.buildAlertMessageNoGps(Deployment.this);
        }

        iv_dealerboard = findViewById(R.id.iv_dealerboard);
        iv_form = findViewById(R.id.iv_form);
        pb_deployment_form = findViewById(R.id.pb_deployment_form);
        pb_deployment_photo = findViewById(R.id.pb_deployment_photo);
        toolbar_deployment_scan = findViewById(R.id.toolbar_deployment_scan);
        tv_longitude_form = findViewById(R.id.tv_longitude_form);
        tv_latitude_form = findViewById(R.id.tv_latitude_form);
        tv_address_form = findViewById(R.id.tv_address_form);
        tv_latitude = findViewById(R.id.tv_latitude);
        tv_longitude = findViewById(R.id.tv_longitude);
        tv_address = findViewById(R.id.tv_address);
        pb_deployment = findViewById(R.id.pb_deployment);
        ac_tv_select_store = findViewById(R.id.ac_tv_select_store);
        coordinatorLayout_deployment = findViewById(R.id.coordinatorLayout_deployment);
        iv_temp = findViewById(R.id.iv_temp);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        et_dealerboard_height = findViewById(R.id.et_dealerboard_height);
        et_dealerboard_width = findViewById(R.id.et_dealerboard_width);

        requestQueue = Volley.newRequestQueue(this); // 'this' is the Context
        if (!checkPermissions()) {
            requestPermissions();

        } else {
            if (Functions.isNetworkAvailable(Deployment.this)) {
                getLastLocation();
            } else {
                getOfflineLatLong();
            }
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        timestamp = simpleDateFormat.format(new Date());
        btn_submit_deployment = findViewById(R.id.btn_submit_deployment);
        setSupportActionBar(toolbar_deployment_scan);
        getSupportActionBar().setTitle("Deployment");
        toolbar_deployment_scan.setTitleTextColor(Color.BLACK);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        databaseHandler = new DatabaseHandler(this);
        toolbar_deployment_scan.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_dealerboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iv_dealerboard.getTag().toString().equals("camera")) {
                    // str_dealerboard = "dealerboard";
                    sharedPreferencesDatabase.addData(StringConfig.IMAGE_type, "dealerboard");
                    takePicture();

                } else {

                    Bitmap bitmap = ((BitmapDrawable) iv_dealerboard.getDrawable()).getBitmap();
                    viewImgDialog(bitmap);
                }

            }
        });

        iv_form.setOnClickListener(new View.OnClickListener()

        {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (iv_form.getTag().toString().equals("camera")) {
                    //str_dealerboard = "form";
                    sharedPreferencesDatabase.addData(StringConfig.IMAGE_type, "form");

                    takePicture();
                } else {

                    Bitmap bitmap = ((BitmapDrawable) iv_form.getDrawable()).getBitmap();
                    viewImgDialog(bitmap);
                }

            }
        });


        btn_submit_deployment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_location = tv_address.getText().toString();
                str_width_dealerboard = et_dealerboard_width.toString();
                str_height_dealerboard = et_dealerboard_height.toString();
                // getShopDetails(ShopCode);
                userid = sharedPreferencesDatabase.getData(StringConfig.LOGIN_ID);
                // shop_iD = sharedPreferencesDatabase.getData(StringConfig.SHOP_ID);

                mlatlongcombined = lattitude + "," + longitude;
                assigned_userid = sharedPreferencesDatabase.getData(StringConfig.ASSIGNED_USERID);
                str_shop_image_response = sharedPreferencesDatabase.getData(StringConfig.DEALERBOARD_IMAGE);
                str_form_image_response = sharedPreferencesDatabase.getData(StringConfig.FORM_IMAGE);
                if (str_width_dealerboard.isEmpty()) {
                    Snackbar.make(coordinatorLayout_deployment, "Please Enter Dealerboard Width", Snackbar.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(str_height_dealerboard)) {
                    Snackbar.make(coordinatorLayout_deployment, "Please Enter Dealerboard Height", Snackbar.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(shop_iD)) {
                    Snackbar.make(coordinatorLayout_deployment, "Please Enter Shop Id", Snackbar.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(str_location)) {
                    Snackbar.make(coordinatorLayout_deployment, "Location is Null", Snackbar.LENGTH_SHORT).show();


                } else if (TextUtils.isEmpty(str_shop_image_response) && str_shop_image_response == null) {
                    Snackbar.make(coordinatorLayout_deployment, "Please Capture Dealerboard Image", Snackbar.LENGTH_SHORT).show();


                } else if (TextUtils.isEmpty(str_form_image_response) && str_form_image_response == null) {

                    Snackbar.make(coordinatorLayout_deployment, "Please Capture Form Image", Snackbar.LENGTH_SHORT).show();


                } else if (TextUtils.isEmpty(mlatlongcombined)) {
                    Snackbar.make(coordinatorLayout_deployment, "Please Lat Long Can not be null", Snackbar.LENGTH_SHORT).show();


                } else if (TextUtils.isEmpty(userid)) {
                    Snackbar.make(coordinatorLayout_deployment, "Please User Id can not be null", Snackbar.LENGTH_SHORT).show();


                } else if (TextUtils.isEmpty(assigned_userid)) {
                    Snackbar.make(coordinatorLayout_deployment, "Please Assigned Id Null", Snackbar.LENGTH_SHORT).show();


                } else {
                    if (Functions.isNetworkAvailable(Deployment.this)) {
                        submit_Deployment(shop_iD, str_location, str_shop_image_response, str_form_image_response, mlatlongcombined, userid, assigned_userid, str_height_dealerboard, str_width_dealerboard);
                        databaseHandler.UpdateData(ShopCode, "1");//send to server
                    } else {

                        databaseHandler.UpdateData(ShopCode, "0");//update to local


                    }
                }


            }

        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (iv_dealerboard.getTag().equals("selected")) {
            iv_dealerboard.setTag("selected");

        } else if (iv_form.getTag().equals("selected")) {
            iv_form.setTag("selected");
        } else {
            iv_dealerboard.setTag("camera");
            iv_form.setTag("camera");
        }
    }

    private void getOfflineLatLong() {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                if (location != null) {
                    Toast toast = Toast.makeText(getApplicationContext(), "" + location, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                if (provider != null) {
                    Toast toast = Toast.makeText(getApplicationContext(), "" + provider, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }

            @Override
            public void onProviderEnabled(String provider) {
                if (provider != null) {
                    Toast toast = Toast.makeText(getApplicationContext(), "" + provider, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }

            @Override
            public void onProviderDisabled(String provider) {
                if (provider != null) {
                    Toast toast = Toast.makeText(getApplicationContext(), "" + provider, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        //locationManager.requestLocationUpdates(LocationManager.KEY_LOCATION_CHANGED, 0, 0, locationListener);

    }

    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            tv_latitude.setText(String.valueOf(mLastLocation.getLatitude()));
                            tv_longitude.setText(String.valueOf(mLastLocation.getLongitude()));

                            tv_latitude_form.setText(String.valueOf(mLastLocation.getLatitude()));
                            tv_longitude_form.setText(String.valueOf(mLastLocation.getLongitude()));
                            lattitude = tv_latitude.getText().toString();
                            longitude = tv_longitude.getText().toString();
                            try {
                                Geocoder geocoder = new Geocoder(Deployment.this, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
                                tv_address.setText(addresses.get(0).getAddressLine(0));
                                tv_address_form.setText(addresses.get(0).getAddressLine(0));
                            } catch (Exception e) {

                                Toast.makeText(Deployment.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());
                            Snackbar.make(coordinatorLayout_deployment, getString(R.string.no_location_detected), Snackbar.LENGTH_SHORT);
                            //showSnackbar(getString(R.string.no_location_detected));
                        }
                    }
                });
    }


    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(Deployment.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            startLocationPermissionRequest();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            } else {
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }


    //Convert bitmap to bytes
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private byte[] profileImage(Bitmap b) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }


    public void viewImgDialog(final Bitmap img) {
        final AlertDialog alertDialog = new AlertDialog.Builder(Deployment.this).create();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.dialog_view_img, null);
        alertDialog.setView(v);
        alertDialog.setCancelable(true);
        alertDialog.show();
        final ImageView iv_view_dialog = (ImageView) v.findViewById(R.id.iv_view_dialog);
        iv_view_dialog.setImageBitmap(img);
    }


    public void submit_Deployment(final String shop_id, final String address, final String fileshop, String fileformphoto, final String latLong, final String userId, final String assignedUserId, String height, String width) {
        btnVisiblity(false);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", shop_id);
            jsonObject.put("address", address);
            jsonObject.put("shopPhoto", fileshop);
            jsonObject.put("formPhoto", fileformphoto);
            jsonObject.put("latLong", latLong);
            jsonObject.put("userId", userId);
            jsonObject.put("assignedUserId", assignedUserId);
            jsonObject.put("height", height);
            jsonObject.put("width", width);
            RequestQueue queue = Volley.newRequestQueue(this.getBaseContext());
            JsonObjectRequestWithHeader jsonObjReq = new JsonObjectRequestWithHeader(Request.Method.POST, IpConfig.ADDRESSFORDEPLOYMENT + "", jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getBoolean("result")) {
                            submitDialog();

                        } else if (response.getString("result").equals("false")) {
                            Snackbar.make(coordinatorLayout_deployment, response.getString("response").toString(), Snackbar.LENGTH_LONG).show();
                        }
                        btnVisiblity(true);
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
                        Snackbar.make(coordinatorLayout_deployment, message, Snackbar.LENGTH_SHORT).show();
                    } else if (volleyError instanceof ServerError) {
                        message = "The server could not be found. Please try again after some time!!";
                        Snackbar.make(coordinatorLayout_deployment, message, Snackbar.LENGTH_SHORT).show();
                    } else if (volleyError instanceof AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                        Snackbar.make(coordinatorLayout_deployment, message, Snackbar.LENGTH_SHORT).show();
                    } else if (volleyError instanceof ParseError) {
                        message = "Parsing error! Please try again after some time!!";
                        Snackbar.make(coordinatorLayout_deployment, message, Snackbar.LENGTH_SHORT).show();
                    } else if (volleyError instanceof NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                        Snackbar.make(coordinatorLayout_deployment, message, Snackbar.LENGTH_SHORT).show();
                    } else if (volleyError instanceof TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection.";
                        Snackbar.make(coordinatorLayout_deployment, message, Snackbar.LENGTH_SHORT).show();
                    }

                    btnVisiblity(true);
                }

            });
            requestQueue.add(jsonObjReq);


        } catch (Exception ex) {

        }
    }


    public void submitDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(Deployment.this).create();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.dialog_submit_deployment, null);
        alertDialog.setView(v);
        alertDialog.setCancelable(true);
        alertDialog.show();

        TextView tv_title_info_dialog = (TextView) v.findViewById(R.id.tv_title_info_dialog);
        Button btn_close_info_dialog = (Button) v.findViewById(R.id.btn_close_info_dialog);
        Button btn_ok = (Button) v.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Deployment.this, Deployment.class));
                finish();
            }
        });
        btn_close_info_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });
    }

    public void btnVisiblity(boolean status) {
        if (status) {
            pb_deployment.setVisibility(View.GONE);
            btn_submit_deployment.setVisibility(View.VISIBLE);

        } else {
            pb_deployment.setVisibility(View.VISIBLE);
            btn_submit_deployment.setVisibility(View.INVISIBLE);
        }
    }

    public void takePicture() {
        cameraPicker = new CameraImagePicker(this);
        cameraPicker.setDebugglable(true);
        cameraPicker.setCacheLocation(CacheLocation.EXTERNAL_STORAGE_APP_DIR);
        cameraPicker.setImagePickerCallback(this);
        cameraPicker.shouldGenerateMetadata(true);

        cameraPicker.shouldGenerateThumbnails(true);
        pickerPath = cameraPicker.pickImage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Picker.PICK_IMAGE_CAMERA && resultCode == RESULT_OK) {
            if (cameraPicker == null) {
                cameraPicker = new CameraImagePicker(this);
                cameraPicker.setImagePickerCallback(this);
                cameraPicker.reinitialize(pickerPath);
            }


            cameraPicker.submit(data);
        } else {
            String uRI = "";
            if (iv_dealerboard.getTag().equals("selected")) {
                uRI = sharedPreferencesDatabase.getData(StringConfig.uri);
                Picasso.with(this).load(Uri.fromFile(new File(uRI))).into(iv_dealerboard);
                iv_dealerboard.setImageResource(R.mipmap.deploy_camera);
                iv_form.setTag("camera");


            } else if (iv_form.getTag().equals("selected")) {
                uRI = sharedPreferencesDatabase.getData(StringConfig.uriform);
                Picasso.with(this).load(Uri.fromFile(new File(uRI))).into(iv_form);
                iv_dealerboard.setTag("camera");
                iv_form.setImageResource(R.mipmap.deploy_camera);

            } else {
                iv_dealerboard.setImageResource(R.mipmap.deploy_camera);
                iv_form.setTag("camera");
                iv_dealerboard.setTag("camera");
                iv_form.setImageResource(R.mipmap.deploy_camera);
            }
        }

    }

    @Override
    public void onImagesChosen(List<ChosenImage> images) {
        ChosenFile file = (ChosenFile) images.get(0);
        final ChosenImage image = (ChosenImage) file;
        uri = image.getThumbnailSmallPath();

        final File files = new File(uri);
        displayImage(files);
    }

    @Override
    public void onError(String message) {

        iv_form.setTag("camera");
        iv_dealerboard.setTag("camera");
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // You have to save path in case your activity is killed.
        // In such a scenario, you will need to re-initialize the CameraImagePicker
        outState.putString("picker_path", pickerPath);
        outState.putString("type", str_image_type);
        outState.putString("uri", uri);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // After Activity recreate, you need to re-intialize these
        // two values to be able to re-intialize CameraImagePicker
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                pickerPath = savedInstanceState.getString("picker_path");
                //pickerPath = savedInstanceState.getString("picker_path");
                String x = savedInstanceState.getString("type");
                String xs = savedInstanceState.getString("uri");
                if (!TextUtils.isEmpty(x)) {
                    if (x.equals("dealerboard")) {
                        iv_dealerboard.setImageBitmap(BitmapFactory.decodeFile(xs));
                    } else {
                        iv_form.setImageBitmap(BitmapFactory.decodeFile(xs));
                    }
                }
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void displayImage(final File d) {
        str_image_type = sharedPreferencesDatabase.getData(StringConfig.IMAGE_type);
        if (Functions.isNetworkAvailable(Deployment.this)) {
            if (str_image_type.equals("dealerboard")) {
                Functions.imageUpload(iv_dealerboard, d, str_image_type, Deployment.this, pb_deployment_photo, sharedPreferencesDatabase);
                Picasso.with(Deployment.this).load(Uri.fromFile(new File(uri))).skipMemoryCache().into(iv_dealerboard, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        sharedPreferencesDatabase.addData(StringConfig.uri, uri);
                        iv_dealerboard.setTag("selected");
                    }

                    @Override
                    public void onError() {

                    }
                });

            } else if (str_image_type.equals("form"))

            {

                Functions.imageUpload(iv_form, d, "form", Deployment.this, pb_deployment_form, sharedPreferencesDatabase);
                Picasso.with(Deployment.this).load(Uri.fromFile(new File(uri))).skipMemoryCache().into(iv_form, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        sharedPreferencesDatabase.addData(StringConfig.uriform, uri);
                        iv_form.setTag("selected");

                    }

                    @Override
                    public void onError() {

                    }
                });

            } else

            {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }

        } else {
            if (str_image_type.equals("dealerboard"))

            {
                Bitmap bitmap = BitmapFactory.decodeFile(uri);
                byte[] imgdealerboard = profileImage(bitmap);
                databaseHandler.UpdateDataImages(ShopCode, imgdealerboard, "dealerboard");
                Picasso.with(Deployment.this).load(Uri.fromFile(new File(uri))).skipMemoryCache().into(iv_dealerboard, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        // sharedPreferencesDatabase.addData(StringConfig.uri, uri);
                        iv_dealerboard.setTag("selected");
                    }

                    @Override
                    public void onError() {

                    }
                });

            } else if (str_image_type.equals("form"))

            {
                Bitmap bitmap = BitmapFactory.decodeFile(uri);
                byte[] imgform = profileImage(bitmap);
                databaseHandler.UpdateDataImages(ShopCode, imgform, "form");
                Picasso.with(Deployment.this).load(Uri.fromFile(new File(uri))).skipMemoryCache().into(iv_form, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        //sharedPreferencesDatabase.addData(StringConfig.uriform, uri);
                        iv_form.setTag("selected");

                    }

                    @Override
                    public void onError() {

                    }
                });

            } else

            {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void loadSpinnerData() {
        // database handler
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        // Spinner Drop down elements
        List<String> lables = db.getAllshopcode();
        // Creating adapter for spinner
        if (lables.isEmpty()) {
            lables.add("No Store Available");
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        ac_sp_select_store.setAdapter(dataAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        ShopCode = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        if (ShopCode.equals("Select Shop Code")) {
            card_view_dealerboard.setVisibility(View.GONE);
            card_view_form.setVisibility(View.GONE);
            ll_btnsubmit.setVisibility(View.GONE);
        } else {
            card_view_dealerboard.setVisibility(View.VISIBLE);
            card_view_form.setVisibility(View.VISIBLE);
            ll_btnsubmit.setVisibility(View.VISIBLE);
            String[] getidandname = databaseHandler.getshopidFromlocaldb(ShopCode);
            shop_iD = getidandname[0];
            String shopname = getidandname[1];
            tv_shopname.setText(shopname);
            //shop_iD = databaseHandler.getshopidFromlocaldb(ShopCode);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

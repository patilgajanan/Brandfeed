package com.appsplanet.brandfeed.Acitvity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appsplanet.brandfeed.Config.Functions;
import com.appsplanet.brandfeed.Config.IpConfig;
import com.appsplanet.brandfeed.Config.StringConfig;
import com.appsplanet.brandfeed.Database.DatabaseHandler;
import com.appsplanet.brandfeed.Database.SharedPreferencesDatabase;
import com.appsplanet.brandfeed.R;
import com.appsplanet.brandfeed.Utils.AppController;
import com.appsplanet.brandfeed.Utils.JsonObjectRequestWithHeader;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by dell on 1/17/2018.
 */

public class LoginActivity extends AppCompatActivity {
    private Button btn_login;
    private EditText et_email_login, et_password_login;
    private SharedPreferencesDatabase sharedPreferencesDatabase;
    private CoordinatorLayout coordinatorLayout_login;
    private ProgressBar pb_login;
    private RequestQueue requestQueue;
    private DatabaseHandler dbhandler;
    private String login_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_email_login = findViewById(R.id.et_email_login);
        coordinatorLayout_login = findViewById(R.id.coordinatorLayout_login);
        et_password_login = findViewById(R.id.et_password_login);
        btn_login = findViewById(R.id.btn_login);
        pb_login = findViewById(R.id.pb_login);
        sharedPreferencesDatabase = new SharedPreferencesDatabase(LoginActivity.this);
        sharedPreferencesDatabase.createDatabase();
        dbhandler = new DatabaseHandler(this);
        requestQueue = Volley.newRequestQueue(this); // 'this' is the Context

        login_status = sharedPreferencesDatabase.getData(StringConfig.LOGIN_STATUS);
        if (!TextUtils.isEmpty(login_status)) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s_et_email_login = et_email_login.getText().toString();
                String s_et_password_login = et_password_login.getText().toString();
                if (TextUtils.isEmpty(s_et_email_login)) {
                    Snackbar.make(coordinatorLayout_login, "Please enter Mobile", Snackbar.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(s_et_password_login)) {
                    Snackbar.make(coordinatorLayout_login, "Please enter Valid mobile", Snackbar.LENGTH_SHORT).show();

                } else {

                    if (Functions.isNetworkAvailable(LoginActivity.this)) {
                        if (dbhandler.checkDb("user")) {
                            btnVisiblity(false);
                            dbhandler.checkUser(s_et_email_login, s_et_password_login);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                            sharedPreferencesDatabase.addData(StringConfig.LOGIN_STATUS, "true");
                        } else {
                            checkLogin(s_et_email_login, s_et_password_login);
                        }
                    } else {
                        btnVisiblity(false);
                        dbhandler.checkUser(s_et_email_login, s_et_password_login);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                        sharedPreferencesDatabase.addData(StringConfig.LOGIN_STATUS, "true");

                    }

                }
            }
        });
    }

    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        btnVisiblity(false);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("deviceToken", "DT989879jjlj");
            jsonObject.put("deviceType", "0");
            JsonObjectRequestWithHeader jsonObjReq = new JsonObjectRequestWithHeader(Request.Method.POST, IpConfig.ADDRESS + "", jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getBoolean("result")) {
                            String array = response.getString("data");
                            JSONObject parentObject = new JSONObject(array);
                            String login_name = parentObject.getString("fullName");
                            String login_email = parentObject.getString("email");
                            String login_id = parentObject.getString("id");
                            String login_mobile = parentObject.getString("contactNumber");
                            String login_photo = parentObject.getString("photoPath");
                            String reportingTo = parentObject.getString("reportingTo");
                            String status = parentObject.getString("status");
                            String city = parentObject.getString("city");
                            sharedPreferencesDatabase.addData(StringConfig.LOGIN_ID, login_id);
                            sharedPreferencesDatabase.addData(StringConfig.LOGIN_CITY, city);
                            sharedPreferencesDatabase.addData(StringConfig.ASSIGNED_USERID, reportingTo);
                            sharedPreferencesDatabase.addData(StringConfig.LOGIN_MOBILE, login_mobile);
                            dbhandler.addUser(login_name, password, login_mobile, city, reportingTo, login_id, email);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                            sharedPreferencesDatabase.addData(StringConfig.LOGIN_STATUS, "true");

                        } else if (response.getString("result").equals("false")) {
                            Snackbar.make(coordinatorLayout_login, response.getString("response").toString(), Snackbar.LENGTH_LONG).show();
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
                        Snackbar.make(coordinatorLayout_login, message, Snackbar.LENGTH_SHORT).show();
                    } else if (volleyError instanceof ServerError) {
                        message = "The server could not be found. Please try again after some time!!";
                        Snackbar.make(coordinatorLayout_login, message, Snackbar.LENGTH_SHORT).show();
                    } else if (volleyError instanceof AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                        Snackbar.make(coordinatorLayout_login, message, Snackbar.LENGTH_SHORT).show();
                    } else if (volleyError instanceof ParseError) {
                        message = "Parsing error! Please try again after some time!!";
                        Snackbar.make(coordinatorLayout_login, message, Snackbar.LENGTH_SHORT).show();
                    } else if (volleyError instanceof NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                        Snackbar.make(coordinatorLayout_login, message, Snackbar.LENGTH_SHORT).show();
                    } else if (volleyError instanceof TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection.";
                        Snackbar.make(coordinatorLayout_login, message, Snackbar.LENGTH_SHORT).show();
                    }

                    btnVisiblity(true);
                }

            });

            requestQueue.add(jsonObjReq);

//            AppController.getInstance().addToRequestQueue(jsonObjReq, "login");


        } catch (Exception ex) {

        }
    }

    public void btnVisiblity(boolean status) {
        if (status) {
            btn_login.setVisibility(View.VISIBLE);
            pb_login.setVisibility(View.GONE);
        } else {
            btn_login.setVisibility(View.INVISIBLE);
            pb_login.setVisibility(View.VISIBLE);
        }
    }
}

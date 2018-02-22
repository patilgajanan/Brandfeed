package com.appsplanet.brandfeed.Config;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appsplanet.brandfeed.Database.SharedPreferencesDatabase;
import com.appsplanet.brandfeed.Model.ImageModel;
import com.appsplanet.brandfeed.Model.RestInterface;
import com.appsplanet.brandfeed.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.appsplanet.brandfeed.Config.IpConfig.URL;

/**
 * Created by surajk44437 on 18/11/2017.
 */

public class Functions {


    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @SuppressLint("RestrictedApi")
    public static void setToolBar(final Context context, Toolbar toolbar, String title, boolean back_button) {
        AppCompatActivity activity = (AppCompatActivity) context;
        activity.setSupportActionBar(toolbar);
        activity.setTitle(title);
        if (back_button) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((AppCompatActivity) context).onBackPressed();
                }
            });
        }
    }

    public static void setDatePicker(Context context, final EditText et, final TextView textView) {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                if (et != null) {
                    et.setText(dateFormatter.format(newDate.getTime()));
                }
                if (textView != null) {
                    textView.setText(dateFormatter.format(newDate.getTime()));
                }
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public static void detailsDialog(Context context, boolean cancelable, String title, String message, String negative_button, String positive_button) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(cancelable);
        if (!TextUtils.isEmpty(positive_button)) {
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, positive_button,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }
        if (!TextUtils.isEmpty(negative_button)) {
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, positive_button,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }
        alertDialog.show();
    }

    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy, hh:mm a");
        Calendar currentCalender = Calendar.getInstance(Locale.getDefault());
        String s_time = dateFormat.format(currentCalender.getTime());
        return s_time;
    }

    public static void Camera_library() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .detectAll()// or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }

    public static void imageUpload(final ImageView imageView, final File imagePath, final String type, final Context context, final ProgressBar progressBar, final SharedPreferencesDatabase sharedPreferencesDatabase) {
        progressBar.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.GONE);
        MultipartBody.Part fileToUpload = null;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RestInterface restInterface = retrofit.create(RestInterface.class);
        RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), imagePath);
        fileToUpload = MultipartBody.Part.createFormData("photo", imagePath.getName(), mFile);
        Call<ImageModel> imageModelCall = restInterface.upload(fileToUpload);
        imageModelCall.enqueue(new Callback<ImageModel>() {
            @Override
            public void onResponse(Call<ImageModel> call, Response<ImageModel> response) {
                if (response.isSuccessful()) {
                    String photo = response.body().getPhoto();
                    if (type.equals("dealerboard")) {
                        sharedPreferencesDatabase.addData(StringConfig.DEALERBOARD_IMAGE, photo);
                    } else {
                        sharedPreferencesDatabase.addData(StringConfig.FORM_IMAGE, photo);
                    }

                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<ImageModel> call, Throwable t) {
                Toast.makeText(context, "Fail to Upload", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                sharedPreferencesDatabase.addData(StringConfig.failedto_upload, "failed" + type);
                imageView.setVisibility(View.VISIBLE);
            }
        });

    }

    public static void buildAlertMessageNoGps(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean checkDataBase(String dbpath) {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(dbpath, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }
        return checkDB != null;
    }
}

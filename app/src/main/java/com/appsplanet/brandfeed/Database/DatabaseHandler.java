package com.appsplanet.brandfeed.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "brandfeeddatabase";


    //master database
    private static final String TABLE_MASTER = "master";
    private static final String KEY_ID_LOCAL = "id";
    private static final String KEY_SHOP_CODE_SERVER = "shopCode";
    private static final String KEY_ID_ONILINE = "iduser";
    private static final String KEY_SHOP_NAME_SERVER = "shopName";
    private static final String KEY_SHOP_ADDRESS_SERVER = "address";
    private static final String KEY_AUTO_ADDRESS = "autoAddress";
    private static final String KEY_SHOP_PHOTO_DEALERBOARD = "shopPhoto";
    private static final String KEY_SHOP_FORM_PHOTO = "formPhoto";
    private static final String KEY_REGION = "region";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_WIDTH = "width";
    private static final String KEY_CITYID = "cityId";
    private static final String KEY_DIVISIONID = "divisionId";
    private static final String KEY_CLUSTER = "cluster";
    private static final String KEY_LAT_LONG = "latLong";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_ASSIGNEDUSER_ID = "assignedUserId";
    private static final String KEY_STATUS = "donestatus";
    private static final String KEY_SUBMITED_ON = "submitedOn";
    private static final String KEY_CREATED_ON = "createdOn";
    private static final String KEY_SYNC_STATUS = "syncstatus";
    //USer Table
    private static final String TABLE_USER = "user";
    //column
    private static final String KEY_ID_USER_LOCAL = "ids";
    private static final String KEY_USER_NAME = "uname";
    private static final String KEY_USER_PASSWORD = "upass";
    private static final String KEY_USER_MOBILE = "umobile";
    private static final String KEY_CITY = "city";
    private static final String KEY_REPORTINGTO = "reportingto";
    private static final String KEY_LOGINID_USER = "userid";
    private static final String KEY_EMAIL = "email";


    // Deployment table name
    private static final String TABLE_DEPLOYMENT = "deployment";
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_LAT = "latitude";
    private static final String KEY_LONG = "longitude";
    private static final String KEY_SHOP_NAME = "shopname";
    private static final String KEY_TYPE = "type";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_TIMESTAMP = "timestamp";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //master table create

        //private static final String CREATE_TABLE_TAG = "CREATE TABLE " + TABLE_TAG+ "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TAG_NAME + " TEXT,"+ KEY_CREATED_AT + " DATETIME" + ")";


        String CREATE_MASTER_TABLE = "CREATE TABLE " + TABLE_MASTER + "(" + KEY_ID_LOCAL + " INTEGER PRIMARY KEY," + KEY_ID_ONILINE + " TEXT," + KEY_CLUSTER + " TEXT," + KEY_SHOP_CODE_SERVER + " TEXT," + KEY_SHOP_NAME_SERVER + " TEXT," + KEY_SHOP_ADDRESS_SERVER + " TEXT," + KEY_AUTO_ADDRESS + " TEXT," + KEY_SHOP_PHOTO_DEALERBOARD + " BLOB," + KEY_SHOP_FORM_PHOTO + " BLOB," + KEY_REGION + " TEXT," + KEY_HEIGHT + " TEXT," + KEY_WIDTH + " TEXT," + KEY_CITYID + " TEXT," + KEY_DIVISIONID + " TEXT," + KEY_LAT_LONG + " TEXT," + KEY_USER_ID + " TEXT," + KEY_ASSIGNEDUSER_ID + " TEXT," + KEY_STATUS + " TEXT," + KEY_SUBMITED_ON + " TEXT," + KEY_CREATED_ON + " TEXT," + KEY_SYNC_STATUS + " TEXT" + ")";
        db.execSQL(CREATE_MASTER_TABLE);
        //deployment table
        String CREATE_DEPLOYMENT_TABLE = "CREATE TABLE " + TABLE_DEPLOYMENT + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LAT + " TEXT," + KEY_LONG + " TEXT," + KEY_SHOP_NAME + " TEXT," + KEY_TYPE + " TEXT," + KEY_IMAGE + " BLOB," + KEY_ADDRESS + " TEXT,"
                + KEY_TIMESTAMP + " TEXT" + ")";
        db.execSQL(CREATE_DEPLOYMENT_TABLE);

        //table create for user
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "(" + KEY_ID_USER_LOCAL + " INTEGER PRIMARY KEY," + KEY_USER_NAME + " TEXT," + KEY_USER_PASSWORD + " TEXT," + KEY_USER_MOBILE + " TEXT," + KEY_CITY + " TEXT," + KEY_REPORTINGTO + " TEXT," + KEY_LOGINID_USER + " TEXT,"
                + KEY_EMAIL + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEPLOYMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

    }

    public void addDeploymentLocalorserver(String shopcode, String key_id, String shop_name, String shop_address, String auto_address, byte[] dealerboard_image, byte[] form_image, String region, String height, String width, String cityid, String divisionId, String cluster, String lant_long, String user_id, String assigned_id, String status_for, String submittedon, String createdon, String sync_status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SHOP_CODE_SERVER, shopcode);
        values.put(KEY_ID_ONILINE, key_id);
        values.put(KEY_SHOP_NAME_SERVER, shop_name);
        values.put(KEY_SHOP_ADDRESS_SERVER, shop_address);
        values.put(KEY_AUTO_ADDRESS, auto_address);
        values.put(KEY_SHOP_PHOTO_DEALERBOARD, dealerboard_image);
        values.put(KEY_SHOP_FORM_PHOTO, form_image);
        values.put(KEY_REGION, region);
        values.put(KEY_HEIGHT, height);
        values.put(KEY_WIDTH, width);
        values.put(KEY_CITYID, cityid);
        values.put(KEY_DIVISIONID, divisionId);
        values.put(KEY_CLUSTER, cluster);
        values.put(KEY_LAT_LONG, lant_long);
        values.put(KEY_USER_ID, user_id);
        values.put(KEY_ASSIGNEDUSER_ID, assigned_id);
        values.put(KEY_STATUS, status_for);
        values.put(KEY_SUBMITED_ON, submittedon);
        values.put(KEY_CREATED_ON, createdon);
        values.put(KEY_SYNC_STATUS, sync_status);
        // Inserting Row
        db.insert(TABLE_MASTER, null, values);
        db.close(); // Closing database connection
    }

    public void addDeployment(String lat_shop, String longitude_shop, String shop_name, String type, byte[] image, String shop_address, String timeStamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LAT, lat_shop);
        values.put(KEY_LONG, longitude_shop);
        values.put(KEY_SHOP_NAME, shop_name);
        values.put(KEY_TYPE, type);
        values.put(KEY_IMAGE, image);
        values.put(KEY_ADDRESS, shop_address);
        values.put(KEY_TIMESTAMP, timeStamp);
        // Inserting Row
        db.insert(TABLE_DEPLOYMENT, null, values);
        db.close(); // Closing database connection
    }


    public void addUser(String uname, String upass, String umobile, String city, String reportingto, String userid, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, uname);
        values.put(KEY_USER_PASSWORD, upass);
        values.put(KEY_USER_MOBILE, umobile);
        values.put(KEY_CITY, city);
        values.put(KEY_REPORTINGTO, reportingto);
        values.put(KEY_LOGINID_USER, userid);
        values.put(KEY_EMAIL, email);
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
    }

    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                KEY_ID_USER_LOCAL
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = KEY_EMAIL + " = ?" + " AND " + KEY_USER_PASSWORD + " = ?";
        // selection arguments
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public boolean checkDb(String tablename) {
        boolean c = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + tablename;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0) {
            c = true;
        }
        return c;

    }

    public List<String> getAllshopcode() {
        List<String> shopcode = new ArrayList<String>();
        String status = "0";
        // Select All Query
        String selectQuery = "SELECT  shopCode FROM master where donestatus = " + status + "";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        shopcode.add("Select Shop Code");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                shopcode.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return shopcode;
    }

    public String[] getshopidFromlocaldb(String shopCode) {

        Cursor cursor = null;
        String shopId = "";
        String shopname = "";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            cursor = db.rawQuery("SELECT iduser,shopName FROM master WHERE shopCode=?", new String[]{shopCode + ""});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                shopId = cursor.getString(cursor.getColumnIndex("iduser"));
                shopname = cursor.getString(cursor.getColumnIndex("shopName"));

            }
            String[] arr = new String[2];
            arr[0] = shopId;
            arr[1] = shopname;
            return arr;
        } finally {
            cursor.close();
        }
    }

    public void UpdateData(String shocode, String syncstatus) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_STATUS, 1); //These Fields should be your String values of actual column names
        cv.put(KEY_SYNC_STATUS, syncstatus);
        String whereClause = "shopCode=?";
        String whereArgs[] = {shocode};
        database.update("master", cv, whereClause, whereArgs);
        //database.update(TABLE_MASTER, cv, KEY_STATUS + "=" + shocode, null);
        database.close();
    }

    public boolean UpdateDataImages(String shocode, byte[] img, String imgtype) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        if (imgtype.equals("dealerboard")) {
            cv.put(KEY_SHOP_PHOTO_DEALERBOARD, img);
        } else {

            cv.put(KEY_SHOP_FORM_PHOTO, img);
        }
        return database.update(TABLE_MASTER, cv, KEY_SHOP_CODE_SERVER + "=" + shocode, null) > 0;
        //database.update(TABLE_MASTER, cv, "shopCode=" + shocode, null);

    }
}

package com.appsplanet.brandfeed.Utils;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vishal Bhosale on 26-09-2017.
 */

public class JsonObjectRequestWithHeader extends JsonObjectRequest {

    public JsonObjectRequestWithHeader(int method, String url, JSONObject requestBody, Response.Listener listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
    }

    public String getBodyContentType() {
        return "application/json";
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Api-Key", "93cab2dc4c777a9f29789e1ed60a4eff");
        return params;
    }


}

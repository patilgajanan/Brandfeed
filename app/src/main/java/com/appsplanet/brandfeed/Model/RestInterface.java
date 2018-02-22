package com.appsplanet.brandfeed.Model;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by dell on 2/10/2018.
 */

public interface RestInterface {
    @Multipart
    @POST("?r=General/UploadPhoto")
    Call<ImageModel> upload(
          @Part MultipartBody.Part photo
    );
}

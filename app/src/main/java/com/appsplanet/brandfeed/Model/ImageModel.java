package com.appsplanet.brandfeed.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dell on 2/10/2018.
 */

public class ImageModel {
    @SerializedName("photo")
    private String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
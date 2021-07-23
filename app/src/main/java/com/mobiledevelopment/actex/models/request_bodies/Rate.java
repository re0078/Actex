package com.mobiledevelopment.actex.models.request_bodies;

import com.google.gson.annotations.SerializedName;

public class Rate {
    @SerializedName("value")
    int value;

    public Rate(int value ) {
        this.value = value;
    }

}

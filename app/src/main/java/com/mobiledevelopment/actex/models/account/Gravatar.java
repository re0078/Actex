
package com.mobiledevelopment.actex.models.account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Gravatar {

    @SerializedName("hash")
    @Expose
    private String hash;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

}

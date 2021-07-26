
package com.mobiledevelopment.actex.models.movie_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;

@Getter
public class ProductionCountry {

    @SerializedName("iso_3166_1")
    @Expose
    private String iso31661;
    @SerializedName("name")
    @Expose
    private String name;
}

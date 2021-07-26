
package com.mobiledevelopment.actex.models.lists;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ListResult {

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("favorite_count")
    @Expose
    private Integer favoriteCount;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("item_count")
    @Expose
    private Integer itemCount;
    @SerializedName("iso_639_1")
    @Expose
    private String iso6391;
    @SerializedName("list_type")
    @Expose
    private String listType;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("poster_path")
    @Expose
    private Object posterPath;
}

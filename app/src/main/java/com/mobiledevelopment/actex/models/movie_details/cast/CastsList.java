
package com.mobiledevelopment.actex.models.movie_details.cast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CastsList {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cast")
    @Expose
    private List<com.mobiledevelopment.actex.models.movie_details.cast.Cast> cast = null;
    @SerializedName("crew")
    @Expose
    private List<Crew> crew = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<com.mobiledevelopment.actex.models.movie_details.cast.Cast> getCast() {
        return cast;
    }

    public void setCast(List<com.mobiledevelopment.actex.models.movie_details.cast.Cast> cast) {
        this.cast = cast;
    }

    public List<Crew> getCrew() {
        return crew;
    }

    public void setCrew(List<Crew> crew) {
        this.crew = crew;
    }

}

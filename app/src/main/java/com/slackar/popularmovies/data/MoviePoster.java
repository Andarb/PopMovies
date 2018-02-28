package com.slackar.popularmovies.data;

import com.google.gson.annotations.SerializedName;

/* Movie poster and accompanying ID retrieved from themoviedb JSON */
public class MoviePoster {
    @SerializedName("poster_path")
    private String posterPath;

    private int id;

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
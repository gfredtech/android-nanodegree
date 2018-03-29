
package me.gfred.popularmovies1.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


public class Movie implements Parcelable {

    public String getOriginalTitle() {
        return originalTitle;
    }

    public int getId() { return id; }
    public void setId() { this.id = id; }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    private String originalTitle;
   private String posterPath;
   private String overview;
   private double voteAverage;
   private String releaseDate;
   private int id;

   public Movie(String originalTitle, String posterPath, String overview, double voteAverage, String releaseDate, int id) {
       this.originalTitle = originalTitle;
       this.posterPath = posterPath;
       this.overview = overview;
       this.voteAverage = voteAverage;
       this.releaseDate = releaseDate;
       this.id = id;
   }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}

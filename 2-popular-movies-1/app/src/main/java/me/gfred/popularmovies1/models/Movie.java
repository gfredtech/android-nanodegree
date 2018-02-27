
package me.gfred.popularmovies1.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Movie {

    public String getOriginalTitle() {
        return originalTitle;
    }

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

   public Movie(String originalTitle, String posterPath, String overview, double voteAverage, String releaseDate) {
       this.originalTitle = originalTitle;
       this.posterPath = posterPath;
       this.overview = overview;
       this.voteAverage = voteAverage;
       this.releaseDate = releaseDate;
   }

}

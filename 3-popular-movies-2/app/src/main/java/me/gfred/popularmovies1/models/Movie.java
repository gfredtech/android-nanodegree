
package me.gfred.popularmovies1.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class Movie implements Parcelable {

    public String getOriginalTitle() {
        return originalTitle;
    }

    public int getId() { return id; }

    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }

    public List<String> getReviews() {
        return reviews;
    }


    public String getPosterPath() {
        return posterPath;
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

    public String getReleaseDate() {
        return releaseDate;
    }

    private String originalTitle;
    private String posterPath;
    private String overview;
    private List<String> reviews;
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
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
       dest.writeString(originalTitle);
       dest.writeString(posterPath);
       dest.writeString(overview);
       dest.writeDouble(voteAverage);
       dest.writeString(releaseDate);
       dest.writeInt(id);
    }

    private Movie(Parcel parcel)  {
       originalTitle = parcel.readString();
       posterPath = parcel.readString();
       overview = parcel.readString();
       voteAverage = parcel.readDouble();
       releaseDate = parcel.readString();
       id = parcel.readInt();

    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[0];
        }
    };
}

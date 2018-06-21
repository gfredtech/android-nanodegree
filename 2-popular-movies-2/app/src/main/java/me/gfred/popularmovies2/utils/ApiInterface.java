package me.gfred.popularmovies2.utils;

import me.gfred.popularmovies2.model.MovieResults;
import me.gfred.popularmovies2.model.ReviewResults;
import me.gfred.popularmovies2.model.TrailerResults;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("top_rated")
    Call<MovieResults> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("popular")
    Call<MovieResults> getPopularMovies(@Query("api_key") String apiKey);

    @GET("{id}/reviews")
    Call<ReviewResults> getMovieReviews(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("{id}/videos")
    Call<TrailerResults> getMovieTrailers(@Path("id") int id, @Query("api_key") String apiKey);
}

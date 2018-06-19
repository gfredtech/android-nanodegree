package me.gfred.popularmovies2.utils;

import java.util.List;

import me.gfred.popularmovies2.model.Results;
import me.gfred.popularmovies2.model.Reviews;
import me.gfred.popularmovies2.model.Trailers;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("top_rated")
    Call<Results> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("popular")
    Call<Results> getPopularMovies(@Query("api_key") String apiKey);

    @GET("{id}/reviews")
    Call<List<Reviews>> getMovieReviews(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("{id}/videos")
    Call<List<Trailers>> getMovieTrailers(@Path("id") int id, @Query("api_key") String apiKey);
}

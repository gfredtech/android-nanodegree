package me.gfred.popularmovies1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.gfred.popularmovies1.models.Movie;

/**
 * Created by Gfred on 2/27/2018.
 */

public class JsonUtils {
    final String API_KEY = " ";

    public static Movie parseMovie(String json) throws JSONException {
        JSONObject reader = new JSONObject(json);
        String originalTitle = reader.getString("original_title");
        String imgURL = reader.getString("poster_path");
        String overview = reader.getString("overview");
        Double voteAverage = reader.getDouble("vote_average");
        String releaseDate = reader.getString("release_date");

        return new Movie(originalTitle, imgURL, overview, voteAverage, releaseDate);
    }

    public static List<Movie> parseListMovies(String json) throws JSONException {
        JSONObject reader = new JSONObject(json);
        JSONArray moviesJson = reader.getJSONArray("results");
        List<Movie> movieList = new ArrayList<>();
        for(int i = 0; i < 100; i++) {
            movieList.add(parseMovie(moviesJson.getString(i)));
        }

        return movieList;
    }
}

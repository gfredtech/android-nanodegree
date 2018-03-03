package me.gfred.popularmovies1.utils;

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

    public static Movie parseMovie(String json) throws JSONException {
        JSONObject reader = new JSONObject(json);
        String originalTitle = reader.getString("original_title");
        String imgURL = reader.getString("poster_path");
        String overview = reader.getString("overview");
        Double voteAverage = reader.getDouble("vote_average");
        String releaseDate = reader.getString("release_date");
        int id = reader.getInt("id");

        return new Movie(originalTitle, imgURL, overview, voteAverage, releaseDate, id);
    }

    public static ArrayList<Movie> parseListMovies(String json) throws JSONException {
        JSONObject reader = new JSONObject(json);

        JSONArray moviesJson = reader.getJSONArray("results");
        ArrayList<Movie> movieList = new ArrayList<>();
        if(moviesJson.length() != 0) {

            for (int i = 0; i < moviesJson.length(); i++) {
                movieList.add(parseMovie(moviesJson.getString(i)));

            }
        }

        return movieList;
    }
}

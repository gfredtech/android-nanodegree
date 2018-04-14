package me.gfred.popularmovies2.utils;

import android.net.Uri;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Gfred on 2/27/2018.
 */

public class NetworkUtils {
    private final static String MOVIE_URL = "https://api.themoviedb.org/3/movie";
    private final static String PARAM_POPULAR = "popular";
    private final static String PARAM_TOP_RATED = "top_rated";
    private final static String PARAM_REVIEWS = "reviews";
    private final static String PARAM_TRAILERS = "videos";
    private final static String PARAM_API ="api_key";
    private final static String apiKey = "";

    public static URL buildPopularMoviesQuery() {

        Uri builtUri = Uri.parse(MOVIE_URL).buildUpon()
                .appendPath(PARAM_POPULAR)
                .appendQueryParameter(PARAM_API, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildMovieReviewsQuery(int id) {
        Uri builtUri = Uri.parse(MOVIE_URL).buildUpon()
                .appendPath(String.valueOf(id))
                .appendPath(PARAM_REVIEWS)
                .appendQueryParameter(PARAM_API, apiKey)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildMovieTrailersQuery(int id) {
        Uri builtUri = Uri.parse(MOVIE_URL).buildUpon()
                .appendPath(String.valueOf(id))
                .appendPath(PARAM_TRAILERS)
                .appendQueryParameter(PARAM_API, apiKey)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildTopRatedMoviesQuery() {
        Uri builtUri = Uri.parse(MOVIE_URL).buildUpon()
                .appendPath(PARAM_TOP_RATED)
                .appendQueryParameter(PARAM_API, apiKey)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) {
        HttpURLConnection urlConnection;
        String server_response = null;
        try {

            urlConnection = (HttpURLConnection) url
                    .openConnection();


            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                server_response = readStream(urlConnection.getInputStream());

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return server_response;
    }

    private static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
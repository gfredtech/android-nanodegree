package me.gfred.popularmovies1;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Gfred on 2/27/2018.
 */

public class NetworkUtils {
    public final static String MOVIE_URL = "https://api.themoviedb.org/3/movie";
    public final static String PARAM_POPULAR = "popular";
    public final static String PARAM_API ="api_key";
    final static String apiKey = "b16c34514b915ef016d679c43db8e6a6";

    public static URL buildMovieQuery(int id) {
        Uri builtUri = Uri.parse(MOVIE_URL).buildUpon()
                .appendPath(String.valueOf(id))
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

    public static URL buildPopularMoviesQuery() {

        Uri builtUri = Uri.parse(MOVIE_URL).buildUpon()
                .appendPath(PARAM_POPULAR)
                .appendQueryParameter(PARAM_API, apiKey)
                .build();

        URL url = null;
        System.out.print(builtUri.toString());
        try {
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        System.out.println("response here");
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            if(scanner.hasNext()) {
                String result = scanner.next();
                System.out.println(scanner.next());
                return result;
            } else {
                System.out.println("null gotten");
                return null;
            }
        }
        finally {
            urlConnection.disconnect();
        }
    }
}

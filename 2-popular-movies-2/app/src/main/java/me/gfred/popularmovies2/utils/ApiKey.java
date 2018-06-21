package me.gfred.popularmovies2.utils;

public class ApiKey {
    public final static String API_KEY = "";
    public static final String IMAGE_PARAM = "http://image.tmdb.org/t/p/w185/";

    public static ApiInterface createRetrofitApi() {
        return ApiClient.getClient().create(ApiInterface.class);
    }
}

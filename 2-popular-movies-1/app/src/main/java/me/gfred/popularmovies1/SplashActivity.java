package me.gfred.popularmovies1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
import java.net.URL;

import me.gfred.popularmovies1.utils.NetworkUtils;

/**
 * Created by Gfred on 3/9/2018.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isOnline()) {
            makePopularMoviesQuery();

        } else {
            Toast.makeText(this,
                    "Please enable your internet connection first and try again",
                    Toast.LENGTH_SHORT).show();
            finish();
        }


    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public class PopularMoviesQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String jsonResults = null;
            try {
                jsonResults = NetworkUtils.getResponseFromHttpUrl(url);
                Log.v("JSON", jsonResults);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResults;
        }

        @Override
        protected void onPostExecute(String s) {
            // super.onPostExecute(s);
            System.out.println("xaxaxa" + s);
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("movies_data", s);
                startActivity(intent);
                finish();
            }
    }

    private void makePopularMoviesQuery() {
        URL popularMovies = NetworkUtils.buildPopularMoviesQuery();
        new PopularMoviesQueryTask().execute(popularMovies);

    }
}

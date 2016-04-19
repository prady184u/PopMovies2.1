package com.udacity.pradeepkumarr.popmovies.asynctask;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.udacity.pradeepkumarr.popmovies.BuildConfig;
import com.udacity.pradeepkumarr.popmovies.domain.Movie;
import com.udacity.pradeepkumarr.popmovies.domain.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by pradeepkumarr on 15/04/16.
 */
public class FetchMovieDetailsTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private final String LOG_TAG = FetchMovieDetailsTask.class.getSimpleName();
    String metric = "metric";
    String mode = "json";
    String appid = BuildConfig.TMDBAPIKEY;
    final String forecastBaseURL = "http://api.themoviedb.org/3/discover/movie?";
    final String QUERY_SORT_BY="sort_by";
    final String QUERY_APP_ID = "api_key";


    final String MOVIES_BASE_URL =
            "http://api.themoviedb.org/3/movie/";
    final String REVIEW_PATH = "reviews";

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {

        if (params == null) {
            return null;
        }
        //http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=API_KEY

        Uri moviesListUri = Uri.parse(forecastBaseURL).buildUpon()
                .appendQueryParameter(QUERY_SORT_BY, params[0])
                .appendQueryParameter(QUERY_APP_ID, appid).build();


        try {
            return getMoviesDataFromJson(getResults(moviesListUri));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    private String getResults(Uri build) {

        Uri builtUri = build;

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        try {
            URL url = new URL(builtUri.toString());
            Log.v(LOG_TAG, "built URI :" + builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();// causes NetworkonmainException

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            movieJsonStr = buffer.toString();
            Log.v(LOG_TAG, "Movie result result JSON :" + movieJsonStr);

            return movieJsonStr;
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        }  finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
    }

    private ArrayList<Movie> getMoviesDataFromJson(String movieJsonStr) throws JSONException {
        ArrayList<Movie> moviesList = new ArrayList<Movie>();


        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray moviesArray = movieJson.getJSONArray("results");


        for(int i = 0; i < moviesArray.length(); i++) {

            JSONObject movieObj = moviesArray.getJSONObject(i);


            Movie movie = new Movie(
                    movieObj.getString("poster_path"),
                    movieObj.getString("overview"),
                    movieObj.getString("release_date"),
                    movieObj.getString("id"),
                    movieObj.getString("original_title"),
                    movieObj.getString("title"),
                    movieObj.getString("backdrop_path"),
                    movieObj.getDouble("popularity"),
                    movieObj.getInt("vote_count"),
                    movieObj.getDouble("vote_average"));
            moviesList.add(movie);
        }

        return moviesList;
    }


    protected void onProgressUpdate(Integer... progress) {
        // setProgressPercent(progress[0]);
    }


}

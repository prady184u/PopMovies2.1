package com.udacity.pradeepkumarr.popmovies.asynctask;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.udacity.pradeepkumarr.popmovies.BuildConfig;
import com.udacity.pradeepkumarr.popmovies.domain.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by pradeepkumarr on 15/04/16.
 */
public class FetchMovieTrailerTask extends AsyncTask<String, Void, ArrayList<Trailer>> {

    private final String LOG_TAG = FetchMovieTrailerTask.class.getSimpleName();

    @Override
    protected ArrayList<Trailer> doInBackground(String... params) {

        if (params == null) {
            return null;
        }
        //http://api.themoviedb.org/3/movie/209112/videos?api_key=API_KEY

        //my TMDB api key
        String appid = BuildConfig.TMDBAPIKEY;

        final String QUERY_APP_ID = "api_key";


        final String MOVIES_BASE_URL =
                "http://api.themoviedb.org/3/movie/";
        final String reviews = "videos";
        String movieid = params[0];
        if (movieid != null && !movieid.equals("")) {
            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    //movie Id
                    .appendPath(params[0])
                    .appendPath(reviews)
                    .appendQueryParameter(QUERY_APP_ID, appid)
                    .build();


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
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Movie result result JSON :" + movieJsonStr);

                return getReviewsFromJSON(movieJsonStr);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } catch (JSONException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
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
        return null;
    }

    private ArrayList<Trailer> getReviewsFromJSON(String movieJsonStr) throws JSONException {
        ArrayList<Trailer> reviewsList = new ArrayList<Trailer>();


        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray moviesArray = movieJson.getJSONArray("results");
        final String youtubeTrailerUrl = "https://www.youtube.com/watch?v=";


        for(int i = 0; i < moviesArray.length(); i++) {

            JSONObject movieObj = moviesArray.getJSONObject(i);
            String trailerKey = movieObj.getString("key");
            String trailerLink = youtubeTrailerUrl + trailerKey;
            String thumbnail = "http://img.youtube.com/vi/" + trailerKey + "/0.jpg";

            Trailer Trailer = new Trailer(trailerLink,
                    thumbnail,movieObj.getString("name"));
            reviewsList.add(Trailer);
        }

        return reviewsList;
    }

    protected void onProgressUpdate(Integer... progress) {
        // setProgressPercent(progress[0]);
    }


}

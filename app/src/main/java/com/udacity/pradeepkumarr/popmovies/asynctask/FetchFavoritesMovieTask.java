package com.udacity.pradeepkumarr.popmovies.asynctask;

import android.database.Cursor;
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
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by pradeepkumarr on 18/04/16.
 */
public class FetchFavoritesMovieTask extends AsyncTask<Cursor, Void, ArrayList<Movie>> {

    private final String LOG_TAG = FetchFavoritesMovieTask.class.getSimpleName();


    @Override
    protected ArrayList<Movie> doInBackground(Cursor... params) {

        if (params == null) {
            return null;
        }

        Cursor cursor = params[0];
        if(cursor.moveToFirst()) {
            cursor.moveToLast();
        }
        ArrayList<Movie> moviesList = new ArrayList<>(cursor.getCount());
        if(cursor.moveToFirst()) {
            do {
                moviesList.add(new Movie(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return moviesList;
    }


    protected void onProgressUpdate(Integer... progress) {
        // setProgressPercent(progress[0]);
    }


}

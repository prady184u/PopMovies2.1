package com.udacity.pradeepkumarr.popmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.udacity.pradeepkumarr.popmovies.db.MovieContract.MovieEntry;

/**
 * Created by pradeepkumarr on 18/04/16.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "popularMovies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    private static final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
            MovieEntry._ID + " INTEGER PRIMARY KEY, " +
            MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
            MovieEntry.COLUMN_POSTER_URL + " TEXT, " +
            MovieEntry.COLUMN_BACKDROP_URL + " TEXT, " +
            MovieEntry.COLUMN_RATING + " REAL, " +
            MovieEntry.COLUMN_VOTE_COUNT + " INTEGER, " +
            MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
            MovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL " +
            ")";



    private static final String SQL_DELETE_MOVIE_TABLE =
            "DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME;



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    //Deletion
    public void delete(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_MOVIE_TABLE);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        delete(db);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

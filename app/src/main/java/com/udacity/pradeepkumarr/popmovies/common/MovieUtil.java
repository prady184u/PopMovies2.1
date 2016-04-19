package com.udacity.pradeepkumarr.popmovies.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.udacity.pradeepkumarr.popmovies.R;
import com.udacity.pradeepkumarr.popmovies.asynctask.FetchMovieDetailsTask;

/**
 * Created by pradeepkumarr on 16/04/16.
 */
public class MovieUtil {


    public static boolean isOnline(Activity activity) {
        ConnectivityManager connMgr = (ConnectivityManager)
                activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void showDialogWhenOffline( Activity activity, final String sort_by){
        new AlertDialog.Builder(activity).setIcon(android.R.drawable.ic_dialog_alert).setTitle("No Internet Connection")
                .setMessage("Oops, It seems there is no Internet Connection. Please retry when you are connected.")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!sort_by.equals("fav"))
                            new FetchMovieDetailsTask().execute(sort_by);
                    }
                }).setNegativeButton("OK", null).show();
    }

}

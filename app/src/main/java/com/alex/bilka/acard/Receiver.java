package com.alex.bilka.acard;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by bilka.a on 25/06/2015.
 */
public class Receiver extends ParsePushBroadcastReceiver {
    private static final String tag = "myLogs";

    @Override
    public void onPushOpen(Context context, Intent intent) {

        //To track "App Opens"
        ParseAnalytics.trackAppOpenedInBackground(intent);

        //Here is data you sent
        Log.i(tag, intent.getExtras().getString("com.parse.Data"));

        Intent i = new Intent(context, MainActivity.class);
        i.putExtras(intent.getExtras());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}

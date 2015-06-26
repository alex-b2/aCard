package com.alex.bilka.acard;
import com.parse.Parse;
import com.parse.ParseACL;

import com.parse.ParseUser;

import android.app.Application;
/**
 * Created by bilka.a on 25/06/2015.
 */
public class ParseApplication extends Application  {
    @Override
    public void onCreate() {
        super.onCreate();

        // Add your initialization code here
        Parse.initialize(this, "2q1tgAZsHizYubxrBi1WNGNNVm7dIVk4YOiwFWrj", "3m2gdJEMXTZoTKfWg6nGbOBcbTe9aZvcoq4y4d5L");


        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }

}

package com.plutos_seup.tweetags;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by androidworkspace on 5/15/2017 AD.
 */

public class MyApp extends android.app.Application{

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

package com.austinjones.whenitswrite;

import android.app.Application;
import com.firebase.client.Firebase;

/**
 * Created by austinjones on 3/31/16.
 */
public class WhenItsWriteApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }

}
package edu.uoc.android.contacts;

import android.app.Application;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class ContactListApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        // The Firebase library must be initialized once with an Android context
        FirebaseApp.initializeApp(this);
        // Enabling disk persistence allows our app to also keep all of its state even after an app restart
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

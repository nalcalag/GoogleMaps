package edu.uoc.android.contacts.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import edu.uoc.android.contacts.R;
import edu.uoc.android.contacts.manager.FirebaseContactManager;
import edu.uoc.android.contacts.model.Contact;

public class SplashActivity extends AppCompatActivity implements ValueEventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseContactManager.getInstance().getContactFromServer(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot contact : dataSnapshot.getChildren()) {
            FirebaseContactManager.getInstance().addContactHashMap(contact.getValue(Contact.class));
        }
        startMainActivity();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        startMainActivity();
    }

    private void startMainActivity() {
        // TODO: make an Intent to go to contact list activity
    }
}

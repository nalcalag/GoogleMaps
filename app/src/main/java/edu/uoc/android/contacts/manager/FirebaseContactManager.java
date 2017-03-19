package edu.uoc.android.contacts.manager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import edu.uoc.android.contacts.model.Contact;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirebaseContactManager {

    private static final String CONTACTS_URL = "https://burning-fire-1164.firebaseio.com/contacts";

    // Class instance
    private static FirebaseContactManager instance = null;
    // Reference of Firebase object
    private DatabaseReference firebaseContactRef;
    // List of edu.uoc.android.contacts
    private HashMap<String, Contact> contactHashMap = new HashMap<>();

    /**
     * Classic Singleton design pattern implementation
     */
    public static FirebaseContactManager getInstance() {
        if (instance == null) {
            instance = new FirebaseContactManager();
        }
        return instance;
    }

    private FirebaseContactManager() {
        // Initialize Firebase reference
        firebaseContactRef = FirebaseDatabase.getInstance().getReferenceFromUrl(CONTACTS_URL);
    }

    /**
     * Retrieving the list of edu.uoc.android.contacts from Firebase server
     */
    public void getContactFromServer(ValueEventListener listener) {
        firebaseContactRef.keepSynced(true);
        Query queryRef = firebaseContactRef.orderByKey();
        queryRef.addListenerForSingleValueEvent(listener);
    }

    /**
     * Retrieving the list of edu.uoc.android.contacts
     */
    public List<Contact> getAllContacts() {
        return new ArrayList<>(contactHashMap.values());
    }

    /**
     * Retrieving a contact from list
     */
    public Contact getContactByObjectId(String objectId) {
        return contactHashMap.get(objectId);
    }

    /**
     * Adding a contact to HashMap
     */
    public void addContactHashMap(Contact contact) {
        this.contactHashMap.put(contact.getObjectId(), contact);
    }
}

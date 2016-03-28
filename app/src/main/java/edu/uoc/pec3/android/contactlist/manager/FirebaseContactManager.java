package edu.uoc.pec3.android.contactlist.manager;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.uoc.pec3.android.contactlist.model.Contact;

/**
 * Created by mgarcia on 23/03/2016.
 */
public class FirebaseContactManager {

    // Class instance
    private static FirebaseContactManager instance = null;
    // Reference of Firebase object
    private Firebase firebaseContactRef;
    // List of contacts
    private HashMap<String, Contact> contactHashMap = new HashMap<>();

    /**
     * Classic Singleton design pattern implementation
     */
    public static FirebaseContactManager getInstance() {
        if(instance == null) {
            instance = new FirebaseContactManager();
        }
        return instance;
    }

    /**
     * Constructor
     */
    private FirebaseContactManager() {
        // init Firebase reference
        firebaseContactRef = new Firebase("https://burning-fire-1164.firebaseio.com/contacts");
    }

    /**
     * Retrieving the list of contacts from Firebase server
     * @param listener
     */
    public void getContactFromServer(ValueEventListener listener) {
        firebaseContactRef.keepSynced(true);
        Query queryRef = firebaseContactRef.orderByKey();
        queryRef.addListenerForSingleValueEvent(listener);
    }

    /**
     * Retrieving the list of contacts
     */
    public List<Contact> getAllContacts() {
        return new ArrayList<>(contactHashMap.values());
    }

    /**
     * Retrieving a contact from list
     * @param objectId
     */
    public Contact getContactByObjectId(String objectId) {
        return contactHashMap.get(objectId);
    }

    /**
     * Adding a contact to HashMap
     * @param contact
     */
    public void addContactHashMap(Contact contact) {
        this.contactHashMap.put(contact.getObjectId(), contact);
    }
}

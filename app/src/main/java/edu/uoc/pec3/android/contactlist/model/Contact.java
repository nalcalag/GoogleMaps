package edu.uoc.pec3.android.contactlist.model;

/**
 * Created by mgarcia on 23/03/2016.
 */
public class Contact {

    private String updatedAt;
    private String birthday;
    private String phone;
    private GeoLocation location;
    private String imageUrl;
    private String email;
    private String objectId;
    private String description;
    private String createdAt;
    private String name;
    private String gender;
    private String country;
    private String city;

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPhone() {
        return phone;
    }

    public GeoLocation getLocation() {
        return location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }
}

package com.apps.brayan.surveyapp.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Survey {

    public String name;
    public String body;
    public String id;

    public Survey() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Survey(String name, String body, String id) {
        this.name = name;
        this.body = body;
        this.id = id;
    }

}
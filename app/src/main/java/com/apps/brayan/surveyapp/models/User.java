package com.apps.brayan.surveyapp.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public class User {

    public String nombre;
    public String id;
    public HashMap<String,String> organizaciones;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String nombre, String id,HashMap<String,String> organizaciones) {
        this.nombre = nombre;
        this.id = id;
        this.organizaciones = organizaciones;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, String> getOrgaizaciones() {
        return organizaciones;
    }

    public void setOrgaizaciones(HashMap<String, String> orgaizaciones) {
        this.organizaciones = orgaizaciones;
    }
}

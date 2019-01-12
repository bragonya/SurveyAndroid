package com.apps.brayan.surveyapp.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public class User {

    public String nombre;
    public String password;
    public String id;
    public HashMap<String,String> orgaizaciones;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String nombre, String password, String id,HashMap<String,String> orgaizaciones) {
        this.nombre = nombre;
        this.password = password;
        this.id = id;
        this.orgaizaciones = orgaizaciones;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, String> getOrgaizaciones() {
        return orgaizaciones;
    }

    public void setOrgaizaciones(HashMap<String, String> orgaizaciones) {
        this.orgaizaciones = orgaizaciones;
    }
}

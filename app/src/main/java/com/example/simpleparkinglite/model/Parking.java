package com.example.simpleparkinglite.model;

import com.example.simpleparkinglite.config.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;

public class Parking {
    private String id;
    private String nome;
    private String latitude;
    private String longitude;
    private String vagas;

    public Parking() {

    }

    public void save() {
        String id;
        DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
        id = firebaseRef.push().getKey();
        setId(id);
        DatabaseReference parkings = firebaseRef.child("parkings").child(getId());
        parkings.setValue(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getVagas() {
        return vagas;
    }

    public void setVagas(String vagas) {
        this.vagas = vagas;
    }
}

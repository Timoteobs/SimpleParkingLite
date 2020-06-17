package com.example.simpleparkinglite.model;

import com.example.simpleparkinglite.config.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;

public class Destiny {
    private String id;
    private String street;
    private String number;
    private String city;
    private String neighborhood;
    private String zipCode;
    private String idRequest;

    private String latitude;
    private String longitude;
    private String idusuario;

    public Destiny() {
    }

    public void saveRequest() {
        idusuario = ConfigFirebase.getIdUser();
        DatabaseReference databaseReference = ConfigFirebase.getFirebaseDatabase();
        DatabaseReference request = databaseReference.child("requests");
        idRequest = request.push().getKey();
        setId(idRequest);
        setIdusuario(idusuario);
        request.child(getIdusuario()).child(getId()).setValue(this);
    }

    public void remover(){
        DatabaseReference databaseReference = ConfigFirebase.getFirebaseDatabase();
        DatabaseReference request = databaseReference.child("requests").child(getIdusuario()).child(getId());
        request.removeValue();
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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
}

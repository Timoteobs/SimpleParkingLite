package com.example.simpleparkinglite.config;

import com.example.simpleparkinglite.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigFirebase {

    private static DatabaseReference database;
    private static FirebaseAuth auth;

    public static String getIdUser(){
        FirebaseAuth auth = getFirebaseAuth();
        return auth.getCurrentUser().getUid();
    }

    public static DatabaseReference getFirebaseDatabase(){

        if ( database == null ){
            database = FirebaseDatabase.getInstance().getReference();
        }
        return database;

    }

    public static FirebaseAuth getFirebaseAuth() {

        if ( auth == null ){
            auth = FirebaseAuth.getInstance();
        }

        return auth;
    }

    public static FirebaseUser getUserCurrent() {
        FirebaseAuth user = getFirebaseAuth();
        return user.getCurrentUser();
    }

    public static User getUser() {
        FirebaseUser firebaseUser = getUserCurrent();
        User user = new User();
        user.setId(firebaseUser.getUid());
        user.setEmail(firebaseUser.getEmail());
        user.setName(firebaseUser.getDisplayName());

        return user;
    }

}

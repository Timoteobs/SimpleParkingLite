package com.example.simpleparkinglite.model;

import com.example.simpleparkinglite.config.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;

public class Request {
    private String id;
    private Destiny destiny;
    private String idusuario;

    public Request() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Destiny getDestiny() {
        return destiny;
    }

    public void setDestiny(Destiny destiny) {
        this.destiny = destiny;
    }
}

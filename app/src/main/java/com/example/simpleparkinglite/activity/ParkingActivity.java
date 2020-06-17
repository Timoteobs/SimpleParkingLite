package com.example.simpleparkinglite.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.simpleparkinglite.R;
import com.example.simpleparkinglite.model.Parking;
import com.google.android.material.textfield.TextInputEditText;

public class ParkingActivity extends AppCompatActivity {

    private TextInputEditText nome;
    private TextInputEditText vagas;
    private TextInputEditText latitude;
    private TextInputEditText logitude;
    private Button salvar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);

        nome = findViewById(R.id.editNomeP);
        vagas = findViewById(R.id.editVagas);
        latitude = findViewById(R.id.editLatitude);
        logitude = findViewById(R.id.editLongitude);
        salvar = findViewById(R.id.buttonSalvarP);

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveParking();
            }
        });



    }

    public void saveParking() {
        Parking parking = new Parking();
        parking.setNome(nome.getText().toString());
        parking.setVagas(vagas.getText().toString());
        parking.setLatitude(latitude.getText().toString());
        parking.setLongitude(logitude.getText().toString());

        parking.save();
    }

}

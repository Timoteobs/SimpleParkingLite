package com.example.simpleparkinglite.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.simpleparkinglite.R;
import com.example.simpleparkinglite.config.ConfigFirebase;
import com.example.simpleparkinglite.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ConfigUserActivity extends AppCompatActivity {

    private TextInputEditText nome;
    private TextInputEditText email;
    private DatabaseReference databaseReference;
    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_user);

        idUsuario = ConfigFirebase.getIdUser();
        databaseReference = ConfigFirebase.getFirebaseDatabase();

        inicializar();
        recuperarDados();
    }

    private void recuperarDados(){
        DatabaseReference usuario = databaseReference.child("users").child(idUsuario);
        usuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    User user = dataSnapshot.getValue(User.class);
                    nome.setText(user.getName());
                    email.setText(user.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void validarDadosUsusario(View view){
        String name = nome.getText().toString();
        String mail = email.getText().toString();
        if (!name.isEmpty()){
            User user = new User();
            user.setId(idUsuario);
            user.setName(name);
            user.setEmail(mail);
            user.save();
            Toast.makeText(this, "Alteração realizada!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void inicializar(){
        nome = findViewById(R.id.editName);
        email = findViewById(R.id.editNomeP);
    }
}

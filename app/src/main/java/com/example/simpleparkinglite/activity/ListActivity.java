package com.example.simpleparkinglite.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;

import com.example.simpleparkinglite.R;
import com.example.simpleparkinglite.adapter.AdapterRquest;
import com.example.simpleparkinglite.config.ConfigFirebase;
import com.example.simpleparkinglite.listiner.RecyclerItemClickListener;
import com.example.simpleparkinglite.model.Destiny;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private AdapterRquest adapter;
    private List<Destiny> destinyList = new ArrayList<>();
    private DatabaseReference reference;
    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getSupportActionBar().show();

        iniciar();
        auth = ConfigFirebase.getFirebaseAuth();
        reference = ConfigFirebase.getFirebaseDatabase();
        idUsuario = ConfigFirebase.getIdUser();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new AdapterRquest(destinyList, this);
        recyclerView.setAdapter(adapter);
        recuperarDestinos();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                            }

                            @Override
                            public void onLongItemClick(View view, final int position) {
                                confirm(position);
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
    }

    private void confirm(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Destino");
        builder.setMessage("Deseja excluir do historico?");
        builder.setCancelable(false);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Destiny destinySelecionado = destinyList.get(position);
                destinySelecionado.remover();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void recuperarDestinos(){

        DatabaseReference destinosRef = reference.child("requests").child(idUsuario);
        destinosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                destinyList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    destinyList.add(ds.getValue(Destiny.class));
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getOut(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Menu");
        builder.setMessage("Deseja sair da aplicação?");
        builder.setCancelable(false);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                auth.signOut();
                finishAffinity();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void abrirPerfil(View view){
        startActivity(new Intent(getApplicationContext(), ConfigUserActivity.class));
    }

    public void abrirParkings(View view){
        startActivity(new Intent(getApplicationContext(), ParkingActivity.class));
    }
    private void iniciar(){
        recyclerView = findViewById(R.id.recyclerDados);
    }

}

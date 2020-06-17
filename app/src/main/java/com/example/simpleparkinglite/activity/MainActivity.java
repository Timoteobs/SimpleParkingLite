package com.example.simpleparkinglite.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.simpleparkinglite.R;
import com.example.simpleparkinglite.config.ConfigFirebase;
import com.example.simpleparkinglite.helper.Permissoes;
import com.example.simpleparkinglite.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText  fieldEmail, fieldPassword;
    private FirebaseAuth auth;
    private String[] permits = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fieldEmail = findViewById(R.id.editEmailLogin);
        fieldPassword = findViewById(R.id.editPasswordLogin);
        getSupportActionBar().hide();

        Permissoes.validarPermissoes(permits, this, 1);

        auth = ConfigFirebase.getFirebaseAuth();
        //auth.signOut();
    }

    public void validateLogin(View view) {
        String email = fieldEmail.getText().toString();
        String password = fieldPassword.getText().toString();

        if (!email.isEmpty()){
            if (!password.isEmpty()){
                User user = new User();
                user.setEmail(email);
                user.setPassword(password);

                loginUser(user);
            } else {
                Toast.makeText(MainActivity.this,
                        "Informe sua Senha", Toast.LENGTH_SHORT).show();
                fieldPassword.requestFocus();
            }
        } else {
            Toast.makeText(MainActivity.this,
                    "Informe o seu Email", Toast.LENGTH_SHORT).show();
            fieldEmail.requestFocus();
        }
    }

    public void loginUser(final User user) {
        auth = ConfigFirebase.getFirebaseAuth();
        auth.signInWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    openMap();
                } else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch ( FirebaseAuthInvalidUserException e){
                        excecao = "Usuario não está cadastrado";
                    }catch ( FirebaseAuthInvalidCredentialsException e){
                        excecao= "Email e Senha, não correspondem";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: "  + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(MainActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openRegister(View view) {
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
    }

    public void openMap(){
        startActivity(new Intent(MainActivity.this, DriverActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null){
            openMap();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int permitsResults : grantResults){
            if (permitsResults == PackageManager.PERMISSION_DENIED){
                alertpermits();
            }
        }
    }

    public void alertpermits(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

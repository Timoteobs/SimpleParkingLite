package com.example.simpleparkinglite.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.simpleparkinglite.R;
import com.example.simpleparkinglite.config.ConfigFirebase;
import com.example.simpleparkinglite.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText fieldName, fieldEmail, fieldPassword, fieldConfirmPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fieldName               = findViewById(R.id.editName);
        fieldEmail              = findViewById(R.id.editRegisterEmail);
        fieldPassword           = findViewById(R.id.editRegisterPassword);
        fieldConfirmPassword    = findViewById(R.id.editConfirmPassword);

    }

    public void validRegisterUser(View view) {
        String textName = fieldName.getText().toString();
        String textEmail = fieldEmail.getText().toString();
        String textPassword = fieldPassword.getText().toString();
        String textConfirmPassword = fieldConfirmPassword.getText().toString();

        if (!textName.isEmpty()){
            if (!textEmail.isEmpty()){
                if (!textPassword.isEmpty()){
                    if (!textConfirmPassword.isEmpty()){

                        User user = new User();
                        user.setName(textName);
                        user.setEmail(textEmail);
                        user.setPassword(textPassword);

                        registerUser(user);

                    }else{
                        Toast.makeText(RegisterActivity.this,
                                "Confirme sua Senha", Toast.LENGTH_SHORT).show();
                        fieldPassword.requestFocus();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this,
                            "Informe sua Senha", Toast.LENGTH_SHORT).show();
                    fieldPassword.requestFocus();
                }
            }else{
                Toast.makeText(RegisterActivity.this,
                        "Informe o seu Email", Toast.LENGTH_SHORT).show();
                fieldEmail.requestFocus();
            }
        }else{
            Toast.makeText(RegisterActivity.this,
                    "Informe o seu nome", Toast.LENGTH_SHORT).show();
            fieldName.requestFocus();
        }
    }

    public void registerUser(final User userRegister){
        auth = ConfigFirebase.getFirebaseAuth();
        auth.createUserWithEmailAndPassword(
                userRegister.getEmail(),
                userRegister.getPassword()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    try{
                        String idUser = task.getResult().getUser().getUid();
                        userRegister.setId(idUser);
                        userRegister.save();
                        startActivity(new Intent(RegisterActivity.this, DriverActivity.class));
                        finish();
                        Toast.makeText(RegisterActivity.this,
                                "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch ( FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    }catch ( FirebaseAuthInvalidCredentialsException e){
                        excecao= "Por favor, digite um e-mail válido";
                    }catch ( FirebaseAuthUserCollisionException e){
                        excecao = "Este conta já foi cadastrada";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: "  + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(RegisterActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

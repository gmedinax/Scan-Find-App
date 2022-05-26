package com.example.scanfindapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity implements View.OnClickListener{
    Button btnSignUp;
    FloatingActionButton btnHome;
    ImageButton btnScan, btnArt, btnBack;
    ActionMenuItemView btnLogin;
    private FirebaseAuth mAuth;
    private EditText txtEditorNombre, txtEditorApellido, txtEditorEmail, txtEditorPassword;
    private ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        addListenerOnButton();
        mAuth = FirebaseAuth.getInstance();
        btnSignUp = (Button) findViewById(R.id.btnRegisterUser);
        btnSignUp.setOnClickListener(this);
        txtEditorNombre = (EditText) findViewById(R.id.txtname);
        txtEditorApellido = (EditText) findViewById(R.id.txtLastName);
        txtEditorEmail = (EditText) findViewById(R.id.txtemailaddress);
        txtEditorPassword = (EditText) findViewById(R.id.txtPass);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
    }

    public void addListenerOnButton() {
        btnHome = (FloatingActionButton) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup.this, Inicio.class);
                startActivity(intent);
            }
        });
        btnScan = (ImageButton) findViewById(R.id.first_menu_item);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup.this, scan.class);
                startActivity(intent);
            }
        });
        btnArt = (ImageButton) findViewById(R.id.fourth_menu_item);
        btnArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup.this, articulos.class);
                startActivity(intent);
            }
        });

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup.this, login.class);
                startActivity(intent);
            }
        });
        btnBack = (ImageButton) findViewById(R.id.btnRegresar);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup.this, login.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRegisterUser:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String name = txtEditorNombre.getText().toString().trim();
        String lastname = txtEditorApellido.getText().toString().trim();
        String email = txtEditorEmail.getText().toString().trim();
        String password = txtEditorPassword.getText().toString().trim();
        if(name.isEmpty()){
            txtEditorNombre.setError("Ingrese su nombre");
            txtEditorNombre.requestFocus();
            return;
        }
        if(lastname.isEmpty()){
            txtEditorApellido.setError("Ingrese su apellido");
            txtEditorApellido.requestFocus();
            return;
        }
        if(email.isEmpty()){
            txtEditorEmail.setError("Ingrese su correo");
            txtEditorEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtEditorEmail.setError("Correo no válido");
            txtEditorEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            txtEditorPassword.setError("Ingrese su contraseña");
            txtEditorPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            txtEditorPassword.setError("Mínimo 6 dígitos");
            txtEditorPassword.requestFocus();
            return;
        }
        progressBar2.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            user usuario = new user(name, lastname, email);
                            FirebaseDatabase.getInstance().getReference("usuario")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(signup.this, "Se ha registrado exitosamente",Toast.LENGTH_LONG).show();
                                        progressBar2.setVisibility(View.GONE);
                                        Intent intent = new Intent(signup.this, login.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(signup.this, "No se ha podido registrar",Toast.LENGTH_LONG).show();
                                        progressBar2.setVisibility(View.GONE);
                                    }
                                }
                            });
                        } else{
                            Toast.makeText(signup.this, "Se ha registrado exitosamente",Toast.LENGTH_LONG).show();
                            progressBar2.setVisibility(View.GONE);
                        }
                    }
                });

    }
}
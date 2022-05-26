package com.example.scanfindapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity implements  View.OnClickListener{
    FloatingActionButton btnHome;
    ImageButton btnScan, btnArt, btnBack;
    ActionMenuItemView btnLogin;
    private TextView Register, forgotPass;
    private FirebaseAuth mAuth;
    private Button btnInicioSesion;
    private EditText txtEditorEmail, txtEditorPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addListenerOnButton();
        Register = (TextView) findViewById((R.id.register));
        Register.setOnClickListener(this);
        forgotPass = (TextView) findViewById(R.id.forgotPassword);
        forgotPass.setOnClickListener(this);
        btnInicioSesion = (Button) findViewById(R.id.btnSignIn);
        btnInicioSesion.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBarLog);
        txtEditorEmail = (EditText) findViewById(R.id.eTEmail);
        txtEditorPassword = (EditText) findViewById(R.id.eTPassword);
        mAuth=FirebaseAuth.getInstance();

    }

    public void addListenerOnButton() {
        btnHome = (FloatingActionButton) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, Inicio.class);
                startActivity(intent);
            }
        });
        btnScan = (ImageButton) findViewById(R.id.first_menu_item);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, scan.class);
                startActivity(intent);
            }
        });
        btnArt = (ImageButton) findViewById(R.id.fourth_menu_item);
        btnArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, articulos.class);
                startActivity(intent);
            }
        });

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, login.class);
                startActivity(intent);
            }
        });
        btnBack = (ImageButton) findViewById(R.id.btnRegresar);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, Inicio.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                startActivity(new Intent(this, signup.class));
                break;
            case R.id.btnSignIn:
                userLogIn();
                break;
        }

    }

    private void userLogIn() {
        String email = txtEditorEmail.getText().toString().trim();
        String password= txtEditorPassword.getText().toString().trim();

        if (email.isEmpty()){
            txtEditorEmail.setError("Ingrese correo");
            txtEditorEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtEditorEmail.setError("Correo inválido");
            txtEditorEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            txtEditorPassword.setError("Ingrese contraseña");
            txtEditorPassword.requestFocus();
            return;
        }

        if(password.length()<6){
            txtEditorPassword.setError("Contraseña inválida");
            txtEditorPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(login.this, Inicio.class);
                    startActivity(intent);
                    btnLogin.setVisibility(View.GONE);
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(login.this, "No se ha podido iniciar sesión",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
package com.example.scanfindapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Inicio extends AppCompatActivity{
    private static final String TAG = "MainActivity";
    CircularProgressIndicator progress_circular;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    private Context mContext;
    private Activity mActivity;
    private ArrayList<producto> imagesList;
    private productoAdapter imageAdapter = null;
    FloatingActionButton btnHome;
    ImageButton btnScan, btnArt, btnMenu;
    ActionMenuItemView btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        mActivity = Inicio.this;
        mContext = getApplicationContext();
        FirebaseApp.initializeApp(this);
        recyclerView = findViewById(R.id.myrecyclerview);
        progress_circular = findViewById(R.id.progress_circular);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 1, GridLayoutManager.VERTICAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        imagesList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("producto");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                imagesList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    producto imagemodel = dataSnapshot.getValue(producto.class);
                    imagesList.add(imagemodel);
                }
                imageAdapter = new productoAdapter(mContext,mActivity, (ArrayList<producto>) imagesList);
                recyclerView.setAdapter(imageAdapter);
                imageAdapter.notifyDataSetChanged();
                progress_circular.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Inicio.this,"Error:" + error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        btnHome = (FloatingActionButton) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Inicio.this, Inicio.class);
                startActivity(intent);
            }
        });
        btnScan = (ImageButton) findViewById(R.id.first_menu_item);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Inicio.this, scan.class);
                startActivity(intent);
            }
        });
        btnArt = (ImageButton) findViewById(R.id.fourth_menu_item);
        btnArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Inicio.this, articulos.class);
                startActivity(intent);
            }
        });

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Inicio.this, login.class);
                startActivity(intent);
            }
        });
    }
}
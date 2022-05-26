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

public class articulos extends AppCompatActivity {
    private static final String TAG = "Artículos Activity";
    CircularProgressIndicator progress_circular_art;
    RecyclerView recyclerView_art;
    DatabaseReference databaseReference_art;
    private Context mContext;
    private Activity mActivity;
    private ArrayList<articulo> listArticulos;
    private articuloAdapter imageArticuloAdapter = null;
    FloatingActionButton btnHome;
    ImageButton btnScan, btnArt, btnBack;
    ActionMenuItemView btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulos);
        mActivity = articulos.this;
        mContext = getApplicationContext();
        FirebaseApp.initializeApp(this);
        recyclerView_art = findViewById(R.id.recyclerviewArticulos);
        progress_circular_art = findViewById(R.id.progress_circular_articulos);
        recyclerView_art.setHasFixedSize(true);
        recyclerView_art.setLayoutManager(new GridLayoutManager(mActivity, 2, GridLayoutManager.VERTICAL, false));
        recyclerView_art.setNestedScrollingEnabled(false);
        listArticulos = new ArrayList<>();

        databaseReference_art = FirebaseDatabase.getInstance().getReference().child("articulo");
        databaseReference_art.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                listArticulos.clear();
                for (DataSnapshot dataSnapshot : datasnapshot.getChildren()) {
                    articulo modelarticulo = dataSnapshot.getValue(articulo.class);
                    listArticulos.add(modelarticulo);
                }
                imageArticuloAdapter = new articuloAdapter(mContext, mActivity, (ArrayList<articulo>) listArticulos);
                recyclerView_art.setAdapter(imageArticuloAdapter);
                imageArticuloAdapter.notifyDataSetChanged();
                progress_circular_art.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(articulos.this, "Error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        btnHome = (FloatingActionButton) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(articulos.this, Inicio.class);
                startActivity(intent);
            }
        });
        btnScan = (ImageButton) findViewById(R.id.first_menu_item);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(articulos.this, scan.class);
                startActivity(intent);
            }
        });
        btnArt = (ImageButton) findViewById(R.id.fourth_menu_item);
        btnArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(articulos.this, articulos.class);
                startActivity(intent);
            }
        });

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(articulos.this, login.class);
                startActivity(intent);
            }
        });
        btnBack = (ImageButton) findViewById(R.id.btnRegresar);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(articulos.this, Inicio.class);
                startActivity(intent);
            }
        });
    }
}
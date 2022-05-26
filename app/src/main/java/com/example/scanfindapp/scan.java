package com.example.scanfindapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.util.List;

public class scan extends AppCompatActivity {
    private  static final String TAG= "MyTag";
    private static final int CAMERA_PERMISSION_CODE=223;
    private static final int READ_STORAGE_PERMISSION_CODE=144;
    private static final int WRITE_STORAGE_PERMISSION_CODE=144;
    ActivityResultLauncher<Intent> cameraLauncher;
    ActivityResultLauncher<Intent> galleryLauncher;
    InputImage inputImage;
    ImageLabeler labeler;
    ImageView ivPicture;
    TextView tvResult;
    Button btnImagen, btnInicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        addListenerOnButton();

        ivPicture = (ImageView) findViewById(R.id.imageViewPicture);
        tvResult = (TextView) findViewById(R.id.textViewResult);

        labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);

        cameraLauncher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent data = result.getData();
                        try {
                            Bitmap photo = (Bitmap) data.getExtras().get("data");
                            ivPicture.setImageBitmap(photo);
                            inputImage = InputImage.fromBitmap(photo, 0);

                            processImage();
                        }catch (Exception e){
                            Log.d(TAG,"onActivityResult"+e.getMessage());
                        }
                    }
                }
        );
        galleryLauncher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent data = result.getData();
                        try {
                            inputImage = InputImage.fromFilePath(scan.this,data.getData());
                            ivPicture.setImageURI(data.getData());

                            processImage();
                        }catch (Exception e){
                            Log.d(TAG,"onActivityResult"+e.getMessage());
                        }
                    }
                }
        );

    }
    private void processImage() {
        labeler.process(inputImage)
                .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                    @Override
                    public void onSuccess(List<ImageLabel> imageLabels) {
                        String result ="";
                        for(ImageLabel label : imageLabels){
                            result=result+"\n"+label.getText();
                        }
                        tvResult.setText(result);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+e.getMessage());
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermission(Manifest.permission.CAMERA,CAMERA_PERMISSION_CODE);
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(scan.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(scan.this, new String[]{permission}, requestCode);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==CAMERA_PERMISSION_CODE){
            if(!(grantResults.length>0 &&grantResults[0]==PackageManager.PERMISSION_GRANTED)){
                Toast.makeText(scan.this, "Acceso a cámara denegado",Toast.LENGTH_SHORT).show();
            }else{
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_STORAGE_PERMISSION_CODE);
            }
        }else if(requestCode==READ_STORAGE_PERMISSION_CODE){
            if (!(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(scan.this, "Acceso a archivos denegado",Toast.LENGTH_SHORT).show();
            }else{
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_STORAGE_PERMISSION_CODE);
            }
        }else if (requestCode==WRITE_STORAGE_PERMISSION_CODE){
            if(!(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)){
                Toast.makeText(scan.this,"Acceso a archivos denegado",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void addListenerOnButton() {
        btnInicio = (Button) findViewById(R.id.btn_home);
        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(scan.this, Inicio.class);
                startActivity(intent);
            }
        });
        btnImagen = (Button) findViewById(R.id.btnChoosePicture);
        btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String [] options={"Cámara","Galería"};
                AlertDialog.Builder builder = new AlertDialog.Builder(scan.this);
                builder.setTitle("Selecciona una opción:")
                        .setItems(options, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    cameraLauncher.launch(cameraIntent);
                                }else{
                                    Intent storageIntent =new Intent();
                                    storageIntent.setType("image/*");
                                    storageIntent.setAction(Intent.ACTION_GET_CONTENT);
                                    galleryLauncher.launch(storageIntent);
                                }
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

}
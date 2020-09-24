package com.example.sketchapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.Stack;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    MyCanvas myCanvas;
    Button clear,undo,red,sizeM,sizeL,redo,green,save,eraser,pen;
    String Storage_Path = "Images/";
    StorageReference storageReference;
    ProgressDialog progressDialog;
    byte[] byteArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myCanvas = (MyCanvas) findViewById(R.id.canvasSing);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        myCanvas.initialise(displayMetrics);

        clear = (Button) findViewById(R.id.clear);
        undo = (Button) findViewById(R.id.undo);
        red = (Button) findViewById(R.id.red);
        sizeM = (Button) findViewById(R.id.sizeM);
        sizeL = (Button) findViewById(R.id.sizeL);
        save = (Button) findViewById(R.id.save);
        green = (Button) findViewById(R.id.green);
        redo = (Button) findViewById(R.id.redo);
        pen = (Button) findViewById(R.id.pen);
        eraser = (Button) findViewById(R.id.erase);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCanvas.clear();
            }
        });

        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCanvas.undo();
            }
        });

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCanvas.setColor(Color.RED);
            }
        });

        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCanvas.setColor(Color.GREEN);
            }
        });

        sizeM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCanvas.setStrokeWidth(8);
            }
        });
        sizeL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCanvas.setStrokeWidth(12);
            }
        });

        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCanvas.redo();
            }
        });



        eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCanvas.setErase(true);
            }
        });

        pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCanvas.setErase(false);
                myCanvas.setColor(Color.BLACK);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Bitmap bitmap =  myCanvas.saveImage();
               storageReference = FirebaseStorage.getInstance().getReference();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
                bitmap.recycle();
                UploadImageFileToDb1();
            }
        });
    }
    private void UploadImageFileToDb1(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        StorageReference reference = storageReference.child("Images/"+ UUID.randomUUID().toString());
        reference.putBytes(byteArray)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,"Image Uploaded",Toast.LENGTH_SHORT).show();
                        Log.e("tag","success");
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                       Toast.makeText(MainActivity.this,"Image is Uploading",Toast.LENGTH_SHORT).show();
                        progressDialog.setMessage("Uploading...");
                        progressDialog.show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("tag","f");
                        Toast.makeText(MainActivity.this,"Failed to Upload Image",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
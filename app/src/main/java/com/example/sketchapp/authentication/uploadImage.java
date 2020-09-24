package com.example.sketchapp.authentication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sketchapp.UploadImage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

public class uploadImage extends Activity {

    String Storage_Path = "Images/";
    String Database_Path = "Images_db";

    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog;
    Uri FilePathUri;
    Bitmap bitmap;
    ByteArrayOutputStream stream;
    byte[] byteArray;


    public uploadImage(Bitmap bitmap){
        this.bitmap = bitmap;
        //FilePathUri = Uri.fromFile(image);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
        convert();
        UploadImageFileToDb1();

    }

    private void convert(){
        stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byteArray = stream.toByteArray();
        bitmap.recycle();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void UploadImageFileToDb1(){
        StorageReference reference = storageReference.child("Images/"+ UUID.randomUUID().toString());
        reference.putBytes(byteArray)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.e("tag","success");
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        Log.e("tag","progress");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("tag","faile");
                    }
                });

    }



   /* private void UploadImageFileToDb(){

        if(FilePathUri!=null){

           *//* final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading Image");
            progressDialog.show();*//*
            StorageReference reference = storageReference.child("Images/"+ UUID.randomUUID().toString());


            reference.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                         //   progressDialog.dismiss();
  //                          Toast.makeText(uploadImage.this,"Image Uploaded",Toast.LENGTH_SHORT).show();
                            Log.i("a","uploaded");
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                           // progressDialog.setMessage("Uploaded"+(int)progress+"%");
                            Log.i("a","uploadeding");

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(uploadImage.this,"Failed",Toast.LENGTH_SHORT).show();
                            Log.i("a","failed");

                        }
                    });
        }
    }*/
}

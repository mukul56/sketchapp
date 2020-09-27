package com.example.sketchapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sketchapp.authentication.Authen;
import com.example.sketchapp.util.sharePrefer;
import com.example.sketchapp.util.showImages;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Fragment fragment;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    FirebaseFirestore db;
    sharePrefer my;

    MyCanvas myCanvas;
    Button clear,undo,red,sizeM,sizeL,redo,green,save,eraser,pen;
    String Storage_Path = "Images/";
    StorageReference storageReference;
    ProgressDialog progressDialog;
    DisplayMetrics displayMetrics;
    byte[] byteArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         my = new sharePrefer(this);

        db = FirebaseFirestore.getInstance();


        final Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        nav = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.homeDrawer);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();



        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.logout:
                        my.logout();
                        Toast.makeText(MainActivity.this, "LogOut", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, Authen.class));
                        finish();
                        break;

                    case R.id.show:
                        //showImages();
                        startActivity(new Intent(MainActivity.this, showImages.class));
                        break;

                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getItemId());
                }
                drawerLayout.closeDrawer(GravityCompat.START);
              //  getSupportFragmentManager().beginTransaction().replace(R.id.frag1,fragment).commit();

                return true;
            }
        });

        myCanvas = (MyCanvas) findViewById(R.id.canvasSing);
        displayMetrics = new DisplayMetrics();
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
        final StorageReference reference = storageReference.child("Images/"+ UUID.randomUUID().toString());



        reference.putBytes(byteArray)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,"Image Uploaded",Toast.LENGTH_SHORT).show();
                     //  uploadReferenceToFs(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                        getUrl(reference);
                        Log.e("tag","success");
                        myCanvas.initialise(displayMetrics);

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



    private void getUrl(StorageReference reference){
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("taf","get url");
                uploadReferenceToFs(uri.toString());
            }
        });
    }



    private void uploadReferenceToFs(String reference){
        Map<String,String> imageFs = new HashMap<>();
        imageFs.put("image",reference);
        db.collection("users").document(my.getPhone()).collection(my.getPhone()).add(imageFs)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("tag","success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("tag","fail");
                    }
                });
    }


/*    private void showImages(){
        db.collection("users").document(my.getPhone()).collection(my.getPhone()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){
                                Log.d("asd",document.getId()+" ==="+document.getData());
                            }
                        }else{
                            Log.d("asd","error");
                        }
                    }
                });
    }*/
}





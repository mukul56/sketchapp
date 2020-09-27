package com.example.sketchapp.util;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.sketchapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class showImages extends AppCompatActivity {


    RecyclerView recyclerView;
    ImageAdapter myAdapter;
    List<ModelImage> imageList;
    ModelImage modelImage;
    LinearLayoutManager linearLayoutManager;
    FirebaseFirestore db;
    sharePrefer my;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images);


        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        imageList = new ArrayList<>();
        myAdapter = new ImageAdapter(this,imageList);
        recyclerView.setAdapter(myAdapter);

        fetchImages();

    }

    private void fetchImages(){

        my = new sharePrefer(this);
        db = FirebaseFirestore.getInstance();
        db.collection("users").document(my.getPhone()).collection(my.getPhone()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){
                                Log.d("asvhjghjd",document.getId()+" ==="+document.getData().values());
                                modelImage = new ModelImage(document.getId(),document.getData().values().toString());
                                imageList.add(modelImage);
                                myAdapter.notifyDataSetChanged();
                            }
                        }else{
                            Log.d("asd","error");
                        }
                    }
                });
    }
}
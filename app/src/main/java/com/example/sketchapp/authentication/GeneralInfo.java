package com.example.sketchapp.authentication;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sketchapp.MainActivity;
import com.example.sketchapp.R;
import com.example.sketchapp.util.sharePrefer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class GeneralInfo extends AppCompatActivity {

    Button btnCont;
    EditText name,about;
    FirebaseFirestore db;
    sharePrefer share;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        btnCont = (Button) findViewById(R.id.buttonContinue);
        name = (EditText) findViewById(R.id.name);
        about = (EditText) findViewById(R.id.about);


         share = new sharePrefer(this);




        btnCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s_name = name.getText().toString().trim();
                String s_about = about.getText().toString().trim();
                share.save(s_name,s_about);
                db = FirebaseFirestore.getInstance();
                Map<String,String> user = new HashMap<>();
                user.put("name",share.getName());
                user.put("phone",share.getPhone());
                user.put("about",share.getAbout());

/*                db.collection("users")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.v("tag","successfully added to db");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.v("tag","error");

                            }
                        });*/


                db.collection("users").document(share.getPhone()).set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.v("tag","successfully added to db");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.v("tag","error");

                            }
                        });

                Intent intent = new Intent(GeneralInfo.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }
}
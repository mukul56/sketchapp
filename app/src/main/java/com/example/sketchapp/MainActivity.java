package com.example.sketchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    MyCanvas myCanvas;
    Button clear,undo,red,sizeM,sizeL,redo,green,save,eraser,pen;
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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCanvas.saveImage();
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

    }





}
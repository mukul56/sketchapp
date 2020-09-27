package com.example.sketchapp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class sharePrefer {
    SharedPreferences sp;
    Context context;
    public sharePrefer(Context context){
         this.context = context;
    }

    public void save(String name, String about){
        sp = context.getSharedPreferences("myPrefer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name",name);
        editor.putString("about",about);
        editor.commit();
    }

    public void save(String phone){
        sp = context.getSharedPreferences("myPrefer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("phone",phone);

        editor.commit();
    }


    public String getName(){
           sp = context.getSharedPreferences("myPrefer", Context.MODE_PRIVATE);
           String S = sp.getString("name",null);
           return S;
    }

    public String getAbout(){
        sp = context.getSharedPreferences("myPrefer", Context.MODE_PRIVATE);
        String p = sp.getString("about",null);
        return p;
    }

    public String getPhone(){
        sp = context.getSharedPreferences("myPrefer", Context.MODE_PRIVATE);
        String p = sp.getString("phone",null);
        return p;
    }

    public boolean isLogged(){
        sp = context.getSharedPreferences("myPrefer", Context.MODE_PRIVATE);
        String tempem = sp.getString("name",null);
        if(tempem==null){
            return false;
        }
        return true;
    }

    public void logout(){
        sp = context.getSharedPreferences("myPrefer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

}

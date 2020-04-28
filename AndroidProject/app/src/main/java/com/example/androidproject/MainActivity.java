package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity{

    FragmentList frag1;
    FragmentDetails frag2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frag1 = new FragmentList();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ftrans = fragmentManager.beginTransaction();
        ftrans.add(R.id.fragment_container, frag1).commit();
    }
}


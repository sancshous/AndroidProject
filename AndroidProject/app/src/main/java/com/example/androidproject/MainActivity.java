package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentList frag1 = new FragmentList();

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ftrans = fragmentManager.beginTransaction();
            ftrans.add(R.id.fragment_container, frag1).commit();
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            TextView ContactName = (TextView) findViewById(R.id.ContactName);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ContactName.getLayoutParams();
            params.width = 100;
            ContactName.setLayoutParams(params);
        }
    }
}


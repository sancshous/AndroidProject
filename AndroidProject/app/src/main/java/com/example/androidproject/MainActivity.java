package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity{

    SimpleService mService;
    private boolean isBound;
    ServiceConnection serviceConnection;
    Bridge bridge;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null){
            bridge = new Bridge() {
                @Override
                public void getData(final List<Person> person) {
                    Log.d("ThreadName1", Thread.currentThread().getName());
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction
                            .replace(R.id.fragment_container, FragmentList.newInstance(person))
                            .commit();
                }
            };
        }
        else{//реализация интерфейса обратного вызова из сервиса
            bridge = new Bridge() {
                @Override
                public void getData(final List<Person> person) {
                    Log.d("ThreadName1", Thread.currentThread().getName());
                }
            };
        }
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                SimpleService.LocalService localService = (SimpleService.LocalService) service;
                mService = localService.getService();
                Log.d("ThreadName2", Thread.currentThread().getName());
                mService.getPerson(bridge); //забираем данные из сервиса
                isBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isBound = false;
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, SimpleService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(serviceConnection);
        }
    }
}


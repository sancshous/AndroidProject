package com.example.androidproject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SimpleService extends Service {
    private final IBinder mBinder = new LocalService();
    private List<Person> person;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void getPerson(final Bridge bridge){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(person == null){
                    person = new ArrayList<>();
                    person.add(new Person(2131165191, "Alex", 88005553535L,
                            "sancshous@gmail.com", "My first contact"));
                    bridge.getData(person); //передаём список контактов в активити
                }
                else{
                    bridge.getData(person);
                }
            }
        }).start();
    }

    public class LocalService extends Binder {

        SimpleService getService(){
            return SimpleService.this;
        }
    }

}

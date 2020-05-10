package com.example.androidproject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class MyService extends Service{
    private final IBinder mBinder = new LocalService();
    private Repository repository = Repository.getInstance();
    private ExecutorService executor = Executors.newCachedThreadPool();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void getPersonsList(final ObserverList callback) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                callback.updateShort(repository.getPersons());
                System.out.println(Thread.currentThread().getName());
            }
        });
        executor.shutdown();
    }

    public void getFullInfo(final int id, final ObserverDetails callback) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                callback.updateFull(repository.getPersons().get(id));
                System.out.println(Thread.currentThread().getName());
            }
        });
        executor.shutdown();
    }

    public class LocalService extends Binder {
        MyService getService(){
            return MyService.this;
        }
    }
}

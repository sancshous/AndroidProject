package com.example.androidproject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyService extends Service {
    private final IBinder mBinder = new LocalService();
    private Repository repository = Repository.getInstance();
    private ExecutorService executor = Executors.newCachedThreadPool();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void getPersonsList(final ObserverList callback) {
        final WeakReference<ObserverList> weakCallbackList = new WeakReference<>(callback);
        executor.submit(new Runnable() {
            @Override
            public void run() {
                weakCallbackList.get().updateShort(repository.getPersons());
                System.out.println(Thread.currentThread().getName());
            }
        });
    }

    public void getFullInfo(final int id, final ObserverDetails callback) {
        final WeakReference<ObserverDetails> weakCallbakInfo = new WeakReference<>(callback);
        executor.submit(new Runnable() {
            @Override
            public void run() {
                weakCallbakInfo.get().updateFull(repository.getPersons().get(id));
                System.out.println(Thread.currentThread().getName());
            }
        });
    }

    public class LocalService extends Binder {
        MyService getService(){
            return MyService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}

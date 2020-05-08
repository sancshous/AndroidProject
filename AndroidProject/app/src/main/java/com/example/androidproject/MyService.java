package com.example.androidproject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MyService extends Service implements Observable{
    private final IBinder mBinder = new LocalService();
    private Repository repository = Repository.getInstance();
    private WeakReference<List<ObserverList>> observerListWeak;
    private WeakReference<List<ObserverDetails>> observerDetailsWeak;
    private List<ObserverList> observersList = new ArrayList<>();
    private List<ObserverDetails> observersDetails = new ArrayList<>();
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void onDestroy() {
        super.onDestroy();
        observersDetails.clear();
        observersList.clear();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public List<Person> getPersonsList() throws ExecutionException, InterruptedException {
        Future<List<Person>> foo = executor.submit(new Callable<List<Person>>() {
            @Override
            public List<Person> call() {
                return repository.getPersons();
            }
        });
        executor.shutdown();
        return foo.get();
    }

    public Person getFullInfo(int id) throws ExecutionException, InterruptedException {
        Future<List<Person>> foo = executor.submit(new Callable<List<Person>>() {
            @Override
            public List<Person> call() {
                return repository.getPersons();
            }
        });
        executor.shutdown();
        return foo.get().get(id);
    }

    @Override
    public void registerList(ObserverList o) {
        observerListWeak = new WeakReference<>(observersList);
        observerListWeak.get().add(o);
    }

    @Override
    public void registerDetails(ObserverDetails o) {
        observerDetailsWeak = new WeakReference<>(observersDetails);
        observerDetailsWeak.get().add(o);
    }

    @Override
    public void notifyList(){
        try {
            for (int i = 0; i < observerListWeak.get().size(); i++) {
                observerListWeak.get().get(i).updateShort(getPersonsList());
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyDetails(int id) {
        try {
            for (int i = 0; i < observerDetailsWeak.get().size(); i++) {
                observerDetailsWeak.get().get(i).updateFull(getFullInfo(id));
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class LocalService extends Binder {
        MyService getService(){
            return MyService.this;
        }
    }
}

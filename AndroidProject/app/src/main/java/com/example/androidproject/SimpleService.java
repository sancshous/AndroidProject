package com.example.androidproject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SimpleService extends Service implements Observable{
    private final IBinder mBinder = new LocalService();
    private Repository repository = Repository.getInstance();
    private List<Observer> observers = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public List<Person> getPersonsList() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
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
        ExecutorService executor = Executors.newSingleThreadExecutor();
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
    public void register(Observer o) {
        observers.add(o);
    }

    @Override
    public void delete(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObserver(){
        try {
            for (int i = 0; i < observers.size(); i++) {
                Observer observer = observers.get(i);
                observer.updateShort(getPersonsList());
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyObserverPerson(int id) {
        try {
            for (int i = 0; i < observers.size(); i++) {
                Observer observer = observers.get(i);
                observer.updateFull(getFullInfo(id));
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class LocalService extends Binder {
        SimpleService getService(){
            return SimpleService.this;
        }
    }
}

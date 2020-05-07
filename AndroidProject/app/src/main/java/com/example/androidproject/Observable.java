package com.example.androidproject;

public interface Observable {
    void register(Observer o);
    void delete(Observer o);
    void notifyObserver();
    void notifyObserverPerson(int id);
}

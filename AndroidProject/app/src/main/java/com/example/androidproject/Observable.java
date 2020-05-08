package com.example.androidproject;

public interface Observable {
    void registerList(ObserverList o);
    void registerDetails(ObserverDetails o);
    void notifyList();
    void notifyDetails(int id);
}

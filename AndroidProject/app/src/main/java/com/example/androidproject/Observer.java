package com.example.androidproject;

import java.util.List;

public interface Observer {
    void updateShort(List<Person> list);
    void updateFull(Person person);
}

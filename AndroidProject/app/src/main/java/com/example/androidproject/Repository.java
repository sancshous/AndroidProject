package com.example.androidproject;

import java.util.Collections;
import java.util.List;

public class Repository {
    private static Repository instance;

    public static synchronized Repository getInstance(){
        if(instance == null){
            instance = new Repository();
        }
        return instance;
    }

    private Repository() {
    }

    private List<Person> persons = Collections.singletonList(new Person(0, "Alex", 880055535359L,
            "sancshous@gmail.com", "My first contact"));

    public List<Person> getPersons() {
        return persons;
    }
}

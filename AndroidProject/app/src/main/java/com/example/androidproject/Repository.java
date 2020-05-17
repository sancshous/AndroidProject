package com.example.androidproject;

import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
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

    private List<Person> persons = Collections.singletonList(new Person(0, "Alex", 88005553535L,
            "sancshous@gmail.com", "My first contact", new GregorianCalendar(1997, Calendar.MAY, 17)));

    public List<Person> getPersons() {
        return persons;
    }
}

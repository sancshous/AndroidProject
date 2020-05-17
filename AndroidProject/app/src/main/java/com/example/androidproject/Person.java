package com.example.androidproject;

import java.util.GregorianCalendar;

public class Person {
    private final int id;
    private final String name;
    private final long phone;
    private final String email;
    private final String description;
    private final GregorianCalendar birthday;

    public Person(int id, String name, long phone, String email, String description, GregorianCalendar birthday) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.description = description;
        this.birthday = birthday;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public GregorianCalendar getBirthday() {
        return birthday;
    }
}

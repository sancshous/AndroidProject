package com.example.androidproject;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {
    private int id;
    private String name;
    private long phone;
    private String email;
    private String description;

    public Person(int id, String name, long phone, String email, String description) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.description = description;
    }

    protected Person(Parcel in) {
        id = in.readInt();
        name = in.readString();
        phone = in.readLong();
        email = in.readString();
        description = in.readString();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeLong(phone);
        dest.writeString(email);
        dest.writeString(description);
    }
}

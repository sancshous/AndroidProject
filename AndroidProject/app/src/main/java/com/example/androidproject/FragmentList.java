package com.example.androidproject;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class FragmentList extends Fragment {
    private TextView contactName;
    private TextView contactPhone;
    private List<Person> persons;
    private Person person;

    public static Fragment newInstance(List<Person> person) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("Parcel", (ArrayList<? extends Parcelable>) person);
        Fragment fragment = new FragmentList();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        if(getActivity() != null){
            getActivity().setTitle("Список контактов");
        }
        if(getArguments() != null){
            persons = getArguments().getParcelableArrayList("Parcel");
        }
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contactName = view.findViewById(R.id.ContactName);
        contactPhone = view.findViewById(R.id.ContactPhone);
        Log.d("ThreadName3", Thread.currentThread().getName());
        person = persons.get(0);
        String personName = person.getName();
        long personPhone = person.getPhone();
        contactName.setText(personName);
        contactPhone.setText(String.valueOf(personPhone));
        final View ContactID = view.findViewById(R.id.ContactID);
        ContactID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == person.getId()){
                    String[] data = {person.getName(), String.valueOf(person.getPhone()),
                            person.getEmail(), person.getDescription()};
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, FragmentDetails.newInstance(data))
                            .addToBackStack(null).commit();
                }
            }
        });
    }
}


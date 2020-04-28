package com.example.androidproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class FragmentDetails extends Fragment {
    public FragmentDetails(int contactID) {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Детали контакта");
    }
}

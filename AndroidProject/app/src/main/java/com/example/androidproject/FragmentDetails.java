package com.example.androidproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentDetails extends Fragment {
    String[] details;
    public static Fragment newInstance(String[] details) {
        Bundle args = new Bundle();
        args.putStringArray("DETAILS", details);
        Fragment fragment = new FragmentDetails();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getActivity() != null){
            getActivity().setTitle("Детали контакта");
        }
        details = getArguments().getStringArray("DETAILS");
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView detalName = view.findViewById(R.id.ContactDetailName);
        TextView detalPhone = view.findViewById(R.id.ContactDetailPhone);
        TextView detalMail = view.findViewById(R.id.ContactDetailEmail);
        TextView detalDescription = view.findViewById(R.id.ContactDetailNotes);
        detalName.setText(details[0]);
        detalPhone.setText(details[1]);
        detalMail.setText(details[2]);
        detalDescription.setText(details[3]);
    }
}

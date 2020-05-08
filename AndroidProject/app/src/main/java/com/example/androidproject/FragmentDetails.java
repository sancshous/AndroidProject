package com.example.androidproject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.leakcanary.RefWatcher;

public class FragmentDetails extends Fragment{
    private int personId;
    private TextView detalName;
    private TextView detalPhone;
    private TextView detalMail;
    private TextView detalDescription;

    private MyService mService;
    private boolean isBound;
    private ServiceConnection serviceConnection;

    private ObserverDetails observer = new ObserverDetails() {
        @Override
        public void updateFull(Person person) {
            detalName.setText(person.getName());
            detalPhone.setText(String.valueOf(person.getPhone()));
            detalMail.setText(person.getEmail());
            detalDescription.setText(person.getDescription());
        }
    };

    public static Fragment newInstance(int personId) {
        Bundle args = new Bundle();
        args.putInt("DETAILS", personId);
        Fragment fragment = new FragmentDetails();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getActivity() != null){
            getActivity().setTitle("Детали контакта");
        }
        personId = getArguments().getInt("DETAILS");
        serviceConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d("FragmentDetails", "Connected!");
                mService = ((MyService.LocalService) service).getService();
                mService.registerDetails(observer);
                mService.notifyDetails(personId);
            }

            public void onServiceDisconnected(ComponentName name) {
                mService = null;
                isBound = false;
            }
        };
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        detalName = view.findViewById(R.id.ContactDetailName);
        detalPhone = view.findViewById(R.id.ContactDetailPhone);
        detalMail = view.findViewById(R.id.ContactDetailEmail);
        detalDescription = view.findViewById(R.id.ContactDetailNotes);
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getActivity(), MyService.class);
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        isBound = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isBound) {
            getActivity().unbindService(serviceConnection);
            isBound = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = App.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        detalDescription = null;
        detalMail = null;
        detalName = null;
        detalPhone = null;
    }
}

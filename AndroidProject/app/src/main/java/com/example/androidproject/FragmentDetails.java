package com.example.androidproject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
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
    private Handler handler = new Handler();

    private MyService mService;
    private boolean isBound;
    private ServiceConnection serviceConnection;

    private ObserverDetails observer = new ObserverDetails() {
        @Override
        public void updateFull(final Person person) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if((detalName != null) || (detalPhone != null) || (detalMail != null) || (detalDescription != null)) {
                        detalName.setText(person.getName());
                        detalPhone.setText(String.valueOf(person.getPhone()));
                        detalMail.setText(person.getEmail());
                        detalDescription.setText(person.getDescription());
                        System.out.println("Поток с view: " + Thread.currentThread().getName());
                    }
                }
            });
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
        requireActivity().setTitle("Детали контакта");
        personId = getArguments().getInt("DETAILS");
        serviceConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d("FragmentDetails", "Connected!");
                mService = ((MyService.LocalService) service).getService();
                mService.getFullInfo(personId, observer);
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
        requireActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        isBound = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isBound) {
            requireActivity().unbindService(serviceConnection);
            isBound = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = App.getRefWatcher(requireActivity());
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

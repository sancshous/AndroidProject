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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class FragmentList extends Fragment implements Observer{
    private TextView contactName;
    private TextView contactPhone;
    private ConstraintLayout contactId;
    private List<Person> personList;

    private SimpleService mService;
    private boolean isBound;

    private ServiceConnection serviceConnection;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        if(getActivity() != null){
            getActivity().setTitle("Список контактов");
        }
        serviceConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d("Из фрагмента", "Связь с сервисом установлена");
                mService = ((SimpleService.LocalService) service).getService();
                mService.register(FragmentList.this);
                mService.notifyObserver();
            }

            public void onServiceDisconnected(ComponentName name) {
                mService = null;
                isBound = false;
            }
        };
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contactName = view.findViewById(R.id.ContactName);
        contactPhone = view.findViewById(R.id.ContactPhone);
        contactId = view.findViewById(R.id.ContactID);
        contactId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, FragmentDetails.newInstance(personList.get(0).getId()))
                        .addToBackStack(null).commit();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getActivity(), SimpleService.class);
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        isBound = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBound) {
            getActivity().unbindService(serviceConnection);
            isBound = false;
            mService.delete(FragmentList.this);
        }
    }

    @Override
    public void updateShort(List<Person> list) {
        FragmentList.this.personList = list;
        contactName.setText(list.get(0).getName());
        contactPhone.setText(String.valueOf(list.get(0).getPhone()));
    }

    @Override
    public void updateFull(Person person) {
        Log.d("FragmentList", "this is callback");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        contactName = null;
        contactId = null;
        contactPhone = null;
    }
}


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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.leakcanary.RefWatcher;

import java.util.List;

public class FragmentList extends Fragment{
    private TextView contactName;
    private TextView contactPhone;
    private ConstraintLayout contactId;
    private List<Person> personList;
    private Handler handler = new Handler();

    private static final String TAG = "myLogs";

    private MyService mService;
    private boolean isBound;
    private ServiceConnection serviceConnection;

    private ObserverList observerList = new ObserverList() {
        @Override
        public void updateShort(final List<Person> list) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    personList = list;
                    if ((contactName != null) && (contactPhone != null)) {
                        contactName.setText(list.get(0).getName());
                        contactPhone.setText(String.valueOf(list.get(0).getPhone()));
                        System.out.println("Поток с view: " + Thread.currentThread().getName());
                    }
                }
            });
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        requireActivity().setTitle("Список контактов");
        serviceConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d("Из фрагмента", "Связь с сервисом установлена");
                mService = ((MyService.LocalService) service).getService();
                mService.getPersonsList(observerList);
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
                if ((personList.size() > 0) && (personList != null)) {
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, FragmentDetails.newInstance(personList.get(0).getId()))
                            .addToBackStack(null).commit();
                }
                else
                    Log.d(TAG, "personList еще не загружен");
            }
        });
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
        contactName = null;
        contactId = null;
        contactPhone = null;
    }
}

package com.example.androidproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.leakcanary.RefWatcher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class FragmentDetails extends Fragment{
    private static final String pref = "settings";

    private int personId;
    private TextView detalName;
    private TextView detalPhone;
    private TextView detalMail;
    private TextView detalDescription;
    private TextView detalBirthday;
    private Switch switchNotify;

    private Handler handler = new Handler();
    private SharedPreferences settings;

    private MyService mService;
    private AlarmManager alarmManager;
    private boolean isBound;
    private ServiceConnection serviceConnection;
    private String name;
    private PendingIntent pendingIntent;
    private Intent intent;

    private GregorianCalendar calendar = (GregorianCalendar)Calendar.getInstance();

    private ObserverDetails observer = new ObserverDetails() {
        @Override
        public void updateFull(final Person person) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if((detalName != null) && (detalPhone != null)
                            && (detalMail != null) && (detalDescription != null)
                    && (detalBirthday != null)) {
                        DateFormat df = new SimpleDateFormat("d MMM", Locale.US);
                        String dateBirthday = df.format(person.getBirthday().getTime());
                        calendar.set(Calendar.DAY_OF_MONTH, person.getBirthday().get(Calendar.DAY_OF_MONTH));
                        calendar.set(Calendar.MONTH, person.getBirthday().get(Calendar.MONTH));
                        name = person.getName();
                        createIntent(name);
                        detalName.setText(person.getName());
                        detalPhone.setText(String.valueOf(person.getPhone()));
                        detalMail.setText(person.getEmail());
                        detalDescription.setText(person.getDescription());
                        detalBirthday.setText(dateBirthday);
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
        requireActivity().setTitle(getString(R.string.TitleDetail));
        personId = requireArguments().getInt("DETAILS");
        settings = requireActivity().getSharedPreferences(pref, Context.MODE_PRIVATE);
        alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
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
        detalBirthday = view.findViewById(R.id.ContactDetailsBirthday);
        switchNotify = view.findViewById(R.id.ContactDetailsSwitcher);
        intent = new Intent(getActivity(), BirthdayAlarmReceiver.class);
        if(settings.contains("state")){
            switchNotify.setChecked(settings.getBoolean("state", false));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        switchNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = settings.edit();
                if(isChecked){
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    editor.putBoolean("state", true).apply();
                }
                else{
                    alarmManager.cancel(pendingIntent);
                    pendingIntent.cancel();
                    editor.putBoolean("state", false).apply();
                }
            }
        });
    }

    private void createIntent(String name){
        if(intent != null){
            intent.putExtra("PERSON_ID", personId);
            intent.putExtra("MESSAGE", requireActivity().getString(R.string.notificationMessage)+" "+name);
        }
        pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
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
        detalBirthday = null;
        switchNotify = null;
    }
}

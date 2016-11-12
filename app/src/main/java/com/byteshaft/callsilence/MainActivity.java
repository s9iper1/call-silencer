package com.byteshaft.callsilence;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        CompoundButton.OnCheckedChangeListener {

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 0;
    private Switch serviceSwitch;
    private boolean buttonState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceSwitch = (Switch) findViewById(R.id.service_switch);
        serviceSwitch.setOnCheckedChangeListener(this);
        checkAndRequestPermissions(false);
    }

    private void checkAndRequestPermissions(boolean value) {
        int outGoingCall = ContextCompat.checkSelfPermission(this,
                Manifest.permission.PROCESS_OUTGOING_CALLS);
        int phoneStatePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
        final List<String> listPermissionsNeeded = new ArrayList<>();
        if (outGoingCall != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.PROCESS_OUTGOING_CALLS);
            Log.i("TAG", "READ_CONTACTS");
        }
        if (phoneStatePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
            Log.i("TAG", "READ_PHONE_STATE");
        }

        if (!listPermissionsNeeded.isEmpty() && listPermissionsNeeded.size() > 0) {
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
                    new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setTitle("Permissions Required");
            alertDialogBuilder.setMessage("In order to use this application, you need to grant some permissions.")
                    .setCancelable(false).setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    ActivityCompat.requestPermissions(MainActivity.this,
                            listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                            REQUEST_ID_MULTIPLE_PERMISSIONS);
                }
            });
            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            if (value) startCallSilence();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                   if (perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                       startCallSilence();
                    } else {
                        Log.d("MainActivity", "Some permissions are not granted ask again ");

                    }
                }
            }
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.service_switch:
                buttonState = b;
                checkAndRequestPermissions(true);
                break;
        }

    }

    private void startCallSilence() {
        if (buttonState) {
            Helpers.saveButtonState(true);
            serviceSwitch.setTextColor(getResources().getColor(android.R.color.white));
        } else  {
            Helpers.saveButtonState(false);
            serviceSwitch.setTextColor(getResources().getColor(android.R.color.black));
        }
    }
}

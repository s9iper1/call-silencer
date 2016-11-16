package com.byteshaft.callsilence;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 0;
    private Switch serviceSwitch;
    private boolean buttonState = false;
    private Button shareButton;
    private Button rateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceSwitch = (Switch) findViewById(R.id.service_switch);
        shareButton = (Button) findViewById(R.id.share_app);
        rateButton = (Button) findViewById(R.id.rate_app);
        shareButton.setOnClickListener(this);
        rateButton.setOnClickListener(this);
        serviceSwitch.setOnCheckedChangeListener(this);
        checkAndRequestPermissions(false);
        serviceSwitch.setChecked(Helpers.getButtonState());
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

    private void askNotificationPermissionForAndroidM() {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && !notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        } else {

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
                       new android.os.Handler().postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               askNotificationPermissionForAndroidM();
                           }
                       }, 1000);
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

    protected void shareApp() {
        String APP_LINK = "https://play.google.com/store/apps/details?id=" + getPackageName();
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.edit, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setTitle(getString(R.string.app_name));
        alertDialogBuilder.setView(promptsView);
        final EditText edtText = (EditText) promptsView.findViewById(R.id.edtName);
        String body = getString(R.string.Share_App_Body_top) + " " + getString(R.string.app_name) + " " +
                getString(R.string.Share_App_Body_middle) + " " + APP_LINK + " " +
                getString(R.string.Share_App_Body_bottom);
        edtText.setText(body);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Send",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                String finalString = (edtText.getText().toString());

                                Intent email = new Intent(Intent.ACTION_SEND);
                                email.setType("text/plain");
                                email.putExtra(Intent.EXTRA_EMAIL, "");
                                email.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                email.putExtra(Intent.EXTRA_SUBJECT, " " + getString(R.string.app_name) + " " + getString(R.string.Share_App_Sub));
                                email.putExtra(Intent.EXTRA_TEXT, finalString);
                                try {
                                    startActivity(Intent.createChooser(email, "Send Message..."));
                                } catch (android.content.ActivityNotFoundException ex) {

                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    private void rateApp() {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
    }

    private void startCallSilence() {
        if (buttonState) {
            Helpers.saveButtonState(true);
            serviceSwitch.setTextColor(getResources().getColor(android.R.color.white));
        } else  {
            Helpers.saveButtonState(false);
            serviceSwitch.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_app:
                shareApp();
                break;
            case R.id.rate_app:
                rateApp();
                break;
        }

    }
}

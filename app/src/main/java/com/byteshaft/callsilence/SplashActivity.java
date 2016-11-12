package com.byteshaft.callsilence;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by s9iper1 on 11/12/16.
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }, 2000);

    }
}

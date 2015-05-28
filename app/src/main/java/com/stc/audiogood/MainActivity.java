package com.stc.audiogood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {

    private static final int EXIT_DELAY_TIME = 300;
    // from 0 to 15
    private static final int VALUE_MUSIC_VOLUME = 10;

    private static final String ZEN_MODE = "zen_mode";
    private static final int ZEN_MODE_OFF = 0;
    private static final int ZEN_MODE_NO_INTERRUPTIONS = 2;

    private Button Bwork=null;
    private Button Bhome=null;
    private TextView TVshow=null;
    private AudioManager audio=null;
    private Process p=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bwork=(Button)findViewById(R.id.button);
        Bhome=(Button)findViewById(R.id.button2);
        TVshow=(TextView)findViewById(R.id.textView);
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        try {
            p = Runtime.getRuntime().exec("su sh");
        } catch (IOException e) {
            e.printStackTrace();
        }
        execShellcommand("pm grant com.stc.audiogood android.permission.WRITE_SECURE_SETTINGS");
        TVshow.setText(R.string.space);
        Bwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TVshow.setText(R.string.work);
                Settings.Global.putInt(getApplicationContext().getContentResolver(), ZEN_MODE, ZEN_MODE_NO_INTERRUPTIONS);
                audio.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                handler.postDelayed(myRunnable, EXIT_DELAY_TIME);
            }
        });

        Bhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TVshow.setText(R.string.home);
                Settings.Global.putInt(getApplicationContext().getContentResolver(), ZEN_MODE, ZEN_MODE_OFF);
                audio.setStreamVolume(AudioManager.STREAM_MUSIC, VALUE_MUSIC_VOLUME, 0);
                handler.postDelayed(myRunnable, EXIT_DELAY_TIME);
            }
        });

        Bwork.setSoundEffectsEnabled(false);
        Bhome.setSoundEffectsEnabled(false);
        //commit test
    }

    public Handler handler = new Handler();

    public Runnable myRunnable= new Runnable() {
        public void run() {
            finish();
        }
    };

    private void execShellcommand(String cmd) {
        DataOutputStream stdin = new DataOutputStream(p.getOutputStream());
        try {
            stdin.writeBytes(cmd+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

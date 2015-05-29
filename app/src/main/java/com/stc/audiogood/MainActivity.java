package com.stc.audiogood;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;

import java.io.DataOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {
    // from 0 to 15
    private static final int VALUE_MUSIC_VOLUME = 10;

    private static final String ZEN_MODE = "zen_mode";
    private static final int ZEN_MODE_OFF = 0;
    private static final int ZEN_MODE_NO_INTERRUPTIONS = 2;

    private AudioManager audio=null;
    private Process p=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        try {
            p = Runtime.getRuntime().exec("su sh");
        } catch (IOException e) {
            e.printStackTrace();
        }
        execShellcommand("pm grant com.stc.audiogood android.permission.WRITE_SECURE_SETTINGS");

        int value = Settings.Global.getInt(getApplicationContext().getContentResolver(), ZEN_MODE, ZEN_MODE_OFF);

        if(ZEN_MODE_OFF == value) {
            Settings.Global.putInt(getApplicationContext().getContentResolver(), ZEN_MODE, ZEN_MODE_NO_INTERRUPTIONS);
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        }else{
            Settings.Global.putInt(getApplicationContext().getContentResolver(), ZEN_MODE, ZEN_MODE_OFF);
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, VALUE_MUSIC_VOLUME, 0);
        }
        finish();
    }

    private void execShellcommand(String cmd) {
        DataOutputStream stdin = new DataOutputStream(p.getOutputStream());
        try {
            stdin.writeBytes(cmd+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

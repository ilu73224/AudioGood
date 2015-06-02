package com.stc.audiogood;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;

import java.io.DataOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {
    // from 0 to 15
    private static final int DEFAULT_VALUE_MUSIC_VOLUME = 10;

    private static final String ZEN_MODE = "zen_mode";
    private static final int ZEN_MODE_OFF = 0;
    private static final int ZEN_MODE_NO_INTERRUPTIONS = 2;

    public static final String PREFS_NAME = "AudioGoodPref";
    public static final String KEY_MUSIC_VOLUME = "AudioGoodPref";

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

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if(ZEN_MODE_OFF == value) {
            Settings.Global.putInt(getApplicationContext().getContentResolver(), ZEN_MODE, ZEN_MODE_NO_INTERRUPTIONS);
            int music_volume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(KEY_MUSIC_VOLUME, music_volume);
            editor.commit();
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        }else{
            Settings.Global.putInt(getApplicationContext().getContentResolver(), ZEN_MODE, ZEN_MODE_OFF);
            int music_volume = settings.getInt(KEY_MUSIC_VOLUME, DEFAULT_VALUE_MUSIC_VOLUME);
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, music_volume, 0);
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

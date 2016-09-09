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
    /*
    // Maximum volume index values for audio streams
    private static int[] MAX_STREAM_VOLUME = new int[] {
            5,  // STREAM_VOICE_CALL
            7,  // STREAM_SYSTEM
            7,  // STREAM_RING
            15, // STREAM_MUSIC
            7,  // STREAM_ALARM
            7,  // STREAM_NOTIFICATION
            15, // STREAM_BLUETOOTH_SCO
            7,  // STREAM_SYSTEM_ENFORCED
            15, // STREAM_DTMF
            15  // STREAM_TTS
    };
    // Minimum volume index values for audio streams
    private static int[] MIN_STREAM_VOLUME = new int[] {
            1,  // STREAM_VOICE_CALL
            0,  // STREAM_SYSTEM
            0,  // STREAM_RING
            0,  // STREAM_MUSIC
            0,  // STREAM_ALARM
            0,  // STREAM_NOTIFICATION
            0,  // STREAM_BLUETOOTH_SCO
            0,  // STREAM_SYSTEM_ENFORCED
            0,  // STREAM_DTMF
            0   // STREAM_TTS
    };
    */
    private static final int DEFAULT_VALUE_MUSIC_VOLUME = 10;
    private static final int DEFAULT_VALUE_ALARM_VOLUME = 5;

    public static final String PREFS_NAME = "AudioGoodPref";
    public static final String KEY_MUSIC_VOLUME = "music_volume";
    public static final String KEY_ALARM_VOLUME = "alarm_volume";
    private AudioManager audio=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if(audio.getRingerMode() != AudioManager.RINGER_MODE_SILENT){
            int music_volume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
            int alarm_volume = audio.getStreamVolume(AudioManager.STREAM_ALARM);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(KEY_MUSIC_VOLUME, music_volume);
            editor.putInt(KEY_ALARM_VOLUME, alarm_volume);
            editor.commit();
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            audio.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0);
            audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }else{
            audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            int music_volume = settings.getInt(KEY_MUSIC_VOLUME, DEFAULT_VALUE_MUSIC_VOLUME);
            int alarm_volume = settings.getInt(KEY_ALARM_VOLUME, DEFAULT_VALUE_ALARM_VOLUME);
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, music_volume, 0);
            audio.setStreamVolume(AudioManager.STREAM_ALARM, alarm_volume, 0);
        }
    }

    @Override
    protected void onResume() {
        finish();
        super.onResume();
    }
}

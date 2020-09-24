package com.stc.audiogood;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

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
            15, // STREAM_TTS
            15  // STREAM_ACCESSIBILITY
    };

    // Minimum volume index values for audio streams
    private static int[] MIN_STREAM_VOLUME = new int[] {
            1,  // STREAM_VOICE_CALL
            0,  // STREAM_SYSTEM
            0,  // STREAM_RING
            0,  // STREAM_MUSIC
            1,  // STREAM_ALARM
            0,  // STREAM_NOTIFICATION
            0,  // STREAM_BLUETOOTH_SCO
            0,  // STREAM_SYSTEM_ENFORCED
            0,  // STREAM_DTMF
            0,  // STREAM_TTS
            1   // STREAM_ACCESSIBILITY
    };
*/
    public static int[] DEFAULT_STREAM_VOLUME = new int[] {
        4,  // STREAM_VOICE_CALL
        7,  // STREAM_SYSTEM
        5,  // STREAM_RING
        5,  // STREAM_MUSIC
        6,  // STREAM_ALARM
        5,  // STREAM_NOTIFICATION
        7,  // STREAM_BLUETOOTH_SCO
        7,  // STREAM_SYSTEM_ENFORCED
        5,  // STREAM_DTMF
        5,  // STREAM_TTS
        5,  // STREAM_ACCESSIBILITY
    };
/*
    //Used to identify the default audio stream volume
    public static final int STREAM_DEFAULT = -1;
    // Used to identify the volume of audio streams for phone calls
    public static final int STREAM_VOICE_CALL = 0;
    // Used to identify the volume of audio streams for system sounds
    public static final int STREAM_SYSTEM = 1;
    //Used to identify the volume of audio streams for the phone ring and message alerts
    public static final int STREAM_RING = 2;
    //Used to identify the volume of audio streams for music playback
    public static final int STREAM_MUSIC = 3;
    //Used to identify the volume of audio streams for alarms
    public static final int STREAM_ALARM = 4;
    // Used to identify the volume of audio streams for notifications
    public static final int STREAM_NOTIFICATION = 5;
    //Used to identify the volume of audio streams for phone calls when connected on bluetooth
    public static final int STREAM_BLUETOOTH_SCO = 6;
    // Used to identify the volume of audio streams for enforced system sounds in certain
    // countries (e.g camera in Japan)
    public static final int STREAM_SYSTEM_ENFORCED = 7;
    //Used to identify the volume of audio streams for DTMF tones
    public static final int STREAM_DTMF = 8;
    //Used to identify the volume of audio streams exclusively transmitted through the
    //speaker (TTS) of the device
    public static final int STREAM_TTS = 9;
    //Used to identify the volume of audio streams for accessibility prompts
    public static final int STREAM_ACCESSIBILITY = 10;
*/
/*
    public static final String PREFS_NAME = "AudioGoodPref";
    public static final String KEY_MUSIC_VOLUME = "music_volume";
    public static final String KEY_ALARM_VOLUME = "alarm_volume";
    public static final String KEY_RING_VOLUME = "ring_volume";
*/
    private AudioManager audio=null;
    public static final String TAG = "AudioGood";
    static final int PICK_CONTACT_REQUEST = 1;
    public static final int INSIDE_STREAM_MUSIC_VOLUME = 2;
    public static final int INSIDE_STREAM_ALARM_VOLUME = 2;
    public static final int INSIDE_STREAM_RING_VOLUME = 1;
    public static final int FLAG_AUDIO_MODES = AudioManager.FLAG_SHOW_UI;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationManager n = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if(n.isNotificationPolicyAccessGranted()) {
            audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            printAudioSetting();
            Intent i = getIntent();
            if(i.getComponent().getShortClassName().equals(".MainActivity")) {
                Log.d(TAG, ".MainActivity");
                defaultAction();
            }
            if(i.getComponent().getShortClassName().equals(".Inside")) {
                Log.d(TAG, ".Inside");
                InsideAction();
            }
            if(i.getComponent().getShortClassName().equals(".Outside")) {
                Log.d(TAG, ".Outside");
                OutsideAction();
            }
            if(i.getComponent().getShortClassName().equals(".Default")) {
                Log.d(TAG, ".Default");
                defaultSettingAction();
            }
            printAudioSetting();
        }else{
            // Ask the user to grant access
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivityForResult(intent, PICK_CONTACT_REQUEST);
        }
    }

    @Override
    protected void onResume() {
        finish();
        super.onResume();
    }

    private void defaultAction() {
        audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

    private void InsideAction() {
        audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, INSIDE_STREAM_MUSIC_VOLUME, FLAG_AUDIO_MODES);
        audio.setStreamVolume(AudioManager.STREAM_ALARM, INSIDE_STREAM_ALARM_VOLUME, FLAG_AUDIO_MODES);
        audio.setStreamVolume(AudioManager.STREAM_RING, INSIDE_STREAM_RING_VOLUME, FLAG_AUDIO_MODES);
    }

    private void OutsideAction() {
        audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), FLAG_AUDIO_MODES);
        audio.setStreamVolume(AudioManager.STREAM_ALARM, audio.getStreamMaxVolume(AudioManager.STREAM_ALARM), FLAG_AUDIO_MODES);
        audio.setStreamVolume(AudioManager.STREAM_RING, audio.getStreamMaxVolume(AudioManager.STREAM_RING), FLAG_AUDIO_MODES);
    }

    private void defaultSettingAction() {
        audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, DEFAULT_STREAM_VOLUME[AudioManager.STREAM_MUSIC], FLAG_AUDIO_MODES);
        audio.setStreamVolume(AudioManager.STREAM_ALARM, DEFAULT_STREAM_VOLUME[AudioManager.STREAM_ALARM], FLAG_AUDIO_MODES);
        audio.setStreamVolume(AudioManager.STREAM_RING, DEFAULT_STREAM_VOLUME[AudioManager.STREAM_RING], FLAG_AUDIO_MODES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "requestCode = PICK_CONTACT_REQUEST" + "resultCode = RESULT_OK");
            } else {
                Log.d(TAG, "requestCode = PICK_CONTACT_REQUEST" + "resultCode = " + resultCode);
            }
        } else {
            Log.d(TAG, "requestCode = " + requestCode + "resultCode = " + resultCode);
        }
    }

    private void printAudioSetting() {
        printByIndex("AudioManager.STREAM_MUSIC",AudioManager.STREAM_MUSIC);
        printByIndex("AudioManager.STREAM_ALARM",AudioManager.STREAM_ALARM);
        printByIndex("AudioManager.STREAM_RING",AudioManager.STREAM_RING);
    }
    private void printByIndex(String name, int index) {
        Log.d(TAG, name + " current: " + audio.getStreamVolume(index)+
                                ", max: " + audio.getStreamMaxVolume(index)+
                                ", default: " + DEFAULT_STREAM_VOLUME[index]);
    }
}

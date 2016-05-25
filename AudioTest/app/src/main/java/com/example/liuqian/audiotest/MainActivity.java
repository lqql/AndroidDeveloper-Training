package com.example.liuqian.audiotest;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private AudioPlayer mAudioPlayer;
    public static AudioManager mAudioManager;
    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                // Pause playback
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // Resume playback
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                mAudioManager.unregisterMediaButtonEventReceiver(new ComponentName(getPackageName(),
                        RemoteControlReceiver.class.getName()));
                mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
                // Stop playback
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // Start playback.
            if (mAudioManager.isBluetoothA2dpOn()) {
                Toast.makeText(MainActivity.this,"Adjust output for Bluetooth.",Toast.LENGTH_LONG).show();
                // Adjust output for Bluetooth.
            } else if (mAudioManager.isSpeakerphoneOn()) {
                // Adjust output for Speakerphone.
                Toast.makeText(MainActivity.this,"Adjust output for Speakerphone.",Toast.LENGTH_LONG).show();

            } else if (mAudioManager.isWiredHeadsetOn()) {
                // Adjust output for headsets
                Toast.makeText(MainActivity.this,"Adjust output for headsets.",Toast.LENGTH_LONG).show();

            } else {
                // If audio plays and noone can hear it, is it still playing?
            }

            mAudioPlayer.play(this);
        }


    }
    public class RemoteControlReceiver extends BroadcastReceiver {
        public RemoteControlReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO: This method is called when the BroadcastReceiver is receiving
            // an Intent broadcast.
            if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
                KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                if (KeyEvent.KEYCODE_MEDIA_PLAY == event.getKeyCode()) {
                    Toast.makeText(MainActivity.this,"You have press the play button",Toast.LENGTH_LONG).show();
                }
                if (KeyEvent.KEYCODE_MEDIA_STOP==event.getKeyCode()){
                    mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
                }

            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        mAudioManager.unregisterMediaButtonEventReceiver(new ComponentName(getPackageName(),
                RemoteControlReceiver.class.getName()));
    }
    private class NoisyAudioStreamReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                // Pause the playback
            }
        }
    }

    private IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    private NoisyAudioStreamReceiver myNoisyAudioStreamReceiver = new NoisyAudioStreamReceiver();
    //动态广播接收器,仅在播放时注册,非播放时解绑;
    private void startPlayback() {
        registerReceiver(myNoisyAudioStreamReceiver, intentFilter);
    }

    private void stopPlayback() {
        unregisterReceiver(myNoisyAudioStreamReceiver);
    }

}

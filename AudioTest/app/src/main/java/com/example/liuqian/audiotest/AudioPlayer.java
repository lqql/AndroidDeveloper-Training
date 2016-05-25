package com.example.liuqian.audiotest;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by liuqian on 16/5/25.
 */
public class AudioPlayer {
    private MediaPlayer mMediaPlayer;
    public void play(Context context){
        stop();
        mMediaPlayer = MediaPlayer.create(context,R.raw.one_small_step);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stop();
            }
        });
        mMediaPlayer.start();

    }
    public void stop(){
        if (mMediaPlayer!=null){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
    public boolean isPalying(){
        return mMediaPlayer!=null;
    }
}

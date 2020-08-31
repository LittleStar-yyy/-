package com.example.lenovo.thedictionaryofsanguo;

/**
 * Created by lenovo on 2017/11/9.
 */

//public class MusicServer {
//}

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.lenovo.thedictionaryofsanguo.R;

public class MusicServer extends Service implements MediaPlayer.OnCompletionListener{

    private MediaPlayer mediaPlayer;
    private final IBinder binder = new AudioBinder();

    @Override
    public final IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCompletion(MediaPlayer player) {
        // TODO Auto-generated method stub
        stopSelf();//结束了，则结束Service
    }

    //在这里我们需要实例化MediaPlayer对象
    public void onCreate(){
        super.onCreate();
        //我们从raw文件夹中获取一个应用自带的mp3文件
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        mediaPlayer.setOnCompletionListener(this);
    }



    public int onStartCommand(Intent intent, int flags, int startId){
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();  
    }

    class AudioBinder extends Binder {

        //返回Service对象
        MusicServer getService(){
            return MusicServer.this;
        }
    }
}

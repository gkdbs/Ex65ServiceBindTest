package com.mrhi2020.ex65servicebindtest;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

//Service는 안드로이드의 4대 컴포넌트 [Manifest.xml에 등록]
public class MusicService extends Service {

    public MediaPlayer mp;

    //startService()로 서비스를 실행하면 자동으로 호출되는 메소드
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "start command", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    //bindService()를 호출하면 자동으로 실행되는 메소드
    //이 메소드에서 리턴되는 객체(Binder:연결자)에게
    // 이 MusicService객체의 참조값을 전달해주면 MainActivty에서
    // 이 전달된 참조값을 통해 제어가 가능해짐. 즉, 연결됨..
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //이 MusicService의 참조값을 가지고 MainActivity로 가는 객체 생성하여 리턴
        return new MyBinder();
    }

    //Binder 능력을 가진(상속받은) 객체만이 ServiceConnection을 통과할 수 있음
    class MyBinder extends Binder{
        //아웃터 클래스인 MusicService클래스 객체의 참조값을 리턴해주는 메소드
        public MusicService getServiceObject(){
            return MusicService.this;
        }
    }



    //MediaPlayer 객체의 제어 메소드들
    public void startMusic(){
        if(mp==null){
            mp= MediaPlayer.create(this, R.raw.kalimba);
            mp.setVolume(0.7f, 0.7f);
            mp.setLooping(true);
        }

        //플레이 중이 아니면.. 플레이 해라
        if( !mp.isPlaying() ) mp.start();
    }

    public void pauseMusic(){
        if(mp!=null && mp.isPlaying()) mp.pause();//일시정지
    }

    public void stopMusic(){
        if(mp!=null){
            mp.stop();
            mp.release();
            mp= null;
        }
    }

}

package com.mrhi2020.ex65servicebindtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //MusicService 참조변수
    MusicService musicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //이 액티비티가 화면에 보여질 때 MusiceServie 객체와 연결작업 수행
    @Override
    protected void onResume() {
        super.onResume();

        if(musicService==null){
            //MusicService 를 실행하고 연결하기!!!
            Intent intent= new Intent(this, MusicService.class);
            startService(intent);//Service객체가 없다면 만들고 이미 있다면 onStartCommand()만 발동

            //서비스를 실행해주면서 연결해주는 메소드
            //flag값 - 서비스객체가 만들어진 적이 없을때 어떻게 할거냐를 설정
            // BIND_AUTO_CREATE : 서비스객체를 만들고 연결해라..[연결할때 마다 새로운 Service객체를 새로 만들어버림]
            // 0 : 서비스객체를 새로만들지 말고 연결만 해라!!
            bindService(intent, connection, 0);

            //## bind는 연결된 상태여서 foreground 서비스가 아니어도
            // 강제로 종료시키지 않음.

        }
    }

    //액티비티에서는 Bind(연결)할 Service 객체와의 지하도같은 통로 객체가 필요함
    ServiceConnection connection= new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            //두번째 파라미터 Binder객체 : MusicService객체의 참조값을 가지고 이 통로를 통과해 온 객체
            // Service클래스의 onBind()메소드의 리턴된 객체
            MusicService.MyBinder myBinder= (MusicService.MyBinder)binder;
            musicService= myBinder.getServiceObject();

            Toast.makeText(MainActivity.this, "binded...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    public void clickStart(View view) {
        if(musicService != null) musicService.startMusic();
    }

    public void clickPause(View view) {
        if(musicService !=null) musicService.pauseMusic();
    }

    public void clickStop(View view) {
        if(musicService != null) {
            musicService.stopMusic();
            unbindService(connection); //서비스와의 연결 종료
            musicService=null;
        }

        //완전하게 서비스객체를 종료하기 위해
        Intent intent= new Intent(this, MusicService.class);
        stopService(intent);

        //액티비티도 종료
        finish();
    }
}
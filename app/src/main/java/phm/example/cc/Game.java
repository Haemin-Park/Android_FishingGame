package phm.example.cc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;

import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class Game extends Activity {

    GameView gv;

    public int rd=0;
    public int rdd=0;
    public int coin;

    public static Activity gactivity;

    private SensorManager sm;
    private int count=0; //화면 터치 카운트 변수
    private Display mDisplay; // 디스플레이 크기의 정보값을 받아옵니다.
    private SensorEventListener accL; // 가속도
    private SensorEventListener gyroL; // 회전
    private android.hardware.Sensor accSensor; // 가속도
    private android.hardware.Sensor gyroSensor; // 회전
    private WindowManager mWin; // 디스플레이의 context를 얻어온다. (방향 전환)
    MediaPlayer mediaPlayer_bg, mediaPlayer_fh_caught, mediaPlayer_ob_caught;
    private BackPressCloseHandler backKeyClickHandler;
    Integer[] num={0,0,0,0,0,0,0,0,0};
    SharedPreferences sf;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gactivity = Game.this;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        backKeyClickHandler = new BackPressCloseHandler(this);
        mWin = (WindowManager) getSystemService(WINDOW_SERVICE);
        mDisplay = mWin.getDefaultDisplay();
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroSensor = sm.getDefaultSensor(android.hardware.Sensor.TYPE_GYROSCOPE);
        gyroL = new Listener(); // 자이로 센서 리스너 인스턴스

        accSensor = sm.getDefaultSensor(android.hardware.Sensor.TYPE_GYROSCOPE);
        accL = new Listener(); // 가속도 센서 리스너 인스턴스

        setContentView(R.layout.main);
        gv=(GameView)findViewById(R.id.game);

        Button b1=(Button)findViewById(R.id.stop);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameView.StopGame();// ①
                finish();
                gv.mediaPlayer_bg.stop();

                SharedPreferences sharedPreferences = getSharedPreferences("sFile2", MODE_PRIVATE);
                //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("coin",gv.coin); // key, value를 이용하여 저장하는 형태
                //최종 커밋
                editor.commit();

            }
        });

        sf = getSharedPreferences("sFile2", MODE_PRIVATE);
        coin=sf.getInt("coin",0);;
        gv.coin=coin;

    }

    public void mOnPopupClick(View v){
        //데이터 담아서 팝업(액티비티) 호출
        Intent intent = new Intent(this, CollectPopup.class);
        intent.putExtra("data", "Test Popup");
        for(int i=0;i<gv.obstacle_inspec.length;i++){
            num[i]= sf.getInt("img"+i, 6);
            intent.putExtra("img"+i,num[i]);
        }
        startActivityForResult(intent, 1);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch(action) {
            case MotionEvent.ACTION_DOWN ://화면을 터치했을때
                if(count==0){
                    sm.registerListener(gyroL, gyroSensor, SensorManager.SENSOR_DELAY_FASTEST);// 가속도
                    break;
                }
                else if(count==1){
                    sm.registerListener(accL, accSensor, SensorManager.SENSOR_DELAY_FASTEST);
                    rdd=1;
                    count=2;
                    break;
                }
                else
                    break;
            case MotionEvent.ACTION_UP :    //화면을 터치했다 땠을때
                sm.unregisterListener(gyroL);
                Toast.makeText(getApplicationContext(), "선택 완료", Toast.LENGTH_LONG).show();
                sm.unregisterListener(accL);
                if(count==0)count=1;
                if(rdd==1){
                    rd=1;
                    gv.state(rd);
                }

                break;

            case MotionEvent.ACTION_MOVE :    //화면을 터치하고 이동할때
                break;
        }
        return super.onTouchEvent(event);
    }
    //----------------------------------------------------------
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sm != null) {
            sm.unregisterListener(gyroL);
            sm.unregisterListener(accL); //가속도 제어 추가
        }
    }
    //-------------------------수정된부분--------------------------------------------
    private class Listener implements SensorEventListener {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            ;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {

            if (event.sensor.getType() != Sensor.TYPE_GYROSCOPE)
                return;

            switch (mDisplay.getRotation()) {
                case Surface.ROTATION_0:
                    if(count==0)gv.move1(event.values[SensorManager.DATA_X],
                            event.values[SensorManager.AXIS_Y]);
                    else gv.move2(-event.values[SensorManager.DATA_X],
                            event.values[SensorManager.AXIS_Y]);
                    break;
                case Surface.ROTATION_90:
                    if(count==0)gv.move1(-event.values[SensorManager.AXIS_X],
                            event.values[SensorManager.AXIS_Y]);
                    else gv.move2(-event.values[SensorManager.DATA_X],
                            event.values[SensorManager.AXIS_Y]);
                    break;
                case Surface.ROTATION_270:
                    if(count==0)gv.move1(event.values[SensorManager.AXIS_X],
                            -event.values[SensorManager.AXIS_Y]);
                    else gv.move2(-event.values[SensorManager.DATA_X],
                            -event.values[SensorManager.AXIS_Y]);
                    break;
            }
        }

    }
    public class BackPressCloseHandler {
        private long backKeyClickTime = 0;
        private Activity activity;

        public BackPressCloseHandler(Activity activity){
            this.activity = activity;
        }

        public void onBackPressed(){
            if(System.currentTimeMillis() > backKeyClickTime + 2000){
                backKeyClickTime = System.currentTimeMillis();
                showToast();
                return;
            }

            if (System.currentTimeMillis() <= backKeyClickTime + 2000){
                activity.finish();
                gv.mediaPlayer_bg.stop();
                gv.StopGame();
            }
        }

        public void showToast(){
            Toast.makeText(activity, "뒤로 가기 버튼을 한 번 더 누르면 게임이 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed(){
        backKeyClickHandler.onBackPressed();
    }

}
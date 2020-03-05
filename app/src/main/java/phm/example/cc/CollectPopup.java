package phm.example.cc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import static android.graphics.Color.BLACK;

public class CollectPopup extends Activity {

    TextView txtText;
    Integer[] num={0,0,0,0,0,0,0,0,0};
    Integer[] fishID = {R.drawable.fish1, R.drawable.fish2, R.drawable.fish3, R.drawable.fish4, R.drawable.fish5, R.drawable.paperplain_dotted, R.drawable.shoes_dotted, R.drawable.toletpaper_dotted, R.drawable.mermaid};
    Integer[] layoutID={R.id.fish_image1, R.id.fish_image2, R.id.fish_image3, R.id.fish_image4, R.id.fish_image5,};
    Integer[] TextID={R.id.fish_name1, R.id.fish_name2, R.id.fish_name3, R.id.fish_name4, R.id.fish_name5,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        setContentView(R.layout.collection_set);

        //UI 객체생성
        //txtText = (TextView)findViewById(R.id.fish_name);
        Intent intent = getIntent();

        for(int i=0; i<fishID.length;i++) {
            num[i] = intent.getIntExtra("img" + i, 0);
        }
        for(int i=0; i<fishID.length;i++) {
            Log.i("에",i+"뭐가 저장되는거야"+num[i]);
            if(num[i]<5){
                    ImageView imgv = (ImageView) findViewById(layoutID[num[i]]);
                    imgv.setImageResource(fishID[num[i]]);
                    TextView txtText = (TextView) findViewById(TextID[num[i]]);
                    txtText.setTextColor(BLACK);
                    //String text=sprite_Thread[i].obstaclesID[i].toString();
                    //String data = intent.getStringExtra(text);
                    //txtText.setText(data);
                }
            else if(num[i]==8){
            TextView txtText1=(TextView) findViewById(R.id.sfish_name);
            ImageView img=(ImageView)findViewById(R.id.sfish_image);
            img.setImageResource(fishID[8]);
            txtText1.setTextColor(BLACK);
            txtText1.setText("#");


    }}}

    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기

        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
/*
    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

 */
}


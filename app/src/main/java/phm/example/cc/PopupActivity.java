package phm.example.cc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class PopupActivity extends Activity {

    TextView txtText;
    ImageView imgView;
    RodDraw ss;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_activity);

        //UI 객체생성
        txtText = (TextView)findViewById(R.id.txtText);
        imgView=(ImageView)findViewById(R.id.imageView);

        Integer[] fishID = {R.drawable.fish1, R.drawable.fish2, R.drawable.fish3, R.drawable.fish4, R.drawable.fish5,R.drawable.paperplain_dotted, R.drawable.shoes_dotted, R.drawable.toletpaper_dotted,R.drawable.mermaid,R.drawable.f_catch};

        //데이터 가져오기
        Intent intent = getIntent();
        String data = intent.getStringExtra("result");
        int img_num= intent.getIntExtra("img_num",0);
        txtText.setText(data);
        imgView.setImageResource(fishID[img_num]);

    }

    //확인 버튼 클릭
    public void mOnClose(View v){

        finish();
    }
    public void retry(View v){

        finish();
        Intent refresh = new Intent(this, Game.class);
        startActivity(refresh);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}

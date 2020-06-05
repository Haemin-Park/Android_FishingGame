package phm.example.cc;


import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class StorePopup extends Activity {

    Integer character=0;
    Integer[] buttonClick={1,0};
    Integer[] LayoutID={R.id.char1,R.id.char2};
    Integer coin;
    GameView ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        coin=intent.getIntExtra("coin",0);
        buttonClick[1]=intent.getIntExtra("btn",0);
        character=intent.getIntExtra("character",0);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.store);

        Button b1=(Button)findViewById(R.id.character1);
        final Button b2=(Button)findViewById(R.id.character2);

        final LinearLayout char1=(LinearLayout) findViewById(R.id.char1);
        final LinearLayout char2=(LinearLayout)findViewById(R.id.char2);

        if(buttonClick[1]==1) b2.setText("SOLD");

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buttonClick[1]==0){
                    if(coin<=0) Toast.makeText(getApplicationContext(), "코인이 부족합니다.", Toast.LENGTH_LONG).show();
                    else{
                        buttonClick[1]=1;
                        coin--;
                        Toast.makeText(getApplicationContext(), "캐릭터를 구매하였습니다.", Toast.LENGTH_LONG).show();
                        b2.setText("SOLD");}}
                else  Toast.makeText(getApplicationContext(), "이미 구매하셨습니다.", Toast.LENGTH_LONG).show();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "이미 구매하셨습니다.", Toast.LENGTH_LONG).show();
            }
        });
        char1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                character=0;
                char1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F0D8C3")));
                char2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            }
        });

        char2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buttonClick[1]==0)
                    Toast.makeText(getApplicationContext(), "캐릭터를 구매해야합니다.", Toast.LENGTH_LONG).show();
                else{
                    character=1;
                    char2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F0D8C3")));
                    char1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            }}
        });

        LinearLayout tint=(LinearLayout) findViewById(LayoutID[character]);
        tint.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F0D8C3")));
    }

    //확인 버튼 클릭
    public void sOnClose(View v){
        //데이터 전달하기
        //액티비티(팝업) 닫기
        Intent intent = new Intent();
        intent.putExtra("res", character);
        intent.putExtra("btn", buttonClick[1]);
        intent.putExtra("coin", coin);
        setResult(RESULT_OK,intent);
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

}


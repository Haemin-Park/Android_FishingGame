package phm.example.cc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Coin {
    final static int ENTER = 1;

    public int status;

    public int x, y;
    public int w, h;
    public boolean isDead;

    private int delay;

    private Random rnd = new Random();             // 난수

    public int screen_width;
    public int back_x = 0, back_y = 0;


    //--------------------------------
    // 생성자
    //--------------------------------
    public Coin(int screen_width) {
        this.screen_width = screen_width;
    }

    public void MakeCoin(int x, int y, int obstacle_num) {

        this.x = x;
        this.y = y;


        Bitmap coin= BitmapFactory.decodeResource(GameView.mContext.getResources(), R.drawable.f_catch);

        w = coin.getWidth() ;
        h = coin.getHeight() ;


        ResetCoin();
    }

    public void ResetCoin() {

        status = ENTER;
        isDead = false; //------------------------------------------------------------------------------------------------------------------------------------------------ 수정
    }

    public void Move() {
        if (isDead) return;
        switch (status) {
            case ENTER:
                EnterCoin();
                break;

        }
    }

    public void EnterCoin() {

        final int v_x, v_y;
        Random r_time = new Random(); //객체생성
        v_x = (r_time.nextInt(20)+5);
        v_y = (r_time.nextInt(60)+10);

        if (--delay >= 0) return;

        if (back_x == 0){//왼쪽에서 오른쪽으로 이동
            x += v_x;//속도 랜덤으로 하자
            if (x+w >= screen_width) {
                back_x = 1;
            }
        }else if(back_x == 1){
            x -= v_x;
            if (x <= 0) {
                back_x = 0;
            }
        }

        if (back_y == 0){//왼쪽에서 오른쪽으로 이동
            y -= v_y;//속도 랜덤으로 하자
            if (y <= 1300) {
                back_y = 1;
            }
        }else if(back_y == 1){
            y += v_y;
            if (y >= 1600) {
                back_y = 0;
            }
        }
    }
}
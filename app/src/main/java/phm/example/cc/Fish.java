package phm.example.cc;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Fish {
    final static int ENTER = 1;

    public int status;

    public int x, y;
    public int w, h;
    public boolean isDead;
    public boolean isWrongObstacle;

    public Bitmap fish;

    private int delay;

    private Random rnd = new Random();             // 난수

    public int screen_width,screen_height;
    public int back_x = 0, back_y = 0;
    public int obstacle_num;
    Integer[] obstaclesID = {R.drawable.fish1, R.drawable.fish2, R.drawable.fish3, R.drawable.fish4, R.drawable.fish5,
            R.drawable.paperplain_dotted, R.drawable.shoes_dotted, R.drawable.toletpaper_dotted};

    //--------------------------------
    // 생성자
    //--------------------------------
    public Fish(int screen_width, int screen_height) {
        this.screen_width = screen_width;
        this.screen_height = screen_height;
    }

    public void MakeFish(int x, int y, int obstacle_num) {

        this.x = x;
        this.y = y;
        this.obstacle_num = obstacle_num;

        fish = BitmapFactory.decodeResource(GameView.mContext.getResources(), obstaclesID[obstacle_num]);

        w = fish.getWidth() ;
        h = fish.getHeight() ;


        ResetFish();
    }

    public void ResetFish() {

        status = ENTER;
        isDead = false; //------------------------------------------------------------------------------------------------------------------------------------------------ 수정
        isWrongObstacle = false;
    }

    public void Move() {
        if (isDead) return;// 사망자는 자격 없음
        if (isWrongObstacle) return;//폭탄도 움직일 자격 X ------------------------------------------------------------------------------------------------------------------------------- 수정
        switch (status) {
            case ENTER:
                EnterFish();
                break;

        }
    }

    public void EnterFish() {

        final int v_x, v_y;
        Random r_time = new Random(); //객체생성
        v_x = (r_time.nextInt(10)+5);
        v_y = (r_time.nextInt(50)+10);

        if (--delay >= 0) return;// delay time이 끝나지 않았으면 대기

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
            if (y <= screen_height/11 * 5+100) {
                back_y = 1;
            }
        }else if(back_y == 1){
            y += v_y;
            if (y >= screen_height/11 * 9) {
                back_y = 0;
            }
        }

    }
}
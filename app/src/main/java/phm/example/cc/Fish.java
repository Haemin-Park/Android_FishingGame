package phm.example.cc;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.util.Random;

public class Fish {
    final static int ENTER = 1;                           // 캐릭터 입장

    public int status;                                        // 캐릭터의 상태(위의 1~7)

    public int x, y;                                          // 좌표
    public int w, h;                                          // 크기
    public boolean isDead;                               // 사망
    public boolean isWrongObstacle;

    private float sx, sy;                                    // 캐릭터 이동 속도

    public Bitmap fish,fish2;    // 16방향 이미지

    private int delay, dir, len;                              // 입장시 지연시간, 현재의 방향, 남은 거리

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
        //Log.i("전달받은 스크린 넓이","" + screen_width);
        // 내용 없음

    }

    //--------------------------------
    // Sprite 만들기
    //--------------------------------

    public void MakeFish(int x, int y, int obstacle_num) {

        this.x = x;
        this.y = y;
        this.obstacle_num = obstacle_num;

        fish = BitmapFactory.decodeResource(RodDraw.mContext.getResources(), obstaclesID[obstacle_num]);

        w = fish.getWidth() ;
        h = fish.getHeight() ;


        ResetSprite();
    }

    //--------------------------------
    // Reset Sprite
    //--------------------------------
    public void ResetSprite() {

        status = ENTER;                                                                     // 초기 상태는 입장
        isDead = false; //------------------------------------------------------------------------------------------------------------------------------------------------ 수정
        isWrongObstacle = false;
    }


    //--------------------------------
    // GetDir - 현위치의 방향과 거리
    //--------------------------------
    private void GetDir(int col) {
        dir = 1;                                       // 이동할 방향
        len = screen_width;                                     // 이동할 거리

        sx = 1;                     // 이동 속도  .......... ⑤
        sy = 1;

    }
    //--------------------------------
    // Move
    //--------------------------------
    public void Move() {
        if (isDead) return;// 사망자는 자격 없음
        if (isWrongObstacle) return;//폭탄도 움직일 자격 X ------------------------------------------------------------------------------------------------------------------------------- 수정
        switch (status) {
            case ENTER:             // 캐릭터 입장
                EnterSprite();
                break;

        }
    }

    //--------------------------------
    // Enter Sprite
    //--------------------------------
    public void EnterSprite() {

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
        //y += (int) (sy * 8);


        //len--;
        //if (len >= 0) return;            // 이동할 거리가 남았는가?
    }
}
package phm.example.cc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.Random;

public class SpecialFish {
    final static int ENTER = 1;                           // 캐릭터 입장

    public int status;                                        // 캐릭터의 상태(위의 1~7)

    public int x, y;                                          // 좌표
    public int w, h;                                          // 크기
    public boolean isDead;                               // 사망
    public boolean isWrongObstacle;

    public Bitmap fish;    // 16방향 이미지
    // 캐릭터의 종류와 번호

    private int delay;                              // 입장시 지연시간, 현재의 방향, 남은 거리

    public int screen_width, screen_height;
    public int back_x = 0, back_y = 0;

    Random r_time = new Random(); //객체생성

    //--------------------------------
    // 생성자
    //--------------------------------
    public SpecialFish(int screen_width, int screen_height) {
        this.screen_width = screen_width;
        this.screen_height = screen_height;
        //Log.i("전달받은 스크린 넓이","" + screen_width);
        // 내용 없음

    }

    public void MakeFish(int x, int y) {

        this.x = x;
        this.y = y;

        fish = BitmapFactory.decodeResource(RodDraw.mContext.getResources(), R.drawable.mermaid);

        w = fish.getWidth();
        h = fish.getHeight();


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
    // Move
    //--------------------------------
    public void Move() {
        if (isDead) return;// 사망자는 자격 없음
        if (isWrongObstacle)
            return;//폭탄도 움직일 자격 X ------------------------------------------------------------------------------------------------------------------------------- 수정
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
        v_y = (r_time.nextInt(50) + 10);
        v_x = (r_time.nextInt(10)+5);

        if (--delay >= 0) return;// delay time이 끝나지 않았으면 대기

        if (back_x == 0){//왼쪽에서 오른쪽으로 이동
            x += v_x * 5;//속도 랜덤으로 하자
            if (x + 300 >= screen_width) {
                back_x = 1;
            }
        }else if(back_x == 1){
            x -= v_x * 5;
            if (x <= 0) {
                back_x = 0;
            }
        }
        if (back_y == 0) {//왼쪽에서 오른쪽으로 이동
            y -= v_y * 5;//속도 랜덤으로 하자
            if (y <= 1300) {
                back_y = 1;
            }
        } else if (back_y == 1) {
            y += v_y * 5;
            if (y >= screen_height/7 * 5) {
                back_y = 0;
            }
        }
        Log.i("------------------------------------로그 x값","" + x + " back_x: " + back_x + " 넓이: " + screen_width + " x + w: " + x + w);

        //Log.i("--------------------------------------------------------------------------------------", "x: " + x);
    }
}

package phm.example.cc;

public class FishingFloat {
    public float x, y;                            // 위치
    public float w, h;                            // 폭과 높이

    //--------------------------------
    // 생성자
    //--------------------------------
    public FishingFloat(float x, float y) {                     // 초기 위치

        this.x = x;
        this.y = y;

        w = 25;           // 우주선의 폭과 높이
        h = 25;

    }

}
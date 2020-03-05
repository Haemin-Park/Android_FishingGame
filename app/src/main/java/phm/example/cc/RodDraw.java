package phm.example.cc;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import java.util.Random;

import static android.content.Context.WINDOW_SERVICE;


public class RodDraw extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder mHolder;

    static Context mContext;
    static GameThread mThread;
    static CheckTime timerThread;
    public int timer = 0;
    public int result;

    private int imageWidth;
    private int imageHeight;
    public float x;
    public float y;
    static int Width, Height;
    public float accX,accY;
    public int rd=0;
    public int num=0;
    Random r_time = new Random();

    int obstacle_w[]= {0, 0, 0, 0, 0};
    int obstacle_h[]={0, 0, 0, 0, 0};

    public int coin;
    int collision_stop[] = {0, 0, 0, 0, 0};
    int change_inspec[] = {0, 0, 0, 0, 0, 0, 0, 0};
    int obstacle_inspec[] = {0, 0, 0, 0, 0, 0, 0, 0, 8};//장애물 구별 만약 index 값이 5, 6, 7이면 장애물 --------------------------------------------------------------------------------------------------- 추가

    Integer[] obstaclesID = {R.drawable.fish1, R.drawable.fish2, R.drawable.fish3, R.drawable.fish4, R.drawable.fish5,
            R.drawable.paperplain_dotted, R.drawable.shoes_dotted, R.drawable.toletpaper_dotted};

    Bitmap[] imgfish;
    Bitmap[] imgc = new Bitmap[8];

    static Fish[] sprite_Thread;
    static SpecialFish specialFish;
    static Coin[] coin_Thread;
    public boolean isResult=false;
    public boolean isCoin=false;
    Bitmap specialFish_bitmap;


    public RodDraw(Context context, AttributeSet attrs) { //이미지 불러오기
        super(context, attrs);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        mHolder = holder;
        mContext = context;
        mThread = new GameThread(holder, context);      // Thread 만들기
        timerThread = new CheckTime();
        InitGame();
        MakeStage();
        setFocusable(true);// View가 Focus받기


    }
    private void InitGame() {

        Display display = ((WindowManager) mContext.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        Width = display.getWidth();
        Height = display.getHeight();

        //txt=(TextView)findViewById(R.id.coin);

        sprite_Thread = new Fish[5];
        specialFish = new SpecialFish(Width, Height);
        coin_Thread=new Coin[5];
        for(int i = 0; i < sprite_Thread.length; i++){
            
            sprite_Thread[i] = new Fish(Width,Height);

        }
        for(int i = 0; i < coin_Thread.length; i++){

            coin_Thread[i]=new Coin(Width);
        }

        imgfish = new Bitmap[8];
        for(int i = 0; i < imgfish.length; i++){
            int r_img_n_1;
            Random r_time = new Random(); //객체생성
            r_img_n_1 = r_time.nextInt(8);//이부분을 바꾸면 물고기 이외의 장애물 나옴

            imgfish[i] = BitmapFactory.decodeResource(RodDraw.mContext.getResources(), obstaclesID[r_img_n_1]);
            obstacle_inspec[i] = r_img_n_1;//--------------------------------------------------------------------------------------------------  for문부터 여기까지 추가

        }
        specialFish_bitmap = BitmapFactory.decodeResource(RodDraw.mContext.getResources(), R.drawable.mermaid);
        obstacle_inspec[imgfish.length - 1] = 8;

    }

    public static void MakeStage() {
        for(int i = 0; i < sprite_Thread.length ; i++){

            sprite_Thread[i].MakeFish((i)*200, Height/11*5 + i * 130, i);

        }

        for(int i = 0; i < coin_Thread.length ; i++){

            coin_Thread[i].MakeCoin((i)*200, 800+ i * 70, i);
        }

        specialFish.MakeFish(Width/2, Height/9 * 10);


    }
    //-------------------------------------
    //  SurfaceView가 생성될 때 실행되는 부분
    //-------------------------------------
    public void surfaceCreated(SurfaceHolder holder) {
        mThread.start();
        timerThread.start();// Thread 실행


    }

    //-------------------------------------
    //  SurfaceView가 바뀔 때 실행되는 부분
    //-------------------------------------
    public void surfaceChanged(SurfaceHolder arg0, int format, int width, int height) {



    }


    //--------------------------------------
    //    Surface가 삭제될 때 호출됨
    //--------------------------------------
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean done = true;
        while (done) {
            try {
                mThread.join();                            // 스레드가 현재 step을 끝낼 때 까지 대기
                done = false;
            } catch (InterruptedException e) {         // 인터럽트 신호가 오면?
                // 그 신호 무시 - 아무것도 않음
            }
        } // while



    } // surfaceDestroyed

    public static void StopGame() {
        mThread.StopThread();
    }

    public void move1(float mx, float my) {
        x -= (mx * 4f);
        y += (my * 4f);
        Log.i("viewHeight =", Height + "");
        Log.i("viewWidth =", Width + "");

        if (x < 0) {
            x = 0;
        } else if ((x + imageWidth) > Width) {
            x = Width - imageWidth;
        }

        if (Width/2-y<Width/10) {
            y= Width/2-Width/10;
        } else if ((Width/2-y)> Width-Width/10) {
            y = Width/2-Width+Width/10;
        }

        Log.i("SENSOR", "movie-[this.x]" + x + "[this.y]" + y + "[mx]" + mx
                + "[my]" + my);

    }

    public void move2(float mx, float my) {
        accY += (mx * 4f);

        Log.i("viewHeight =", Height + "");
        Log.i("viewWidth =", Width + "");

        if (accY < 0) {
            accY =0;
        } else if ((accY + Height/9*4) > Height) {
            accY = Height - Height/9*4;
        }

        Log.i("SENSOR", "movie-[this.x]" + x + "[this.y]" + accY+ "[mx]" + mx
                + "[my]" + my);

    }

    public void state(int rd_state) {

        rd=rd_state;

    }



    class GameThread extends Thread {
        boolean canRun = true;   // Thread 제어용
        boolean isWait = false;   // Thread 제어용

        //-------------------------------------
        //  생성자
        //-------------------------------------
        public GameThread(SurfaceHolder holder, Context context) {

            mHolder=holder;
        }

    public void CheckCollision() {


        float fx,fy,fw,fh;
        float cx,cy,cw,ch;
        float rx,ry,rw,rh;
        int count=0;


        if(rd==1) {
            // 모든 적기에 대해서 조사
            for (int i = 0; i < sprite_Thread.length; i++) {

                rx=Width/2-y-25;
                ry=Height/3+accY/6*5;
                rw=Width/2-y+25;
                rh=Height/3+accY/6*5-50;

                FishingFloat g=new FishingFloat(rx,ry);

                fx = sprite_Thread[i].x;
                fy = sprite_Thread[i].y;
                fw = obstacle_w[i];
                fh = obstacle_h[i];

                cx = coin_Thread[i].x;
                cy = coin_Thread[i].y;
                cw = coin_Thread[i].w;
                ch = coin_Thread[i].h;

                if (g.x <= fx && g.x + g.w >= fx || fx <= g.x && fx + fw >= g.x) {
                    if (g.y <= fy && g.y + g.h >= fy || fy <= g.y && fy + fh >= g.y) {
                        if(obstacle_inspec[i] <5){//물고기의 개수에 따라 변경하기--------------------------------------------------------------------------------------------- 이 부분 추가
                            sprite_Thread[i].isDead = true;
                            StopThread();
                            num=i;
                            isResult=true;
                        }else{
                            sprite_Thread[i].isWrongObstacle = true;//----------------------------------------------------------------------------------------------------------------------- 이 부분 추가
                            StopThread();
                            num=i;
                            isResult=true;
                        }
                    }
                }

                if (g.x <= cx && g.x + g.w >= cx || cx <= g.x && cx + cw >= g.x) {
                    if (g.y <= cy && g.y + g.h >= cy || cy <= g.y && cy + ch >= g.y) {

                            coin_Thread[i].isDead = true;
                            coin++;
                            StopThread();

                            isCoin=true;

                    }
                }

            }

            rx=Width/2-y-25;
            ry=Height/3+accY/6*5;
            rw=Width/2-y+25;
            rh=Height/3+accY/6*5-50;

            FishingFloat g=new FishingFloat(rx,ry);

            fx = specialFish.x;
            fy = specialFish.y;
            fw = specialFish.w;
            fh = specialFish.h;

            if (g.x <= fx && g.x + g.w >= fx || fx <= g.x && fx + fw >= g.x) {
                if (g.y <= fy && g.y + g.h >= fy || fy <= g.y && fy + fh >= g.y) {
                    specialFish.isDead = true;
                    StopThread();
                    num=8;
                    isResult=true;
                }
            }




        }

    }

        //-------------------------------------
        //  Move All
        //-------------------------------------
        public void MoveAll() {

            int r_width;
            r_width = r_time.nextInt(Width - 300);//이부분을 바꾸면 물고기 이외의 장애물 나옴


            for(int i = 0 ; i < sprite_Thread.length ; i++){
                sprite_Thread[i].Move();

            }
            for(int i = 0 ; i < coin_Thread.length ; i++){

                coin_Thread[i].Move();
            }
            if(timer >= 5 && timer <= 8){
                specialFish.Move();
                if (timer == 8){
                    timer = 0;
                    specialFish.y = Height/9 * 10;
                    specialFish.x = r_width;
                }
            }



        }
        //-------------------------------------
        //  DrawAll
        //-------------------------------------
        public void DrawAll(Canvas canvas) {
            Bitmap f_catch = BitmapFactory.decodeResource(RodDraw.mContext.getResources(), R.drawable.f_catch);
            Bitmap bump = BitmapFactory.decodeResource(RodDraw.mContext.getResources(), R.drawable.bump);
            for (int i = 0; i < sprite_Thread.length; i++) {
                if (sprite_Thread[i].back_x == 0 && change_inspec[i] == 1) {
                    final int r_img_n_1;
                    //객체생성
                    r_img_n_1 = r_time.nextInt(8);//이부분을 바꾸면 물고기 이외의 장애물 나옴
                    imgfish[i] = BitmapFactory.decodeResource(RodDraw.mContext.getResources(), obstaclesID[r_img_n_1]);

                    obstacle_inspec[i] = r_img_n_1;//-----------------------------------------------------------------------------------------------------------------------------------------------obstacle_inspec 부분 다 추가
                    change_inspec[i] = 0;
                }
                if (sprite_Thread[i].back_x == 1 && change_inspec[i] == 0)
                    change_inspec[i] = 1;


            }
            Paint paint = new Paint();
            for (int i = 0; i < sprite_Thread.length; i++) {

                if (sprite_Thread[i].isWrongObstacle) {
                    canvas.drawBitmap(bump, sprite_Thread[i].x, sprite_Thread[i].y, null);
                    sprite_Thread[i].isDead = true;//---------------------------------------------------------------------------------------------------------------------------------------- 빼도 됨
                } else {
                    if (!sprite_Thread[i].isDead) {//살아있으면

                        canvas.drawBitmap(imgfish[i], sprite_Thread[i].x, sprite_Thread[i].y, null);//변경된 x값과 y값에 물고기를 그려넣자(갱신)
                        //paint.setColor(Color.argb(60, 20, 20, 20));

                        //int obstacle_w, obstacle_h;
                        obstacle_w[i] = imgfish[i].getWidth();
                        obstacle_h[i] = imgfish[i].getHeight();
                        //canvas.drawRect(sprite_Thread[i].x, sprite_Thread[i].y, sprite_Thread[i].x + obstacle_w[i], sprite_Thread[i].y + obstacle_h[i], paint);

                    }
                    if (!specialFish.isWrongObstacle) {
                        if (!specialFish.isDead) {//살아있으면
                            //paint.setColor(Color.argb(60, 20, 20, 20));
                            //canvas.drawRect(specialFish.x, specialFish.y, specialFish.x + specialFish.w, specialFish.y + specialFish.h, paint);
                            canvas.drawBitmap(specialFish_bitmap, specialFish.x, specialFish.y, null);//변경된 x값과 y값에 물고기를 그려넣자(갱신)
                        }
                    }
                }

            }
            for (int i = 0; i < coin_Thread.length; i++) {
                if (!coin_Thread[i].isDead)//살아있으면
                    //if(sprite_Thread[i].back_x==0)
                    canvas.drawBitmap(f_catch, coin_Thread[i].x, coin_Thread[i].y, null);//변경된

            }
            canvas.drawBitmap(f_catch, Width - Width / 4, 50, null);

        }
//////////////////////////////////////////////////////////////////////////
        public void draw(Canvas canvas) { //이미지를 그려줌.

            Paint paint = new Paint();


            Bitmap imgBack = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bb);
            imgBack = Bitmap.createScaledBitmap(imgBack, Width, Height, true);
            canvas.drawBitmap(imgBack, 0, 0, null);

            Integer[] cID = {R.drawable.c1, R.drawable.c2, R.drawable.c3, R.drawable.c4, R.drawable.c5,
                    R.drawable.c6, R.drawable.c7, R.drawable.c8};


                imgc[0] = BitmapFactory.decodeResource(RodDraw.mContext.getResources(), cID[result]);
                //imgc[0] = Bitmap.createScaledBitmap(imgc[0], Width/7, Height/9, true);
                canvas.drawBitmap(imgc[0], Width/2-20, 11*Height/35+30, null);


            paint.setColor(Color.BLACK);

            paint.setStrokeWidth(8);
            canvas.drawLine( Width/2, (Height/5)*2,Width/2-y, Height/4, paint);
            paint.setStrokeWidth(3);
            canvas.drawLine(Width/2-y, Height/4, Width/2-y, Height/3, paint);
//얘 하나 추가--------------------------------------낚시줄 위아래
            canvas.drawLine(Width/2-y, Height/4, Width/2-y, Height/3+accY/6*5, paint);
//-----------------------------------------------------------------------

            paint.setColor(Color.RED);
            canvas.drawRect(Width/2-y-25/2, Height/3+accY/6*5, Width/2-y+25/2,Height/3+accY/6*5-50/2,paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(90);
            AssetManager am = getResources().getAssets() ;
            Typeface font = Typeface.createFromAsset(am, "slkscr.ttf");
            paint.setTypeface(font);

            canvas.drawText(coin+" ",Width-Width/7,125,paint);

        }

        //-------------------------------------
        //  스레드 본체
        //-------------------------------------
        public void run() {
            Canvas canvas=null;

            while (canRun) {

                canvas = mHolder.lockCanvas();

                try {
                    synchronized (mHolder) {

                        CheckCollision();
                        MoveAll();// 모든 캐릭터 이동
                        draw(canvas);
                        DrawAll(canvas);// Canvas에 그리기

                    } // sync
                } finally {
                    if (canvas != null)
                        mHolder.unlockCanvasAndPost(canvas);
                } // try

                synchronized (this) {
                    if (isWait)             // Pause 모드이면
                        try {
                            wait();       // 스레드 대기
                        } catch (Exception e) {
                            // nothing
                        }
                } // sync

            } // while
        } // run

        //-------------------------------------
        //  스레드 완전 정지
        //-------------------------------------
        public void StopThread() {
            canRun = false;
            synchronized (this) {
                this.notify();                   // 스레드에 통지
            }
        }

        //-------------------------------------
        //  스레드 일시정지 / 재기동
        //-------------------------------------
        public void PauseNResume(boolean value) {
            isWait = value;
            synchronized (this) {
                this.notify();               // 스레드에 통지
            }
        }


    } // GameThread 끝

    public class CheckTime extends Thread{
        CheckTime(){

        }

        public void run(){
            while(true){
                try {
                    // 스레드에게 수행시킬 동작들 구현
                    Thread.sleep(1000); //Thread를 잠재운다
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                timer ++;//1초마다 증가
            }
        }
    }


}

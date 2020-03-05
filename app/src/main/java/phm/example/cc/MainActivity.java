package phm.example.cc;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.Calendar;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageView iv_moon=(ImageView)findViewById(R.id.fish);
        Animation anim_moon= AnimationUtils.loadAnimation(this,R.anim.fish);
        iv_moon.startAnimation(anim_moon);


        ImageView iv_bird=(ImageView)findViewById(R.id.c);
        AnimationDrawable animation_drawable=new AnimationDrawable();
        BitmapDrawable frame1=(BitmapDrawable)getResources().getDrawable(R.drawable.cc1);
        BitmapDrawable frame2=(BitmapDrawable)getResources().getDrawable(R.drawable.cc2);
        BitmapDrawable frame3=(BitmapDrawable)getResources().getDrawable(R.drawable.cc3);
        BitmapDrawable frame4=(BitmapDrawable)getResources().getDrawable(R.drawable.cc4);

        animation_drawable.addFrame(frame1,200);
        animation_drawable.addFrame(frame2,200);
        animation_drawable.addFrame(frame3,200);
        animation_drawable.addFrame(frame4,200);
        iv_bird.setBackgroundDrawable(animation_drawable);
        animation_drawable.start();

        Animation anim_bird=AnimationUtils.loadAnimation(this,R.anim.ch);
        iv_bird.startAnimation(anim_bird);

        ImageButton b1=(ImageButton)findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this, Game.class);
                startActivity(intent);
            }
        });

    }

}


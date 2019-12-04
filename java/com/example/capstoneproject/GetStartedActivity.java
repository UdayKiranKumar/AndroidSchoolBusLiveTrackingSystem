package com.example.capstoneproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GetStartedActivity extends AppCompatActivity {

    TextView tvwel,tvRed;
  FloatingActionButton fab_gs;
  ImageView imageView,imageView1,imageView2,imageView3;
  Animation frombottom,fromtop,fromleft,fromright;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        imageView = (ImageView)findViewById(R.id.imageuday);
        imageView1 = (ImageView)findViewById(R.id.boyrun);
        imageView2 = (ImageView)findViewById(R.id.mvbus);
        imageView3 = (ImageView)findViewById(R.id.boymv);
        tvRed = (TextView)findViewById(R.id.tvRed);
        tvwel = (TextView)findViewById(R.id.tvwel);

        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);
        fromtop = AnimationUtils.loadAnimation(this,R.anim.fromtop);
        fromleft = AnimationUtils.loadAnimation(this,R.anim.fromleft);
        fromright = AnimationUtils.loadAnimation(this,R.anim.fromright);

        tvwel.setAnimation(fromtop);
        imageView.setAnimation(fromright);
        imageView1.setAnimation(fromleft);
        imageView2.setAnimation(fromright);
        imageView3.setAnimation(fromright);
        tvRed.setAnimation(frombottom);

        fab_gs = (FloatingActionButton) findViewById(R.id.fab_gs);
        fab_gs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(GetStartedActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
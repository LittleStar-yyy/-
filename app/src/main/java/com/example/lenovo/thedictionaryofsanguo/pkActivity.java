package com.example.lenovo.thedictionaryofsanguo;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ClipDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.Map;


/**
 * Created by zhaoyuying on 2017/11/26.
 */

public class pkActivity extends AppCompatActivity {

    private ImageView figure1, figure2;
    private TextView tips_1, tips_2;
    int life1 = 0, force1 = 0, life2 = 0, force2 = 0;
    int process1 = 0, process2 = 0;
    ClipDrawable clip1, clip2;

    void init()
    {
        ImageView iv = (ImageView) findViewById(R.id.blood1_clip);
        clip1 = (ClipDrawable)iv.getDrawable();
        ImageView iv2 = (ImageView) findViewById(R.id.blood2_clip);
        clip2 = (ClipDrawable)iv2.getDrawable();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case 1:
                    clip1.setLevel(process1);//掉血process1
                case 2:
                    clip2.setLevel(process2);//掉血process2
            }
        }
    };

    private Handler handler2 = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case 1:
                    clip1.setLevel(process1);//掉血process1
                case 2:
                    clip2.setLevel(process2);//掉血process2
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pk);
        getSupportActionBar().hide();// 去掉标题栏

        init();

        // 设置图片透明度
        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.pk_layout);//找到控件
        constraintLayout.getBackground().setAlpha(50);


        // 提示信息
        tips_1 = (TextView) findViewById(R.id.tips_1);
        int vi_1 = tips_1.getVisibility();
        if(vi_1 == View.INVISIBLE)vi_1 = View.VISIBLE;
        tips_1.setVisibility(vi_1);
        tips_2 = (TextView) findViewById(R.id.tips_2);
        int vi_2 = tips_2.getVisibility();
        if(vi_2 == View.INVISIBLE)vi_2 = View.VISIBLE;
        tips_2.setVisibility(vi_2);
        tips_1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(pkActivity.this, activity_pk_figures.class);
                startActivityForResult(intent, 0);
            }
        });
        tips_2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(pkActivity.this, activity_pk_figures.class);
                startActivityForResult(intent, 1);
            }
        });

        // 设置图片
        // 获得动画的变量
        figure1 = (ImageView) findViewById(R.id.figure_1);
        final ImageView figure_1bk = (ImageView) findViewById(R.id.figure_1bk);
        final Animation translateAnimation = AnimationUtils.loadAnimation(this, R.anim.figure1_trasition);
        figure2 = (ImageView) findViewById(R.id.figure_2);
        final ImageView figure_2bk = (ImageView) findViewById(R.id.figure_2bk);
        final Animation translateAnimation_2 = AnimationUtils.loadAnimation(this, R.anim.figure2_trasition);
        figure1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(pkActivity.this, activity_pk_figures.class);
                startActivityForResult(intent, 2);
            }
        });
        figure2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent(pkActivity.this, activity_pk_figures.class);
                startActivityForResult(intent, 3);
            }
        });
        // bubble
        final TextView bubble1 = (TextView) findViewById(R.id.bubble);
        int bu_1 = bubble1.getVisibility();
        if(bu_1 == View.VISIBLE)bu_1 = View.INVISIBLE;
        bubble1.setVisibility(bu_1);
        final TextView bubble2 = (TextView) findViewById(R.id.bubble2);
        int bu_2 = bubble2.getVisibility();
        if(bu_2 == View.VISIBLE)bu_2 = View.INVISIBLE;
        bubble2.setVisibility(bu_2);
        final Animation translateAnimation_3 = AnimationUtils.loadAnimation(this, R.anim.bubble_alpha);
        // 设置监听器
        ImageView pk = (ImageView) findViewById(R.id.text_button);
        pk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // 判断血量
                if (process1 <= 10000 && process2 <= 10000) {
                    // 点击图片
                    // 设置动画，动画结束需要3s
                    figure1.startAnimation(translateAnimation);
                    figure_1bk.startAnimation(translateAnimation);
                    figure2.startAnimation(translateAnimation_2);
                    figure_2bk.startAnimation(translateAnimation_2);
                    // 隐藏
                    int bu_1 = bubble1.getVisibility();
                    if(bu_1 == View.INVISIBLE)bu_1 = View.VISIBLE;
                    bubble1.setVisibility(bu_1);
                    int bu_2 = bubble2.getVisibility();
                    if(bu_2 == View.INVISIBLE)bu_2 = View.VISIBLE;
                    bubble2.setVisibility(bu_2);
                    // 血量减少
                    if(force1 < force2)
                    {
                        if(process1 < 2000){
                            bubble1.setText(R.string.dying_1);
                            bubble2.setText(R.string.success_1);
                        }
                        else if(process1 < 4000) {
                            bubble1.setText(R.string.dying_2);
                            bubble2.setText(R.string.success_2);
                        }
                        else if(process1 < 6000) {
                            bubble1.setText(R.string.dying_3);
                            bubble2.setText(R.string.success_3);
                        }
                        else if(process1 < 8000) {
                            bubble1.setText(R.string.dying_4);
                            bubble2.setText(R.string.success_4);
                        }
                        else {
                            bubble1.setText(R.string.dying_5);
                            bubble2.setText(R.string.success_5);
                        }
                        bubble1.startAnimation(translateAnimation_3);
                        bubble2.startAnimation(translateAnimation_3);
                        // 第一个人物掉血量为 force2-force1
                        final int lose = (int) ((force2 - force1)*1.0/(life1*1.0) * 10000);

                        Thread thread = new Thread(new Runnable(){
                            @Override
                            public void run(){
                                int cnt = 0;
                                for(int m=0; m<lose; m++) {
                                    cnt++;
                                    if(cnt == 50){
                                        cnt = 0;
                                        process1 += 50;
                                        handler.sendEmptyMessage(1);
                                        try{
                                            Thread.sleep(36);
                                        }catch (InterruptedException e){
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        });
                        thread.start();

                    }
                    else if(force1 > force2)
                    {
                        if(process2 < 2000){
                            bubble2.setText(R.string.dying_1);
                            bubble1.setText(R.string.success_1);
                        }
                        else if(process2 < 4000) {
                            bubble2.setText(R.string.dying_2);
                            bubble1.setText(R.string.success_2);
                        }
                        else if(process2 < 6000) {
                            bubble2.setText(R.string.dying_3);
                            bubble1.setText(R.string.success_3);
                        }
                        else if(process2 < 8000) {
                            bubble2.setText(R.string.dying_4);
                            bubble1.setText(R.string.success_4);
                        }
                        else {
                            bubble2.setText(R.string.dying_5);
                            bubble1.setText(R.string.success_5);
                        }
                        bubble1.startAnimation(translateAnimation_3);
                        bubble2.startAnimation(translateAnimation_3);

                        // 第一个人物掉血量为 force2-force1
                        final int lose = (int) ((force1 - force2)*1.0/(life2*1.0) * 10000);

                        Thread thread = new Thread(new Runnable(){
                            @Override
                            public void run(){
                                int cnt = 0;
                                for(int m=0; m<lose; m++) {
                                    cnt++;
                                    if(cnt == 50){
                                        cnt = 0;
                                        process2 += 50;
                                        handler.sendEmptyMessage(2);
                                        try{
                                            Thread.sleep(36);
                                        }catch (InterruptedException e){
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        });
                        thread.start();

                    }
                    else // 相等
                    {
                        if(process1 > 7000 && process2 < 10000)bubble1.setText(R.string.let);
                        else if(process1 < 10000 && process2 >= 10000)bubble1.setText(R.string.success_1);
                        else bubble1.setText(String.valueOf(-1*force1));
                        bubble1.startAnimation(translateAnimation_3);
                        if(process2 > 7000 && process1 < 10000)bubble2.setText(R.string.let);
                        else if(process2 < 10000 && process1 >= 10000)bubble2.setText(R.string.success_1);
                        else bubble2.setText(String.valueOf(-1*force2));
                        bubble2.startAnimation(translateAnimation_3);

                        // 第一个人物掉血量为 force2-force1
                        final int lose1 = (int) (force1*1.0/(life1*1.0) * 10000);
                        final int lose2 = (int) (force2*1.0/(life2*1.0) * 10000);

                        Thread thread = new Thread(new Runnable(){
                            @Override
                            public void run(){
                                int cnt = 0;
                                int max = 0;
                                if(lose1 != lose2)
                                {
                                    if(lose1 < lose2) max = lose2;
                                    else if(lose1 > lose2) max = lose1;

                                    for(int m=0; m<max; m++) {
                                        cnt++;
                                        if(cnt == 50){
                                            cnt = 0;
                                            if(m < lose1){
                                                process1 += 50;
                                                handler.sendEmptyMessage(1);
                                            }
                                            if(m < lose2){
                                                process2 += 50;
                                                handler.sendEmptyMessage(2);
                                            }
                                            try{
                                                Thread.sleep(36);
                                            }catch (InterruptedException e){
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    for(int m=0; m<lose1; m++) {
                                        cnt++;
                                        if(cnt == 50){
                                            cnt = 0;
                                            process1 += 50;
                                            process2 += 50;
                                            handler.sendEmptyMessage(1);
                                            handler.sendEmptyMessage(2);
                                            try{
                                                Thread.sleep(36);
                                            }catch (InterruptedException e){
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }

                            }
                        });
                        thread.start();
                    }
                }
                else{
                    Toast.makeText(pkActivity.this, R.string.tip, Toast.LENGTH_SHORT).show();
                }

            }
        });

        ImageView mBack = (ImageView) findViewById(R.id.back);
        // 监听
        mBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                finish();//finish()结束该Activity
            }
        });


    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle extras = data.getExtras();
        String photo = extras.getString("photo");
        String flag = extras.getString("flag");
        String name = extras.getString("name");
        final String life = extras.getString("life");
        final String force = extras.getString("force");

        if(requestCode == 0 || requestCode == 2)
        {
            if(resultCode == RESULT_OK)
            {
                if(requestCode == 2){
                    process1 = 0;
                    handler.sendEmptyMessage(1);
                }
                force1 = Integer.parseInt(force);
                life1 = Integer.parseInt(life);
                // 将 tips_1 隐藏
                int vi = tips_1.getVisibility();
                if(vi == View.VISIBLE)vi = View.INVISIBLE;
                tips_1.setVisibility(vi);
                // 设置图片
                if(flag != null && flag.equals("0"))
                {
                    int photo_id;
                    if(photo != null) {
                        photo_id = getResources().getIdentifier(photo.substring(9, photo.length()), "mipmap", getPackageName());
                        figure1.setImageResource(photo_id);
                    }
                }
                else if (flag != null && flag.equals("1"))
                {
//                    String filePath = s.get("photo");// “获取到的照片的本地绝对路径”
                    if(photo != null){
                        File file = new File(photo);
                        if(file.exists()){
                            figure1.setImageURI(Uri.fromFile(file));
                        }
                    }
                }
                // 设置姓名
                TextView figure_1name = (TextView) findViewById(R.id.figure_1name);
                figure_1name.setText(name);
                // 设置生命值
                TextView figure_1life = (TextView) findViewById(R.id.figure_1life);
                figure_1life.setText(life);
                // 设置武力值
                TextView figure_1force = (TextView) findViewById(R.id.figure_1force);
                figure_1force.setText(force);
            }

        }
        else if(requestCode == 1 || requestCode == 3)
        {
            if(resultCode == RESULT_OK)
            {
                if(requestCode == 3) {
                    process2 = 0;
                    handler.sendEmptyMessage(2);
                }
                force2 = Integer.parseInt(force);
                life2 = Integer.parseInt(life);

                // 将 tips_2 隐藏
                int vi = tips_2.getVisibility();
                if(vi == View.VISIBLE)vi = View.INVISIBLE;
                tips_2.setVisibility(vi);
                // 设置图片
                if(flag != null && flag.equals("0"))
                {
                    int photo_id;
                    if(photo != null) {
                        photo_id = getResources().getIdentifier(photo.substring(9, photo.length()), "mipmap", getPackageName());
                        figure2.setImageResource(photo_id);
                    }
                }
                else if (flag != null && flag.equals("1"))
                {
//                    String filePath = s.get("photo");// “获取到的照片的本地绝对路径”
                    if(photo != null){
                        File file = new File(photo);
                        if(file.exists()){
                            figure2.setImageURI(Uri.fromFile(file));
                        }
                    }
                }
                // 设置姓名
                TextView figure_2name = (TextView) findViewById(R.id.figure_2name);
                figure_2name.setText(name);
                // 设置生命值
                TextView figure_2life = (TextView) findViewById(R.id.figure_2life);
                figure_2life.setText(life);
                // 设置武力值
                TextView figure_2force = (TextView) findViewById(R.id.figure_2force);
                figure_2force.setText(force);

            }

        }



    }



}


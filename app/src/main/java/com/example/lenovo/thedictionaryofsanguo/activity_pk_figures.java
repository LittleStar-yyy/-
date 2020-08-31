package com.example.lenovo.thedictionaryofsanguo;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

/**
 * Created by zhaoyuying on 2017/11/28.
 */

public class activity_pk_figures extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();// 去掉标题栏
        setContentView(R.layout.activity_pk_figures);

        Log.d("xiaoru11","dghs");

        // 获得人物信息：生命值和武力值
        // 创建数据库
        DataBaseHelper dbHelper = new DataBaseHelper(this);
        // 建立新表，调用getReadableDatabase()或getWritableDatabase()才算真正创建或打开数据库
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
        // recyclerView
        final List<Map<String,String>> recyclerView_data = dbHelper.GetPkInformation();
        dbHelper.close();//关闭数据库
        // 设置recyclerView的显示方式
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
        final CommonAdapter commonAdapter = new CommonAdapter<Map<String,String>>(this, R.layout.person_item, recyclerView_data) {
            @Override
            public void convert(MyViewHolder holder, Map<String, String> s) {

                CircleImageView photo = holder.getView(R.id.photo);
                if(s.get("flag").equals("0"))
                {
                    String id = s.get("photo");
                    int photo_id = getResources().getIdentifier(id.substring(9,id.length()), "mipmap", getPackageName());
                    photo.setImageResource(photo_id);
//                    Log.d("hhh","照片设置成功");
                }
                else{
                    String filePath = s.get("photo");// “获取到的照片的本地绝对路径”
                    File file = new File(filePath);
                    if(file.exists()){
                        photo.setImageURI(Uri.fromFile(file));
                    }
                }
                TextView name = holder.getView(R.id.name);
                name.setText("英雄："+s.get("name")+"");
                TextView life = holder.getView(R.id.sexuality);
                life.setText("生命值："+s.get("life")+"");
                TextView force = holder.getView(R.id.birthday);
                force.setText("武力值："+s.get("force")+"");
            }
        };
        commonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

                Map<String,String> tem = recyclerView_data.get(position);
                Intent intent = new Intent();
                intent.putExtra("photo",tem.get("photo"));
                intent.putExtra("flag",tem.get("flag"));
                intent.putExtra("name",tem.get("name"));
                intent.putExtra("life",tem.get("life"));
                intent.putExtra("force",tem.get("force"));
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onLongClick(int position) {
                // do nothing
            }
        });
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(commonAdapter);
        animationAdapter.setDuration(1000);
        mRecyclerView.setAdapter(animationAdapter);
        mRecyclerView.setItemAnimator(new OvershootInLeftAnimator());
    }
}
package com.example.lenovo.thedictionaryofsanguo;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import android.widget.ImageView;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/11/26.
 */

public class DetailLayout extends Activity {

    private ImageButton ib = null;
    private ImageView iv = null;
    private String fileName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_layout);
        //更改字体
        TextView tv = (TextView)findViewById(R.id.name);
        tv.setTypeface(Typeface.createFromAsset(getAssets(),"sssss.ttf"));
        tv.setEnabled(false);
        TextView tv2 = (TextView)findViewById(R.id.introduction);
        tv2.setTypeface(Typeface.createFromAsset(getAssets(),"sssss.ttf"));
        tv2.setEnabled(false);
        ib = findViewById(R.id.edit);


        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        CircleImageView circleImageView = findViewById(R.id.cv_1);
//        String test = "/storage/emulated/0/Pictures/1511171950411.jpg";
//        circleImageView = (CircleImageView) findViewById(R.id.picture);
//        File file = new File(test);
//        if(file.exists()){
//            Log.d("asdfasdf",file.getPath());
//            Bitmap bmp = BitmapFactory.decodeFile(test);
//            circleImageView.setImageBitmap(bmp);
//        }
        TextView name = findViewById(R.id.name);
        TextView sex = findViewById(R.id.sex);
        TextView time = findViewById(R.id.time);
        TextView place = findViewById(R.id.place);
        TextView introduction = findViewById(R.id.introduction);


        Bundle bundle = this.getIntent().getExtras();
        final int ID = bundle.getInt("ID");
        // 创建数据库
        DataBaseHelper dbHelper = new DataBaseHelper(this);
        // 建立新表，调用getReadableDatabase()或getWritableDatabase()才算真正创建或打开数据库
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
        // 利用游标获得数据
        final List<Map<String, String>> data_6 = dbHelper.GetAllInformationFromId(ID);

        name.setText(data_6.get(0).get("name"));
        sex.setText(data_6.get(0).get("sex"));
        time.setText(data_6.get(0).get("birth_to_death"));
        place.setText(data_6.get(0).get("hometown"));
        introduction.setText(data_6.get(0).get("introduction"));
        int flag=Integer.parseInt(data_6.get(0).get("flag"));
        if(flag==0){
            int imagenum = getResources().getIdentifier(data_6.get(0).get("photo").substring(9,data_6.get(0).get("photo").length()),"mipmap",getPackageName());
            circleImageView.setImageResource(imagenum);
        }else if(flag==1){
            String filePath = data_6.get(0).get("photo");
            File file = new File(filePath);
            if(file.exists()){
                circleImageView.setImageURI(Uri.fromFile(file));
            }
        }
        dbHelper.close();


        //点击编辑按钮
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailLayout.this,Update_Information.class);
                Bundle bundle = new Bundle();
                bundle.putString("update","update");//extras根据改页面对应的人物信息进行修改
                bundle.putInt("myid",ID);
                intent.putExtras(bundle);
                startActivityForResult(intent,4);
                finish();
            }
        });
    }

}


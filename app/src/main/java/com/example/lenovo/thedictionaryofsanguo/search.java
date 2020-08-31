package com.example.lenovo.thedictionaryofsanguo;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2017/11/25.
 */

public class search extends Activity {


    ConstraintLayout constraintLayout;

    private int editStart;//光标开始位置
    private int editEnd;//光标结束位置
    String name_1="";
    String ID1="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        constraintLayout = findViewById(R.id.search_result);
        final EditText editText = findViewById(R.id.search_mes);
        final ImageButton imageButton = findViewById(R.id.delete);//删除键

        constraintLayout.setVisibility(View.INVISIBLE);
        imageButton.setVisibility(View.INVISIBLE);
        ImageButton back = findViewById(R.id.search_back);
        //返回键的点击作用
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        //删除键的点击作用
        imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                editText.setText("");
            }
        });



        editText.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                imageButton.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
            public void afterTextChanged(Editable s){
                    editStart = editText.getSelectionStart();
                    editEnd = editText.getSelectionEnd();
                    Log.d("xiaoru","start "+editStart+" end "+editEnd);
                    if(editEnd!=0){
                        imageButton.setVisibility(View.VISIBLE);
                    }
             }
        });

        final DataBaseHelper dbHelper = new DataBaseHelper(this);
        // 建立新表，调用getReadableDatabase()或getWritableDatabase()才算真正创建或打开数据库
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();

        //点击搜索按钮，出现搜索结果。
        ImageButton search = findViewById(R.id.search_search);
        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                name_1 = editText.getText().toString();
                List<Map<String, String>> data_2 = dbHelper.GetItemFromName(name_1);
                if(data_2.size()==0){
                    Toast.makeText(getApplication(),"没有找到相关内容",Toast.LENGTH_SHORT).show();
                }else{
                    ID1 = data_2.get(0).get("Id");
                    CircleImageView cv = findViewById(R.id.photo);
                    int imagenum = getResources().getIdentifier(data_2.get(0).get("photo").substring(9,data_2.get(0).get("photo").length()),"mipmap",getPackageName());
                    cv.setImageResource(imagenum);
                    TextView name = findViewById(R.id.name);
                    name.setText(data_2.get(0).get("name"));
                    TextView sex = findViewById(R.id.sexuality);
                    sex.setText(data_2.get(0).get("sex"));
                    TextView birthday1 = findViewById(R.id.birthday);
                    birthday1.setText(data_2.get(0).get("country"));
                    constraintLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        dbHelper.close();


        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(search.this, DetailLayout.class);
                Bundle bundle = new Bundle();
                int ID=Integer.parseInt(ID1);
                bundle.putInt("ID", ID);
                intent.putExtras(bundle);
                startActivityForResult(intent, 3);
            }
        });

    }

}

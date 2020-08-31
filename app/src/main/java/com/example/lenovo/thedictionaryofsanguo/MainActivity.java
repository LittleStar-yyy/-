package com.example.lenovo.thedictionaryofsanguo;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

public class MainActivity extends AppCompatActivity {

    int flag=0;
    RecyclerView recyclerView;
    CommonAdapter commonAdapter;
    private MusicServer musicServer;

    //使用ServiceConnection来监听Service状态的变化
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            musicServer = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            //这里我们实例化audioService,通过binder来实现
            musicServer = ((MusicServer.AudioBinder)binder).getService();

        }
    };


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE" };


    List<Map<String, String>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);



        final FloatingActionButton button = (FloatingActionButton) findViewById(R.id.play);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MusicServer.class);
                if (flag == 0) {
                    startService(intent);
                    bindService(intent, conn, Context.BIND_AUTO_CREATE);
                    flag = 1;
                    button.setImageResource(R.mipmap.start);
                } else {
                    unbindService(conn);
                    stopService(intent);
                    flag = 0;
                    button.setImageResource(R.mipmap.stop);
                }
            }
        });

        ImageButton Pk = (ImageButton)findViewById(R.id.PK);
        Pk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(MainActivity.this, pkActivity.class);
                startActivity(i);
            }
        });


        //查找按钮的点击事件
        final ImageButton search = (ImageButton) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, search.class);
                Log.d("xiaoru", "hsgdhdvh");
                startActivityForResult(i, 5);
            }
        });

        //添加按钮的点击事件
        final ImageButton add = (ImageButton) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Update_Information.class);
                Bundle bundle = new Bundle();
                bundle.putString("Insert","Insert");
                i.putExtras(bundle);
                startActivityForResult(i, 6);
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        // 创建数据库
        final DataBaseHelper dbHelper = new DataBaseHelper(this);
        // 建立新表，调用getReadableDatabase()或getWritableDatabase()才算真正创建或打开数据库
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
        // 利用游标获得数据
        data = dbHelper.getAllTests();
        dbHelper.close();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commonAdapter = new CommonAdapter<Map<String, String>>(this, R.layout.person_item, data) {
            @Override
            public void convert(MyViewHolder holder, Map<String, String> s) {
                //Log.d("xiaoru9", "sdyyt "+Integer.parseInt(s.get("Id")));
                TextView name1 = holder.getView(R.id.name);
                name1.setText(s.get("name"));
                TextView sexuality = holder.getView(R.id.sexuality);
                sexuality.setText(s.get("sex"));
                TextView birthday1 = holder.getView(R.id.birthday);
                birthday1.setText(s.get("country"));
                int flag=Integer.parseInt(s.get("flag"));
                Log.d("flag","flag: "+flag);
                if(flag==0){
                    CircleImageView photo1 = holder.getView(R.id.photo);
                    int imagenum = getResources().getIdentifier(s.get("photo").substring(9,s.get("photo").length()),"mipmap",getPackageName());
                    photo1.setImageResource(imagenum );
                }else if(flag==1){
                    CircleImageView photo1 = holder.getView(R.id.photo);
                    String filePath = s.get("photo");
                    File file = new File(filePath);
                    if(file.exists()){
                        photo1.setImageURI(Uri.fromFile(file));
                    }
                }
            }
        };
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(commonAdapter);
        animationAdapter.setDuration(1000);
        recyclerView.setAdapter(animationAdapter);
        recyclerView.setItemAnimator(new OvershootInLeftAnimator());
        commonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, DetailLayout.class);
                Bundle bundle = new Bundle();
                int ID=Integer.parseInt(data.get(position).get("Id"));
                bundle.putInt("ID", ID);
                intent.putExtras(bundle);
                startActivityForResult(intent, 3);
            }

            @Override
            public void onLongClick(final int position) {
                final int id = Integer.parseInt(data.get(position).get("Id"));
                final String name_1= data.get(position).get("name");


                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View view = View.inflate(MainActivity.this,R.layout.my_alert_layout, null);

                builder.setView(view);
                builder.setCancelable(true);

                final AlertDialog dialog = builder.create();
                dialog.show();
                Button btn_cancel=view.findViewById(R.id.btn_cancel);//取消按钮
                Button btn_comfirm=view.findViewById(R.id.btn_comfirm);//确定按钮
                TextView msg=view.findViewById(R.id.alert_msg);
                msg.setText("确定删除"+name_1+"?");
                //取消或确定按钮监听事件处理
                btn_cancel.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Toast.makeText(getApplicationContext(), "取消", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                btn_comfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dbHelper.delete(id);
                        commonAdapter.removeItem(position);
                        Toast.makeText(getApplicationContext(),"已经删除"+name_1+"!",Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
            }
        });

    }

    @Override
    protected void onStop(){
        Intent intent = new Intent(MainActivity.this,MusicServer.class);
        stopService(intent);
        super.onStop();
    }

    public void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            Log.i("TAG","权限申请enter");
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.READ_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                Log.i("TAG","权限申请no");
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
            else {
                Log.i("TAG","权限申请ok");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




public List<Map<String,String>> getLastTenNews(){

    List<Map<String,String>> records = new ArrayList<>();
    SQLiteDatabase db = getWritableDatabase();
    Cursor cursor = db.rawQuery("select id, title, content, image, label, comment_num from News;", null);
    cursor.moveToLast();// 将cursor移到最后一条记录上
    int count = 10;
    while(cursor.moveToPrevious() && count>=0){
        // 获取数据
        // 需要每个人物（元组）的头像、姓名、性别、生卒年月
        Map<String, String> tem = new LinkedHashMap<>();
        tem.put("id",cursor.getString(0));
        tem.put("title",cursor.getString(1));
        tem.put("image",cursor.getString(3));
        records.add(tem);
        count--;
    }
    // 当用完时，要将cursor释放
    cursor.close();
    db.close();
    return records;
}

public List<Map<String,String>> getAllCollections(String user_id){

    List<Map<String,String>> records = new ArrayList<>();

    SQLiteDatabase db = getWritableDatabase();//得到数据库实体
    String selection = "user_id = ?";
    String[] selectionArgs = { user_id };
    Cursor cursor = db.query(Collection_TABLE_NAME, null, selection, selectionArgs, null,null, null);
    cursor.moveToLast();
    while(cursor.moveToPrevious()){
        String news_id = cursor.getString(2);
        // 查询新闻名称
        String selection2 = "news_id = ?";
        String[] selectionArgs2 = { news_id };
        Cursor cursor2 = db.query(News_TABLE_NAME, null, selection2, selectionArgs2, null,null, null);
        cursor2.moveToNext();
        String news_title = cursor2.getString(2);
        // 获取数据
        // 需要每个人物（元组）的头像、姓名、性别、生卒年月
        Map<String, String> tem = new LinkedHashMap<>();
        tem.put("news_title",news_title);
        records.add(tem);
        cursor2.close();
    }
    // 当用完时，要将cursor释放
    cursor.close();
    db.close();
    return records;
}

public int comment_update_upAdd(String id){
    SQLiteDatabase db = getWritableDatabase();//得到数据库实体
    String whereClause = "id = ?";//主键列名
    String[] whereArgs = {id};//主键属性值

    Cursor cursor = db.query(Comment_TABLE_NAME, null, whereClause, whereArgs, null,null, null);
    int count = cursor.getCount();
    String old_up_num = "";
    if(count > 0){
        cursor.moveToFirst();
        do{
            old_up_num = cursor.getString(cursor.getColumnIndex("up_num"));
            //Log.d("hhh","photo = "+photo);
        }while(cursor.moveToNext());
    }
    int up_num = Integer.parseInt(old_up_num);
    up_num++;

    ContentValues values = new ContentValues();
    values.put("up_num",up_num);
    int rows = db.update(Comment_TABLE_NAME, values, whereClause, whereArgs);
    db.close();
    return rows;
}


<style name="ActionSheetDialogStyle" parent="@android:style/Theme.Dialog">
    <!-- 背景透明 -->
    <item name="android:windowBackground">@android:color/transparent</item>
    <item name="android:windowContentOverlay">@null</item>
    <!-- 浮于Activity之上 -->
    <item name="android:windowIsFloating">true</item>
    <!-- 边框 -->
    <item name="android:windowFrame">@null</item>
    <!-- Dialog以外的区域模糊效果 -->
    <item name="android:backgroundDimEnabled">true</item>
    <!-- 无标题 -->
    <item name="android:windowNoTitle">true</item>
    <!-- 半透明 -->
    <item name="android:windowIsTranslucent">true</item>
    <!-- Dialog进入及退出动画 -->
    <item name="android:windowAnimationStyle">@style/ActionSheetDialogAnimation</item>
</style>
<!-- ActionSheet进出动画 -->
<style name="ActionSheetDialogAnimation" parent="@android:style/Animation.Dialog">
    <item name="android:windowEnterAnimation">@anim/actionsheet_dialog_in</item>
    <item name="android:windowExitAnimation">@anim/actionsheet_dialog_out</item>
</style>



public void show(View view){
    // 对话框出现以及消失的动画
    dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
    //填充对话框的布局
    inflate = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);
    //初始化控件
    final EditText inputComment = (EditText) inflate.findViewById(R.id.input_comment);
    Button send = (Button) inflate.findViewById(R.id.send);

    //将布局设置给Dialog
    dialog.setContentView(inflate);
    //获取当前Activity所在的窗体
    Window dialogWindow = dialog.getWindow();
    //设置Dialog从窗体底部弹出
    dialogWindow.setGravity( Gravity.BOTTOM);
    //获得窗体的属性
    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
    lp.y = 0;//设置Dialog距离底部的距离
    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
    //将属性设置给窗体
    dialogWindow.setAttributes(lp);
    dialog.show();//显示对话框

    send.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View view)
        {
            String comment = inputComment.getText().toString();
            //得到long类型当前时间
            long l = System.currentTimeMillis();
            //new日期对象
            Date date = new Date(l);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Log.d("hhh","date = "+dateFormat.format(date));
            dialog.dismiss();
            long res = dbHelper.comment_insert(news_id, user_id, dateFormat.format(date),comment);
            if(res > 0)Toast.makeText(NewsInfo.this, "评论成功", Toast.LENGTH_SHORT).show();
        }
    });
}
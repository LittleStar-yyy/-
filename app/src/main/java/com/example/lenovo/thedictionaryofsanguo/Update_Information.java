package com.example.lenovo.thedictionaryofsanguo;

/**
 * Created by Administrator on 2017/11/26.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Update_Information extends Activity {
    private CircleImageView cv;
    private TextInputLayout myTextInput1;
    private TextInputLayout myTextInput2;
    private TextInputLayout myTextInput3;
    private TextInputLayout myTextInput4;
    private TextInputLayout myTextInput5;
    private TextInputLayout myTextInput6;

    private EditText edit1;
    private EditText edit2;
    private EditText edit3;
    private EditText edit4;
    private EditText edit5;
    private EditText edit6;
    private Button mybutton1;
    private Button mybutton2;
    private String fileName;
    int flag=-1;//用来判断是更新还是增加,0为更新，1为增加
    int flag1=-1;//用来判断图片是否有修改,1代表有修改
    int flag2=-1;//用来判断图片是从相册里导入的，还是从相机里得到的，0是相册，1是相机
    int ID;//这个ID是需要更新的那一项的ID,也就是从修改页面传递过来的

    private String filePath_takephoto;
    private String filePath_photographers;
    private File imageFile;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information);
        Intent intent = this.getIntent();//得到激活它的Intent
        Bundle bundle = intent.getExtras();//通过bundle里面的内容来确定是更新还是增加
        //final String person = intent.getStringExtra("update");//提取该页面的人物名称

        TextView tv = (TextView)findViewById(R.id.title);
        tv.setTypeface(Typeface.createFromAsset(getAssets(),"sssss.ttf"));
        tv.setEnabled(false);
        mybutton1 = (Button) findViewById(R.id.Button1);//保存按钮
        mybutton2 = findViewById(R.id.Button2);
        edit1 = (EditText) findViewById(R.id.edit_text1);//姓名
        edit2 = (EditText) findViewById(R.id.edit_text2);//生卒
        edit3 = (EditText) findViewById(R.id.edit_text3);//籍贯
        edit4 = (EditText) findViewById(R.id.edit_text4);//简介
        edit5 = (EditText) findViewById(R.id.edit_text5);//性别
        edit6 = (EditText) findViewById(R.id.edit_text6);//国籍
        cv = (CircleImageView) findViewById(R.id.picture);//photo
        myTextInput1 = (TextInputLayout) findViewById(R.id.Textinputlayout_1);
        myTextInput2 = (TextInputLayout) findViewById(R.id.Textinputlayout_2);
        myTextInput3 = (TextInputLayout) findViewById(R.id.Textinputlayout_3);
        myTextInput4 = (TextInputLayout) findViewById(R.id.Textinputlayout_4);
        myTextInput5 = (TextInputLayout) findViewById(R.id.Textinputlayout_5);
        myTextInput6 = (TextInputLayout) findViewById(R.id.Textinputlayout_6);

        ImageButton back = findViewById(R.id.back_2);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        // 创建数据库
        final DataBaseHelper dbHelper = new DataBaseHelper(this);
        // 建立新表，调用getReadableDatabase()或getWritableDatabase()才算真正创建或打开数据库
        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
        // 利用名字查询到该项

        if(bundle != null&&bundle.containsKey("update")){
            //如果传过来的bundle有关键字update
            //则需要先把这个页面的所有信息都填完整，然后再在这个基础上修改
            flag=0;
            ID = bundle.getInt("myid");

            List<Map<String, String>> data_6 = dbHelper.GetAllInformationFromId(ID);
            edit1.setText(data_6.get(0).get("name"));
            edit2.setText(data_6.get(0).get("birth_to_death"));
            edit3.setText(data_6.get(0).get("hometown"));
            edit4.setText(data_6.get(0).get("introduction"));
            edit5.setText(data_6.get(0).get("sex"));
            edit6.setText(data_6.get(0).get("country"));
            int flag=Integer.parseInt(data_6.get(0).get("flag"));
            if(flag==0) {
                int imagenum = getResources().getIdentifier(data_6.get(0).get("photo").substring(9,data_6.get(0).get("photo").length()),"mipmap",getPackageName());
                cv.setImageResource(imagenum);
            }else if(flag==1){
                String filePath = data_6.get(0).get("photo");
                File file = new File(filePath);
                if(file.exists()){
                    cv.setImageURI(Uri.fromFile(file));
                }
            }

        }

        if(bundle != null&&bundle.containsKey("Insert")){
            //如果传过来的bundle有关键字Insert；
            //则把标志设置为1，为下面的保存按钮设置基础
            Log.d("xiaoru10","进来");
            flag=1;
        }

        Log.d("xiaoru10","flag: "+flag);


        //点击编辑按钮
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag1=1;
                ShowPickDialog();
            }
        });

        //点击保存按钮
        mybutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edit1.getText().toString();
                String time = edit2.getText().toString();
                String place = edit3.getText().toString();
                String introduction = edit4.getText().toString();
                String sex = edit5.getText().toString();
                String country = edit6.getText().toString();
                if(TextUtils.isEmpty(name)){
                    myTextInput1.setErrorEnabled(true);
                    myTextInput1.setError("姓名不能为空");
                    if(!TextUtils.isEmpty(time)||!TextUtils.isEmpty(place)||!TextUtils.isEmpty(introduction)||!TextUtils.isEmpty(sex)||!TextUtils.isEmpty(country)){
                        myTextInput2.setErrorEnabled(false);
                        myTextInput3.setErrorEnabled(false);
                        myTextInput4.setErrorEnabled(false);
                        myTextInput5.setErrorEnabled(false);
                        myTextInput6.setErrorEnabled(false);
                    }
                }
                else{
                    if(TextUtils.isEmpty(time)){
                        myTextInput2.setErrorEnabled(true);
                        myTextInput2.setError("生卒信息不能为空");
                        myTextInput1.setErrorEnabled(false);
                        if(!TextUtils.isEmpty(place)||!TextUtils.isEmpty(introduction)||!TextUtils.isEmpty(sex)||!TextUtils.isEmpty(country)){
                            myTextInput3.setErrorEnabled(false);
                            myTextInput4.setErrorEnabled(false);
                            myTextInput5.setErrorEnabled(false);
                            myTextInput6.setErrorEnabled(false);
                        }
                    }
                    else{
                        if(TextUtils.isEmpty(sex)){
                            myTextInput5.setErrorEnabled(true);
                            myTextInput5.setError("性别不能为空");
                            myTextInput1.setErrorEnabled(false);
                            myTextInput2.setErrorEnabled(false);
                            if(!TextUtils.isEmpty(place)||!TextUtils.isEmpty(introduction)||!TextUtils.isEmpty(country)){
                                myTextInput6.setErrorEnabled(false);
                                myTextInput3.setErrorEnabled(false);
                                myTextInput4.setErrorEnabled(false);
                            }
                        }
                        else{

                            if(TextUtils.isEmpty(country)){
                                myTextInput6.setErrorEnabled(true);
                                myTextInput6.setError("效忠势力不能为空");
                                myTextInput1.setErrorEnabled(false);
                                myTextInput2.setErrorEnabled(false);
                                myTextInput5.setErrorEnabled(false);
                                if(!TextUtils.isEmpty(place)||!TextUtils.isEmpty(introduction)){
                                    myTextInput3.setErrorEnabled(false);
                                    myTextInput4.setErrorEnabled(false);
                                }
                            }
                            else{
                                if(TextUtils.isEmpty(place)){
                                    myTextInput3.setErrorEnabled(true);
                                    myTextInput3.setError("籍贯信息不能为空");
                                    myTextInput1.setErrorEnabled(false);
                                    myTextInput2.setErrorEnabled(false);
                                    myTextInput5.setErrorEnabled(false);
                                    myTextInput6.setErrorEnabled(false);
                                    if(!TextUtils.isEmpty(introduction)){
                                        myTextInput4.setErrorEnabled(false);
                                    }
                                }
                                else{
                                    if(TextUtils.isEmpty(introduction)){
                                        myTextInput4.setErrorEnabled(true);
                                        myTextInput4.setError("人物简介不能为空");
                                        myTextInput1.setErrorEnabled(false);
                                        myTextInput2.setErrorEnabled(false);
                                        myTextInput3.setErrorEnabled(false);
                                        myTextInput5.setErrorEnabled(false);
                                        myTextInput6.setErrorEnabled(false);
                                    }
                                    else{
                                        myTextInput1.setErrorEnabled(false);
                                        myTextInput2.setErrorEnabled(false);
                                        myTextInput3.setErrorEnabled(false);
                                        myTextInput4.setErrorEnabled(false);
                                        myTextInput5.setErrorEnabled(false);
                                        myTextInput6.setErrorEnabled(false);
                                        Snackbar.make(mybutton1,"信息保存成功!",Snackbar.LENGTH_SHORT)
                                                .setActionTextColor(getResources().getColor((R.color.colorPrimary)))
                                                .setDuration(4000)
                                                .show()
                                        ;
                                        //////////////////////////////////////此处根据接受的intent来判断以上信息该执行增操作还是改操作/////////////////////////////////////////////
                                        if(flag==0){
                                            // 更新数据库
                                            ContentValues values_2 = new ContentValues();
                                            values_2.put("name",name);
                                            values_2.put("birth_to_death",time);
                                            values_2.put("hometown",place);
                                            values_2.put("introduction",introduction);
                                            values_2.put("sex",sex);
                                            values_2.put("country",country);

                                            if(flag1==1){
                                                //检测到图像有被修改
                                                String s="";
                                                s = filePath_takephoto;

                                                values_2.put("photo",s);
                                                values_2.put("flag",1);
                                            }
                                            dbHelper.update(values_2, ID);
                                        }else if(flag==1){
                                            Log.d("xiaoru10","jdhjdhf");
                                            //插入操作
                                            ContentValues values = new ContentValues();
                                            values.put("flag", 1);
                                            values.put("name", name);
                                            values.put("sex", sex);
                                            values.put("birth_to_death", time);
                                            values.put("hometown", place);
                                            values.put("country", country);
                                            values.put("introduction",introduction);
                                            values.put("life",8);
                                            values.put("force",3);
                                            String s="";
                                            s = filePath_takephoto;
                                            values.put("photo",s);
                                            values.put("flag",1);
                                            dbHelper.insert(values);
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        });
        dbHelper.close();
        //点击取消按钮
        mybutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void ShowPickDialog() {
        //根据日期确定照片名称
        Calendar now = new GregorianCalendar();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        fileName = simpleDate.format(now.getTime())+".jpg";
        filePath_takephoto = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/"+ fileName;
        imageFile = new File(filePath_takephoto);

        Log.d("asdfone",filePath_takephoto);
        new AlertDialog.Builder(this)
                .setTitle("设置头像...")
                .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                "image/*");
                        startActivityForResult(intent, 1);
                    }
                })
                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 下面这句指定调用相机拍照后的照片存储的路径
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                                .fromFile(imageFile));
                        startActivityForResult(intent, 2);
                    }
                }).show();
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                startPhotoZoom(data.getData());
                Uri originalUri = data.getData();
                String[] proj = {MediaStore.Images.Media.DATA};
                //android多媒体数据库的封装接口
                Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                //获得用户选择的图片的索引值
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                //将光标移至开头 ，这个很重要，不小心很容易引起越界
                cursor.moveToFirst();
                //最后根据索引值获取图片路径
                filePath_photographers = cursor.getString(column_index);
                flag2=0;

//                Log.d("zhjasdf",filePath_photographers);
                break;
            // 如果是调用相机拍照时
            case 2:
                File temp = new File(filePath_takephoto);
                startPhotoZoom(Uri.fromFile(temp));
                flag2=1;
                break;
            // 取得裁剪后的图片
            case 3:
                /**
                 * 非空判断，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
                 * 当前功能时，会报NullException，只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况
                 *
                 */
                if (data != null) {
                    setPicToView(data);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */


    public void startPhotoZoom(Uri uri) {
 /*
 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
 * yourself_sdk_path/docs/reference/android/content/Intent.html
 * 直接在里面Ctrl+F搜：CROP ，之前没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的
 */
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo= extras.getParcelable("data");
            // Drawable drawable = new BitmapDrawable(photo);
            try {
                File cut_file = new File(filePath_takephoto);
                FileOutputStream fos = new FileOutputStream(cut_file);
                photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            cv.setImageBitmap(photo);
        }
    }

}

package com.example.lenovo.thedictionaryofsanguo;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DataBaseHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "figures.db";//数据库名字
    private final static int VERSION = 1;//数据库版本
    private SQLiteDatabase db;

    private final static String TABLE_NAME = "information";

    private int initial_size = 13;
    private String[] photo = {"R.mipmap.liubei", "R.mipmap.simayi", "R.mipmap.xunyu", "R.mipmap.huatuo", "R.mipmap.caocao",
              "R.mipmap.xiaoqiao", "R.mipmap.zhenmi", "R.mipmap.guanyu", "R.mipmap.zhangfei", "R.mipmap.zhaoyun",
              "R.mipmap.zhugeliang", "R.mipmap.sunquan", "R.mipmap.zhouyu"};
//    , "R.mipmap.caopi", "R.mipmap.lvbu",
//              "R.mipmap.yuanshao", "R.mipmap.diaochan", "R.mipmap.liushan", "R.mipmap.yangxiu", "R.mipmap.sunshangxiang",
//              "R.mipmap.machao" };
    private String[] name = {"刘备", "司马懿", "荀彧", "华佗", "曹操",
             "小乔", "甄宓", "关羽", "张飞", "赵云",
             "诸葛亮", "孙权", "周瑜"};
    // 生命值：10、8、7
    // 攻击值：5、3、2
    private int[] life = {10,7,8,10,10,
            7,7,8,8,8,
            8,10,7};
    private int[] force = {3,5,3,2,3,
            2,5,5,3,3,
            3,3,2};
//            , "曹丕", "吕布",
//             "袁绍", "貂蝉", "刘禅", "杨修", "孙尚香",
//             "马超"};
    private String[] sex = {"男", "男", "男", "男", "男",
            "女", "女", "男", "男", "男",
            "男", "男", "男"};
//            , "男", "男",
//            "男", "女", "男", "男", "女",
//            "男"};
    private String[] birth_to_death = {"生卒（161 - 223）", "生卒（179 - 251）", "生卒（163 - 212）", "生卒（？ - ？）", "生卒（155 - 220）",
            "生卒（？ - ？）", "生卒（183 - 221）", "生卒（？ - 219）",  "生卒（？ - 221）", "生卒（？ - 229）",
            "生卒（181 - 234）", "生卒（182 - 252）", "生卒（175 - 210）"};
//            , "", "",
//            "刘备", "", "", "", "",
//            "刘备"};
    private String[] hometown = {"幽州涿郡涿（河北保定市涿州）", "司隶河内郡温（河南焦作市温县西）", "豫州颍川郡颍阴（河南许昌市）", "豫州沛国谯（安徽亳州市亳县）", "豫州沛国谯（安徽亳州市亳县）",
            "扬州庐江郡皖（安徽安庆市潜山县）", "冀州中山国毋极（河北石家庄市无极县西、滋水北岸）", "司隶河东郡解（山西运城市临猗县西南）", "幽州涿郡（河北保定市涿州）", "冀州常山国真定（河北石家庄市正定县南）",
            "徐州琅邪国阳都（山东临沂市沂南县南）", "扬州吴郡富春（浙江杭州市富阳）", "扬州庐江郡舒（安徽合肥市庐江县西南）"};
    //            , "", "",
//            "刘备", "", "", "", "",
//            "刘备"};
    private String[] country = {"蜀", "魏国", "魏", "在野", "魏",
            "吴", "魏", "蜀", "蜀", "蜀",
            "蜀", "吴", "吴"};
//    , "", "",
//            "刘备", "", "", "", "",
//            "刘备"};
    private String[] introduction = {"刘备，蜀汉的开国皇帝，汉景帝之子中山靖王刘胜的后代。刘备少年孤贫，以贩鞋织草席为生。黄巾起义时，刘备与关羽、张飞桃园结义，成为异姓兄弟，一同剿除黄巾，有功，任安喜县尉，不久辞官；董卓乱政之际，刘备随公孙瓒讨伐董卓，三人在虎牢关战败吕布。后诸侯割据，刘备势力弱小，经常寄人篱下，先后投靠过公孙瓒、曹操、袁绍、刘表等人，几经波折，却仍无自己的地盘。赤壁之战前夕，刘备在荆州三顾茅庐，请诸葛亮出山辅助，在赤壁之战中，联合孙权打败曹操，奠定了三分天下的基础。刘备在诸葛亮的帮助下占领荆州，不久又进兵益州，夺取汉中，建立了横跨荆益两州的政权。后关羽战死，荆州被孙权夺取，刘备大怒，于称帝后伐吴，在夷陵之战中为陆逊用火攻打得大败，不久病逝于白帝城，临终托孤于诸葛亮。",
        "早年任文学掾，后任主簿，是曹操帐下谋士之一，但并不出名。襄樊之战时和蒋济劝曹操勿迁都，可割江南封孙权，令其袭杀关羽，事后如他所料。后来司马懿协助曹丕代汉，出谋五路伐蜀，开始崭露头角，在曹丕病逝前成为顾命大臣。曹叡继位后，司马懿主动请命去防御魏国西部，后因蜀汉马谡的反间计一度被废，但面对诸葛亮的北伐强攻，魏国不得不再次启用司马懿，先擒孟达，后败马谡于街亭。司马懿多次败于诸葛亮，于是采用闭门不战的策略防守，直至诸葛亮病逝。后出兵平公孙渊。曹芳继位后，司马懿、曹爽共同辅政。司马懿受曹爽排挤，于是发动政变诛杀曹爽一族，自此掌握魏国大权。司马懿病逝后，魏国政权仍由其儿子把持，多年后导致晋朝代魏的发生。司马懿被后代追封为晋朝皇帝。",
        "旧从袁绍，因知袁绍终不能成大事遂与其侄荀攸投奔曹操，与曹操长谈后被称为“吾之子房也”。曹操攻打陶谦时吕布袭取了兖州，荀彧与程昱保住了三座城池。荀彧为曹操出过很多重要的战略谋划，如以兖州为基地、二虎竞食和驱虎吞狼、迎汉献帝往许都、开玄武湖练水军等等，立有大功。荀彧后为汉侍中、尚书令，参与国家大事並经常留守许都。袁绍势力强大，曹操对是否与他开战而犹豫不决，荀彧就用四胜四败之说开导曹操，使他终下决心抗袁。官渡之战筹划粮草供给及回信坚定了曹操的意志，最后得已击败袁绍统一中原，这都是荀彧的计划。时董昭劝曹操称魏公，荀彧表示反对，曹操深感不满，遂将其招入军中，从征孙权。荀彧因病留于寿春，不久在曹操的暗示下服毒自杀。",
        "人称神医，传有百岁。以麻沸散为麻醉药施行手术。曾经救重伤的周泰、为关羽刮骨疗毒。后被怀疑有暗杀曹操的企图，死在狱中。托付给狱吏的医学书《青囊书》也被烧掉大半。 ",
        "曹操是西园八校尉之一，曾只身行刺董卓，失败后和袁绍共同联合天下诸侯讨伐董卓，后独自发展自身势力，一生中先后战胜了袁术、吕布、张绣、袁绍、刘表、张鲁、马超等割据势力，统一了北方。但是在南下讨伐江东的战役中，曹操在赤壁惨败。后来在和蜀汉的汉中争夺战中，曹操再次无功而返。曹操一生未称帝，他病死后，曹丕继位后不久称帝，追封曹操为魏武皇帝。",
        "庐江皖县桥国老次女，秀美绝伦，貌压群芳，又琴棋书画无所不通周瑜攻取皖城，迎娶小乔为妻。周郎小乔英雄美女、郎才女貌 ，被流传为千古佳话。",
        "甄氏初期嫁与袁绍次子袁熙，袁熙带兵出外征战，留下甄氏独身照顾婆婆，袁氏败亡后，曹操之子曹丕见其美艳动人，便纳为己有。黄初年间，魏文帝曹丕新纳的宠妾郭后栽赃甄后，诬陷她埋木偶诅咒文帝。文帝曹丕大怒，将甄后赐死。",
        "因本处势豪倚势凌人，关羽杀之而逃难江湖。闻涿县招军破贼，特来应募。与刘备、张飞桃园结义，羽居其次。使八十二斤青龙偃月刀随刘备东征西讨。虎牢关温酒斩华雄，屯土山降汉不降曹。为报恩斩颜良、诛文丑，解曹操白马之围。后得知刘备音信，过五关斩六将，千里寻兄。刘备平定益州后，封关羽为五虎大将之首，督荆州事。羽起军攻曹，放水淹七军，威震华夏。围樊城右臂中箭，幸得华佗医治，刮骨疗伤。但未曾提防东吴袭荆州，关羽父子败走麦城，突围中被捕，不屈遭害。",
        "与刘备和关羽桃园结义，张飞居第三。随刘备征讨黄巾，刘备终因功被朝廷受予平原相，后张飞鞭挞欲受赂的督邮。十八路诸侯讨董时，三英战吕布，其勇为世人所知。曹操以二虎竞食之计迫刘备讨袁术，刘备以张飞守徐州，诫禁酒，但还是因此而鞭打曹豹招致吕布东袭。刘备反曹后，反用劫寨计擒曹将刘岱，为刘备所赞。徐州终为曹操所破，张飞与刘备失散，占据古城。误以为降汉的关羽投敌，差点一矛将其杀掉。曹操降荊州后引骑追击，刘备败逃，张飞引二十余骑，立马于长阪桥，吓退曹军数十里。庞统死后刘备召其入蜀，张飞率军沿江而上，智擒巴郡太守严颜并生获之，张飞壮而释放。于葭萌关和马超战至夜间，双方点灯，终大战数百回合。瓦口关之战时扮作醉酒，智破张郃。后封为蜀汉五虎大将。及关羽卒，张飞悲痛万分，每日饮酒鞭打部下，导致为帐下将张达、范强所杀，他们持其首顺流而奔孙权。",
        "初为袁绍将，后见绍不仁，于磐河战退绍将文丑，救瓒并投之。后又刺杀麹义。先主依讬瓒，云与之为田楷拒袁绍。后与先主执手泣别。后瓒败，云流浪卧牛山，与先主见，投之。当阳长阪恶战，云怀抱幼主，七进七出，杀曹军五十余将。先主娶孙夫人，云相随。及征蜀，云随诸葛亮、张飞等人沿江而上。及蜀平，又往征汉中，退曹大军。关羽亡，先主怒欲伐吴，云劝止，不从。后先主崩，云随亮南征、北伐，单骑退追兵。七年卒，后主哭倒于龙床上，谥云顺平侯、追大将军。",
        "人称卧龙先生，有经天纬地之才，鬼神不测之机。刘皇叔三顾茅庐，遂允出山相助。曾舌战群儒、借东风、智算华容、三气周瑜，辅佐刘备于赤壁之战大败曹操，更取得荆州为基本。后奉命率军入川，于定军山智激老黄忠，斩杀夏侯渊，败走曹操，夺取汉中。刘备伐吴失败，受遗诏托孤，安居平五路，七纵平蛮，六出祁山，鞠躬尽瘁，死而后已。其手摇羽扇，运筹帷幄的潇洒形象，千百年来已成为人们心中“智慧”的代名词。",
        "孙权19岁就继承了其兄孙策之位，力据江东，击败了黄祖。后东吴联合刘备，在赤壁大战击溃了曹操军。东吴后来又和曹操军在合肥附近鏖战，并从刘备手中夺回荆州、杀死关羽、大破刘备的讨伐军。曹丕称帝后孙权先向北方称臣，后自己建吴称帝，迁都建业。",
        "偏将军、南郡太守。自幼与孙策交好，策离袁术讨江东，瑜引兵从之。为中郎将，孙策相待甚厚，又同娶二乔。策临终，嘱弟权曰：“外事不决，可问周瑜”。瑜奔丧还吴，与张昭共佐权，并荐鲁肃等，掌军政大事。赤壁战前，瑜自鄱阳归。力主战曹，后于群英会戏蒋干、怒打黄盖行诈降计、后火烧曹军，大败之。后下南郡与曹仁相持，中箭负伤，与诸葛亮较智斗，定假涂灭虢等计，皆为亮破，后气死于巴陵，年三十六岁。临终，上书荐鲁肃代其位，权为其素服吊丧。"};
//    , "", "",
//            "刘备", "", "", "", "",
//            "刘备"};



    // SQLiteOpenHelper子类必须要的一个构造函数
    public DataBaseHelper(Context context, String name, CursorFactory factory,int version) {
        //必须通过super 调用父类的构造函数
        super(context, name, factory, version);
    }
    //数据库的构造函数，传递三个参数的
    public DataBaseHelper(Context context, String name, int version){this(context, name, null, version);}
    //数据库的构造函数，传递一个参数的， 数据库名字和版本号都写死了
    public DataBaseHelper(Context context){this(context, DB_NAME, null, VERSION);}


    //第一次创建数据库的时候回调该方法
    //当使用getReadableDatabase()方法获取数据库实例的时候, 如果数据库不存在, 就会调用这个方法;
    //作用：创建数据库表：将创建数据库表的 execSQL()方法 和 初始化表数据的一些 insert()方法写在里面;
    @Override
    public void onCreate(SQLiteDatabase db) {
//        Log.d("hhh","onCreate");
        this.db = db;
        // 执行sql语句完成数据库的创建
        String CREATE_TBL = "create table information(_id integer primary key autoincrement, photo String, flag int, " +
                "name text, sex text, birth_to_death text, hometown text, country text, introduction text, life int, force int);";
        db.execSQL(CREATE_TBL);
        // 数据库实际上是没有被创建或者打开的，直到getWritableDatabase() 或者 getReadableDatabase()
        // 插入一些初始化数据
        // 创建ContentValues对象
        for(int i=0; i<initial_size; i++)
        {
            ContentValues values = getValues(photo[i], 0, name[i], sex[i], birth_to_death[i], hometown[i], country[i], introduction[i], life[i], force[i]);
            db.insert(TABLE_NAME, null, values);
//            Log.i("hhh","* i = "+i);
        }
//        Log.d("hhh","onCreate_finish");
        // *************************************************************************************************************************



    }

    public ContentValues getValues(String photo, int flag, String name, String sex, String birth_to_death, String hometown, String country, String introduction, int life, int force)
    {
        ContentValues values = new ContentValues();
        // 向该对象中插入键值对
        values.put("photo", photo);
        values.put("flag",flag);
        values.put("name", name);
        values.put("sex", sex);
        values.put("birth_to_death", birth_to_death);
        values.put("hometown", hometown);
        values.put("country", country);
        values.put("introduction", introduction);
        values.put("life",life);
        values.put("force",force);
        return values;
    }

    //回调函数，当你构造DBHelper的传递的Version与之前的Version调用此函数
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("update Database");
    }

    //插入方法
    public void insert(ContentValues values){
        //获取SQLiteDatabase实例
        db = getReadableDatabase();
        //插入数据库中
        db.insert(TABLE_NAME, null, values);
        // 第一个参数：要操作的表名称
        // 第二个参数：SQl不允许一个空列，如果ContentValues是空的，那么这一列被明确的指明为NULL值
        // 第三个参数：ContentValues对象
    }

    public List<Map<String,String>> getAllTests(){

        List<Map<String,String>> information = new ArrayList<>();
        // _id integer primary key autoincrement, photo String, " +
        // "name text, sex text, birth_to_death text, hometown text, country text, introduction text

        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select _id, name, sex, country, photo, flag from information;", null);

        while(cursor.moveToNext()){
            // 获取数据
            // 需要每个人物（元组）的头像、姓名、性别、生卒年月
            Map<String, String> tem = new LinkedHashMap<>();
            tem.put("Id",cursor.getString(0));
            tem.put("name",cursor.getString(1));
            tem.put("sex",cursor.getString(2));
            tem.put("country",cursor.getString(3));
            tem.put("photo",cursor.getString(4));
            tem.put("flag",cursor.getString(5));
            information.add(tem);
        }
        // 当用完时，要将cursor释放
        cursor.close();
        return information;
    }

    // 查询功能：通过名字查询表项
    public List<Map<String,String>> GetItemFromName(String name){

        List<Map<String,String>> information = new ArrayList<>();

        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select _id, name, sex, country, photo, flag from information where name=?;", new String[]{name});

        while(cursor.moveToNext()){
            // 获取数据
            // 需要每个人物（元组）的头像、姓名、性别、生卒年月
            Map<String, String> tem = new LinkedHashMap<>();
            tem.put("Id",cursor.getString(0));
            tem.put("name",cursor.getString(1));
            tem.put("sex",cursor.getString(2));
            tem.put("country",cursor.getString(3));
            tem.put("photo",cursor.getString(4));
            tem.put("flag",cursor.getString(5));
            information.add(tem);
        }
        // 当用完时，要将cursor释放
        cursor.close();
        return information;
    }

    // 通过 id，查询该人物的所有信息
    public List<Map<String,String>> GetAllInformationFromId(int id){

        List<Map<String,String>> information = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select _id, name, sex, country, birth_to_death, hometown, photo, flag, introduction from information where _id=?;", new String[]{String.valueOf(id)});

        while(cursor.moveToNext()){
            Map<String, String> tem = new LinkedHashMap<>();
            // 获取数据
            // 需要每个人物（元组）的头像、姓名、性别、生卒年月
//            Map<String, String> tem = new LinkedHashMap<>();
            tem.put("Id",cursor.getString(0));
            tem.put("name",cursor.getString(1));
            tem.put("sex",cursor.getString(2));
            tem.put("country",cursor.getString(3));
            tem.put("birth_to_death",cursor.getString(4));
            tem.put("hometown",cursor.getString(5));
            tem.put("photo",cursor.getString(6));
            tem.put("flag",cursor.getString(7));
            tem.put("introduction",cursor.getString(8));
            information.add(tem);
        }
        // 当用完时，要将cursor释放
        cursor.close();
        return information;
    }
    // pk ：获得所有人物的姓名、图片、flag\生命值、武力值
    public List<Map<String,String>> GetPkInformation(){

        List<Map<String,String>> information = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select name, photo, flag, life, force from information;",null);

        while(cursor.moveToNext()){
            Map<String, String> tem = new LinkedHashMap<>();
            // 获取数据
            // 需要每个人物（元组）的头像、姓名、性别、生卒年月
//            Map<String, String> tem = new LinkedHashMap<>();
            tem.put("name",cursor.getString(0));
            tem.put("photo",cursor.getString(1));
            tem.put("flag",cursor.getString(2));
            tem.put("life",cursor.getString(3));
            tem.put("force",cursor.getString(4));
            information.add(tem);
        }
        // 当用完时，要将cursor释放
        cursor.close();
        return information;
    }


    //根据唯一标识_id ，来删除数据
    public void delete(int id){
        db = getReadableDatabase();
        db.delete(TABLE_NAME, "_id=?", new String[]{String.valueOf(id)});
    }

    //更新数据库的内容
    public void update(ContentValues values, int id){
        db = getReadableDatabase();
        // values里存放需要更改的列名及更改后的数值
        db.update(TABLE_NAME, values, "_id=?", new String[]{String.valueOf(id)});
    }
//    public void update(ContentValues values, String whereClause, String[]whereArgs){
//        db = getReadableDatabase();
//        db.update(TABLE_NAME, values, whereClause, whereArgs);
//    }

    //关闭数据库
    public void close(){
        if(db != null){
            db.close();
        }
    }

}

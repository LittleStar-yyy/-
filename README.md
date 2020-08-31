# SanGuo
本三国电子词典收录三国演义和三国志中出现的部分人物，详尽提供了多样的三国英雄 人物的个人数据。 利用数据库技术，方便快捷地对三国人物信息进行数据整理和显示，还提 供智能搜索功能，可根据搜索内容快速查找对应英雄人物；提供“增、删、改”功能，允许 用户创建或删除人物或对本三国电子词典的人物信息进行修改；同时，本应用还提供英雄人 物 PK 对决功能，娱乐性较强。

## 功能模块
### 以 SQLite 数据库为基础的增删查改功能
1. 增加英雄人物：可添加新增人物的姓名、籍贯、性别、主效势力、生卒年月、简介以及人物图片。其中人物图片的来源可以是直接用相机拍照获得，也可以从相册中选取；
2. 删除英雄人物：长按列表中的人物，会弹出对话框询问是否确定删除人物，如果点击确定，则删除；否则，取消删除；
3. 查找英雄人物：在搜索框中输入人物名字，便可查到数据库中对应的人物；
4. 修改人物信息：点击编辑按钮，可以对人物的详细信息进行修改，可以更换人物的图片（来源是相机拍取或本地相册）、姓名等信息；
### 英雄 PK 功能
1. 可以选择自己喜欢的两个英雄任务进行 PK；
2. 点击 PK 按钮，两个人物开始 PK，此时，我们可以看到人物 PK 的动画效果，也能观察到武力值低的人物的血槽逐渐变空，还能看到对战的两个人物的对话，对话内容是放在气泡动画的效果中，十分形象逼真。
![image](https://github.com/LittleStar-yyy/SanGuo/blob/master/readme_images/流程图.png)


## 应用效果
### 点击 app 图标进入应用，该应用有三个引导界面；
![image](https://github.com/LittleStar-yyy/SanGuo/blob/master/readme_images/1.png)
### 点击“开启征途”按钮，进入应用主界面，主界面显示的主要是三国英雄人物列表；
<img src="https://github.com/LittleStar-yyy/SanGuo/blob/master/readme_images/2.png" width="300">
### 长按人物列表时，会弹出一个对话框，让你选择是否删除该英雄人物
<img src="https://github.com/LittleStar-yyy/SanGuo/blob/master/readme_images/3.png" width="300">
### 点击标题栏的搜索按钮，可以进入搜索界面；
![image](https://github.com/LittleStar-yyy/SanGuo/blob/master/readme_images/4.png)
### 点击该人物可以进入详细信息界面；
![image](https://github.com/LittleStar-yyy/SanGuo/blob/master/readme_images/5.png)
### 点击编辑按钮，进入修改界面，在这个界面可以实现在原有的基础上对英雄人物信息的修改；
![image](https://github.com/LittleStar-yyy/SanGuo/blob/master/readme_images/6.png)
### 同时也可以自己添加英雄人物，点击主界面的“ + ”按钮，进入人物增加界面，如下图所示，人物头像的选择有两种方式，一种是拍照，另一种是相册;
![image](https://github.com/LittleStar-yyy/SanGuo/blob/master/readme_images/7.png)
### 点击应用主界面工具栏的“ Vs”进入人物 PK 界面
1. 进入 PK 界面
![image](https://github.com/LittleStar-yyy/SanGuo/blob/master/readme_images/8.png)
2. 点击“相框”，会跳转到英雄列表界面，选择想要进行 PK 的英雄
![image](https://github.com/LittleStar-yyy/SanGuo/blob/master/readme_images/9.png)
3. 选择之后，跳转回 PK 界面，此时选中的英雄信息也会被加载到 PK 界面，包括英雄名字、生命值、武力值以及英雄照片：
![image](https://github.com/LittleStar-yyy/SanGuo/blob/master/readme_images/10.png)
4. PK 英雄加载成功后，可以点击“杀”按钮，那么两个英雄会进行一次 PK， PK 的过程是动态的，此时界面会有三个动态过程： 1. 两个英雄图片同时向中间移动，相撞后再返回原位，代表着激烈的厮杀； 2. 武力值弱的英雄会以肉眼可见的缓慢速度逐渐掉血； 3.两个英雄附近会产生绿色气泡，气泡逐渐上升的同时也慢慢变大，并且由透明状态渐渐清晰，气泡中是人物的互动交谈。
![image](https://github.com/LittleStar-yyy/SanGuo/blob/master/readme_images/11.png)



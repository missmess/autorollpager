#AutoRollViewPager
  
  AutoRollViewPager控件可以展示一组轮播图或一组自定义view，可以自动横向滚动，带指示点和标题，支持无限循环模式。
  
  An AutoRollViewPager can be used to display a sort of images or your custom views which can auto roll horizontally. Loop Mode supported.
  
---


###截图
![image](https://raw.githubusercontent.com/missmess/autorollpager/master/raw/picc1.jpg)
![image](https://raw.githubusercontent.com/missmess/autorollpager/master/raw/picc2.png)
![image](https://raw.githubusercontent.com/missmess/autorollpager/master/raw/picc3.jpg)
![image](https://raw.githubusercontent.com/missmess/autorollpager/master/raw/picc4.jpg)

---

###主要功能介绍

* 可以实现view自动轮播，带指示点。
* 支持轮播图：可以使用网络图片、本地图片资源、Drawable对象，可以混用。
* 支持轮播自定义的view。 
* 支持显示轮播view的文字描述。
* 支持开关自动轮播。
* 支持普通模式和无限循环模式。
* 支持轮播view的点击事件
* 支持选择自动轮播方向。
* 可以自定义轮播时间间隔。
* 触摸时停止自动滚动，离开后继续滚动。
* 不影响竖向滑动。如ScrollView、ListView。

---

###如何添加到项目中

Android Studio用户，可以很轻松地通过maven仓库引用到本library。只需要在项目的build.gradle中添加该depandencies：
  
  `
    compile 'com.missmess.autorollpager:library:1.0.3'
  `

---

###如何使用
  
######1、在xml布局中定义，或者代码中 new AutoRollViewPager(context)。
  ```xml
  <com.missmess.autorollpager.AutoRollImagePager
            android:id="@+id/arp"
            android:layout_width="wrap_content"
            android:layout_height="220dp"/>
  ```
  
######2、代码中提供适配器，并显示：
  
  ```java
  AutoRollViewPager arp = (AutoRollViewPager) findViewById(R.id.arp);
  arp.setRollAdapter(new MyRollAdapter());
  arp.showUp();
  ```
  
######3、在activity的onDestory()或fragment的onDestoryView()中关闭AutoRollViewPager。
  ```java
  @Override
    protected void onDestroy() {
        super.onDestroy();
        arp.tearDown();
 }
  ```
---

###示例代码
######1、使用AutoRollImagePager显示轮播图

AutoRollImagePager为AutoRollViewPager专用于轮播图片的一个实现。支持网络图片、本地资源、自定义drawable，以及三者混用的显示。

```java
        List<Object> images = new ArrayList<>();
        titles = new ArrayList<>();

        images.add(R.mipmap.pic1);
        images.add(R.mipmap.pic2);
        images.add("http://img3.imgtn.bdimg.com/it/u=509912007,3678988032&fm=21&gp=0.jpg");
        images.add(createADrawable());

        titles.add("本地图片1");
        titles.add("本地图片2");
        titles.add("网络图片");
        titles.add("Drawable图片");

        //设置图片源
        arp.setImageLists(images);
        //设置描述标题
        arp.setTitles(titles);
        //自动滚动（默认true）
        arp.setAutoRoll(true);
        //设置指示点间隔（默认5dp）
        arp.setDotInterval(8);
        //是否无限循环模式（默认false）
        arp.setLoopMode(true);
        //设置自动滚动间隔（默认3秒）
        arp.setRollInterval(3000);
        //设置轮播view点击事件
        arp.setOnPageClickListener(new ClickPager());
        //设置自动滚动方向
        arp.setRollDirection(AutoRollViewPager.DIRECTION_RIGHT);
        //完成配置并显示
        arp.showUp();

```

######2、使用AutoRollViewPager

使用AutoRollViewPager可参考上文中的 `如何使用` 。提供的adapter需要继承于BaseRollAdapter。
```java
    class MyRollAdapter extends BaseRollAdapter {

        public MyRollAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            View content = null;
            switch (position) {
                case 0:
                    content = View.inflate(context, R.layout.item_roll_view_1, null);
                    break;
                case 1:
                    content = View.inflate(context, R.layout.item_roll_view_2, null);
                    break;
                case 2:
                    content = View.inflate(context, R.layout.item_roll_view_3, null);
                    break;
                case 3:
                    content = View.inflate(context, R.layout.item_roll_view_4, null);
                    break;
            }

            return content;
        }

        @Override
        public int getRealCount() {
            return 4;
        }
    }
```
---

###关于作者
在使用中有任何问题，欢迎反馈给我，可以用以下联系方式跟我交流：

* QQ: 478271233
* 邮箱：<478271233@qq.com>
* GitHub: [@missmess](https://github.com/missmess)

---
######CopyRight：`missmess`

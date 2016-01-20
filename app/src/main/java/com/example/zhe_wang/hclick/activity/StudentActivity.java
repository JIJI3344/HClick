package com.example.zhe_wang.hclick.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.zhe_wang.hclick.R;
import com.example.zhe_wang.hclick.data.StudentList;
import com.example.zhe_wang.hclick.tools.AccessToServer;
import com.example.zhe_wang.hclick.tools.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String url = Url.url + "studentAndroid";
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        int ClassId = i.getIntExtra("classID",0);
        Log.d("StudentActivity", "ClassId:" + ClassId);

        toInternet(ClassId);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setmListView(String Message){
        listView = (ListView)findViewById(R.id.list);

        Type type = new TypeToken<ArrayList<StudentList>>(){}.getType();

        ArrayList<StudentList> studentLists = new Gson().fromJson(Message,type);//JsonArray 解析成list形式

        List<HashMap<String ,Object>> data = new ArrayList<HashMap<String , Object>>();

        for(StudentList stu : studentLists){
            HashMap<String , Object> item = new HashMap<String , Object>();
            item.put("Sno", stu.Sno);
            item.put("Sname", stu.Sname);
            item.put("Ssex", stu.Ssex);
            item.put("Spercent",stu.Spercent);
            data.add(item);
        }

        View header;
        header = getLayoutInflater().inflate(R.layout.list_item,null);
        ((TextView)header.findViewById(R.id.Sno)).setText("学号");
        ((TextView)header.findViewById(R.id.Sname)).setText("姓名");
        ((TextView)header.findViewById(R.id.Ssex)).setText("性别");
        ((TextView)header.findViewById(R.id.Spercent)).setText("到课比");
        listView.addHeaderView(header);

        SimpleAdapter spa = new SimpleAdapter(this, data, R.layout.list_item,
                new String[]{"Sno","Sname","Ssex","Spercent"}, new int[]{R.id.Sno,R.id.Sname,R.id.Ssex,R.id.Spercent});

        listView.setAdapter(spa);
    }
    private void toInternet(final int classId) {
        //1.创建一个Handler类对象，并重写handleMessage()方法
        final Handler myHandler = new Handler() {
            public void handleMessage(Message msg) {
                //读取obj发送过来的消息
                Log.d("StudentActivity", "msg.obj:" + msg.obj);

                setmListView((String)msg.obj);


            }
        };
        //2.创建一个子线程123456
        //当服务器没有开启时应用会异常终止，这里我们需要处理这个异常防止程序异常退出
        new Thread(new Runnable() {
            public void run() {
                AccessToServer login = new AccessToServer(url);
                String result = login.doGet(new String[]{"id"}, new String[]{String.valueOf(classId)});
                Message msg = new Message();
                msg.obj = result;
                myHandler.sendMessage(msg);

            }
        }).start();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(StudentActivity.this, SettingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent intent = new Intent();
            intent.setClass(StudentActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_class) {
            Intent intent = new Intent();
            intent.setClass(StudentActivity.this, ClassActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_record) {
            Intent intent = new Intent();
            intent.setClass(StudentActivity.this, RecordActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent();
            intent.setClass(StudentActivity.this, SettingActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

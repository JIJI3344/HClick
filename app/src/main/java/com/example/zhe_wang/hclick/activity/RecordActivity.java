package com.example.zhe_wang.hclick.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.zhe_wang.hclick.R;
import com.example.zhe_wang.hclick.data.ClassList;
import com.example.zhe_wang.hclick.data.QuestionsList;
import com.example.zhe_wang.hclick.tools.AccessToServer;
import com.example.zhe_wang.hclick.tools.Url;
import com.google.gson.Gson;

public class RecordActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String url = Url.url + "questionsAndroid";
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toInternet();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void ListView(String[] time , final int[] ID){

        mListView = (ListView)findViewById(R.id.list);

        //Declaring Array adapter
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, time);
        //Setting the ListView's adapter to the newly created adapter
        mListView.setAdapter(timeAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //The position where the list item is clicked is obtained from the
                //the parameter position
                int itemPosition = position;

                //Get the String value of the item where the user clicked
                String itemValue = (String) mListView.getItemAtPosition(position);

                //In order to start displaying new activity we need an intent
                Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);

                //Putting the Id of image as an extra in intent
                intent.putExtra("classID", ID[position]);

                //Here we will pass the previously created intent as parameter
                startActivity(intent);

            }
        });
    }
    private void toInternet() {
        //1.创建一个Handler类对象，并重写handleMessage()方法
        final Handler myHandler = new Handler() {
            public void handleMessage(Message msg) {
                //读取obj发送过来的消息
                Log.d("RecordActivity", "msg.obj:" + msg.obj);

                QuestionsList[] questionsList = new Gson().fromJson((String) msg.obj, QuestionsList[].class);

                Log.d("RecordActivity", "questionsList:" + questionsList);
                int[] id = new int[questionsList.length];
                String[] time = new String[questionsList.length];
                for (int i = 0 ; i< questionsList.length ; i++){
                    id[i] = Integer.parseInt(questionsList[i].id);
                    time[i] = questionsList[i].time;

                    Log.d("RecordActivity"," id: " +id[i] + " time: "+time[i]);

                    ListView(time,id);
                }

            }
        };
        //2.创建一个子线程
        //当服务器没有开启时应用会异常终止，这里我们需要处理这个异常防止程序异常退出
        new Thread(new Runnable() {
            public void run() {
                AccessToServer login = new AccessToServer(url);
                String result = login.doGet(new String[]{"id"}, new String[]{Url.USER_ID});
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
            intent.setClass(RecordActivity.this, SettingActivity.class);
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
            intent.setClass(RecordActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_class) {
            Intent intent = new Intent();
            intent.setClass(RecordActivity.this, ClassActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_record) {
            Intent intent = new Intent();
            intent.setClass(RecordActivity.this, RecordActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent();
            intent.setClass(RecordActivity.this, SettingActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

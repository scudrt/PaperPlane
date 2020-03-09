package com.paperplane.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.paperplane.Manager.ChatClientManager;
import com.paperplane.Adapter.FriendHeaderAdapter;
import com.paperplane.Data.FriendListView;
import com.paperplane.Network.NetworkService;
import com.paperplane.R;
import com.paperplane.Network.SimpleClient;

public class MainActivity extends Activity {
    /**
     * 列表
     */
    private FriendListView explistview;
    private String[][] childrenData=new String[10][10];
    private String[] groupData=new String[10];
    private int expandFlag=-1;
    private FriendHeaderAdapter adapter;
    private Intent networkIntent = null;
    /**
     * 测试用的活动
      */
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onDestroy(){
        System.out.println("stopping");
        if (SimpleClient.currentAskingClient != null){ //close ask_message socket connection
            stopService(this.networkIntent);
            SimpleClient.currentAskingClient.close();
            System.out.println("asking client closed");
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * 初始化
         */
        /*
        this.initView();
        this.initData();
        */

        /**
         * 启动网络服务
         */

        ChatClientManager chatClientManager = ChatClientManager.getInstance();

        Button button = (Button)findViewById(R.id.test_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, ChatListWindow.class);
                startActivity(intent);
            }
        });

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /**
     * 数据初始化
     */
    private void initData() {
        for(int i=0;i<10;i++){
            groupData[i] ="fenzu"+i;
        }

        for(int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                childrenData[i][j] = "haoyou"+i+-+j;
            }
        }
        //设置悬浮头部VIEW
        this.explistview.setHeaderView(getLayoutInflater().inflate(R.layout.friend_list_ghead, explistview, false));
        adapter = new FriendHeaderAdapter(childrenData, groupData, getApplicationContext(),explistview);
        this.explistview.setAdapter(adapter);

        //设置单个分组展开
        //explistview.setOnGroupClickListener(new GroupClickListener());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    /**
     * 初始化view
     */
    private void initView(){
        this.explistview = findViewById(R.id.explistview);
    }
/*
    class GroupClickListener implements ExpandableListView.OnGroupClickListener{

        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            if (expandFlag == -1) {
                // 展开被选的group
                explistview.expandGroup(groupPosition);
                // 设置被选中的group置于顶端
                explistview.setSelectedGroup(groupPosition);
                expandFlag = groupPosition;
            } else if (expandFlag == groupPosition) {
                explistview.collapseGroup(expandFlag);
                expandFlag = -1;
            } else {
                explistview.collapseGroup(expandFlag);
                // 展开被选的group
                explistview.expandGroup(groupPosition);
                // 设置被选中的group置于顶端
                explistview.setSelectedGroup(groupPosition);
                expandFlag = groupPosition;
            }
            return true;
        }
    }
    */
}

package example.com.empreinte.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import example.com.empreinte.Adapter.ShareContentAdapter;
import example.com.empreinte.Bean.ShareContent;
import example.com.empreinte.Dao.ShareContentDao;
import example.com.empreinte.R;

/**
 * Created by huanghaojian on 17/6/19.
 */

public class MyShareContentListActivity extends BaseActivity{
    private EditText search;
    private Button searchButton;
    private RecyclerView recyclerView;
    private ShareContentAdapter adapter;
    private List<ShareContent> myshareList=new ArrayList<>();
    public static final String CONTENTDELETE_BROADCAST_ACTION="xingyingyue.contentDelete.BROADCAST";
    private IntentFilter intentFilter;
    private ContentDeleteBackBroadcastReceiver contentDeleteBackBroadcastReceiver=new ContentDeleteBackBroadcastReceiver();
    @Override
    protected void onCreate(Bundle savedInstancedState){
        super.onCreate(savedInstancedState);
        setContentView(R.layout.share_content_list);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        initList();
        initView();

        intentFilter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(CONTENTDELETE_BROADCAST_ACTION);
        registerReceiver(contentDeleteBackBroadcastReceiver,intentFilter);

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(contentDeleteBackBroadcastReceiver);
    }
    void initView(){
        TextView title=(TextView) findViewById(R.id.title_text);
        title.setText("我的分享");
        search=(EditText)findViewById(R.id.share_content_search);
        searchButton=(Button)findViewById(R.id.share_content_search_button);
        searchButton.setOnClickListener(this);
        Button back=(Button)findViewById(R.id.title_back);
        back.setOnClickListener(this);
        recyclerView=(RecyclerView)findViewById(R.id.share_content_list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter=new ShareContentAdapter(myshareList);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.share_content_search_button:
                searchResult();
                break;
            default:
                break;
        }
    }
    private void searchResult(){
        ShareContentDao shareContentDao=new ShareContentDao();
        if (search.getText().toString().trim().equals("")){
            myshareList=shareContentDao.queryShareContentList(MyShareContentListActivity.this);
        }else{
            myshareList=shareContentDao.queryShareContentListByTitle(MyShareContentListActivity.this,search.getText().toString().trim());
        }
        ShareContentAdapter newAdapter=new ShareContentAdapter(myshareList);
        recyclerView.setAdapter(newAdapter);
    }
    private void initList(){
        ShareContentDao shareContentDao=new ShareContentDao();
        myshareList=shareContentDao.queryShareContentList(MyShareContentListActivity.this);
    }
    //接收内容删除事件广播的广播接收器
    private class ContentDeleteBackBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(CONTENTDELETE_BROADCAST_ACTION)) {
                ShareContentDao shareContentDao=new ShareContentDao();
                myshareList=shareContentDao.queryShareContentList(MyShareContentListActivity.this);
                ShareContentAdapter newAdapter=new ShareContentAdapter(myshareList);
                recyclerView.setAdapter(newAdapter);
            }
        }
    }
    public static void actionStart(Context context){
        Intent intent=new Intent(context,MyShareContentListActivity.class);
        context.startActivity(intent);
    }

}

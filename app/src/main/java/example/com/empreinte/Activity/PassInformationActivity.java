package example.com.empreinte.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import example.com.empreinte.Bean.Pass;
import example.com.empreinte.Dao.PassDao;
import example.com.empreinte.R;

/**
 * Created by huanghaojian on 17/7/3.
 */

public class PassInformationActivity extends BaseActivity{
    private TextView passName;
    private TextView passIntroduction;
    private TextView passState;
    private String place=null;
    private String content=null;
    private int state=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pass_information);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getData();
        initView();
    }
    private void initView(){
        passName=(TextView)findViewById(R.id.pass_info_name);
        passIntroduction=(TextView)findViewById(R.id.pass_introduction);
        passState=(TextView)findViewById(R.id.pass_state);
        passName.setText(place);
        passIntroduction.setText(content);
        if(state==0){
            passState.setText("未点亮");
            passState.setTextColor(getResources().getColor(R.color.colorAccent));
        }else{
            passState.setText("已点亮");
            passState.setTextColor(getResources().getColor(R.color.text_green));
        }
    }

    //通过传来的经纬度从数据库取得pass信息
    private void getData(){
        Intent intent=getIntent();
        double latitude=intent.getDoubleExtra("latitude",0);
        double longitude=intent.getDoubleExtra("longitude",0);
        Log.e("test",String.valueOf(latitude));
        PassDao passDao=new PassDao();
        List<Pass> passList=passDao.queryPassByplace(this,latitude,longitude);
        if(passList.size()>0){
            Pass pass=passList.get(0);
            place=pass.getAddress();
            content=pass.getContent();
            state=pass.getState();
        }else{
            place="随机百宝箱";
            content="随机百宝箱";
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public static void actionStart(Context context,double latitude,double longitude){
        Intent intent=new Intent(context,PassInformationActivity.class);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        context.startActivity(intent);
    }
}

package example.com.empreinte.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import example.com.empreinte.Bean.User;
import example.com.empreinte.Dao.UserDao;
import example.com.empreinte.R;


/**
 * Created by huanghaojian on 17/5/28.
 */

public class ModifyRoleInfoActivity extends BaseActivity{
    private EditText EditUserName;
    private EditText EditAge;
    private EditText EditSex;
    private Button sureModifyButton;
    private List<User>users=new ArrayList<>();
    private UserDao userDao=new UserDao();
    private String userAccount;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        setContentView(R.layout.modify_role_information);
        initView();
        getData();
    }
    private void initView(){
        EditUserName=(EditText)findViewById(R.id.username_);
        EditAge=(EditText)findViewById(R.id.modify_role_age);
        EditSex=(EditText)findViewById(R.id.modify_role_sex);
        sureModifyButton=(Button)findViewById(R.id.modify_role_button);
        sureModifyButton.setOnClickListener(this);
    }
    private void getData(){
        Intent intent=getIntent();
        userAccount=intent.getStringExtra("userAccount");
        password=intent.getStringExtra("password");
        users=userDao.queryRoleInfoByUserAccount(this,userAccount);
        if(users.size()>0){
            User user=users.get(0);
            EditUserName.setText(user.getUserName());
            EditAge.setText(user.getAge().toString());
            EditSex.setText(user.getSex());
            Log.e("test",user.getUserName());
        }else{
            EditUserName.setHint("未填写");
            EditAge.setHint("未填写");
            EditSex.setHint("未填写");
        }
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.modify_role_button:
                store();
                Intent intent=new Intent("xingyingyue.modifyRoleInfoback.BROADCAST");
                intent.putExtra("userName",EditUserName.getText().toString().trim());
                sendBroadcast(intent);
                finish();
                break;
            default:
                break;
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
    public void store(){
        User user=new User();
        user.setUserAccount(userAccount);
        user.setUserName(EditUserName.getText().toString().trim());
        user.setSex(EditSex.getText().toString().trim());
        user.setAge(Integer.parseInt(EditAge.getText().toString().trim()));
        if(users.size()>0){
            userDao.updateRoleInfo(this,userAccount,user);

        }else{
            userDao.insertRoleInfo(this,user);
        }
    }
    public static void actionStart(Context context,String userAccount,String password){
        Intent intent=new Intent(context,ModifyRoleInfoActivity.class);
        intent.putExtra("userAccount",userAccount);
        intent.putExtra("password",password);
        context.startActivity(intent);
    }
}


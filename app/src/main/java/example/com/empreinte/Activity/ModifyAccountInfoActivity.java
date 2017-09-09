package example.com.empreinte.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import example.com.empreinte.R;
import example.com.empreinte.Util.OkHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by huanghaojian on 17/6/28.
 */

public class ModifyAccountInfoActivity extends BaseActivity{
    private String userAccount;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_account_information);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //获取从登录界面传来的账号密码
        Intent intent=getIntent();
        userAccount=intent.getStringExtra("userAccount");
        password=intent.getStringExtra("password");

        final TextView pwdHint,pwdReHint;
        pwdHint = (TextView)findViewById(R.id.pwdHint);
        pwdReHint = (TextView)findViewById(R.id.pwdReHint);
        pwdHint.setVisibility(View.INVISIBLE);
        pwdReHint.setVisibility(View.INVISIBLE);

        final Button sureModify;
        sureModify = (Button)findViewById(R.id.btnmakesuremodify);
        sureModify.setVisibility(View.INVISIBLE);


        final EditText ModPwd,ModPwdAgain;
        ModPwd = (EditText)findViewById(R.id.ModPwd);
        ModPwdAgain = (EditText)findViewById(R.id.ModPwdAgain);
        ModPwd.setVisibility(View.INVISIBLE);
        ModPwdAgain.setVisibility(View.INVISIBLE);

        final TextView myUserAccount;
        myUserAccount=(TextView)findViewById(R.id.userAccount_);
        myUserAccount.setText(userAccount);

        Button btnchangepwd = (Button)findViewById(R.id.btnchangepsw);
        btnchangepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pwdHint.setVisibility(View.VISIBLE);
                pwdReHint.setVisibility(View.VISIBLE);
                sureModify.setVisibility(View.VISIBLE);
                ModPwd.setVisibility(View.VISIBLE);
                ModPwdAgain.setVisibility(View.VISIBLE);

            }
        });
        Button btnlogout=(Button)findViewById(R.id.btnlogout);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //logout(userAccount);
                LoginActvity.actionStart(ModifyAccountInfoActivity.this);
                finish();
            }
        });

        sureModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ModPwd.getText().toString().trim().equals("")||ModPwdAgain.getText().toString().trim().equals("")) {
                    Toast.makeText(ModifyAccountInfoActivity.this,"请输入完整的信息",Toast.LENGTH_LONG).show();
                }else
                if(ModPwd.getText().toString().trim().equals(ModPwdAgain.getText().toString().trim())) {
                    //modifyFunc(userAccount, password, ModPwd.getText().toString().trim());
                    Toast.makeText(ModifyAccountInfoActivity.this,"因为后台没做好，网络请求部分的代码背注释了",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ModifyAccountInfoActivity.this,"两次的密码输入不一致",Toast.LENGTH_LONG).show();
                }
            }
        });
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

    public void modifyFunc(final String userAccount,final String oldPassword,final String newPassword){
        String modifyPasswordUrl="http://110.64.90.22:8080/user/modifypassword?userAccount="+userAccount+"&oldPassword="+oldPassword+
                "&newPassword="+newPassword;
        OkHttpUtil.sendOkHttpRequest(modifyPasswordUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ModifyAccountInfoActivity.this,"修改密码失败",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String response1=response.body().string();
                Log.e("test",response1);
                if(response1.trim().equals("0")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ModifyAccountInfoActivity.this, "用户不存在或密码不正确", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                if(response1.trim().equals("2")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ModifyAccountInfoActivity.this, "修改失败", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                if(response1.trim().equals("1")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ModifyAccountInfoActivity.this, "修改成功,退出重新登录", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent("xingyingyue.logout.BROADCAST");
                            sendBroadcast(intent);
                            LoginActvity.actionStart(ModifyAccountInfoActivity.this);
                            finish();
                        }
                    });
                }
            }
        });
    }

    private void logout(String userAccount){
        String logoutUrl="http://110.64.90.22:8080/login/logout?userAccount="+userAccount;

        OkHttpUtil.sendOkHttpRequest(logoutUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ModifyAccountInfoActivity.this,"登出失败，请检查网络",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String response1=response.body().string();
                Log.e("test",response1);
                if(response1.trim().equals("0")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ModifyAccountInfoActivity.this, "登出失败，请重试", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                if(response1.trim().equals("1")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ModifyAccountInfoActivity.this, "登出成功", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent("xingyingyue.logout.BROADCAST");
                            sendBroadcast(intent);
                            LoginActvity.actionStart(ModifyAccountInfoActivity.this);
                            finish();
                        }
                    });
                }
            }
        });
    }
    public static void actionStart(Context context,String userAccount,String password){
        Intent intent=new Intent(context,ModifyAccountInfoActivity.class);
        intent.putExtra("userAccount",userAccount);
        intent.putExtra("password",password);
        context.startActivity(intent);
    }
}


package example.com.empreinte.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import example.com.empreinte.Bean.User;
import example.com.empreinte.Dao.UserDao;
import example.com.empreinte.R;
import example.com.empreinte.Util.OkHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by huanghaojian on 17/6/14.
 */

public class LoginActvity extends BaseActivity  {
    private static final String FILENAME = "xth";
    private EditText inputAccount;
    private EditText inputPassword;
    private Button login;
    private Button register;
    private TextView forgetPassword;
    private UserDao userDao=new UserDao();
    private CheckBox optionRemember;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        setContentView(R.layout.login);
        initView();
    }
    private void initView(){
        inputAccount=(EditText)findViewById(R.id.input_account);
        inputPassword=(EditText)findViewById(R.id.input_password);
        login=(Button)findViewById(R.id.button_login);
        register=(Button)findViewById(R.id.button_register);
        forgetPassword=(TextView)findViewById(R.id.button_forget_password);
        optionRemember=(CheckBox)findViewById(R.id.option_remember_password);

        optionRemember.setOnClickListener(this);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);

        if (readBoolean("option")) {
            optionRemember.setChecked(true);
            inputPassword.setText(readData("password").toString());
            inputAccount.setText(readData("userAccount").toString());
        }

    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_login:
                final String phoneNumber=inputAccount.getText().toString().trim();
                final String password=inputPassword.getText().toString().trim();
                if(phoneNumber.trim().equals("")||phoneNumber.length()<0||password.trim().equals("")||password.length()<0){
                    Toast.makeText(this,"请输入完整信息",Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    //loginFunc(phoneNumber,password);
                }
                loadAccountInfo();
                writeData(inputAccount.getText().toString().trim(),inputPassword.getText().toString().trim(),optionRemember.isChecked());
                MainActivity.actionStart(LoginActvity.this,inputAccount.getText().toString().trim(),inputPassword.getText().toString().trim());
                finish();
                break;
            case R.id.button_register:
                RegisterActivity.actionStart(LoginActvity.this);
                break;
            case R.id.button_forget_password:
                ForgetPasswordActivity.actionStart(LoginActvity.this);
                break;
            case R.id.option_remember_password:
                writeBoolean(optionRemember.isChecked());
                break;
            default:
                break;
        }
    }

    private void writeBoolean(boolean option) {
        SharedPreferences.Editor share_edit = getSharedPreferences(FILENAME,
                MODE_MULTI_PROCESS).edit();
        share_edit.putBoolean("option", option);
        share_edit.commit();
    }
    private boolean writeData(String userAccount, String password, boolean option) {
        SharedPreferences.Editor share_edit = getSharedPreferences(FILENAME,
                MODE_MULTI_PROCESS).edit();
        share_edit.putString("userAccount",userAccount);
        share_edit.putString("password",password);
        share_edit.putBoolean("option,",option);
        share_edit.commit();
        return true;
    }
    private String readData(String data) {
        SharedPreferences pref = getSharedPreferences(FILENAME, MODE_MULTI_PROCESS);
        String str = pref.getString(data, "");
        return str;
    }

    private boolean readBoolean(String data) {
        SharedPreferences pref = getSharedPreferences(FILENAME, MODE_MULTI_PROCESS);
        return pref.getBoolean(data, false);
    }

    public static void actionStart(Context context){
        Intent intent=new Intent(context,LoginActvity.class);
        context.startActivity(intent);
    }
    public void loginFunc(final String userAccount,final String password){
        String registerUrl="http://110.64.90.22:8080/login/login?userAccount="+userAccount+"&password="+password;
        OkHttpUtil.sendOkHttpRequest(registerUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActvity.this,"登录失败",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String response1=response.body().string();
                System.out.println(response1);
                if(response1.trim().equals("0")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActvity.this, "用户不存在或密码不正确", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                if(response1.trim().equals("1")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(LoginActvity.this, "该账号正在登录", Toast.LENGTH_LONG).show();
                            Toast.makeText(LoginActvity.this, "登录成功", Toast.LENGTH_LONG).show();
                            loadAccountInfo();
                            writeData(inputAccount.getText().toString().trim(),inputPassword.getText().toString().trim(),optionRemember.isChecked());
                            MainActivity.actionStart(LoginActvity.this,inputAccount.getText().toString().trim(),inputPassword.getText().toString().trim());
                            finish();
                        }
                    });
                }
                if(response1.trim().equals("2")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActvity.this, "登录成功", Toast.LENGTH_LONG).show();
                            loadAccountInfo();
                            writeData(inputAccount.getText().toString().trim(),inputPassword.getText().toString().trim(),optionRemember.isChecked());
                            MainActivity.actionStart(LoginActvity.this,inputAccount.getText().toString().trim(),inputPassword.getText().toString().trim());
                            finish();
                        }
                    });
                }
            }
        });
    }
    public void loadAccountInfo(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<User>userList=userDao.queryRoleInfoByUserAccount(LoginActvity.this,inputAccount.getText().toString().trim());
                if(userList.size()>0){

                }else{
                    userDao.loadRoleInfo(LoginActvity.this,inputAccount.getText().toString().trim());
                }
            }
        });
    }
}

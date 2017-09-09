package example.com.empreinte.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import example.com.empreinte.R;
import example.com.empreinte.Util.OkHttpUtil;
import example.com.empreinte.View.MyToast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by huanghaojian on 17/5/28.
 */

public class ForgetPasswordActivity extends BaseActivity {
    private EditText inputPhoneNumber;
    private EditText inputPassword;
    private EditText inputVerificationCode;
    private Button getVerificationCode;
    private Button modifyPassword;
    private Button back;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        setContentView(R.layout.forget_password);
        initView();
    }
    private void initView(){
        inputPhoneNumber=(EditText)findViewById(R.id.forget_password_input_phone_number);
        inputPassword=(EditText)findViewById(R.id.forget_password_input_password);
        inputVerificationCode=(EditText)findViewById(R.id.forget_password_input_verification_code);
        getVerificationCode=(Button)findViewById(R.id.forget_password_get_verification_code);
        modifyPassword=(Button)findViewById(R.id.forget_password_button);
        back=(Button)findViewById(R.id.title_back);
        title=(TextView)findViewById(R.id.title_text);

        title.setText("忘记密码");

        getVerificationCode.setOnClickListener(this);
        modifyPassword.setOnClickListener(this);
        back.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.title_back:
                finish();
                break;
            case R.id.forget_password_get_verification_code:
                Toast.makeText(this,"click getVerificationCode",Toast.LENGTH_SHORT).show();
                break;
            case R.id.forget_password_button:
                final String phoneNumber=inputPhoneNumber.getText().toString().trim();
                final String password=inputPassword.getText().toString().trim();
                String code=inputVerificationCode.getText().toString().trim();
                if(phoneNumber.trim().equals("")||phoneNumber.length()==0||password.trim().equals("")||password.length()==0){
                    Toast.makeText(this,"请输入完整信息",Toast.LENGTH_SHORT).show();
                    break;
                }else if(isMobileNo(phoneNumber)==false){
                    MyToast.makeText(ForgetPasswordActivity.this,"请输入正确的手机号",Toast.LENGTH_LONG).show();
                }else if (isMobileNo(phoneNumber)){
                    //forgetPasswordFunc(phoneNumber,password);
                    Toast.makeText(ForgetPasswordActivity.this,"因为后台没做好，网络请求部分的代码背注释了",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }
    private void forgetPasswordFunc(String userAccount,String password){
        String forgetPasswordUrl="http://110.64.90.22:8080/login/forgetpassword?userAccount="+userAccount+"&newPassword="+password;
        OkHttpUtil.sendOkHttpRequest(forgetPasswordUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ForgetPasswordActivity.this,"修改失败，请检查网络情况",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String response1=response.body().string();
                System.out.println(response1);
                if(response1.trim().equals("0")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ForgetPasswordActivity.this, "账号不存在，可能未注册", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                if(response1.trim().equals("1")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ForgetPasswordActivity.this, "修改成功", Toast.LENGTH_LONG).show();
                            LoginActvity.actionStart(ForgetPasswordActivity.this);
                            finish();
                        }
                    });
                }
                if(response1.trim().equals("2")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ForgetPasswordActivity.this, "修改失败，请重试", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
    private boolean isMobileNo(String mobiles){
        String telRegex="[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)){
            return false;
        }else{
            return mobiles.matches(telRegex);
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ForgetPasswordActivity.class);
        context.startActivity(intent);
    }
}


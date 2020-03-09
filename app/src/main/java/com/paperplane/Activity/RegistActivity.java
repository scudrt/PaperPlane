package com.paperplane.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v7.app.AppCompatActivity;

import com.alibaba.fastjson.*;
import com.paperplane.R;
import com.paperplane.Manager.UserAccountClientManager;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextP,editTextPWD0,editTextPWD1;//输入框：电话、密码、再次输入密码
    private Button button,SMSBtn;//注册按钮、验证码按钮
    private ImageView returnImage;//返回按钮
    private TextView enterText;//登陆按钮
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_regist);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        editTextP=(EditText)findViewById(R.id.user_id);
        editTextPWD0=(EditText)findViewById(R.id.password_0);
        editTextPWD1=(EditText) findViewById(R.id.password_1);
        button=(Button)findViewById(R.id.btn_now_register);
        enterText=(TextView)findViewById(R.id.tv_enter);
        returnImage=(ImageView) findViewById(R.id.iv_return);
        SMSBtn=(Button)findViewById(R.id.bn_sms_code);
        button.setOnClickListener(this);
        enterText.setOnClickListener(this);
        returnImage.setOnClickListener(this);
        SMSBtn.setOnClickListener(this);
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        //todo: modify it
        switch (v.getId()){
            case R.id.btn_now_register:
                register();
                break;
            case R.id.tv_enter:
            case R.id.iv_return:
                returnEnter();
                break;
            case R.id.bn_sms_code:
                String phone=editTextP.getText().toString().trim();
                if (phone.length() < 4){
                    Toast.makeText(this, "用户名过短", Toast.LENGTH_SHORT).show();
                }else if (phone.contains(" ")){
                    Toast.makeText(this, "打字不要带空格哦", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "用户名合法", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void register() {
        final String username = editTextP.getText().toString().trim();
        final String pwd0 = editTextPWD0.getText().toString().trim();
        final String pwd1 = editTextPWD1.getText().toString().trim();
        //check locally
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "未输入用户名", Toast.LENGTH_SHORT).show();
            editTextP.requestFocus();
            return;
        } else if (username.length() < 4) {
            Toast.makeText(this, "用户名过短", Toast.LENGTH_SHORT).show();
            editTextP.requestFocus();
            return;
        }else if (username.contains(" ")) {
            Toast.makeText(this, "用户名不可带有空格", Toast.LENGTH_SHORT).show();
            editTextP.requestFocus();
            return;
        } else if (TextUtils.isEmpty(pwd0)) {
            Toast.makeText(this, "未设置密码", Toast.LENGTH_SHORT).show();
            editTextPWD0.requestFocus();
            return;
        } else if (TextUtils.isEmpty(pwd1)) {
            Toast.makeText(this, "请再次输入密码", Toast.LENGTH_SHORT).show();
            editTextPWD1.requestFocus();
            return;
        } else if (!pwd0.equals(pwd1)) {
            Toast.makeText(this, "两次密码不相同", Toast.LENGTH_SHORT).show();
            editTextPWD1.requestFocus();
            return;
        }

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("正在注册...");
        pd.show();

        new Thread(new Runnable() {
                @Override
                public void run () {
                    JSONObject json = new JSONObject();
                    //send sign up message
                    json.put("userID", username);
                    json.put("password", pwd0);
                    final JSONObject res = UserAccountClientManager.getInstance().signup(json);
                    pd.dismiss();
                    Looper.prepare();
                    new Handler().post(new Runnable(){
                        public void run(){
                            solveRegisterResult(res);
                        }
                    });
                    Looper.loop();
                }
            }
        ).start();
    }

    private void solveRegisterResult(JSONObject res){
        if (res.getBoolean("result")){
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            returnEnter();
        }else{
            Toast.makeText(this, "server: " + res.getString("response"), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 跳转到登陆界面
     */
    private void returnEnter() {
        Intent intent=new Intent(this, EnterActivity.class);
        startActivity(intent);
        finish();
    }
}

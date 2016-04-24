package cn.edu.nju.software.tongbaoshipper.view1.activity1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.edu.nju.software.tongbaoshipper.R;
import cn.edu.nju.software.tongbaoshipper.common1.PostRequest;
import cn.edu.nju.software.tongbaoshipper.common1.User;
import cn.edu.nju.software.tongbaoshipper.constant.Common;
import cn.edu.nju.software.tongbaoshipper.constant.Net;
import cn.edu.nju.software.tongbaoshipper.constant.Prefs;
import cn.edu.nju.software.tongbaoshipper.service1.UserService;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etPhone, etPassword;
    private TextView tvLoginError;
    protected LinearLayout btnRegister;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        requestQueue = Volley.newRequestQueue(this);
    }

    /**
     * init view
     */
    private void initView() {
        etPhone = (EditText) findViewById(R.id.login_et_phone);
        etPassword = (EditText) findViewById(R.id.login_et_password);
        tvLoginError = (TextView) findViewById(R.id.login_tv_error);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.login_btn_back);
        LinearLayout btnLogin = (LinearLayout) findViewById(R.id.login_btn);
        btnRegister = (LinearLayout) findViewById(R.id.login_btn_register);

        // 添加监听事件
        tvLoginError.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn_back:
                Log.d(LoginActivity.class.getName(), "back");
                finish();
                break;
            case R.id.login_btn:
                Log.d(LoginActivity.class.getName(), "login");
                Map<String, String> params = new HashMap<>();
                params.put("phoneNumber", etPhone.getText().toString());
                params.put("password", etPassword.getText().toString());
                params.put("type", String.valueOf(Common.USER_TYPE_SHIPPER));
                Request<JSONObject> request = new PostRequest(Net.URL_USER_LOGIN,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Log.d(LoginActivity.class.getName(), jsonObject.toString());
                                try {
                                    if (UserService.login(jsonObject, etPhone.getText().toString(),
                                            etPassword.getText().toString(), Common.USER_TYPE_SHIPPER)) {
                                        // local record user message
                                        UserService.saveObject(LoginActivity.this, Prefs.PREF_KEY_USER, User.getInstance());
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, UserService.getErrorMsg(jsonObject),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Log.e(LoginActivity.class.getName(), volleyError.getMessage(), volleyError);
                                Toast.makeText(LoginActivity.this, LoginActivity.this.getResources().getString(R.string.network_error),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }, params);

                requestQueue.add(request);
                break;
            case R.id.login_tv_error:
                Log.d(LoginActivity.class.getName(), "error");
                break;
            case R.id.login_btn_register:
                Log.d(LoginActivity.class.getName(), "register");
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                Log.e(LoginActivity.class.getName(), "Unknown id");
        }
    }
}

package cn.edu.nju.software.tongbaoshipper.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.edu.nju.software.tongbaoshipper.View.Adapter.AccountAdapter;
import cn.edu.nju.software.tongbaoshipper.Common.Account;
import cn.edu.nju.software.tongbaoshipper.Common.PostRequest;
import cn.edu.nju.software.tongbaoshipper.Common.User;
import cn.edu.nju.software.tongbaoshipper.Const.Net;
import cn.edu.nju.software.tongbaoshipper.R;
import cn.edu.nju.software.tongbaoshipper.Service.UserService;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvYear, tvMonth;
    private ListView lvAccount;
    private LinearLayout vEmpty;
    private AccountAdapter accountAdapter;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        initViw();
    }

    private void initViw() {
        tvYear = (TextView) findViewById(R.id.account_tv_year);
        tvMonth = (TextView) findViewById(R.id.account_tv_month);
        lvAccount = (ListView) findViewById(R.id.account_lv);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.account_btn_back);
        LinearLayout btnTime = (LinearLayout) findViewById(R.id.account_btn_time);
        vEmpty = (LinearLayout) findViewById(R.id.account_empty);

        btnBack.setOnClickListener(this);
        btnTime.setOnClickListener(this);

        if (!User.isLogin()) {
            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        // 设置默认账单时间
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        tvYear.setText(String.format("%d%s", year, this.getResources().getString(R.string.year)));
        tvMonth.setText(String.format("%02d%s", month, this.getResources().getString(R.string.month)));

        // init account detail view
        requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        Request<JSONObject> request = new PostRequest(Net.URL_USER_SHOW_ACCOUNT,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d(AccountActivity.class.getName(), jsonObject.toString());
                        try {
                            if (UserService.getResult(jsonObject)) {
                                // 初始化账单数据
                                ArrayList<Account> arrAccount = UserService.showAccount(jsonObject);
                                accountAdapter = new AccountAdapter(AccountActivity.this, arrAccount);
                                lvAccount.setAdapter(accountAdapter);
                                if (arrAccount.size() > 0) {
                                    vEmpty.setVisibility(View.INVISIBLE);
                                } else {
                                    vEmpty.setVisibility(View.VISIBLE);
                                    lvAccount.setEmptyView(vEmpty);
                                }
                            } else {
                                Toast.makeText(AccountActivity.this, UserService.getErrorMsg(jsonObject),
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
                        Log.e(AccountActivity.class.getName(), volleyError.getMessage(), volleyError);
                        // http authentication 401
//                        if (volleyError.networkResponse.statusCode == Net.NET_ERROR_AUTHENTICATION) {
//                            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                            return;
//                        }
                        Toast.makeText(AccountActivity.this, AccountActivity.this.getResources().getString(R.string.network_error),
                                Toast.LENGTH_SHORT).show();
                    }
                }, params);
        requestQueue.add(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_btn_back:
                Log.d(AccountActivity.class.getName(), "back");
                finish();
                break;
            case R.id.account_btn_time:
                Log.d(AccountActivity.class.getName(), "time");
                showPopWindow();
                break;
            default:
                Log.e(AccountActivity.class.getName(), "Unknown id");
        }
    }

    /**
     * 显示底部选择时间菜单
     */
    private void showPopWindow() {

    }
}

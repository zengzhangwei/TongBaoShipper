package cn.edu.nju.software.tongbaoshipper.view.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import cn.edu.nju.software.tongbaoshipper.R;
import cn.edu.nju.software.tongbaoshipper.service.UserService;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout btnBack, btnChangePassword, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView() {
        btnBack = (LinearLayout) findViewById(R.id.setting_btn_back);
        btnChangePassword = (LinearLayout) findViewById(R.id.setting_btn_change_password);
        btnLogout = (LinearLayout) findViewById(R.id.setting_btn_logout);

        // 添加事件监听
        btnBack.setOnClickListener(this);
        btnChangePassword.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_btn_back:
                Log.d(SettingActivity.class.getName(), "back");
                finish();
                break;
            case R.id.setting_btn_change_password:
                Log.d(SettingActivity.class.getName(), "change password");
                Intent intentChangePassword = new Intent(this, ChangePasswordActivity.class);
                startActivity(intentChangePassword);
                break;
            case R.id.setting_btn_logout:
                Log.d(SettingActivity.class.getName(), "logout");
                Dialog dialog = new AlertDialog.Builder(this).setTitle(R.string.setting_logout_confirm_msg).setPositiveButton(
                        R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 用户注销
                                Log.d(SettingActivity.class.getName(), "confirm logout");
                                // just use token invalids
                                UserService.tokenInvalid(SettingActivity.this, false);
                                // login activity
                                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                ).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(SettingActivity.class.getName(), "cancel logout");
                        dialog.dismiss();
                    }
                }).create();
                dialog.show();
                break;
            default:
                Log.e(SettingActivity.class.getName(), "Unknown id");
        }
    }
}

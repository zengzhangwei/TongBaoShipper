package cn.edu.nju.software.tongbaoshipper.View.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.edu.nju.software.tongbaoshipper.R;
import cn.edu.nju.software.tongbaoshipper.View.Fragment.FragmentHome;
import cn.edu.nju.software.tongbaoshipper.View.Fragment.FragmentMy;
import cn.edu.nju.software.tongbaoshipper.View.Fragment.FragmentNearBy;
import cn.edu.nju.software.tongbaoshipper.View.Fragment.FragmentOrder;

public class FrameActivity extends AppCompatActivity implements View.OnClickListener {

    // 监听返回按键
    private static long exitTime = 0;

    private LinearLayout btnHome, btnNearby, btnOrder, btnMy;
    private ImageView ivHome, ivNearby, ivOrder, ivMy;
    private TextView tvHome, tvNearby, tvOrder, tvMy;
    private FragmentManager fm;
    private FragmentTransaction ft;
    // fragment my contact PopupWindow
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_frame);
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        btnHome = (LinearLayout) findViewById(R.id.frame_btn_home);
        btnNearby = (LinearLayout) findViewById(R.id.frame_btn_nearby);
        btnOrder = (LinearLayout) findViewById(R.id.frame_btn_order);
        btnMy = (LinearLayout) findViewById(R.id.frame_btn_my);
        ivHome = (ImageView) findViewById(R.id.frame_iv_home);
        ivNearby = (ImageView) findViewById(R.id.frame_iv_nearby);
        ivOrder = (ImageView) findViewById(R.id.frame_iv_order);
        ivMy = (ImageView) findViewById(R.id.frame_iv_my);
        tvHome = (TextView) findViewById(R.id.frame_tv_home);
        tvNearby = (TextView) findViewById(R.id.frame_tv_nearby);
        tvOrder = (TextView) findViewById(R.id.frame_tv_order);
        tvMy = (TextView) findViewById(R.id.frame_tv_my);

        // 进入应用到首页
        fm = this.getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.frame_fragment, new FragmentHome(this));
        ft.commit();

        // 添加底部按钮监听
        btnHome.setOnClickListener(this);
        btnNearby.setOnClickListener(this);
        btnOrder.setOnClickListener(this);
        btnMy.setOnClickListener(this);
    }

    /**
     * 初始化底部按钮样式
     */
    private void initBottomBtnStyle() {
        // frame图片资源重新配置
        ivHome.setImageResource(R.drawable.frame_home);
        ivNearby.setImageResource(R.drawable.frame_nearby);
        ivOrder.setImageResource(R.drawable.frame_order);
        ivMy.setImageResource(R.drawable.frame_my);
        // 初始化底部文字颜色
        tvHome.setTextColor(ContextCompat.getColor(this, R.color.font_gray));
        tvNearby.setTextColor(ContextCompat.getColor(this, R.color.font_gray));
        tvOrder.setTextColor(ContextCompat.getColor(this, R.color.font_gray));
        tvMy.setTextColor(ContextCompat.getColor(this, R.color.font_gray));

    }


    @Override
    public void onClick(View v) {
        initBottomBtnStyle();
        ft = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.frame_btn_home:
                Log.d(this.getClass().getName(), "home");
                ivHome.setImageResource(R.drawable.frame_home_pressed);
                tvHome.setTextColor(ContextCompat.getColor(this, R.color.font_blue));
                ft.replace(R.id.frame_fragment, new FragmentHome(this));
                break;
            case R.id.frame_btn_nearby:
                Log.d(this.getClass().getName(), "nearby");
                ivNearby.setImageResource(R.drawable.frame_nearby_pressed);
                tvNearby.setTextColor(ContextCompat.getColor(this, R.color.font_blue));
                ft.replace(R.id.frame_fragment, new FragmentNearBy(this));
                break;
            case R.id.frame_btn_order:
                Log.d(this.getClass().getName(), "order");
                ivOrder.setImageResource(R.drawable.frame_order_pressed);
                tvOrder.setTextColor(ContextCompat.getColor(this, R.color.font_blue));
                ft.replace(R.id.frame_fragment, new FragmentOrder(this));
                break;
            case R.id.frame_btn_my:
                Log.d(this.getClass().getName(), "my");
                ivMy.setImageResource(R.drawable.frame_my_pressed);
                tvMy.setTextColor(ContextCompat.getColor(this, R.color.font_blue));
                ft.replace(R.id.frame_fragment, new FragmentMy(this));
                break;
            default:
                Log.e(this.getClass().getName(), "unknown frame button");
                break;
        }
        ft.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
                popupWindow = null;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置popupWindow点击外部dismiss
     *
     * @param event motion event
     * @return boolean
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 显示底部联系客服菜单（FragmentMy）
     */
    public void showPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_my_contact, null);

        popupWindow = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.pop_window_anim);
        popupWindow.showAtLocation(findViewById(R.id.activity_frame), Gravity.BOTTOM, 0, 0);

        // popupWindow调整屏幕亮度
        final Window window = this.getWindow();
        final WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = 0.5f;
        window.setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                window.setAttributes(params);
            }
        });

        // popupWindow设置按键监听
        LinearLayout btnDial = (LinearLayout) view.findViewById(R.id.contact_btn_dial);
        LinearLayout btnCancel = (LinearLayout) view.findViewById(R.id.contact_btn_cancel);
        btnDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(FrameActivity.class.getName() + FragmentMy.class.getName(), "pop dial");
                Uri telUri = Uri.parse("tel:" + FrameActivity.this.getResources().getString(R.string.contact_telephone_number));
                Intent intent = new Intent(Intent.ACTION_DIAL, telUri);
                startActivity(intent);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(FrameActivity.class.getName() + FragmentMy.class.getName(), "pop cancel");
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
    }
}

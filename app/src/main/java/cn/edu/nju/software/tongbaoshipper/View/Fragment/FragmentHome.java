package cn.edu.nju.software.tongbaoshipper.View.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.nju.software.tongbaoshipper.View.Activity.AddressActivity;
import cn.edu.nju.software.tongbaoshipper.View.Activity.DriverActivity;
import cn.edu.nju.software.tongbaoshipper.View.Activity.MapActivity;
import cn.edu.nju.software.tongbaoshipper.View.Activity.MessageActivity;
import cn.edu.nju.software.tongbaoshipper.Common.PostRequest;
import cn.edu.nju.software.tongbaoshipper.Const.Common;
import cn.edu.nju.software.tongbaoshipper.Const.Net;
import cn.edu.nju.software.tongbaoshipper.R;
import cn.edu.nju.software.tongbaoshipper.View.Activity.WalletActivity;
import cn.edu.nju.software.tongbaoshipper.View.Adapter.BannerPagerAdapter;
import cn.edu.nju.software.tongbaoshipper.Service.UserService;

import static cn.edu.nju.software.tongbaoshipper.R.id.home_btn_help;

public class FragmentHome extends Fragment implements View.OnClickListener {

    private Context context;
    private List<View> ivList;
    private int[] ivIdList = {R.drawable.top_banner_android, R.drawable.shadow_article, R.drawable.avatar};

    private LinearLayout layoutDot;
    private ViewPager vpBanner;

    private TextView btnMessage, btnAddress, btnDriver,
            btnWallet, btnOrder, btnHelp;
    private RelativeLayout btnOrderTruck;

    public FragmentHome() {
        super();
    }

    @SuppressLint("ValidFragment")
    public FragmentHome(Context context) {
        super();
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(context, R.layout.fragment_home, null);
        // 初始化视图
        initView(view);
        // 初始化banner图片数据
        initBannerData(view);
        // 初始化banner事件
        initBannerAction();

        // 自动登陆
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        Map<String, String> params = new HashMap<>();
        params.put("phoneNumber", "15850775808");
        params.put("password", "12345678");
        params.put("type", String.valueOf(Common.USER_TYPE_SHIPPER));
        Request<JSONObject> request = new PostRequest(Net.URL_USER_LOGIN,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d(FragmentHome.class.getName(), jsonObject.toString());
                        try {
                            if (UserService.login(jsonObject, "15850775808",
                                    "12345678", Common.USER_TYPE_SHIPPER)) {
//                                UserService.showUserInfo();
                            } else {
                                Toast.makeText(context, UserService.getErrorMsg(jsonObject),
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
                        Log.e(FragmentHome.class.getName(), volleyError.getMessage(), volleyError);
                        Toast.makeText(context, context.getResources().getString(R.string.network_error),
                                Toast.LENGTH_SHORT).show();
                    }
                }, params);
        requestQueue.add(request);

        return view;
    }

    /**
     * 初始化视图
     *
     * @param view
     */
    private void initView(View view) {
        layoutDot = (LinearLayout) view.findViewById(R.id.home_layout_dot);
        vpBanner = (ViewPager) view.findViewById(R.id.home_vp_banner);
        btnMessage = (TextView) view.findViewById(R.id.home_btn_message);
        btnAddress = (TextView) view.findViewById(R.id.home_btn_address);
        btnDriver = (TextView) view.findViewById(R.id.home_btn_driver);
        btnWallet = (TextView) view.findViewById(R.id.home_btn_wallet);
        btnOrder = (TextView) view.findViewById(R.id.home_btn_order);
        btnHelp = (TextView) view.findViewById(R.id.home_btn_help);
        btnOrderTruck = (RelativeLayout) view.findViewById(R.id.home_btn_order_truck);

        // 添加事件监听器
        btnMessage.setOnClickListener(this);
        btnAddress.setOnClickListener(this);
        btnDriver.setOnClickListener(this);
        btnWallet.setOnClickListener(this);
        btnOrder.setOnClickListener(this);
        btnHelp.setOnClickListener(this);
        btnOrderTruck.setOnClickListener(this);
    }

    private void initBannerData(View view) {
        ivList = new ArrayList<>();
        for (int ivID : ivIdList) {
            ImageView iv = new ImageView(view.getContext());
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            iv.setImageResource(ivID);
            ivList.add(iv);

            // 设置标识点
            View vDot = new View(view.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(5, 5);
            params.leftMargin = 2;
            vDot.setLayoutParams(params);
            vDot.setBackgroundResource(R.drawable.dot_background);
            vDot.setEnabled(false);
            layoutDot.addView(vDot);
        }

        // 设置banner ViewPager adapter
        vpBanner.setAdapter(new BannerPagerAdapter(ivList));
    }

    private void initBannerAction() {
        vpBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int index = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                layoutDot.getChildAt(index).setEnabled(false);
                layoutDot.getChildAt(position).setEnabled(true);
                index = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.home_btn_message:
                Log.d(this.getClass().getName(), "message");
                Intent intentMessage = new Intent(context, MessageActivity.class);
                startActivity(intentMessage);
                break;
            case R.id.home_btn_address:
                Log.d(this.getClass().getName(), "address");
                Intent intentAddress = new Intent(context, AddressActivity.class);
                startActivity(intentAddress);
                break;
            case R.id.home_btn_driver:
                Log.d(this.getClass().getName(), "driver");
                Intent intentDriver = new Intent(context, DriverActivity.class);
                startActivity(intentDriver);
                break;
            case R.id.home_btn_wallet:
                Log.d(this.getClass().getName(), "wallet");
                Intent intentUser = new Intent(context, WalletActivity.class);
                startActivity(intentUser);
                break;
            case R.id.home_btn_order:
                Log.d(this.getClass().getName(), "order");
                Intent intentOrder = new Intent(context, MapActivity.class);
                startActivity(intentOrder);
                break;
            case home_btn_help:
                Log.d(this.getClass().getName(), "help");
                break;
            case R.id.home_btn_order_truck:
                Log.d(this.getClass().getName(), "order truck");
                break;
            default:
                Log.d(this.getClass().getName(), "unknown button id");
                break;
        }
    }
}
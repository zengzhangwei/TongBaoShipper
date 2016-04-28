package cn.edu.nju.software.tongbaoshipper.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.edu.nju.software.tongbaoshipper.BaiduOverlay.MarkerListOverlay;
import cn.edu.nju.software.tongbaoshipper.common.Driver;
import cn.edu.nju.software.tongbaoshipper.R;
import cn.edu.nju.software.tongbaoshipper.common.DriverPosition;
import cn.edu.nju.software.tongbaoshipper.common.PostRequest;
import cn.edu.nju.software.tongbaoshipper.common.User;
import cn.edu.nju.software.tongbaoshipper.constant.Net;
import cn.edu.nju.software.tongbaoshipper.service.ShipperService;
import cn.edu.nju.software.tongbaoshipper.service.UserService;
import cn.edu.nju.software.tongbaoshipper.view.activity.LoginActivity;

/**
 * Created by MoranHe on 2016/1/13.
 */
public class FragmentNearBy extends Fragment {

    private Context context;
    private MapView mvMap;
    private ArrayList<LatLng> driverpositions;
    private ArrayList<Driver> drivername;
    private ArrayList<DriverPosition> allList;
    private BaiduMap baiduMap;
    private PopupWindow popupWindow;
    private TextView driverinfo_tv;
    private LinearLayout btn_add_driver,btn_contact_driver,btn_cancel_pop;
    private RequestQueue requestQueue;

    private DriverOverlay overlay;

    public FragmentNearBy() {
        super();
    }

    @SuppressLint("ValidFragment")
    public FragmentNearBy(Context context) {
        super();
        this.context = context;
    }
    @Override
    public void onResume() {
        super.onResume();
        // 初始化用户常用地址信息
        if (User.isLogin()) {
            Map<String, String> params = new HashMap<>();
            params.put("token", User.getInstance().getToken());
            Request<JSONObject> request = new PostRequest(Net.URL_SHIPPER_GET_DRIVERS_POSITION,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.d(getActivity().getClass().getName(), jsonObject.toString());
                            try {
                                if (ShipperService.getResult(jsonObject)) {
                                    System.out.println("VVVVV");
                                    System.out.println(jsonObject);
                                    allList  = ShipperService.getDriverPositionList(jsonObject);
                                    splitDriverPosition();
                                    overlay.setData(driverpositions);
                                    overlay.addToMap();
                                    overlay.zoomToSpan();
                                    System.out.println("VVVVV"+allList.size()+"XXX");

                                } else {
                                    Toast.makeText(getActivity(), UserService.getErrorMsg(jsonObject),
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
                            Log.e(getActivity().getClass().getName(), volleyError.getMessage(), volleyError);
                            // http authentication 401
//                        if (volleyError.networkResponse.statusCode == Net.NET_ERROR_AUTHENTICATION) {
//                            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                            return;
//                        }
                            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.network_error),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }, params);
            requestQueue.add(request);
        } else {
            Intent intent = new Intent(this.getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }


    private void splitDriverPosition()
    {
        driverpositions=new ArrayList<LatLng>();
        drivername=new ArrayList<Driver>();
        for (DriverPosition dp:allList)
        {
            driverpositions.add(dp.getPosition());
            drivername.add(dp.getDriver());
        }

    }

    protected void initView(View view)
    {

        mvMap = (MapView) view.findViewById(R.id.nearby_map);
        mvMap.showZoomControls(false);
        mvMap.showScaleControl(false);
        driverpositions=new ArrayList<LatLng>();
        LatLng llA = new LatLng(33.963175, 116.400244);
        LatLng llB = new LatLng(33.942821, 116.369199);
        LatLng llC = new LatLng(33.939723, 116.425541);
        LatLng llD = new LatLng(33.906965, 116.401394);
        driverpositions.add(llA);
        driverpositions.add(llB);
        driverpositions.add(llC);
        driverpositions.add(llD);
        drivername=new ArrayList<Driver>();

        String name[]={"高健","邹源","菠萝","若曦"};

        for (String s:name)
        {
            Driver driver=new Driver();
            driver.setNickName(s);
            driver.setPhoneNum("1233-233-2323");
            drivername.add(driver);
        }

        baiduMap=mvMap.getMap();

        overlay=new DriverOverlay(baiduMap);

        baiduMap.setOnMarkerClickListener(overlay);

        overlay.setData(driverpositions);
        overlay.addToMap();
        overlay.zoomToSpan();



    }

    private void showPopWindow(Driver driver) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_driver_popinfo, null);

        popupWindow = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.pop_window_anim);
        popupWindow.showAtLocation(getView().findViewById(R.id.fragment_nearby), Gravity.BOTTOM, 0, 0);

        // popupWindow调整屏幕亮度
        final Window window = getActivity().getWindow();
        final WindowManager.LayoutParams params = window.getAttributes();
        params.alpha = 0.5f;
        window.setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                window.setAttributes(params);
            }
        });

        driverinfo_tv =(TextView) view.findViewById(R.id.driver_info_tv);
        btn_cancel_pop=(LinearLayout) view.findViewById(R.id.pop_driver_btn_cancel);
        btn_add_driver=(LinearLayout) view.findViewById(R.id.add_driver_btn);
        btn_add_driver=(LinearLayout) view.findViewById(R.id.contact_driver_btn);

        driverinfo_tv.setText(driver.getId()+"司机： "+driver.getNickName()+" "+driver.getPhoneNum());

        btn_cancel_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(FragmentNearBy.class.getName(), "pop cancel");
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            }
        });


    }

    private class DriverOverlay extends MarkerListOverlay
    {
        public DriverOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onListMarkerClick(int index) {
            super.onListMarkerClick(index);

            showPopWindow(drivername.get(index));
           // Toast.makeText(context, drivername.get(index)+"",
              //      Toast.LENGTH_SHORT).show();
            System.out.println(drivername.get(index).getNickName());

            return false;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= View.inflate(context, R.layout.fragment_nearby, null);
        initView(view);
        requestQueue = Volley.newRequestQueue(this.getActivity());

        return view;


    }
}

package cn.edu.nju.software.tongbaoshipper.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
import cn.edu.nju.software.tongbaoshipper.view.activity.UserActivity;

/**
 * Created by MoranHe on 2016/1/13.
 */
public class FragmentNearBy extends Fragment implements OnGetDistricSearchResultListener {

    private Context context;
    private MapView mvMap;
    private ArrayList<LatLng> driverpositions;
    private ArrayList<Driver> driverID;
    private ArrayList<DriverPosition> allList;
    private BaiduMap baiduMap;
    private PopupWindow popupWindow;
    private TextView driverinfo_tv;
    private LinearLayout btn_add_driver,btn_contact_driver,btn_cancel_pop;
    private RequestQueue requestQueue;
    private DriverOverlay overlay;
    private String phoneNum;
    private DistrictSearch mDistrictSearch;
    private LatLngBounds.Builder builder;
    private LatLngBounds CityBounds;
    private Timer timer;

    public FragmentNearBy() {
        super();
    }



    private void freshDiverList()
    {

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
                                    System.out.println("司机列表");
                                    System.out.println(jsonObject);
                                    allList  = ShipperService.getDriverPositionList(jsonObject);
                                    /*DriverPosition test=new DriverPosition();
                                    test.setDriver(new Driver());
                                    test.setPosition(new LatLng(39,116));
                                    allList.add(test);*/
                                    selectDriverPosition();
                                    splitDriverPosition();
                                    mvMap.showZoomControls(false);
                                    mvMap.showScaleControl(false);
                                    overlay=new DriverOverlay(baiduMap);
                                    overlay.setData(driverpositions);
                                    overlay.addToMap();
                                    overlay.zoomToSpan();
                                    baiduMap.setOnMarkerClickListener(overlay);

                                    baiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
                                        @Override
                                        public void onMapLoaded() {
                                            overlay.addToMap();

                                            overlay.zoomToSpan();
                                        }
                                    });



                                    System.out.println("一共"+allList.size()+"条记录");

                                } else {
                                    Toast.makeText(getActivity(), ShipperService.getErrorMsg(jsonObject),
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

    @Override
    public void onGetDistrictResult(DistrictResult districtResult) {
        if (districtResult == null) {
            return;
        }
        if (districtResult.error == SearchResult.ERRORNO.NO_ERROR) {
            List<List<LatLng>> polyLines = districtResult.getPolylines();
            if (polyLines == null) {
                return;
            }
            builder = new LatLngBounds.Builder();
            for (List<LatLng> polyline : polyLines) {

                for (LatLng latLng : polyline) {
                    builder.include(latLng);
                }
            }
            CityBounds= builder.build();
        }
        freshDiverList();
        TimerTask task = new TimerTask(){
            public void run() {
               freshDiverList();
                System.out.println("更新了一次司机列表");
            }
        };

        timer = new Timer(true);
        timer.schedule(task,10000, 10000); //延时1000ms后执行，1000ms执行一次
        //timer.cancel();
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
        String city="南京";
        mDistrictSearch.searchDistrict(new DistrictSearchOption().cityName(city));
    }
    @Override
    public void onDestroy() {
        mDistrictSearch.destroy();
        timer.cancel();
        super.onDestroy();
    }


    private void splitDriverPosition()
    {
        driverpositions=new ArrayList<LatLng>();
        driverID=new ArrayList<Driver>();
        for (DriverPosition dp:allList)
        {
            driverpositions.add(dp.getPosition());
            driverID.add(dp.getDriver());
        }

    }

    private void selectDriverPosition()
    {
        for (DriverPosition dp:allList)
        {
            if(!CityBounds.contains(dp.getPosition()))
                allList.remove(dp);
        }

    }

    protected void initView(View view)
    {

        mvMap = (MapView) view.findViewById(R.id.nearby_map);
        baiduMap=mvMap.getMap();


    }

    private void showPopWindow(final int id) {



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
        btn_contact_driver=(LinearLayout) view.findViewById(R.id.contact_driver_btn);


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
        phoneNum="110";

        btn_contact_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(context.getClass().getName(), "dial driver");
                Uri telUri = Uri.parse("tel:" + phoneNum);
                Intent intent = new Intent(Intent.ACTION_DIAL, telUri);
                context.startActivity(intent);
            }
        });

        btn_add_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(getActivity().getClass().getName(), "add driver");
                Map<String, String> params = new HashMap<>();
                params.put("token", User.getInstance().getToken());
                params.put("id", String.valueOf(id));
                Request<JSONObject> request = new PostRequest(Net.URL_SHIPPER_ADD_FREQUENT_DRIVER,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Log.d(getActivity().getClass().getName(), jsonObject.toString());
                                try {
                                    if (ShipperService.addFrequentDriver(jsonObject)) {
                                        Toast.makeText(context, context.getResources().getString(R.string.item_driver_add_success),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, ShipperService.getErrorMsg(jsonObject)+" 该司机已在您的收藏列表！",
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
                                Log.e(context.getClass().getName(), volleyError.getMessage(), volleyError);
                                //http authentication 401
//                                        if (volleyError.networkResponse.statusCode == Net.NET_ERROR_AUTHENTICATION) {
//                                            Intent intent = new Intent(UserActivity.this, LoginActivity.class);
//                                            startActivity(intent);
//                                            return;
//                                        }
                                Toast.makeText(context, context.getResources().getString(R.string.network_error),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }, params);
                requestQueue.add(request);
            }
        });





        Map<String, String> paramsdetail = new HashMap<>();
        paramsdetail.put("token", User.getInstance().getToken());
        paramsdetail.put("id",id+"");
        Request<JSONObject> request = new PostRequest(Net.URL_USER_GET_CONTACT_DETAIL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d(getActivity().getClass().getName(), jsonObject.toString());
                        try {
                            if (UserService.getResult(jsonObject)) {
                                System.out.println("开始解析司机信息");
                                System.out.println(jsonObject);

                                Driver driver=UserService.getDriverDetail(jsonObject);
                                phoneNum=driver.getPhoneNum();
                                driverinfo_tv.setText( "司机：" + driver.getNickName() + " 号码：" + driver.getPhoneNum());


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
                }, paramsdetail);
        requestQueue.add(request);

    }

    private class DriverOverlay extends MarkerListOverlay
    {
        public DriverOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onListMarkerClick(int index) {
            super.onListMarkerClick(index);

            showPopWindow(driverID.get(index).getId());
           // Toast.makeText(context, driverID.get(index)+"",
              //      Toast.LENGTH_SHORT).show();
            System.out.println(driverID.get(index).getId()+"号司机");

            return false;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= View.inflate(context, R.layout.fragment_nearby, null);
        initView(view);
        mDistrictSearch = DistrictSearch.newInstance();
        mDistrictSearch.setOnDistrictSearchListener(this);
        requestQueue = Volley.newRequestQueue(this.getActivity());

        return view;


    }
}

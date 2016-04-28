package cn.edu.nju.software.tongbaoshipper.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

import cn.edu.nju.software.tongbaoshipper.common.Order;
import cn.edu.nju.software.tongbaoshipper.common.PostRequest;
import cn.edu.nju.software.tongbaoshipper.common.User;
import cn.edu.nju.software.tongbaoshipper.constant.Net;
import cn.edu.nju.software.tongbaoshipper.R;
import cn.edu.nju.software.tongbaoshipper.service.ShipperService;
import cn.edu.nju.software.tongbaoshipper.service.UserService;

/**
 * Created by zhang on 2016/4/16.
 */
public class HistoryOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView order_state;
    private TextView order_id;
    private TextView from_address;
    private TextView to_address;
    private TextView from_contact;
    private TextView to_contact;
    private TextView load_time;
    private TextView place_time;
    private TextView truck_type;
    private TextView order_price;
    private TextView cancel_tv;
    private TextView ok_tv;
    private RequestQueue requestQueue;

    private Order order;
    private LinearLayout btn_back,btn_cancel,btn_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initView();
        requestQueue = Volley.newRequestQueue(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        // 初始化用户常用地址信息
        Intent intent=getIntent();
        String orderid=intent.getStringExtra("id");
        Map<String, String> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("id",orderid);

        Request<JSONObject> request = new PostRequest(Net.URL_SHIPPER_GET_ORDER_DETAIL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d(HistoryOrderActivity.class.getName(), jsonObject.toString());
                        try {
                            if (ShipperService.getResult(jsonObject)) {
                                order=ShipperService.getDetailOrder(jsonObject);

                                if (order.getState()==3)
                                    order_state.setText("订单已完成，您可以评价订单或者删除记录");
                                else
                                    order_state.setText("订单已取消，您可以删除记录");

                                order_id.setText(order.getId()+"");
                                from_address.setText(order.getAddressFrom());
                                to_address.setText(order.getAddressTo());
                                from_contact.setText(order.getFromContactName()+" "+order.getFromContactPhone());
                                to_contact.setText(order.getToContactName()+" "+order.getToContactPhone());
                                load_time.setText(order.getLoadTime());
                                place_time.setText(order.getPlaceTime());
                                order_price.setText(order.getPrice()+"元");

                                StringBuilder sb=new StringBuilder();
                                for (int i:order.getTruckTypes())
                                    sb.append(ShipperService.getAllTruckType(HistoryOrderActivity.this).get(i).getTruckType()+" ");
                                sb.append(order.getPrice() + "元");
                                truck_type.setText(sb.toString());



                            } else {
                                Toast.makeText(HistoryOrderActivity.this, ShipperService.getErrorMsg(jsonObject),
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
                        Log.e(AllTruckActivity.class.getName(), volleyError.getMessage(), volleyError);
                        // http authentication 401
//                        if (volleyError.networkResponse.statusCode == Net.NET_ERROR_AUTHENTICATION) {
//                            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                            return;
//                        }
                        Toast.makeText(HistoryOrderActivity.this, HistoryOrderActivity.this.getResources().getString(R.string.network_error),
                                Toast.LENGTH_SHORT).show();
                    }
                }, params);
        requestQueue.add(request);

    }


    private void initView()
    {
        order= ShipperService.getTestOrder();
        order_state=(TextView) findViewById(R.id.order_state_tv);
        order_id=(TextView) findViewById(R.id.detail_order_id_tv);
        from_address=(TextView) findViewById(R.id.detail_order_from_tv);
        to_address=(TextView) findViewById(R.id.detail_order_to_tv);
        from_contact=(TextView) findViewById(R.id.detail_order_from_contact_tv);
        to_contact=(TextView) findViewById(R.id.detail_order_to_contact_tv);
        load_time=(TextView) findViewById(R.id.detail_order_loadtime_tv);
        place_time=(TextView) findViewById(R.id.detail_order_placetime_tv);
        truck_type=(TextView) findViewById(R.id.detail_order_truckType_tv);
        order_price=(TextView) findViewById(R.id.detail_order_price_tv);
        cancel_tv=(TextView) findViewById(R.id.order_detail_cancel_tv);
        ok_tv=(TextView) findViewById(R.id.order_detail_ok_tv);


        btn_cancel=(LinearLayout) findViewById(R.id.order_detail_btn_cancel);
        btn_ok=(LinearLayout) findViewById(R.id.order_detail_btn_ok);
        btn_back=(LinearLayout) findViewById(R.id.order_detail_btn_back);

        btn_back.setOnClickListener(this);

        order_state.setText("订单已完成，您可以评价订单或者删除记录");
        order_id.setText(order.getId()+"");
        from_address.setText(order.getAddressFrom());
        to_address.setText(order.getAddressTo());
        from_contact.setText(order.getFromContactName()+" "+order.getFromContactPhone());
        to_contact.setText(order.getToContactName()+" "+order.getToContactPhone());
        load_time.setText(order.getLoadTime());
        place_time.setText(order.getPlaceTime());
        order_price.setText(order.getPrice()+"元");

        StringBuilder sb=new StringBuilder();
        for (int i:order.getTruckTypes())
            sb.append(ShipperService.getAllTruckType(HistoryOrderActivity.this).get(i).getTruckType()+" ");
        sb.append(order.getPrice() + "元");
        truck_type.setText(sb.toString());

        cancel_tv.setText("删除订单");
        ok_tv.setText("评价订单");




    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.order_detail_btn_back:
                Log.d(HistoryOrderActivity.class.getName(), "back");
                finish();
                break;
            case R.id.order_detail_btn_cancel:
                Log.d(HistoryOrderActivity.class.getName(), "cancel order");

                break;
            case R.id.order_detail_btn_ok:
                Log.d(HistoryOrderActivity.class.getName(), "finish order");

                break;
            default:
                Log.e(HistoryOrderActivity.class.getName(), "Unknown id");
        }
    }

}

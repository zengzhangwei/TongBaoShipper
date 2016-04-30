package cn.edu.nju.software.tongbaoshipper.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.Map;

import cn.edu.nju.software.tongbaoshipper.common.PostRequest;
import cn.edu.nju.software.tongbaoshipper.common.Truck;
import cn.edu.nju.software.tongbaoshipper.R;
import cn.edu.nju.software.tongbaoshipper.service.ShipperService;
import cn.edu.nju.software.tongbaoshipper.service.UserService;
import cn.edu.nju.software.tongbaoshipper.view.adapter.AllTruckAdapter;
import cn.edu.nju.software.tongbaoshipper.constant.Net;

public class AllTruckActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lvTruck;
    private LinearLayout vEmpty;
    private AllTruckAdapter allTruckAdapter;
    final int RESULT_CODE_TRUCK=104;
    private LinearLayout btnBack;
    private ArrayList<Truck> allList;

      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_truck);
        initView();
    }

    /**
     * init view
     */
    private void initView() {
        btnBack = (LinearLayout) findViewById(R.id.add_truck_btn_back);
        lvTruck  = (ListView) findViewById(R.id.add_truck_lv);
        vEmpty = (LinearLayout) findViewById(R.id.address_empty);

        btnBack.setOnClickListener(this);
        System.out.println("All  truck Get TruckType");

        allList  = ShipperService.getAllTruckType(AllTruckActivity.this);

        if (allList==null)
            vEmpty.setVisibility(View.VISIBLE);
        else {
            allTruckAdapter = new AllTruckAdapter(AllTruckActivity.this, allList, lvTruck);
            lvTruck.setAdapter(allTruckAdapter);
        }

        lvTruck.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.putExtra("truck",position);
                setResult(RESULT_CODE_TRUCK, intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_truck_btn_back:
                Log.d(AllTruckActivity.class.getName(), "back");
                finish();
                break;
            default:
                Log.e(AllTruckActivity.class.getName(), "Unknown id");
                break;
        }
    }
}

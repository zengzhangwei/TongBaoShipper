package cn.edu.nju.software.tongbaoshipper.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.software.tongbaoshipper.R;

import com.sevenheaven.segmentcontrol.CompleteListView;

/**
 * Created by zhang on 2016/4/16.
 */
public class PlaceOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private CompleteListView vehicleList;
    private List<String> textList;
    private EditText date_input,start_point_input,arrive_point_input;
    private MyAdapter adapter;
    private LinearLayout btn_back,btn_add_trunk;
    private RelativeLayout btn_place_order;
    private ScrollView sv;
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return textList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return textList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            TextView textView = new TextView(PlaceOrderActivity.this);
            textView.setText(textList.get(position));
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(18.0f);
            return textView;
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        initView();
    }

    private void initView()
    {

        date_input=(EditText)findViewById(R.id.date_input);
        start_point_input=(EditText)findViewById(R.id.start_address_input);
        arrive_point_input=(EditText)findViewById(R.id.arrive_address_input);
        btn_add_trunk=(LinearLayout)findViewById(R.id.add_trunk_btn);
        btn_back=(LinearLayout)findViewById(R.id.place_order_btn_back);
        btn_place_order=(RelativeLayout)findViewById(R.id.place_order_trunk_btn);
        vehicleList=(CompleteListView) findViewById(R.id.vehicle_list);
        sv=(ScrollView)findViewById(R.id.place_order_sv);
        textList = new ArrayList<String>();
        for (int i = 0; i < 2; i++) {
            textList.add("这是一条ListView的数据" + i);
        }
        adapter = new MyAdapter();
        vehicleList.setAdapter(adapter);
        textList.add("面包车");
        textList.add("面包车");

        btn_back.setOnClickListener(this);
        btn_add_trunk.setOnClickListener(this);
        btn_place_order.setOnClickListener(this);
        date_input.setOnTouchListener(new EditTextClickListener());

        start_point_input.setOnTouchListener(new EditTextClickListener());
        arrive_point_input.setOnTouchListener(new EditTextClickListener());

    }

    private class EditTextClickListener implements View.OnTouchListener
    {
        int touch_flag=0;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            touch_flag++;
            if(touch_flag>=2){
                //自己业务

                switch(v.getId()) {
                    case R.id.date_input:
                        Log.d(PlaceOrderActivity.class.getName(), "get date");
                        date_input.setText("2015-12-12"+touch_flag);
                        break;
                    case R.id.start_address_input:
                        Log.d(PlaceOrderActivity.class.getName(), "get start");
                        start_point_input.setText("2015-12-12+" + touch_flag);
                        textList.get(0).replace('0', '1');
                        vehicleList.setAdapter(adapter);

                        break;
                    case R.id.arrive_address_input:
                        Log.d(PlaceOrderActivity.class.getName(), "get arrive");
                        arrive_point_input.setText("2015-12-12"+touch_flag);
                        break;
                    default:
                        Log.e(PlaceOrderActivity.class.getName(), "Unknown id");
                }


            }
            return false;
        }
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.place_order_btn_back:
                Log.d(PlaceOrderActivity.class.getName(), "back");
                finish();
                break;
            case R.id.date_input:
                Log.d(PlaceOrderActivity.class.getName(), "get date");
                date_input.setText("2015-12-12");
                break;
            case R.id.add_trunk_btn:
                Log.d(PlaceOrderActivity.class.getName(), "add trunk");
                start_point_input.setText("2015-12-12——————+");
                textList.add("sdsdsd");
                vehicleList.setAdapter(adapter);

                break;
            case R.id.place_order_trunk_btn:
                Log.d(PlaceOrderActivity.class.getName(), "order trunk");
                textList.add("面包车250元");
                vehicleList.setAdapter(adapter);

                break;
            default:
                Log.e(PlaceOrderActivity.class.getName(), "Unknown id");
        }
    }
}

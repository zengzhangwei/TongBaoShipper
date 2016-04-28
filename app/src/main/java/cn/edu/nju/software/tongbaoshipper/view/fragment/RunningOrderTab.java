package cn.edu.nju.software.tongbaoshipper.view.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.sevenheaven.segmentcontrol.OnRefreshListener;
import com.sevenheaven.segmentcontrol.RefreshListView;

import java.util.ArrayList;

import cn.edu.nju.software.tongbaoshipper.common.Message;
import cn.edu.nju.software.tongbaoshipper.common.Order;
import cn.edu.nju.software.tongbaoshipper.R;
import cn.edu.nju.software.tongbaoshipper.service.ShipperService;
import cn.edu.nju.software.tongbaoshipper.view.activity.RunningOrderActivity;
import cn.edu.nju.software.tongbaoshipper.view.adapter.OrderListAdapter;


/**
 * Created by zhang on 2016/4/13.
 */


public class RunningOrderTab extends Fragment implements OnRefreshListener {
    private ArrayList<Order> orderList;
    private OrderListAdapter adapter;
    private RefreshListView rListView;
    private RequestQueue requestQueue;
    private LinearLayout emptyView;




    @Override
    public void onDownPullRefresh() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(1000);
                //添加数据
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                adapter.notifyDataSetChanged();
                rListView.hideHeaderView();
            }
        }.execute(new Void[] {});
    }

    @Override
    public void onLoadingMore() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(1000);

                //添加数据
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                adapter.notifyDataSetChanged();

                // 控制脚布局隐藏
                rListView.hideFooterView();
            }
        }.execute(new Void[]{});
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume


        } else {
            //相当于Fragment的onPause
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.fragment_orderrunning, container, false);
        initView(view);
        return  view;


    }

    private void initView(View view)
    {
        Log.i(this.getClass().getName(), "init view");

        rListView = (RefreshListView) view.findViewById(R.id.running_order_lv);
        emptyView = (LinearLayout) view.findViewById(R.id.ruuning_order_empty);

        orderList=new ArrayList<Order>();
        if (orderList!=null)
        {
            if (orderList.size()<1) emptyView.setVisibility(View.VISIBLE);
            else  emptyView.setVisibility(View.INVISIBLE);
            adapter = new OrderListAdapter(getContext(),orderList,rListView);
            rListView.setAdapter(adapter);
        }
        else  emptyView.setVisibility(View.INVISIBLE);

        rListView.setOnRefreshListener(this);

        rListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), RunningOrderActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);

            }
        });
    }


}


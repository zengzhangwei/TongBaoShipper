package cn.edu.nju.software.tongbaoshipper.view.fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.software.tongbaoshipper.R;
import com.sevenheaven.segmentcontrol.OnRefreshListener;
import com.sevenheaven.segmentcontrol.RefreshListView;


/**
 * Created by zhang on 2016/4/13.
 */


public class RunningOrderTab extends Fragment implements OnRefreshListener {
    private List<String> textList;
    private MyAdapter adapter;
    private RefreshListView rListView;


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
            TextView textView = new TextView(getContext());
            textView.setText(textList.get(position));
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(18.0f);
            return textView;
        }

    }


    @Override
    public void onDownPullRefresh() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(2000);
                for (int i = 0; i < 2; i++) {
                    textList.add(0, "这是下拉刷新出来的数据" + i);
                }
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
                SystemClock.sleep(5000);

                textList.add("这是加载更多出来的数据1");
                textList.add("这是加载更多出来的数据2");
                textList.add("这是加载更多出来的数据3");
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                adapter.notifyDataSetChanged();

                // 控制脚布局隐藏
                rListView.hideFooterView();
            }
        }.execute(new Void[] {});
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
        rListView = (RefreshListView) view.findViewById(R.id.refreshlistview);
        textList = new ArrayList<String>();
        for (int i = 0; i < 25; i++) {
            textList.add("这是一条ListView的数据" + i);
        }
        adapter = new MyAdapter();
        rListView.setAdapter(adapter);
        rListView.setOnRefreshListener(this);
    }
}


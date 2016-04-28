package cn.edu.nju.software.tongbaoshipper.view.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sevenheaven.segmentcontrol.RefreshListView;

import java.util.ArrayList;

import cn.edu.nju.software.tongbaoshipper.common.Order;
import cn.edu.nju.software.tongbaoshipper.R;
import cn.edu.nju.software.tongbaoshipper.service.ShipperService;

public class OrderListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Order> arrorder;
    private Dialog dialog;
    private RefreshListView lvOrder;


    public OrderListAdapter(Context context, ArrayList<Order> arrorder, RefreshListView lvOrder) {
        super();
        this.context = context;
        this.arrorder = arrorder;
        this.lvOrder = lvOrder;
    }

    @Override
    public int getCount() {
        return arrorder.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return arrorder.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_my_order, parent, false);
        Order order = arrorder.get(position);

        TextView tv_orderId = (TextView) convertView.findViewById(R.id.my_order_id_tv);
        TextView tv_fromaddress = (TextView) convertView.findViewById(R.id.my_order_from_tv);
        TextView tv_toaddress = (TextView) convertView.findViewById(R.id.my_order_to_tv);
        TextView tv_loadtime = (TextView) convertView.findViewById(R.id.my_order_loadtime_tv);
        TextView tv_placetime = (TextView) convertView.findViewById(R.id.my_order_placetime_tv);
        TextView tv_truckType = (TextView) convertView.findViewById(R.id.my_order_truckType_tv);


        tv_orderId.setText(order.getId()+"");
        tv_fromaddress.setText(order.getAddressFrom());
        tv_toaddress.setText(order.getAddressTo());
        tv_placetime.setText(order.getPlaceTime());
        tv_loadtime.setText(order.getLoadTime());
        StringBuilder sb=new StringBuilder();
        for (int i:order.getTruckTypes())
            sb.append(ShipperService.getTestTruckList().get(i).getTruckType()+" ");
        sb.append(order.getPrice()+"元");
        tv_truckType.setText(sb.toString());


        return convertView;
    }

}

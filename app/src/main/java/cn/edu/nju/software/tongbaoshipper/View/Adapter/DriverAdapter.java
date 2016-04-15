package cn.edu.nju.software.tongbaoshipper.View.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.Map;

import cn.edu.nju.software.tongbaoshipper.Common.Driver;
import cn.edu.nju.software.tongbaoshipper.Common.PostRequest;
import cn.edu.nju.software.tongbaoshipper.Common.User;
import cn.edu.nju.software.tongbaoshipper.Const.Net;
import cn.edu.nju.software.tongbaoshipper.R;
import cn.edu.nju.software.tongbaoshipper.Service.ShipperService;

public class DriverAdapter extends BaseAdapter implements AdapterView.OnItemLongClickListener {

    private Context context;
    private ArrayList<Driver> arrDriver;
    private Dialog dialog;
    private RequestQueue requestQueue;

    public DriverAdapter(Context context, ArrayList<Driver> arrDriver, ListView lvDriver) {
        super();
        this.context = context;
        this.arrDriver = arrDriver;
        lvDriver.setOnItemLongClickListener(this);
        requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public int getCount() {
        return arrDriver.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return arrDriver.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_driver, parent, false);
        Driver driver = arrDriver.get(position);
        ImageView ivAvatar = (ImageView) convertView.findViewById(R.id.item_driver_iv_avatar);
        ImageView ivOperation = (ImageView) convertView.findViewById(R.id.item_driver_iv_operation);
        TextView tvName = (TextView) convertView.findViewById(R.id.item_driver_tv_name);
        TextView tvTruck = (TextView) convertView.findViewById(R.id.item_driver_tv_truck);
        TextView tvPhone = (TextView) convertView.findViewById(R.id.item_driver_tv_phone);

        tvName.setText(driver.getNickName());
        tvPhone.setText(driver.getPhoneNum());

        return convertView;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        View vDialog = LayoutInflater.from(context).inflate(R.layout.dialog_item_driver, parent, false);
        dialog = new AlertDialog.Builder(context)
                .setView(vDialog)
                .create();
        Button btnDelete = (Button) vDialog.findViewById(R.id.dialog_item_driver_btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(DriverAdapter.class.getName(), "driver delete");
                dialog.dismiss();

                Map<String, String> params = new HashMap<>();
                params.put("token", User.getInstance().getToken());
                params.put("id", String.valueOf(arrDriver.get(position).getId()));
                Request<JSONObject> request = new PostRequest(Net.URL_SHIPPER_DELETE_FREQUENT_DRIVER,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Log.d(DriverAdapter.class.getName(), jsonObject.toString());
                                try {
                                    if (ShipperService.deleteFrequentDriver(jsonObject)) {
                                        Toast.makeText(context, context.getResources().getString(R.string.item_driver_delete_success),
                                                Toast.LENGTH_SHORT).show();
                                        // 修改相应adapter arr
                                        arrDriver.remove(position);
                                        notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(context, ShipperService.getErrorMsg(jsonObject),
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
                                Log.e(DriverAdapter.class.getName(), volleyError.getMessage(), volleyError);
                                //http authentication 401
//                                if (volleyError.networkResponse.statusCode == Net.NET_ERROR_AUTHENTICATION) {
//                                    Intent intent = new Intent(UserActivity.this, LoginActivity.class);
//                                    startActivity(intent);
//                                    return;
//                                }
                                Toast.makeText(context, context.getResources().getString(R.string.network_error),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }, params);
                requestQueue.add(request);
            }
        });
        dialog.show();
        return true;
    }
}

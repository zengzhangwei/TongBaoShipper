package cn.edu.nju.software.tongbaoshipper.View.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import cn.edu.nju.software.tongbaoshipper.Common.Address;
import cn.edu.nju.software.tongbaoshipper.Common.PostRequest;
import cn.edu.nju.software.tongbaoshipper.Common.User;
import cn.edu.nju.software.tongbaoshipper.Const.Net;
import cn.edu.nju.software.tongbaoshipper.R;
import cn.edu.nju.software.tongbaoshipper.Service.ShipperService;

public class ChangeAddressActivity extends AppCompatActivity implements View.OnClickListener{

    private Address address;
    private EditText etLocation, etPhone, etContact;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        Intent intent = this.getIntent();
        address = (Address) intent.getSerializableExtra(Address.class.getName());
        initView();
        requestQueue = Volley.newRequestQueue(this);
    }

    /**
     * init view
     */
    private void initView() {
        TextView tvTitle = (TextView) findViewById(R.id.add_address_tv_title);
        etLocation = (EditText) findViewById(R.id.add_address_et_location);
        etPhone = (EditText) findViewById(R.id.add_address_et_phone);
        etContact = (EditText) findViewById(R.id.add_address_et_contact);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.add_address_btn_back);
        LinearLayout btnConfirm = (LinearLayout) findViewById(R.id.add_address_btn);

        tvTitle.setText(R.string.title_change_address);
        etLocation.setText(address.getAddressName());
        etPhone.setText(address.getPhoneNum());
        etContact.setText(address.getContactName());

        btnBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_address_btn_back:
                Log.d(ChangeAddressActivity.class.getName(), "back");
                finish();
                break;
            case R.id.add_address_btn:
                Log.d(ChangeAddressActivity.class.getName(), "confirm");
                Map<String, String> params = new HashMap<>();
                params.put("token", User.getInstance().getToken());
                params.put("id", String.valueOf(address.getId()));
                params.put("address", etLocation.getText().toString());
                // TODO add important message
                params.put("lat", "");
                params.put("lng", "");
                params.put("contactName", etContact.getText().toString());
                params.put("contactPhone", etPhone.getText().toString());
                Request<JSONObject> request = new PostRequest(Net.URL_SHIPPER_EDIT_FREQUENT_ADDRESS,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                try {
                                    if (ShipperService.editFrequentAddress(jsonObject)) {
                                        Toast.makeText(ChangeAddressActivity.this, ChangeAddressActivity.this.getResources().getString(R.string.change_address_success),
                                                Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(ChangeAddressActivity.this, ShipperService.getErrorMsg(jsonObject),
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
                                Log.e(ChangeAddressActivity.class.getName(), volleyError.getMessage(), volleyError);
//                                http authentication 401
//                                                if (volleyError.networkResponse.statusCode == Net.NET_ERROR_AUTHENTICATION) {
//                                                    Intent intent = new Intent(UserActivity.this, LoginActivity.class);
//                                                    startActivity(intent);
//                                                    return;
//                                                }
                                Toast.makeText(ChangeAddressActivity.this, ChangeAddressActivity.this.getResources().getString(R.string.network_error),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }, params);
                requestQueue.add(request);
                break;
            default:
                Log.e(ChangeAddressActivity.class.getName(), "Unknown id");
                break;
        }
    }
}

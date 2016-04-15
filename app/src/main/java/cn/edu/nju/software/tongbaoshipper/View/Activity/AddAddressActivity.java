package cn.edu.nju.software.tongbaoshipper.View.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import cn.edu.nju.software.tongbaoshipper.Common.PostRequest;
import cn.edu.nju.software.tongbaoshipper.Common.User;
import cn.edu.nju.software.tongbaoshipper.Const.Net;
import cn.edu.nju.software.tongbaoshipper.R;
import cn.edu.nju.software.tongbaoshipper.Service.ShipperService;

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etLocation, etContact, etPhone;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        initView();
        requestQueue = Volley.newRequestQueue(this);
    }

    /**
     * init view
     */
    private void initView() {
        etLocation = (EditText) findViewById(R.id.add_address_et_location);
        etContact = (EditText) findViewById(R.id.add_address_et_contact);
        etPhone = (EditText) findViewById(R.id.add_address_et_phone);
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.add_address_btn_back);
        LinearLayout btnConfirm = (LinearLayout) findViewById(R.id.add_address_btn);

        btnBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {

        switch (v.getId()) {
            case R.id.add_address_btn_back:
                Log.d(AddAddressActivity.class.getName(), "back");
                finish();
                break;
            case R.id.add_address_btn:
                Log.d(AddAddressActivity.class.getName(), "confirm");
                // TODO confirm params something is wrong
                Map<String, String> params = new HashMap<>();
                params.put("token", User.getInstance().getToken());
                params.put("address", etLocation.getText().toString());
                params.put("contact", etContact.getText().toString());
                params.put("phone", etPhone.getText().toString());
                params.put("lat", "lat");
                params.put("lng", "lng");
                Request<JSONObject> request = new PostRequest(Net.URL_SHIPPER_ADD_FREQUENT_ADDRESS,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Log.d(AddAddressActivity.class.getName(), jsonObject.toString());
                                try {
                                    if (ShipperService.addFrequentAddress(jsonObject)) {
                                        Toast.makeText(AddAddressActivity.this, AddAddressActivity.this.getResources().getString(R.string.add_address_success),
                                                Toast.LENGTH_SHORT).show();
                                        // 添加成功自动关闭
                                        finish();
                                    } else {
                                        Toast.makeText(AddAddressActivity.this, ShipperService.getErrorMsg(jsonObject),
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
                                Log.e(AddAddressActivity.class.getName(), volleyError.getMessage(), volleyError);
                                //http authentication 401
//                                if (volleyError.networkResponse.statusCode == Net.NET_ERROR_AUTHENTICATION) {
//                                    Intent intent = new Intent(UserActivity.this, LoginActivity.class);
//                                    startActivity(intent);
//                                    return;
//                                }
                                Toast.makeText(AddAddressActivity.this, AddAddressActivity.this.getResources().getString(R.string.network_error),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }, params);
                requestQueue.add(request);
                break;
            default:
                Log.e(AddAddressActivity.class.getName(), "Unknown id");
                break;
        }

    }
}

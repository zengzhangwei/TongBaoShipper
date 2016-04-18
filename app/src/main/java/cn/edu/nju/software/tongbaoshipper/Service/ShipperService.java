package cn.edu.nju.software.tongbaoshipper.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import cn.edu.nju.software.tongbaoshipper.Common.Address;
import cn.edu.nju.software.tongbaoshipper.Common.Driver;
import cn.edu.nju.software.tongbaoshipper.Const.Net;

public class ShipperService {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);

    /**
     * 获取错误信息
     * @param jsonObject    response
     * @return String
     * @throws JSONException
     */
    public static String getErrorMsg(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("errorMsg");
    }

    /**
     * 获取请求处理结果
     * @param jsonObject    response
     * @return boolean
     * @throws JSONException
     */
    public static boolean getResult(JSONObject jsonObject) throws JSONException {
        return jsonObject.getInt("result") == Net.NET_OPERATION_SUCCESS;
    }

    /**
     * 获取常用司机列表
     * @param jsonObject    response
     * @return  ArrayList<Driver></>
     * @throws JSONException
     */
    public static ArrayList<Driver> getFrequentDrivers(JSONObject jsonObject) throws JSONException {
        ArrayList<Driver> arrDriver = new ArrayList<>();
        if (getResult(jsonObject)) {
            JSONArray data = jsonObject.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                Driver driver = new Driver();
                JSONObject object = data.getJSONObject(i);
                driver.setId(object.getInt("id"));
                driver.setNickName(object.getString("nickName"));
                driver.setPhoneNum(object.getString("phoneNum"));
                driver.setIconUrl(object.getString("iconUrl"));
                try {
                    driver.setRegisterTime(sdf.parse(object.getString("registerTime")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                arrDriver.add(driver);
            }
        }
        return arrDriver;
    }

    /**
     * 获取常用地址列表
     * @param jsonObject    response
     * @return ArrayList<Address></>
     * @throws JSONException
     */
    public static ArrayList<Address> getFrequentAddresses(JSONObject jsonObject) throws JSONException {
        ArrayList<Address> arrAddress = new ArrayList<>();
        if (getResult(jsonObject)) {
            JSONArray data = jsonObject.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                Address address = new Address();
                //TODO complete
                JSONObject object = data.getJSONObject(i);
                address.setId(object.getInt("id"));
                address.setAddressName(object.getString("name"));
                address.setContactName(object.getString("contact"));
                address.setPhoneNum(object.getString("phoneNum"));
                address.setLat(object.getDouble("lat"));
                address.setLng(object.getDouble("lng"));
                arrAddress.add(address);
            }
        }
        return arrAddress;
    }

    /**
     * 添加常用司机
     * @param jsonObject    response
     * @return  boolean
     */
    public static boolean addFrequentDriver(JSONObject jsonObject) throws JSONException {
        return getResult(jsonObject);
    }

    /**
     * 删除常用司机
     * @param jsonObject    response
     * @return  boolean
     * @throws JSONException
     */
    public static boolean deleteFrequentDriver(JSONObject jsonObject) throws JSONException {
        return getResult(jsonObject);
    }

    /**
     * 根据手机号搜索司机（可以是模糊搜索）
     * @param jsonObject    response
     * @return  boolean
     * @throws JSONException
     */
    public static ArrayList<Driver> searchDriver(JSONObject jsonObject) throws JSONException {
        ArrayList<Driver> arrDriver = new ArrayList<>();
        if (getResult(jsonObject)) {
            JSONArray data = jsonObject.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject object = data.getJSONObject(i);
                Driver driver = new Driver();
                driver.setId(object.getInt("id"));
                driver.setNickName(object.getString("nickName"));
                driver.setPhoneNum(object.getString("phoneNum"));
                driver.setIconUrl(object.getString("iconUrl"));
                try {
                    driver.setRegisterTime(sdf.parse(object.getString("registerTime")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                arrDriver.add(driver);
            }
        }
        return arrDriver;
    }

    /**
     * 添加常用地址
     * @param jsonObject    response
     * @return boolean
     * @throws JSONException
     */
    public static boolean addFrequentAddress(JSONObject jsonObject) throws JSONException {
        return getResult(jsonObject);
    }

    /**
     * 修改常用地址
     * @param jsonObject    response
     * @return  boolean
     * @throws JSONException
     */
    public static boolean editFrequentAddress(JSONObject jsonObject) throws JSONException {
        return getResult(jsonObject);
    }

    /**
     * 删除常用地址
     * @param jsonObject    response
     * @return  boolean
     * @throws JSONException
     */
    public static boolean deleteFrequentAddress(JSONObject jsonObject) throws JSONException {
        return getResult(jsonObject);
    }
}

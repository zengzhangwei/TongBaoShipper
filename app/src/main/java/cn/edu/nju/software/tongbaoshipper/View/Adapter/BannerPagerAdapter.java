package cn.edu.nju.software.tongbaoshipper.View.Adapter;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by MoranHe on 2016/1/15.
 */
public class BannerPagerAdapter extends PagerAdapter {

    private List<View> iv_list;

    public BannerPagerAdapter(List<View> list) {
        this.iv_list = list;
    }

    @Override
    public int getCount() {
        return iv_list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.i(this.getClass().getName(), "instantiate item position: " + position);
        container.addView(iv_list.get(position));
        return iv_list.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.i(this.getClass().getName(), "destroy item position: " + position);
        container.removeView(iv_list.get(position));
    }
}

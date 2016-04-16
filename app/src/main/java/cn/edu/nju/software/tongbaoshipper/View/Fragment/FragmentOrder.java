package cn.edu.nju.software.tongbaoshipper.View.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sevenheaven.segmentcontrol.SegmentControl;
import com.sevenheaven.segmentcontrol.SegmentControl.OnSegmentControlClickListener;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.software.tongbaoshipper.R;

/**
 * Created by MoranHe on 2016/1/13.
 */
public class FragmentOrder extends Fragment {

    private Context context;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private SegmentControl selectbutton;
    private View view;

    public FragmentOrder() {
        super();
    }

    @SuppressLint("ValidFragment")
    public FragmentOrder(Context context) {
        super();
        this.context = context;
    }

    private void initView(View view)
    {
        Log.i(this.getClass().getName(), "init view");
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        selectbutton =(SegmentControl) view.findViewById(R.id.segment_control);

        WaitingOrderTab tab01 = new WaitingOrderTab();
        RunningOrderTab tab02 = new RunningOrderTab();
        HistoryOrderTab tab03 = new HistoryOrderTab();
        mFragments.add(tab01);
        mFragments.add(tab02);
        mFragments.add(tab03);
        selectbutton.setOnSegmentControlClickListener(new OnSegmentControlClickListener() {
            @Override
            public void onSegmentControlClick(int index) {
                mViewPager.setCurrentItem(index);
            }
        });
        selectbutton.setSelectedIndex(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(context, R.layout.fragment_order, null);
        initView(view);
        mAdapter = new FragmentPagerAdapter(getChildFragmentManager())
        {
            @Override
            public int getCount()
            {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int arg0)
            {
                return mFragments.get(arg0);
            }
        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            private int currentIndex;

            @Override
            public void onPageSelected(int position) {

                ((SegmentControl) view.findViewById(R.id.segment_control)).setSelectedIndex(position);
                currentIndex = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        mViewPager.setCurrentItem(1);
        return view;
    }


}

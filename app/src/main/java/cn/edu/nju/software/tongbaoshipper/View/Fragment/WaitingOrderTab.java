package cn.edu.nju.software.tongbaoshipper.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.nju.software.tongbaoshipper.R;

/**
 * Created by zhang on 2016/4/13.
 */
public class WaitingOrderTab extends Fragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return  inflater.inflate(R.layout.fragment_orderwaiting, container, false);

    }

}

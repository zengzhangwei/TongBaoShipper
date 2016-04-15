package cn.edu.nju.software.tongbaoshipper.View.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.nju.software.tongbaoshipper.R;

/**
 * Created by MoranHe on 2016/1/13.
 */
public class FragmentOrder extends Fragment {

    private Context context;

    public FragmentOrder() {
        super();
    }

    @SuppressLint("ValidFragment")
    public FragmentOrder(Context context) {
        super();
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(context, R.layout.fragment_order, null);
    }
}

package cn.edu.nju.software.tongbaoshipper.view1.fragment1;

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
public class FragmentNearBy extends Fragment {

    private Context context;

    public FragmentNearBy() {
        super();
    }

    @SuppressLint("ValidFragment")
    public FragmentNearBy(Context context) {
        super();
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(context, R.layout.fragment_nearby, null);
    }
}

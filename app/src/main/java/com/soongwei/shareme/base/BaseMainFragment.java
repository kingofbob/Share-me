package com.soongwei.shareme.base;

import android.widget.Toast;

import com.soongwei.shareme.R;


public abstract class BaseMainFragment extends BaseFragment {

    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;


    @Override
    public boolean onBackPressedSupport() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            Toast.makeText(_mActivity, R.string.press_again_exit, Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}

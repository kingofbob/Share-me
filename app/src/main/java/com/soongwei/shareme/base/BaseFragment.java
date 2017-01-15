package com.soongwei.shareme.base;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.soongwei.shareme.R;

import me.yokeyword.fragmentation.SupportFragment;


public class BaseFragment extends SupportFragment {


    protected void initToolbarMenu(Toolbar toolbar) {
//        toolbar.inflateMenu(R.menu.share);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
               /* switch (item.getItemId()) {
                    case R.id.action_hierarchy:
                        _mActivity.showFragmentStackHierarchyView();
                        _mActivity.logFragmentStackHierarchy(TAG);
                        break;
                }*/
                return true;
            }
        });
    }
}

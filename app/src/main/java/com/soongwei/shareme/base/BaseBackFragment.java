package com.soongwei.shareme.base;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.soongwei.shareme.R;

import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.swipebackfragment.SwipeBackFragment;


public class BaseBackFragment extends SupportFragment {


    protected void initToolbarNav(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });

        initToolbarMenu(toolbar);
    }

    protected void initToolbarMenu(Toolbar toolbar) {
//        toolbar.inflateMenu(R.menu.share);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//           switch (item.getItemId()) {
//                    case R.id.action_share:
//                        Bitmap icon = mBitmap;
//                        Intent share = new Intent(Intent.ACTION_SEND);
//                        share.setType("image/jpeg");
//                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//                        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
//                        try {
//                            f.createNewFile();
//                            FileOutputStream fo = new FileOutputStream(f);
//                            fo.write(bytes.toByteArray());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
//                        startActivity(Intent.createChooser(share, "Share Image"));
//                        break;
//                }
//                return true;
//            }
//        });
    }
}

package com.anenn.photopick;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.anenn.core.utils.T;

import java.util.ArrayList;

/**
 * 图片选择详情界面
 * Created by Anenn on 2015/7/29
 */
public class PhotoPickDetailActivity extends AppCompatActivity {

    public static final String PICK_DATA = "PICK_DATA";
    public static final String ALL_DATA = "ALL_DATA";
    public static final String FOLDER_NAME = "FOLDER_NAME";
    public static final String PHOTO_BEGIN = "PHOTO_BEGIN";
    public static final String EXTRA_MAX = "EXTRA_MAX";

    private ViewPager mViewPager; // 图片容器控件
    private CheckBox mCheckBox; // 复选按钮控件
    private MenuItem mMenuSend; // 发送按钮控件

    // 游标控件
    private Cursor mCursor;
    // 所有图片数据集
    private ArrayList<ImageInfo> mAllImageInfo;
    // 选中图片数据集
    private ArrayList<ImageInfo> mPickImageInfo;
    // 最多可选择图片的数目
    private int mMaxPick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_pick_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle extras = getIntent().getExtras();
        mAllImageInfo = extras.getParcelableArrayList(ALL_DATA);
        mPickImageInfo = extras.getParcelableArrayList(PICK_DATA);
        int mBegin = extras.getInt(PHOTO_BEGIN, 0);
        mMaxPick = extras.getInt(EXTRA_MAX, 6);
        if (mAllImageInfo == null) {
            String folderName = extras.getString(FOLDER_NAME);
            String where = folderName;
            if (!TextUtils.isEmpty(folderName)) {
                where = String.format("%s='%s'",
                        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                        folderName);
            }
            mCursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{
                            MediaStore.Images.ImageColumns._ID,
                            MediaStore.Images.ImageColumns.DATA},
                    where,
                    null,
                    MediaStore.MediaColumns.DATE_ADDED + " DESC");
        }

        ImagesAdapter adapter = new ImagesAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(mBegin);

        mCheckBox = (CheckBox) findViewById(R.id.checkbox);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateDisplay(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = mViewPager.getCurrentItem();

                String uri = getImagePath(pos);
                if (((CheckBox) v).isChecked()) {
                    if (mPickImageInfo.size() >= mMaxPick) {
                        ((CheckBox) v).setChecked(false);
                        String content = String.format(getString(R.string.photo_limit), mMaxPick);
                        T.t(content);
                        return;
                    }
                    addPicked(uri);
                } else {
                    removePicked(uri);
                }
                updateDataPickCount();
            }
        });
        updateDisplay(mBegin);
    }

    /**
     * 更新 ActionBar 标题文本内容
     *
     * @param pos 选中图片的索引值
     */
    private void updateDisplay(int pos) {
        String uri = getImagePath(pos);
        mCheckBox.setChecked(isPicked(uri));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            String actionbarTitle = "%d/%d";
            actionBar.setTitle(String.format(actionbarTitle, pos + 1, getImageCount()));
        }
    }

    /**
     * 图片是否选中
     *
     * @param path 图片Uri
     * @return true 表示在选中的图片列表中，反之亦然
     */
    private boolean isPicked(String path) {
        for (ImageInfo item : mPickImageInfo) {
            if (item.getPath().equals(path)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 添加图片到选中的图片列表中
     *
     * @param path 图片Uri
     */
    private void addPicked(String path) {
        if (!isPicked(path)) {
            mPickImageInfo.add(new ImageInfo(path, true));
        }
    }

    /**
     * 从选中的图片列表中移除图片
     *
     * @param path 图片的Uri
     */
    private void removePicked(String path) {
        for (int i = 0; i < mPickImageInfo.size(); ++i) {
            if (mPickImageInfo.get(i).getPath().equals(path)) {
                mPickImageInfo.remove(i);
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo_pick_detail, menu);
        mMenuSend = menu.getItem(0);
        updateDataPickCount();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            selectAndSend(false);
            return true;
        } else if (id == R.id.action_send) {
            selectAndSend(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 更新选中图片的数目
     */
    private void updateDataPickCount() {
        String send = String.format(getString(R.string.photo_choice_confirm), mPickImageInfo.size(), mMaxPick);
        mMenuSend.setTitle(send);
    }

    private void selectAndSend(boolean send) {
        Intent intent = new Intent();
        intent.putExtra("data", mPickImageInfo);
        intent.putExtra("send", send);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    class ImagesAdapter extends FragmentStatePagerAdapter {

        ImagesAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ImagePagerFragment fragment = new ImagePagerFragment();
            Bundle bundle = new Bundle();
            String path = getImagePath(position);
            bundle.putString("uri", ImageInfo.pathAddPreFix(path));
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return getImageCount();
        }
    }

    private String getImagePath(int pos) {
        if (mAllImageInfo != null) {
            return mAllImageInfo.get(pos).getPath();
        } else {
            String path = "";
            if (mCursor.moveToPosition(pos)) {
                path = ImageInfo.pathAddPreFix(mCursor.getString(1));
            }
            return path;
        }
    }

    int getImageCount() {
        if (mAllImageInfo != null) {
            return mAllImageInfo.size();
        } else {
            return mCursor.getCount();
        }
    }

    @Override
    public void onBackPressed() {
        selectAndSend(false);
    }

    @Override
    protected void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
        super.onDestroy();
    }
}

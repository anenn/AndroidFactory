/**
 * ****************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * *****************************************************************************
 */
package com.anenn.photopick;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anenn.core.utils.DialogUtil;

import java.util.ArrayList;

/**
 * 图片查看界面
 */
public class ImagePagerActivity extends AppCompatActivity {

    private final String SAVE_INSTANCE_INDEX = "SAVE_INSTANCE_INDEX";
    public static final String SHOW_URIS = "SHOW_URIS";
    public static final String PAGER_POSITION = "POSITION";
    public static final String EDITABLE = "EDITABLE";
    public static final String SINGLE_URI = "SINGLE_URI";
    public static final String DEL_URIS = "DEL_URIS";

    private ViewPager viewPager; // 图片容器控件
    private TextView tvImagePos; // ActionBar 右侧栏的文本控件

    // 图片适配器
    private ImagePager mImagePager;
    // 待查看的图片的Uri数据集
    private ArrayList<String> mShowUris;
    // 当前查看的图片的索引值
    private int mPagerPosition;
    // 是否需要编辑
    private boolean mEditable;
    // 被删除的图片的Uri集合
    private ArrayList<String> mDelUris = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pager);

        if (savedInstanceState != null) {
            mPagerPosition = savedInstanceState.getInt(SAVE_INSTANCE_INDEX, mPagerPosition);
        }

        initViewById();
        initValue();
    }

    private void initViewById() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    private void initValue() {
        Intent intent = getIntent();
        mShowUris = intent.getStringArrayListExtra(SHOW_URIS);
        mPagerPosition = intent.getIntExtra(PAGER_POSITION, -1);
        mEditable = intent.getBooleanExtra(EDITABLE, false);
        String mSingleUri = intent.getStringExtra(SINGLE_URI);

        if (mSingleUri != null) {
            mShowUris = new ArrayList<>();
            mShowUris.add(mSingleUri);
            mPagerPosition = 0;
        }

        View customActionBar = getLayoutInflater().inflate(R.layout.layout_image_pager_actionbar, null);
        tvImagePos = (TextView) customActionBar.findViewById(R.id.imagePos);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (mEditable) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setIcon(android.R.color.transparent);
                actionBar.setCustomView(customActionBar);
                actionBar.setDisplayShowCustomEnabled(true);
                viewPager.setBackgroundColor(getResources().getColor(R.color.bg_grep));
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        setPosDisplay(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
                setPosDisplay(0);
            } else {
                actionBar.hide();
                viewPager.setBackgroundColor(getResources().getColor(android.R.color.black));
            }
        }

        initPager();
    }

    /**
     * 显示当前图片位置占总图片数目的比率
     *
     * @param position 当前图片的位置
     */
    private void setPosDisplay(int position) {
        String pos = String.format("%d/%d", position + 1, mShowUris.size());
        tvImagePos.setText(pos);
    }

    /**
     * 初始化图片容器
     */
    private void initPager() {
        mImagePager = new ImagePager(getSupportFragmentManager());
        viewPager.setAdapter(mImagePager);
        viewPager.setCurrentItem(mPagerPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null)
            outState.putInt(SAVE_INSTANCE_INDEX, viewPager.getCurrentItem());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
            mPagerPosition = savedInstanceState.getInt(SAVE_INSTANCE_INDEX, mPagerPosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mEditable) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_image_pager_del, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_photo_del) {
            final int selectPos = viewPager.getCurrentItem();
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("图片")
                    .setMessage("确定删除？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String s = mShowUris.remove(selectPos);
                            mDelUris.add(s);
                            if (mShowUris.isEmpty()) {
                                onBackPressed();
                            } else {
                                setPosDisplay(viewPager.getCurrentItem());
                                mImagePager.notifyDataSetChanged();
                            }
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
            DialogUtil.dialogTitleLineColor(this, dialog);
            return true;
        } else if (itemId == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDelUris.isEmpty()) {
            setResult(RESULT_CANCELED);
        } else {
            Intent intent = new Intent();
            intent.putExtra(DEL_URIS, mDelUris);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    class ImagePager extends FragmentPagerAdapter {

        public ImagePager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new ImagePagerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("uri", mShowUris.get(i));
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImagePagerFragment fragment = (ImagePagerFragment) super.instantiateItem(container, position);
            fragment.setData(mShowUris.get(position));
            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mShowUris.size();
        }
    }
}
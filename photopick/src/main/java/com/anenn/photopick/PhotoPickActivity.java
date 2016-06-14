package com.anenn.photopick;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.anenn.core.utils.T;
import com.anenn.imageloader.GlobalDisplayImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 图片选择界面
 * Created by Anenn on 2015/12/31.
 */
public class PhotoPickActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, OnClickListener {

    public static final String EXTRA_MAX = "EXTRA_MAX"; //Max Data
    public static final String EXTRA_PICKED = "EXTRA_PICKED"; // Pick Data
    private static final String RESTORE_FILE_URI = "fileUri";
    private final String ALL_PHOTOS = "所有图片";
    private final int REQUEST_PICK = 20; // 图库选择回调标志位
    private final int REQUEST_CAMERA = 21; // 拍照回调标志位

    private GridView mGridView; // 网格控件
    private TextView mPreView; // 预览文本控件
    private TextView tvFoldName; // 文件名控件
    private View listViewGroup; // 列表容器控件
    private ListView lvFolder; // 列表控件
    private MenuItem mMenuFinish; // ActionBar 右侧栏确定控件

    // 选中的图片数据集
    private ArrayList<ImageInfo> mPickData = new ArrayList<>();
    // 图片网格适配器
    private GridPhotoAdapter photoAdapter;
    // 图片文件夹适配器
    private PhotoFolderAdapter mPhotoFolderAdapter;
    // 文件夹Id
    private int mFolderId = 0;
    // 最多图片选择数目
    private int mMaxPick;
    // 拍照图片储存路径
    private Uri fileUri;

    public static final DisplayImageOptions optionsImage = new DisplayImageOptions
            .Builder()
            .showImageOnLoading(GlobalDisplayImage.getImageOnLoading())
            .showImageForEmptyUri(GlobalDisplayImage.getImageForEmptyUri())
            .showImageOnFail(GlobalDisplayImage.getImageOnFail())
            .cacheInMemory(true)
            .cacheOnDisk(false)
            .considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();

    private final String[] PROJECTION = {
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.ImageColumns.WIDTH,
            MediaStore.Images.ImageColumns.HEIGHT
    };

    private View.OnClickListener mOnClickFoldName = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listViewGroup.getVisibility() == View.VISIBLE) {
                hideFolderList();
            } else {
                showFolderList();
            }
        }
    };

    /**
     * 隐藏文件夹列表
     */
    private void showFolderList() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.listview_up);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.listview_fade_in);

        lvFolder.startAnimation(animation);
        listViewGroup.startAnimation(fadeIn);
        listViewGroup.setVisibility(View.VISIBLE);
    }

    /**
     * 显示文件夹列表
     */
    private void hideFolderList() {
        Animation animation = AnimationUtils.loadAnimation(PhotoPickActivity.this, R.anim.listview_down);
        Animation fadeOut = AnimationUtils.loadAnimation(PhotoPickActivity.this, R.anim.listview_fade_out);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                listViewGroup.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        lvFolder.startAnimation(animation);
        listViewGroup.startAnimation(fadeOut);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_pick);

        initViewById();
        initValue();
    }

    private void initViewById() {
        mGridView = (GridView) findViewById(R.id.gridView);
        tvFoldName = (TextView) findViewById(R.id.foldName);
        mPreView = (TextView) findViewById(R.id.preView);
        listViewGroup = findViewById(R.id.listViewGroup);
        lvFolder = (ListView) findViewById(R.id.listView);
    }

    private void initValue() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.photo);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final Intent intent = getIntent();
        mMaxPick = intent.getIntExtra(EXTRA_MAX, 6);
        mPickData = intent.getParcelableArrayListExtra(EXTRA_PICKED);

        tvFoldName.setText(ALL_PHOTOS);
        mPreView.setOnClickListener(this);
        listViewGroup.setOnClickListener(mOnClickFoldName);
        findViewById(R.id.selectFold).setOnClickListener(mOnClickFoldName);

        initListView1();
        initListView2();
    }

    private void initListView1() {
        final String[] needInfo = {
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
        };

        LinkedHashMap<String, Integer> mNames = new LinkedHashMap<>();
        LinkedHashMap<String, ImageInfo> mData = new LinkedHashMap<>();
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                needInfo, "", null, MediaStore.MediaColumns.DATE_ADDED + " DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(2);
                if (!mNames.containsKey(name)) {
                    mNames.put(name, 1);
                    ImageInfo imageInfo = new ImageInfo(cursor.getString(1));
                    mData.put(name, imageInfo);
                } else {
                    int newCount = mNames.get(name) + 1;
                    mNames.put(name, newCount);
                }
            }
            ArrayList<ImageInfoExtra> mFolderData = new ArrayList<>();
            if (cursor.moveToFirst()) {
                ImageInfo imageInfo = new ImageInfo(cursor.getString(1));
                int allImagesCount = cursor.getCount();
                mFolderData.add(new ImageInfoExtra(ALL_PHOTOS, imageInfo, allImagesCount));
            }

            for (String item : mNames.keySet()) {
                ImageInfo info = mData.get(item);
                Integer count = mNames.get(item);
                mFolderData.add(new ImageInfoExtra(item, info, count));
            }

            cursor.close();

            mPhotoFolderAdapter = new PhotoFolderAdapter(mFolderData);
            lvFolder.setAdapter(mPhotoFolderAdapter);
            lvFolder.setOnItemClickListener(mOnFolderItemClick);
        }
    }

    private void initListView2() {
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onClick(View v) {
        if (mPickData.size() == 0) {
            return;
        }

        Intent intent = new Intent(PhotoPickActivity.this, PhotoPickDetailActivity.class);
        intent.putExtra(PhotoPickDetailActivity.FOLDER_NAME, mPhotoFolderAdapter.getSelect());
        intent.putExtra(PhotoPickDetailActivity.PICK_DATA, mPickData);
        intent.putExtra(PhotoPickDetailActivity.ALL_DATA, mPickData);
        intent.putExtra(PhotoPickDetailActivity.EXTRA_MAX, mMaxPick);
        startActivityForResult(intent, REQUEST_PICK);
    }

    private GridView.OnItemClickListener mOnPhotoItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(PhotoPickActivity.this, PhotoPickDetailActivity.class);
            intent.putExtra(PhotoPickDetailActivity.PICK_DATA, mPickData);
            intent.putExtra(PhotoPickDetailActivity.EXTRA_MAX, mMaxPick);
            String folderParam = "";
            if (isAllPhotoMode()) {
                // 第一个item是照相机
                intent.putExtra(PhotoPickDetailActivity.PHOTO_BEGIN, position - 1);
            } else {
                intent.putExtra(PhotoPickDetailActivity.PHOTO_BEGIN, position);
                folderParam = mPhotoFolderAdapter.getSelect();
            }
            intent.putExtra(PhotoPickDetailActivity.FOLDER_NAME, folderParam);
            startActivityForResult(intent, REQUEST_PICK);
        }
    };

    private ListView.OnItemClickListener mOnFolderItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mPhotoFolderAdapter.setSelect((int) id);
            String folderName = mPhotoFolderAdapter.getSelect();
            tvFoldName.setText(folderName);
            hideFolderList();

            if (mFolderId != position) {
                getLoaderManager().destroyLoader(mFolderId);
                mFolderId = position;
            }
            getLoaderManager().initLoader(mFolderId, null, PhotoPickActivity.this);
        }
    };

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
            fileUri = savedInstanceState.getParcelable(RESTORE_FILE_URI);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null && fileUri != null) {
            outState.putParcelable(RESTORE_FILE_URI, fileUri);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo_pick, menu);
        mMenuFinish = menu.getItem(0);
        updatePickCount();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_finish) {
            send();
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK) {
                mPickData.clear();
                List<ImageInfo> imageInfoList = data.getParcelableArrayListExtra("data");
                mPickData.addAll(imageInfoList);
                photoAdapter.notifyDataSetChanged();
                boolean send = data.getBooleanExtra("send", false);
                if (send) {
                    send();
                } else {
                    updatePickCount();
                }
            } else if (requestCode == REQUEST_CAMERA) {
                ImageInfo item = new ImageInfo(fileUri.toString(), true);
                mPickData.add(item);
                send();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void send() {
        if (mPickData.isEmpty()) {
            setResult(Activity.RESULT_CANCELED);
        } else {
            ArrayList<String> pickPathList = new ArrayList<>();
            for (ImageInfo imageInfo : mPickData) {
                pickPathList.add(imageInfo.getPath());
            }
            Intent intent = new Intent();
            intent.putStringArrayListExtra("data", pickPathList);
            setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }

    /**
     * 添加图片
     *
     * @param path 图片Uri
     */
    private void addPicked(String path) {
        if (!isPicked(path)) {
            mPickData.add(new ImageInfo(path, true));
        }
    }

    /**
     * 当前图片是否已经被选中
     *
     * @param path 图片Uri
     * @return true 表示已包含在选中的图片列表中，反之亦然
     */
    public boolean isPicked(String path) {
        for (ImageInfo item : mPickData) {
            if (item.getPath().equals(path)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 移除选中的图片
     *
     * @param path 图片的Uri
     */
    private void removePicked(String path) {
        for (int i = 0; i < mPickData.size(); ++i) {
            if (mPickData.get(i).getPath().equals(path)) {
                mPickData.remove(i);
                return;
            }
        }
    }

    /**
     * 更新图片选择数目
     */
    private void updatePickCount() {
        String format = getString(R.string.photo_choice_confirm_amount);
        mMenuFinish.setTitle(String.format(format, mPickData.size(), mMaxPick));

        String formatPreview = getString(R.string.photo_choice_preview);
        mPreView.setText(String.format(formatPreview, mPickData.size(), mMaxPick));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String where;
        if (!isAllPhotoMode()) {
            String select = ((PhotoFolderAdapter) lvFolder.getAdapter()).getSelect();
            where = String.format("%s='%s'",
                    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                    select
            );
        } else {
            where = "";
        }

        return new CursorLoader(
                this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PROJECTION,
                where,
                null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (isAllPhotoMode()) {
            photoAdapter = new AllPhotoAdapter(data, false, this);
        } else {
            photoAdapter = new GridPhotoAdapter(data, false, this);
        }
        mGridView.setAdapter(photoAdapter);
        mGridView.setOnItemClickListener(mOnPhotoItemClick);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        photoAdapter.swapCursor(null);
    }

    /**
     * 选择了ListView的第一个项，GridView的第一个是照相机
     */
    private boolean isAllPhotoMode() {
        return mFolderId == 0;
    }

    /**
     * 勾选图片事件
     *
     * @param view 复选控件
     */
    public void clickPhotoItem(View view) {
        GridViewCheckTag tag = (GridViewCheckTag) view.getTag();
        if (((CheckBox) view).isChecked()) {
            if (mPickData.size() >= mMaxPick) {
                ((CheckBox) view).setChecked(false);
                String content = String.format(getString(R.string.photo_limit), mMaxPick);
                T.show(content);
                return;
            }
            addPicked(tag.path);
            tag.iconFore.setVisibility(View.VISIBLE);
        } else {
            removePicked(tag.path);
            tag.iconFore.setVisibility(View.INVISIBLE);
        }

        updatePickCount();
    }

    /**
     * 照相机按钮事件
     */
    public void camera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = CameraPhotoUtil.getOutputMediaFileUri();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    static class GridViewCheckTag {
        View iconFore;
        String path = "";

        GridViewCheckTag(View iconFore) {
            this.iconFore = iconFore;
        }
    }
}

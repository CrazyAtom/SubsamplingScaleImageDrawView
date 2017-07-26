package com.github.crazyatom.imagedrawviewsample;

import android.content.res.Configuration;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import com.github.crazyatom.subsamplingscaleimagedrawview.drawtools.BaseDrawTool;
import com.github.crazyatom.subsamplingscaleimagedrawview.Event.NewDrawViewCallback;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.DrawViewSetting;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.Utillity;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

public class ImageDrawViewActivity extends AppCompatActivity implements NewDrawViewCallback {

    private ImageDrawViewPager mPager;
    private ImageDrawViewPagerAdapter mAdapter;
    private ArrayList<ImageInfo> mDataset = new ArrayList<>();

    private CheckBox checkInk;
    private CheckBox checkRectangle;
    private CheckBox checkEllipse;
    private CheckBox checkLine;
    private CheckBox checkCloud;
    private CheckBox checkText;
    private CheckBox checkPhoto;
    private CheckBox checkBaseDimension;
    private CheckBox checkDimension;
    private CheckBox checkEraser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_draw_view);

        initialise();
        mPager = (ImageDrawViewPager) findViewById(R.id.image_pager);
        mAdapter = new ImageDrawViewPagerAdapter(mDataset, new ImageDrawViewPagerAdapter.CreateviewListener() {
            @Override
            public void createView(ImageDrawView imageView, int position) {
                if (mPager.getCurrentItem() == position) {
                    mAdapter.createView(position);
                }
            }
        });
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener(mOnPageChangeListener);

        changeTitle(0);

        checkInk = (CheckBox) findViewById(R.id.ink);
        checkInk.setOnCheckedChangeListener(checkedChangeListener);
        checkRectangle = (CheckBox) findViewById(R.id.rectangle);
        checkRectangle.setOnCheckedChangeListener(checkedChangeListener);
        checkEllipse = (CheckBox) findViewById(R.id.ellipse);
        checkEllipse.setOnCheckedChangeListener(checkedChangeListener);
        checkLine = (CheckBox) findViewById(R.id.line);
        checkLine.setOnCheckedChangeListener(checkedChangeListener);
        checkCloud = (CheckBox) findViewById(R.id.cloud);
        checkCloud.setOnCheckedChangeListener(checkedChangeListener);
        checkText = (CheckBox) findViewById(R.id.text);
        checkText.setOnCheckedChangeListener(checkedChangeListener);
        checkPhoto = (CheckBox) findViewById(R.id.photo);
        checkPhoto.setOnCheckedChangeListener(checkedChangeListener);
        checkBaseDimension = (CheckBox) findViewById(R.id.dimension_ref);
        checkBaseDimension.setOnCheckedChangeListener(checkedChangeListener);
        checkDimension = (CheckBox) findViewById(R.id.dimension);
        checkDimension.setOnCheckedChangeListener(checkedChangeListener);
        checkEraser = (CheckBox) findViewById(R.id.free_eraser);
        checkEraser.setOnCheckedChangeListener(checkedChangeListener);
    }

    CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            checkedAnnotation(compoundButton.getId(), b);
            final ImageDrawView imageDrawView = mAdapter.getImageView(mPager.getCurrentItem());
            if (b == true) {
                imageDrawView.changeTool(getToolTypeById(compoundButton.getId()));
            } else {
                imageDrawView.changeTool(BaseDrawTool.DrawToolType.NONE);
            }
        }
    };

    private BaseDrawTool.DrawToolType getToolTypeById(final int id) {
        switch (id) {
            case R.id.ink:
                return BaseDrawTool.DrawToolType.INK;
            case R.id.rectangle:
                return BaseDrawTool.DrawToolType.RECTANGLE;
            case R.id.ellipse:
                return BaseDrawTool.DrawToolType.ELLIPSE;
            case R.id.line:
                return BaseDrawTool.DrawToolType.LINE;
            case R.id.cloud:
                return BaseDrawTool.DrawToolType.CLOUD;
            case R.id.text:
                return BaseDrawTool.DrawToolType.TEXT;
            case R.id.photo:
                return BaseDrawTool.DrawToolType.PHOTO;
            case R.id.dimension_ref:
                return BaseDrawTool.DrawToolType.DIMENSION_REF;
            case R.id.dimension:
                return BaseDrawTool.DrawToolType.DIMENSION;
            case R.id.free_eraser:
                return BaseDrawTool.DrawToolType.ERASER;
            default:
                return BaseDrawTool.DrawToolType.INK;
        }
    }

    private void checkedAnnotation(final int id, boolean checked) {
        checkInk.setChecked((id == checkInk.getId()) && checked);
        checkRectangle.setChecked((id == checkRectangle.getId()) && checked);
        checkEllipse.setChecked((id == checkEllipse.getId()) && checked);
        checkLine.setChecked((id == checkLine.getId()) && checked);
        checkCloud.setChecked((id == checkCloud.getId()) && checked);
        checkText.setChecked((id == checkText.getId()) && checked);
        checkPhoto.setChecked((id == checkPhoto.getId()) && checked);
        checkBaseDimension.setChecked((id == checkBaseDimension.getId()) && checked);
        checkDimension.setChecked((id == checkDimension.getId()) && checked);
        checkEraser.setChecked((id == checkEraser.getId()) && checked);
    }

    private void initialise() {
        DrawViewSetting.init(getApplicationContext());

        mDataset.clear();
        File downloadFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File list[] = downloadFile.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".png");
            }
        });
        if (list != null) {
            for (File file : list) {
                if (file.getName().contains("preview") == true) {
                    String preview = file.getAbsolutePath();
                    String origin = preview.replace("_preview", "");
                    mDataset.add(new ImageInfo(origin, preview));
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static String convertFromByte(long value) {
        double convert = value;
        final long B = 1024;
        final long KB = B * 1024;
        final long MB = KB * 1024;
        final long GB = MB * 1024;
        if (value < B) {
            return String.format("%.1fB", convert);
        } else if (value < KB) {
            return String.format("%.1fKB", convert / B);
        } else if (value < MB) {
            return String.format("%.1fMB", convert / KB);
        } else {
            return String.format("%.1fGB", convert / MB);
        }
    }

    private void changeTitle(int position) {
        final ImageInfo imageInfo = mAdapter.getItem(position);
        if (imageInfo != null) {
            File file = new File(imageInfo.path);
            this.setTitle(convertFromByte(file.length()) + " (" + imageInfo.getWidth() + " x " + imageInfo.getHeight() + ")");
        }
    }

    ViewPager.SimpleOnPageChangeListener mOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            changeTitle(position);
            mAdapter.createView(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
        }
    };

    @Override
    public String newUUID() {
        return this.getClass().getSimpleName() + Utillity.getUUID();
    }
}

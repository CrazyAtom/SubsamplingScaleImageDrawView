package kr.co.aroad.subsamplingscaleimagedrawview;

import android.content.res.Configuration;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import kr.co.aroad.subscaleimagedrawview.util.AnnotationSetting;
import kr.co.aroad.subscaleimagedrawview.views.ImageDrawView;

public class ImageDrawViewActivity extends AppCompatActivity {

    private ImageDrawViewPager mPager;
    private ImageDrawViewPagerAdapter mAdapter;
    private ArrayList<ImageInfo> mDataset = new ArrayList<>();

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
    }

    private void initialise() {
        AnnotationSetting.init(getApplicationContext());

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
}

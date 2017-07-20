package com.github.crazyatom.subsamplingscaleimagedrawview;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.github.crazyatom.subscaleimagedrawview.views.ImageDrawView;
import com.github.crazyatom.subscaleimagedrawview.views.ImageSource;

/**
 * Created by crazy on 2017-07-06.
 */

public class ImageDrawViewPagerAdapter extends PagerAdapter {

    public interface CreateviewListener {
        void createView(ImageDrawView imageView, int position);
    }

    private ArrayList<ImageInfo> mDataset = new ArrayList<>();
    private CreateviewListener mCreateviewListener;
    private SparseArray<ImageDrawView> mImageViewSparseArray = new SparseArray<>();

    public ImageDrawViewPagerAdapter(ArrayList<ImageInfo> dataset, CreateviewListener createviewListener) {
        this.mDataset = dataset;
        this.mCreateviewListener = createviewListener;
    }

    @Override
    public int getItemPosition(Object object) {
        int position = mDataset.indexOf(object);
        if (position == -1)
            return POSITION_NONE;
        else
            return position;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.image_draw_view, container, false);
        ImageDrawView imageView = (ImageDrawView) view.findViewById(R.id.view);
        container.addView(view);
        mImageViewSparseArray.put(position, imageView);

        if (mCreateviewListener != null) {
            mCreateviewListener.createView(imageView, position);
        }

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
        mImageViewSparseArray.remove(position);
    }

    @Override
    public int getCount() {
        return mDataset.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public ImageInfo getItem(int position) {
        if (mDataset.size() > 0) {
            return mDataset.get(position);
        } else {
            return null;
        }
    }

    /**
     * position에 해당하는 view
     *
     * @param position
     * @return view
     */
    public ImageDrawView getImageView(int position) {
        return mImageViewSparseArray.get(position);
    }

    /**
     * view에 이미지 설정
     *
     * @param position
     */
    public void createView(int position) {
        final ImageInfo imageInfo = mDataset.get(position);
        if (imageInfo != null) {
            final ImageDrawView imageDrawView = getImageView(position);
//            imageDrawView.setImage(
//                    ImageSource.uri(imageInfo.path).dimensions(imageInfo.getWidth(), imageInfo.getHeight()).tiling(false),
//                    ImageSource.uri(imageInfo.preview_path));
            imageDrawView.setImage(ImageSource.uri(imageInfo.path).tiling(false));
        }
    }
}

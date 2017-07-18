package kr.co.aroad.multiphotopicker.utils;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.aroad.multiphotopicker.activity.PhotoPickerActivity;
import kr.co.aroad.multiphotopicker.R;
import kr.co.aroad.multiphotopicker.models.Photo;
import kr.co.aroad.multiphotopicker.views.PhotoHolder;

/**
 * Created by crazy on 2017-05-26.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> implements LoaderManager.LoaderCallbacks<List<Photo>>, PhotoHolder.OnPhotoPickListener {

    private final PhotoPickerActivity activity;
    private final boolean isMultiple;

    private List<Photo> photos;
    private List<Photo> selected = new ArrayList<>();

//    private Handler handler;

    public PhotoAdapter(PhotoPickerActivity activity, boolean isMultiple) {
        this.activity = activity;
        this.isMultiple = isMultiple;
    }

//    private void initPhotoList() {
//        photos = new ArrayList<>();
//        handler = new Handler();
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                Looper.prepare();
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        photos = loadPhoto();
//                    }
//                });
//                Looper.loop();
//            }
//        }.start();
//    }
//
//    private List<Photo> loadPhoto() {
//        // DATA 이미지 파일 스트림 데이터 경로
//        // _ID 이미지 고유 ID
//        final String[] projection = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
//        final String sortOrder = MediaStore.Images.Media.DATE_ADDED;
//
//        // 모든 이미지 개체 출력
//        Cursor imageCursor = activity.getContentResolver().query(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                projection,
//                null,
//                null,
//                sortOrder);
//
//        ArrayList<Photo> result = new ArrayList<>(imageCursor.getCount());
//        int dataColumnIndex = imageCursor.getColumnIndex(projection[0]);
//
//        if (imageCursor != null && imageCursor.moveToFirst()) {
//            do {
//                String filePath = imageCursor.getString(dataColumnIndex);
//                Photo photo = new Photo(filePath);
//                result.add(photo);
//            } while (imageCursor.moveToNext());
//        }
//
//        imageCursor.close();
//        return result;
//    }

    /**
     * 선택된 사진
     *
     * @return
     */
    public Photo[] getPicked() {
        Photo[] photos = new Photo[selected.size()];

        int i = 0;
        for (Photo photo : selected) {
            photos[i] = photo;
            i++;
        }

        return photos;
    }

    @Override
    public Loader<List<Photo>> onCreateLoader(int id, Bundle args) {
        return new PhotoLoader(activity);
    }

    @Override
    public void onLoadFinished(Loader<List<Photo>> loader, List<Photo> photos) {
        this.photos = new ArrayList<>(photos);
        notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Photo>> loader) {
        this.photos.clear();
        notifyDataSetChanged();
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_photo, parent, false);
        return new PhotoHolder(view, this);
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        holder.setPhoto(photos.get(position), activity);
        holder.reset();
    }

    @Override
    public int getItemCount() {
        return (photos != null) ? photos.size() : 0;
    }

    @Override
    public void onSelect(Photo photo) {
        if (isMultiple == false) {
            // 단일 선택 모드일 경우 이전에 선택한 아이템이 있으면 해제
            if (selected.isEmpty() == false) {
                selected.get(0).setSelected(false);
                selected.get(0).getContainView().reset();
                selected.clear();
            }
        } else {
            // 다중 선택 모드일 경우
            photo.setSelectedCnt(selected.size() + 1);
            photo.getContainView().reset();
        }

        selected.add(photo);
        notifyIfCanBeDone();
    }

    @Override
    public void onDeselect(Photo photo) {
        int index = selected.indexOf(photo);
        if (selected.remove(photo)) {
            photo.getContainView().reset();

            for (int i = index; i < selected.size(); i++) {
                selected.get(i).setSelectedCnt(i + 1);
                selected.get(i).getContainView().reset();
            }
        }

        notifyIfCanBeDone();
    }

    /**
     * activity의 확인 메뉴 활성/비활성
     */
    private void notifyIfCanBeDone() {
        activity.onSelect(!selected.isEmpty());
    }
}

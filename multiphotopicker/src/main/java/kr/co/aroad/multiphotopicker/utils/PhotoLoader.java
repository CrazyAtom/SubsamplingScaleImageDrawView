package kr.co.aroad.multiphotopicker.utils;

import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import kr.co.aroad.multiphotopicker.models.Photo;

/**
 * Created by crazy on 2017-05-26.
 */

public class PhotoLoader extends AsyncTaskLoader<List<Photo>> {

    private List<Photo> photos;
    private ContentResolver contentResolver;

    public PhotoLoader(Context context) {
        super(context);
        contentResolver = context.getContentResolver();
    }

    @Override
    public List<Photo> loadInBackground() {
        // DATA 이미지 파일 스트림 데이터 경로
        // _ID 이미지 고유 ID
        final String[] projection = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String sortOrder = MediaStore.Images.Media._ID + " COLLATE LOCALIZED DESC";

        // 모든 이미지 개체 출력
        Cursor imageCursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder);

        ArrayList<Photo> result = new ArrayList<>(imageCursor.getCount());
        int dataColumnIndex = imageCursor.getColumnIndex(projection[0]);
        int idColumnIndex = imageCursor.getColumnIndex(projection[1]);

        if (imageCursor != null && imageCursor.moveToFirst()) {
            do {
                String filePath = imageCursor.getString(dataColumnIndex);
//                String imageId = imageCursor.getString(idColumnIndex);
//                String thumbnailPath = getThumbnail(imageId);
                Photo photo = new Photo(filePath);
                result.add(photo);
            } while (imageCursor.moveToNext());
        }

        imageCursor.close();



        return result;
    }

    /**
     * 이미지 ID에 해당하는 이미지 파일의 썸네일
     * 썸네일이 없을 경우 썸네일 생성
     *
     * @param imageId
     * @return
     */
    private String getThumbnail(String imageId) {
        // DATA 이미지 파일 스트림 데이터 경로
        String[] projection = { MediaStore.Images.Thumbnails.DATA };

        // 이미지 ID가 imageId인 썸네일을 출력
        Cursor thumbnailCursor = contentResolver.query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                projection,
                MediaStore.Images.Thumbnails.IMAGE_ID + "=?",
                new String[]{imageId},
                null);

        if (thumbnailCursor != null) {
            if (thumbnailCursor.moveToFirst()) {
                // 썸네일이 있으면 썸네일 경로를 Uri로 변환해서 리턴
                int thumbnailColumnIndex = thumbnailCursor.getColumnIndex(projection[0]);
                String thumbnailPath = thumbnailCursor.getString(thumbnailColumnIndex);
                thumbnailCursor.close();
                return thumbnailPath;
            } else {
                // 썸네일이 없으면 썸네일 생성을 요청하고 다시 썸네일 uri 검색
                MediaStore.Images.Thumbnails.getThumbnail(contentResolver, Long.parseLong(imageId), MediaStore.Images.Thumbnails.MINI_KIND, null);
                thumbnailCursor.close();
                return getThumbnail(imageId);
            }
        } else {
            return null;
        }
    }

    @Override
    public void deliverResult(List<Photo> photos) {
        if (isReset()) {
            // 로더가 중지 된 동안 비동기 쿼리가 들어오는 경우는 결과를 제공 하지 않는다
            if (photos != null) {
                onReleaseResources(photos);
            }
        }
        // 현재 데이터를 새로운 데이터로 교체 하고
        // 이전 데이터는 필요시 사용할 수 있기 때문에 남겨둔다
        List<Photo> oldPhotos = this.photos;
        this.photos = photos;

        if (isStarted()) {
            // 로더가 시작된 경우 즉시 결과를 제공 한다
            super.deliverResult(this.photos);
        }

        // 필요한 경우 이전 결과를 제공한다
        if (oldPhotos != null) {
            onReleaseResources(oldPhotos);
        }
    }

    @Override
    protected void onStartLoading() {
        if (photos != null) {
            deliverResult(photos);
        } else {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(List<Photo> photos) {
        super.onCanceled(photos);
        onReleaseResources(photos);
    }

    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();

        if (photos != null) {
            onReleaseResources(photos);
            photos = null;
        }
    }

    protected void onReleaseResources(List<Photo> photos) {

    }
}

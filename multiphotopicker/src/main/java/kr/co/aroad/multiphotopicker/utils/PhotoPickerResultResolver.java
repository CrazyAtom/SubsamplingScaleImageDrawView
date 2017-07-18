package kr.co.aroad.multiphotopicker.utils;

import android.content.Intent;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import kr.co.aroad.multiphotopicker.activity.PhotoPickerActivity;
import kr.co.aroad.multiphotopicker.models.Photo;

/**
 * Created by crazy on 2017-05-26.
 */

public class PhotoPickerResultResolver {

    public static List<Photo> resolve(Intent data) {
        Parcelable[] photoPicked = data.getParcelableArrayExtra(PhotoPickerActivity.PICKED_PHOTOS);
        // TODO: 2017-05-26 콜백으로 인텐트 및 결과를 반환 할지 고려
        boolean isMultiple = data.getBooleanExtra(PhotoPickerActivity.ARG_IS_MULTIPLE, true);

        List<Photo> photosToReturn = new ArrayList<>();
        for (Parcelable photo : photoPicked) {
            photosToReturn.add((Photo) photo);
        }

        return photosToReturn;
    }
}

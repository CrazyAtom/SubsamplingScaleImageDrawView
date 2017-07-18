package kr.co.aroad.multiphotopicker.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

import kr.co.aroad.multiphotopicker.views.PhotoHolder;

/**
 * Created by crazy on 2017-05-26.
 */

public class Photo implements Parcelable {

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    private String imagePath;
    private boolean selected = false;
    private int selectedCnt = 0;
    private PhotoHolder containView = null;

    public Photo(String imagePath) {
        this.imagePath = imagePath;
    }

    protected Photo(Parcel in) {
        this.imagePath = in.readString();
        this.selected = in.readByte() != 0;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getSelectedCnt() {
        return selectedCnt;
    }

    /**
     * 선택 번호
     *
     * @param selectedCnt
     */
    public void setSelectedCnt(int selectedCnt) {
        this.selectedCnt = selectedCnt;
    }

    /**
     * 사진 아이템이 속한 viewHolder
     *
     * @return
     */
    public PhotoHolder getContainView() {
        return containView;
    }

    /**
     * 사진 아이템이 속한 viewHolder 설정
     *
     * @param containView
     */
    public void setContainView(PhotoHolder containView) {
        this.containView = containView;
    }

    /**
     * 이미지 파일 경로 uri
     *
     * @return
     */
    public Uri getImageUri() {
        return Uri.fromFile(new File(imagePath));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imagePath);
        dest.writeByte((byte) (this.selected ? 1 : 0));
    }
}

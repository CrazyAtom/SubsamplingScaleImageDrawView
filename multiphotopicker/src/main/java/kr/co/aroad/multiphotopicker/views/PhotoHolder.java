package kr.co.aroad.multiphotopicker.views;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.ButterKnife;
import kr.co.aroad.multiphotopicker.R;
import kr.co.aroad.multiphotopicker.models.Photo;

/**
 * Created by crazy on 2017-05-26.
 */

public class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public interface OnPhotoPickListener {
        void onSelect(Photo photo);
        void onDeselect(Photo photo);
    }

    ImageView photoView;
    ImageView square;
    TextView orderText;

    private Photo photo;
    private OnPhotoPickListener listener;

    public PhotoHolder(View itemView, OnPhotoPickListener listener) {
        super(itemView);
        this.photoView = ButterKnife.findById(itemView, R.id.photo);
        this.square = ButterKnife.findById(itemView, R.id.square);
        this.orderText = ButterKnife.findById(itemView, R.id.order_text);

        itemView.setOnClickListener(this);

        this.listener = listener;
    }

    /**
     * 사진 데이터 설정 및 이미지뷰에 사진 로드
     *
     * @param photo
     * @param context
     */
    public void setPhoto(Photo photo, Context context) {
        this.photo = photo;
        this.photo.setContainView(this);
        Glide.with(context).load(this.photo.getImageUri()).thumbnail(0.5f).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(photoView);
    }

    public Photo getPhoto() {
        return photo;
    }

    @Override
    public void onClick(View v) {
        boolean isSelected = this.photo.isSelected();
        this.photo.setSelected(!isSelected);
        if (isSelected == true) {
            listener.onDeselect(this.photo);
        } else {
            listener.onSelect(this.photo);
        }
    }

    /**
     * 선택에 따른 재설정
     *
     */
    public void reset() {
        if (this.photo.isSelected() == false) {
            // 선택 해제일 경우 번호와 선택 상자를 비활성화 한다
            orderText.setVisibility(View.INVISIBLE);
            square.setVisibility(View.INVISIBLE);
        } else {
            // 선택일 경우 번호와 선택 상자를 활성화 한다
            // 선택 번호가 0일 경우는 선택 번호는 비활성화 (단일 선택모드)
            square.setVisibility(View.VISIBLE);
            if (this.photo.getSelectedCnt() == 0) {
                orderText.setVisibility(View.INVISIBLE);
            } else {
                orderText.setVisibility(View.VISIBLE);
                orderText.setText(String.valueOf(this.photo.getSelectedCnt()));
            }
        }
    }
}

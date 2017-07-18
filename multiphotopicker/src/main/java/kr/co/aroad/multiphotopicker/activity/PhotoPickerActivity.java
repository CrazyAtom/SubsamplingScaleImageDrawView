package kr.co.aroad.multiphotopicker.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import kr.co.aroad.multiphotopicker.R;
import kr.co.aroad.multiphotopicker.utils.PhotoAdapter;

public class PhotoPickerActivity extends AppCompatActivity {

    public static final String PICKED_PHOTOS = "Picked Photos";
    public static final String ARG_IS_MULTIPLE = "Is Multiple";

    private PhotoAdapter photoAdapter;
    private MenuItem confirmIcon;
    private boolean isMultiple;

    private RecyclerView photoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);

        photoList = ButterKnife.findById(this, R.id.photo_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isMultiple = getIntent().getBooleanExtra(ARG_IS_MULTIPLE, false);
        photoAdapter = new PhotoAdapter(this, isMultiple);

        getLoaderManager().initLoader(0, null, photoAdapter);
        photoList.setLayoutManager(new StaggeredGridLayoutManager(getSpanCount(), StaggeredGridLayoutManager.VERTICAL));
        photoList.setAdapter(photoAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo_picker, menu);
        confirmIcon = menu.findItem(R.id.confirm);
        confirmIcon.setEnabled(false);

        Drawable iconDrawable = confirmIcon.getIcon();
        iconDrawable.setTint(ContextCompat.getColor(this, R.color.colorWhite));
        iconDrawable.setAlpha((int) (255 * 0.3));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.confirm) {
            Intent intent = new Intent();
            intent.putExtra(PICKED_PHOTOS, photoAdapter.getPicked());
            intent.putExtra(ARG_IS_MULTIPLE, isMultiple);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        } else if (id == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 해상도에 따른 간격 구하기
     * @return
     */
    private int getSpanCount() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        final int displayWidth = displayMetrics.widthPixels;
        final int itemWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
        int count = Math.max(4, displayWidth / itemWidth);
        return count;
    }

    /**
     * 사진 선택 확인 메뉴 활성/비활성
     *
     * @param canBeDone
     */
    public void onSelect(boolean canBeDone) {
        confirmIcon.setEnabled(canBeDone);
        Drawable iconDrawable = confirmIcon.getIcon();
        iconDrawable.setAlpha(canBeDone ? 255 : (int) (255 * 0.3));
    }
}

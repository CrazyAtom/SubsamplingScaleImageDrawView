package com.github.crazyatom.subsamplingscaleimagedrawview.util;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import com.github.crazyatom.subsamplingscaleimagedrawview.drawviews.BaseDrawView;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

import java.util.LinkedList;

/**
 * Created by kanghb on 2017. 9. 7..
 */

public class UndoManager {

    private static final String TAG = ImageDrawView.class.getSimpleName();

    public enum UndoState {
        REMOVE,
        ADD,
        UPDATE
    }

    private class UndoItem {
        private String uniqId;
        private UndoState undoState;
        private Pair<BaseDrawView, BaseDrawView> drawView;

        public UndoItem(final String uniqId, final UndoState undoState, final BaseDrawView before, final BaseDrawView after) {
            this.uniqId = uniqId;
            this.undoState = undoState;
            this.drawView = Pair.create(before, after);
        }
    }

    private ImageDrawView imageDrawView;
    private LinkedList<UndoItem> undoItemList;
    private LinkedList<UndoItem> redoItemList;

    public UndoManager(@NonNull final ImageDrawView imageDrawView) {
        this.imageDrawView = imageDrawView;
    }

    /**
     * undo/redo list 초기화
     */
    public void initItemList() {
        if (undoItemList != null) {
            undoItemList.clear();
            undoItemList = null;
        }

        if (redoItemList != null) {
            redoItemList.clear();
            redoItemList = null;
        }
    }

    /**
     * undo item 추가
     */
    public void addUndoItem(final UndoItem item, final boolean initRedoList) {
        if (undoItemList == null) {
            undoItemList = new LinkedList<>();
        }
        undoItemList.push(item);

        if (initRedoList && redoItemList != null) {
            redoItemList.clear();
        }
    }

    /**
     * redo item 추가
     */
    private void addRedoItem(final UndoItem item) {
        if (redoItemList == null) {
            redoItemList = new LinkedList<>();
        }
        redoItemList.push(item);
    }

    /**
     * undo가 가능한지 여부
     */
    private boolean canUndo() {
        return undoItemList.size() > 0;
    }

    /**
     * redo가 가능한지 여부
     */
    private boolean canRedo() {
        return redoItemList.size() > 0;
    }

    /**
     * undo 실행
     * undo item은 redo 리스트에 add
     */
    public boolean doUndo() {
        if (canUndo()) {
            final UndoItem item = undoItemList.pop();
            addRedoItem(item);
            switch (item.undoState) {
                case REMOVE:
                    this.imageDrawView.getDrawViewMap().remove(item.uniqId);
                    break;
                case ADD:
                    this.imageDrawView.getDrawViewMap().put(item.uniqId, item.drawView.first);
                    if (this.imageDrawView.getRemoveDrawViewListener() != null) {
                        this.imageDrawView.getRemoveDrawViewListener().cancelRemoveDrawView(item.uniqId);
                    }
                    break;
                case UPDATE:
                    this.imageDrawView.addDrawView(item.drawView.first); // key 중복시 새로운 아이템으로 덮어 쓰도록
                    break;
            }
            Log.d(TAG, "doUndo: " + undoItemList.size());
            this.imageDrawView.postInvalidate();
            return true;
        } else {
            return false;
        }
    }

    /**
     * redo 실행
     * redo item은 undo 리스트에 add
     */
    public boolean doRedo() {
        if (canRedo()) {
            final UndoItem item = redoItemList.pop();
            addUndoItem(item, false);
            switch (item.undoState) {
                case REMOVE:
                    this.imageDrawView.getDrawViewMap().put(item.uniqId, item.drawView.second);
                    break;
                case ADD:
                    this.imageDrawView.getDrawViewMap().remove(item.uniqId);
                    if (this.imageDrawView.getRemoveDrawViewListener() != null) {
                        this.imageDrawView.getRemoveDrawViewListener().removeDrawView(item.uniqId);
                    }
                    break;
                case UPDATE:
                    this.imageDrawView.addDrawView(item.drawView.second); // key 중복시 새로운 아이템으로 덮어 쓰도록
                    break;
            }
            Log.d(TAG, "doRedo: " + redoItemList.size());
            this.imageDrawView.postInvalidate();
            return true;
        } else {
            return false;
        }
    }

    /**
     * undo item 생성
     * @param state undo item 상태 (액션에 반대되는 상태로 기록)
     * @param before undo item
     * @param after redo item
     * @return UndoItem
     */
    public UndoItem makeUndoItem(final UndoState state, final BaseDrawView before, final BaseDrawView after) {
        return new UndoItem(before.getUniqId(), state, before, after);
    }
}

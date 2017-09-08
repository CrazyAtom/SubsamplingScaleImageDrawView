package com.github.crazyatom.subsamplingscaleimagedrawview.util;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.github.crazyatom.subsamplingscaleimagedrawview.drawviews.BaseDrawView;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

import java.util.ArrayList;
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

    public class UndoItem {
        private UndoState undoState;
        private ArrayList<Pair<BaseDrawView, BaseDrawView>> items = null;

        public UndoItem(UndoState undoState) {
            this.undoState = undoState;
        }

        public UndoItem(final UndoState undoState, final BaseDrawView before, final BaseDrawView after) {
            this.undoState = undoState;
            pushItem(before, after);
        }

        public void pushItem(final BaseDrawView before, final BaseDrawView after) {
            if (items == null) {
                items = new ArrayList<>();
            }
            if (before != null || after != null) {
                items.add(Pair.create((before != null) ? before.cloneObj() : null, (after != null) ? after.cloneObj() : null));
            }
        }

        /**
         * drawView UniqId
         * @param index item index
         * @return String
         */
        public String getId(final int index) {
            if (items.size() > 0) {
                return items.get(index).first.getUniqId();
            }
            return null;
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
            final UndoItem undoItem = undoItemList.pop();
            addRedoItem(undoItem);
            for (final Pair<BaseDrawView, BaseDrawView> item : undoItem.items) {
                switch (undoItem.undoState) {
                    case REMOVE:
                        this.imageDrawView.getDrawViewMap().remove(item.first.getUniqId());
                        break;
                    case ADD:
                        this.imageDrawView.getDrawViewMap().put(item.first.getUniqId(), item.first);
                        if (this.imageDrawView.getRemoveDrawViewListener() != null) {
                            this.imageDrawView.getRemoveDrawViewListener().cancelRemoveDrawView(item.first.getUniqId());
                        }
                        break;
                    case UPDATE:
                        this.imageDrawView.addDrawView(item.first); // key 중복시 새로운 아이템으로 덮어 쓰도록
                        break;
                }
            }
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
            final UndoItem undoItem = redoItemList.pop();
            addUndoItem(undoItem, false);
            for (final Pair<BaseDrawView, BaseDrawView> item : undoItem.items) {
                switch (undoItem.undoState) {
                    case REMOVE:
                        this.imageDrawView.getDrawViewMap().put(item.first.getUniqId(), item.first);
                        break;
                    case ADD:
                        this.imageDrawView.getDrawViewMap().remove(item.first.getUniqId());
                        if (this.imageDrawView.getRemoveDrawViewListener() != null) {
                            this.imageDrawView.getRemoveDrawViewListener().removeDrawView(item.first.getUniqId());
                        }
                        break;
                    case UPDATE:
                        this.imageDrawView.addDrawView(item.second); // key 중복시 새로운 아이템으로 덮어 쓰도록
                        break;
                }
            }
            this.imageDrawView.postInvalidate();
            return true;
        } else {
            return false;
        }
    }

    /**
     * undo item 생성
     * @param state undo item 상태 (액션에 반대되는 상태로 기록)
     * @return UndoItem
     */
    public UndoItem makeUndoItem(final UndoState state) {
        return new UndoItem(state);
    }

    /**
     * undo item 생성
     * @param state undo item 상태 (액션에 반대되는 상태로 기록)
     * @param before undo item
     * @param after redo item
     * @return UndoItem
     */
    public UndoItem makeUndoItem(final UndoState state, final BaseDrawView before, final BaseDrawView after) {
        return new UndoItem(state, before, after);
    }
}

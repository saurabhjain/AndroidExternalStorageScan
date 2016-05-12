package com.exercise.storage.storagesearch.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by sjain70 on 5/11/16.
 */
public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int mVerticalSpaceHeight;

    /**
     * @param mVerticalSpaceHeight: verticals space in DPI
     * @param mActivity: activity
     */
    public VerticalSpaceItemDecoration(int mVerticalSpaceHeight, Activity mActivity) {
        final float scale = mActivity.getResources().getDisplayMetrics().density;
        int verticalSpace = (int) (mVerticalSpaceHeight * scale + 0.5f);
        this.mVerticalSpaceHeight = verticalSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1)
            outRect.bottom = mVerticalSpaceHeight;
    }
}
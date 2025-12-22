package com.idol.prank.call.chat.video.activities;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CustomLayoutManager extends LinearLayoutManager {

    public CustomLayoutManager(Context context) {
        super(context);
    }

    public CustomLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        scaleDownView();
    }

    private void scaleDownView() {
        float mid = getHeight() / 2.0f;
        float height = getHeight();

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            float childMid = (getDecoratedBottom(child) + getDecoratedTop(child)) / 2.0f;
            float scale = 1 - Math.abs(childMid - mid) / height * 0.5f;

            child.setScaleX(scale);
            child.setScaleY(scale);
        }
    }
}

package com.idol.prank.call.chat.video;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.idol.prank.call.chat.video.R;

public class CustomItemDecoration extends RecyclerView.ItemDecoration {

    private final int smallPadding;
    private final int fullPadding;

    public CustomItemDecoration(Context context) {
        Resources resources = context.getResources();
        smallPadding = resources.getDimensionPixelSize(R.dimen.small_padding);
        fullPadding = resources.getDimensionPixelSize(R.dimen.full_padding);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        // Get the center of the screen
        int centerX = parent.getWidth() / 2;

        // Get the center of the current view
        int childCenterX = view.getLeft() + view.getWidth() / 2;

        // Calculate the distance between the centers
        int distanceFromCenter = Math.abs(centerX - childCenterX);

        // Set the padding based on the distance from the center
        if (distanceFromCenter <= fullPadding) {
            int padding = fullPadding - distanceFromCenter;
            view.setPadding(padding, padding, padding, padding);
            outRect.set(padding, padding, padding, padding);
        } else {
            view.setPadding(smallPadding, smallPadding, smallPadding, smallPadding);
            outRect.set(smallPadding, smallPadding, smallPadding, smallPadding);
        }
    }
}


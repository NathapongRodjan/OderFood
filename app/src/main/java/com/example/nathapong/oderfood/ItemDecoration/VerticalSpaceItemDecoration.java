package com.example.nathapong.oderfood.ItemDecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration{

    private final int topMargin;
    private final int leftMargin;
    private final int rightMargin;
    private final int bottomMargin;


    public VerticalSpaceItemDecoration(int topMargin, int leftMargin, int rightMargin, int bottomMargin) {
        this.topMargin = topMargin;
        this.leftMargin = leftMargin;
        this.rightMargin = rightMargin;
        this.bottomMargin = bottomMargin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.top = topMargin;
        outRect.left = leftMargin;
        outRect.right = rightMargin;

        //outRect.set(leftMargin,topMargin,rightMargin,bottomMargin);

        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = bottomMargin;
        }
    }
}

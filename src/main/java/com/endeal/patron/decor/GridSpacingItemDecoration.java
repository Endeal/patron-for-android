package com.endeal.patron.decor;

import android.support.v7.widget.RecyclerView;
import android.graphics.Rect;
import android.view.View;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration
{
    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge)
    {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        /*
        outRect.left = spacing - column * spacing / spanCount;
        outRect.right = (column + 1) * spacing / spanCount;
        outRect.top = spacing;
        outRect.bottom = spacing;
        */
        outRect.left = spacing;
        outRect.right = spacing;
        outRect.top = spacing;
        outRect.bottom = spacing;
    }
}

package com.example.psychology.base_app.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration2 extends RecyclerView.ItemDecoration {

    private int space;//空白间隔

    public SpacesItemDecoration2(int space){
        this.space = space;
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
        final int itemPosition = parent.getChildAdapterPosition(view);
        if (itemPosition == 0) {
            return; }
        outRect.set(space, 0, 0, 0);
    }
}
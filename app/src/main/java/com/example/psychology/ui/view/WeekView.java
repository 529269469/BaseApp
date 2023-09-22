package com.example.psychology.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.psychology.R;

public class WeekView  extends View {
    private Paint mPaint;
    private RectF mRectF;
    private int mProgressBarColor;
    private int mProgressBarHeight;
    private int mMaxValue;
    private int mCurrentValue;


    public WeekView(Context context) {
        this(context, null);
    }

    public WeekView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mProgressBarColor = Color.BLUE;
        mProgressBarHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        mMaxValue = 100;
        mCurrentValue = 0;

        mPaint = new Paint();

        mRectF = new RectF();



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(mProgressBarColor);
        mRectF.set(0, 0, getWidth() * mCurrentValue / mMaxValue, mProgressBarHeight);
        canvas.drawRect(mRectF, mPaint);
    }

    public void setMaxValue(int maxValue) {
        this.mMaxValue = maxValue;
    }

    public void setCurrentValue(int currentValue) {
        this.mCurrentValue = currentValue;
        invalidate();
    }
}

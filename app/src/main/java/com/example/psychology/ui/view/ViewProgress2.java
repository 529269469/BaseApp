package com.example.psychology.ui.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.example.psychology.R;


public class ViewProgress2 extends View {
    private int mCurrent;//当前进度

    private Paint mBgPaint;//背景弧线paint

    private Paint mProgressPaint;//进度Paint

    private float mProgressWidth;//进度条宽度

//    private int mProgressColor = Color.rgb(168, 229, 233);//进度条颜色

    private int locationStart;//起始位置

    private float startAngle;//开始角度

    private ValueAnimator mAnimator;

    public ViewProgress2(Context context) {
        this(context, null);

    }

    public ViewProgress2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public ViewProgress2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);

        locationStart = typedArray.getInt(R.styleable.CircleProgressBar_location_start, 1);

        mProgressWidth = typedArray.getDimension(R.styleable.CircleProgressBar_progress_width, dp2px(context, 12));

//        mProgressColor = typedArray.getColor(R.styleable.CircleProgressBar_progress_color, mProgressColor);

        typedArray.recycle();


//背景圆弧

        mBgPaint = new Paint();

        mBgPaint.setAntiAlias(true);

        mBgPaint.setStrokeWidth(mProgressWidth);

        mBgPaint.setStyle(Paint.Style.STROKE);

        mBgPaint.setColor(Color.parseColor("#F1F4F8"));

        mBgPaint.setStrokeCap(Paint.Cap.ROUND);

//进度圆弧

        mProgressPaint = new Paint();

        mProgressPaint.setAntiAlias(true);

        mProgressPaint.setStyle(Paint.Style.STROKE);

        mProgressPaint.setStrokeWidth(mProgressWidth);

        int[] colors = {0xff97B4FE, 0xffE1E7FF};
        //circleWidth 圆的直径 取中心点
        SweepGradient sweepGradient = new SweepGradient(this.width / 2, this.width / 2, colors, null);
        //旋转 不然是从0度开始渐变
        Matrix matrix = new Matrix();
        matrix.setRotate(-90, this.width / 2, this.width / 2);
        sweepGradient.setLocalMatrix(matrix);

        mProgressPaint.setShader(sweepGradient);

        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);

        //进度条起始角度
        startAngle = -90;


    }

    int width;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);

        int size = width < height ? width : height;

        setMeasuredDimension(size, size);

    }

    /**
     * oval // 绘制范围
     * <p>
     * startAngle // 开始角度
     * <p>
     * sweepAngle // 扫过角度
     * <p>
     * useCenter // 是否使用中心
     */

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景圆弧
        RectF rectF = new RectF(mProgressWidth / 2, mProgressWidth / 2, getWidth() - mProgressWidth / 2, getHeight() - mProgressWidth / 2);
        canvas.drawArc(rectF, 0, 360, false, mBgPaint);

        //绘制当前进度
        float sweepAngle = 360 * mCurrent / 100;
        canvas.drawArc(rectF, startAngle, sweepAngle, false, mProgressPaint);
    }


    public int getCurrent() {
        return mCurrent;
    }

    /**
     * 设置进度
     *
     * @param current
     */

    public void setCurrent(int current) {
        mCurrent = current;
        invalidate();
    }


    private int tCurrent = -1;

    /**
     * 动画效果
     *
     * @param current  精度条进度：0-100
     * @param duration 动画时间
     */

    public void startAnimProgress(int current, int duration) {
        mAnimator = ValueAnimator.ofInt(0, current);

        mAnimator.setDuration(duration);

        mAnimator.setInterpolator(new LinearInterpolator());

        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override

            public void onAnimationUpdate(ValueAnimator animation) {
                int current = (int) animation.getAnimatedValue();

                if (tCurrent != current) {
                    tCurrent = current;

                    setCurrent(current);

                    if (mOnAnimProgressListener != null)

                        mOnAnimProgressListener.valueUpdate(current);

                }

            }

        });

        mAnimator.start();

    }

    public interface OnAnimProgressListener {
        void valueUpdate(int progress);

    }

    private OnAnimProgressListener mOnAnimProgressListener;

    /**
     * 监听进度条进度
     *
     * @param onAnimProgressListener
     */

    public void setOnAnimProgressListener(OnAnimProgressListener onAnimProgressListener) {
        mOnAnimProgressListener = onAnimProgressListener;

    }

    public void destroy() {
        if (mAnimator != null) {
            mAnimator.cancel();

        }

    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);

    }

}





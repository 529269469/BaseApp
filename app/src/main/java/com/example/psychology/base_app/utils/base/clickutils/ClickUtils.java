package com.example.psychology.base_app.utils.base.clickutils;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.util.StateSet;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author      : silveryTitan
 * Email       : silverytitan94@gmail.com
 * Date        : on 2021-08-27 14:54.
 * Description : 点击时间工具类
 */
public class ClickUtils {
    private static final int PRESSED_VIEW_SCALE_TAG = -1;
    private static final float PRESSED_VIEW_SCALE_DEFAULT_VALUE = -0.06f;

    private static final int PRESSED_VIEW_ALPHA_TAG = -2;
    private static final int PRESSED_VIEW_ALPHA_SRC_TAG = -3;
    private static final float PRESSED_VIEW_ALPHA_DEFAULT_VALUE = 0.8f;

    private static final int PRESSED_BG_ALPHA_STYLE = 4;
    private static final float PRESSED_BG_ALPHA_DEFAULT_VALUE = 0.9f;

    private static final int PRESSED_BG_DARK_STYLE = 5;
    private static final float PRESSED_BG_DARK_DEFAULT_VALUE = 0.9f;

    private static final long DEBOUNCING_DEFAULT_VALUE = 500;

    private static final int CACHE_SIZE = 64;
    private static final Map<String, Long> KEY_MILLIS_MAP = new ConcurrentHashMap<>(CACHE_SIZE);
    private static final long TIP_DURATION = 2000L;
    private static long sLastClickMillis;
    private static int sClickCount;

    private ClickUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Apply scale animation for the views' click.
     *
     * @param views The views.
     */
    public static void applyPressedViewScale(final View... views) {
        applyPressedViewScale(views, null);
    }

    /**
     * Apply scale animation for the views' click.
     *
     * @param views        The views.
     * @param scaleFactors The factors of scale for the views.
     */
    public static void applyPressedViewScale(final View[] views, final float[] scaleFactors) {
        if (views == null || views.length == 0) {
            return;
        }
        for (int i = 0; i < views.length; i++) {
            if (scaleFactors == null || i >= scaleFactors.length) {
                applyPressedViewScale(views[i], PRESSED_VIEW_SCALE_DEFAULT_VALUE);
            } else {
                applyPressedViewScale(views[i], scaleFactors[i]);
            }
        }
    }

    /**
     * Apply scale animation for the views' click.
     *
     * @param view        The view.
     * @param scaleFactor The factor of scale for the view.
     */
    public static void applyPressedViewScale(final View view, final float scaleFactor) {
        if (view == null) {
            return;
        }
        view.setTag(PRESSED_VIEW_SCALE_TAG, scaleFactor);
        view.setClickable(true);
        view.setOnTouchListener(OnUtilsTouchListener.getInstance());
    }

    /**
     * Apply alpha for the views' click.
     *
     * @param views The views.
     */
    public static void applyPressedViewAlpha(final View... views) {
        applyPressedViewAlpha(views, null);
    }

    /**
     * Apply alpha for the views' click.
     *
     * @param views  The views.
     * @param alphas The alphas for the views.
     */
    public static void applyPressedViewAlpha(final View[] views, final float[] alphas) {
        if (views == null || views.length == 0) return;
        for (int i = 0; i < views.length; i++) {
            if (alphas == null || i >= alphas.length) {
                applyPressedViewAlpha(views[i], PRESSED_VIEW_ALPHA_DEFAULT_VALUE);
            } else {
                applyPressedViewAlpha(views[i], alphas[i]);
            }
        }
    }

    /**
     * Apply scale animation for the views' click.
     *
     * @param view  The view.
     * @param alpha The alpha for the view.
     */
    public static void applyPressedViewAlpha(final View view, final float alpha) {
        if (view == null) {
            return;
        }
        view.setTag(PRESSED_VIEW_ALPHA_TAG, alpha);
        view.setTag(PRESSED_VIEW_ALPHA_SRC_TAG, view.getAlpha());
        view.setClickable(true);
        view.setOnTouchListener(OnUtilsTouchListener.getInstance());
    }

    /**
     * Apply alpha for the view's background.
     *
     * @param view The views.
     */
    public static void applyPressedBgAlpha(View view) {
        applyPressedBgAlpha(view, PRESSED_BG_ALPHA_DEFAULT_VALUE);
    }

    /**
     * Apply alpha for the view's background.
     *
     * @param view  The views.
     * @param alpha The alpha.
     */
    public static void applyPressedBgAlpha(View view, float alpha) {
        applyPressedBgStyle(view, PRESSED_BG_ALPHA_STYLE, alpha);
    }

    /**
     * Apply alpha of dark for the view's background.
     *
     * @param view The views.
     */
    public static void applyPressedBgDark(View view) {
        applyPressedBgDark(view, PRESSED_BG_DARK_DEFAULT_VALUE);
    }

    /**
     * Apply alpha of dark for the view's background.
     *
     * @param view      The views.
     * @param darkAlpha The alpha of dark.
     */
    public static void applyPressedBgDark(View view, float darkAlpha) {
        applyPressedBgStyle(view, PRESSED_BG_DARK_STYLE, darkAlpha);
    }

    private static void applyPressedBgStyle(View view, int style, float value) {
        if (view == null) return;
        Drawable background = view.getBackground();
        Object tag = view.getTag(-style);
        if (tag instanceof Drawable) {
            ViewCompat.setBackground(view, (Drawable) tag);
        } else {
            background = createStyleDrawable(background, style, value);
            ViewCompat.setBackground(view, background);
            view.setTag(-style, background);
        }
    }

    private static Drawable createStyleDrawable(Drawable src, int style, float value) {
        if (src == null) {
            src = new ColorDrawable(0);
        }
        if (src.getConstantState() == null) return src;

        Drawable pressed = src.getConstantState().newDrawable().mutate();
        if (style == PRESSED_BG_ALPHA_STYLE) {
            pressed = createAlphaDrawable(pressed, value);
        } else if (style == PRESSED_BG_DARK_STYLE) {
            pressed = createDarkDrawable(pressed, value);
        }

        Drawable disable = src.getConstantState().newDrawable().mutate();
        disable = createAlphaDrawable(disable, 0.5f);

        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, pressed);
        drawable.addState(new int[]{-android.R.attr.state_enabled}, disable);
        drawable.addState(StateSet.WILD_CARD, src);
        return drawable;
    }

    private static Drawable createAlphaDrawable(Drawable drawable, float alpha) {
        ClickDrawableWrapper drawableWrapper = new ClickDrawableWrapper(drawable);
        drawableWrapper.setAlpha((int) (alpha * 255));
        return drawableWrapper;
    }

    private static Drawable createDarkDrawable(Drawable drawable, float alpha) {
        ClickDrawableWrapper drawableWrapper = new ClickDrawableWrapper(drawable);
        drawableWrapper.setColorFilter(getDarkColorFilter(alpha));
        return drawableWrapper;
    }

    private static ColorMatrixColorFilter getDarkColorFilter(float darkAlpha) {
        return new ColorMatrixColorFilter(new ColorMatrix(new float[]{
                darkAlpha, 0, 0, 0, 0,
                0, darkAlpha, 0, 0, 0,
                0, 0, darkAlpha, 0, 0,
                0, 0, 0, 2, 0
        }));
    }

    /**
     * Apply single debouncing for the view's click.
     *
     * @param view     The view.
     * @param listener The listener.
     */
    public static void applySingleDebouncing(final View view, final View.OnClickListener listener) {
        applySingleDebouncing(new View[]{view}, listener);
    }

    /**
     * Apply single debouncing for the view's click.
     *
     * @param view     The view.
     * @param duration The duration of debouncing.
     * @param listener The listener.
     */
    public static void applySingleDebouncing(final View view, @IntRange(from = 0) long duration,
                                             final View.OnClickListener listener) {
        applySingleDebouncing(new View[]{view}, duration, listener);
    }

    /**
     * Apply single debouncing for the views' click.
     *
     * @param views    The views.
     * @param listener The listener.
     */
    public static void applySingleDebouncing(final View[] views, final View.OnClickListener listener) {
        applySingleDebouncing(views, DEBOUNCING_DEFAULT_VALUE, listener);
    }

    /**
     * Apply single debouncing for the views' click.
     *
     * @param views    The views.
     * @param duration The duration of debouncing.
     * @param listener The listener.
     */
    public static void applySingleDebouncing(final View[] views,
                                             @IntRange(from = 0) long duration,
                                             final View.OnClickListener listener) {
        applyDebouncing(views, false, duration, listener);
    }

    /**
     * Apply global debouncing for the view's click.
     *
     * @param view     The view.
     * @param listener The listener.
     */
    public static void applyGlobalDebouncing(final View view, final View.OnClickListener listener) {
        applyGlobalDebouncing(new View[]{view}, listener);
    }

    /**
     * Apply global debouncing for the view's click.
     *
     * @param view     The view.
     * @param duration The duration of debouncing.
     * @param listener The listener.
     */
    public static void applyGlobalDebouncing(final View view, @IntRange(from = 0) long duration,
                                             final View.OnClickListener listener) {
        applyGlobalDebouncing(new View[]{view}, duration, listener);
    }

    /**
     * Apply global debouncing for the views' click.
     *
     * @param views    The views.
     * @param listener The listener.
     */
    public static void applyGlobalDebouncing(final View[] views, final View.OnClickListener listener) {
        applyGlobalDebouncing(views, DEBOUNCING_DEFAULT_VALUE, listener);
    }

    /**
     * Apply global debouncing for the views' click.
     *
     * @param views    The views.
     * @param duration The duration of debouncing.
     * @param listener The listener.
     */
    public static void applyGlobalDebouncing(final View[] views,
                                             @IntRange(from = 0) long duration,
                                             final View.OnClickListener listener) {
        applyDebouncing(views, true, duration, listener);
    }

    private static void applyDebouncing(final View[] views,
                                        final boolean isGlobal,
                                        @IntRange(from = 0) long duration,
                                        final View.OnClickListener listener) {
        if (views == null || views.length == 0 || listener == null) return;
        for (View view : views) {
            if (view == null) continue;
            view.setOnClickListener(new OnDebouncingClickListener(isGlobal, duration) {
                @Override
                public void onDebouncingClick(View v) {
                    listener.onClick(v);
                }
            });
        }
    }

    /**
     * Expand the click area of ​​the view
     *
     * @param view       The view.
     * @param expandSize The size.
     */
    public static void expandClickArea(@NonNull final View view, final int expandSize) {
        expandClickArea(view, expandSize, expandSize, expandSize, expandSize);
    }

    public static void expandClickArea(@NonNull final View view,
                                       final int expandSizeTop,
                                       final int expandSizeLeft,
                                       final int expandSizeRight,
                                       final int expandSizeBottom) {
        final View parentView = (View) view.getParent();
        if (parentView == null) {
            Log.e("ClickUtils", "expandClickArea must have parent view.");
            return;
        }
        parentView.post(new Runnable() {
            @Override
            public void run() {
                final Rect rect = new Rect();
                view.getHitRect(rect);
                rect.top -= expandSizeTop;
                rect.bottom += expandSizeBottom;
                rect.left -= expandSizeLeft;
                rect.right += expandSizeRight;
                parentView.setTouchDelegate(new TouchDelegate(rect, view));
            }
        });
    }

    public static boolean isValid(@NonNull String key, final long duration) {
        if (TextUtils.isEmpty(key)) {
            throw new IllegalArgumentException("The key is null.");
        }
        if (duration < 0) {
            throw new IllegalArgumentException("The duration is less than 0.");
        }
        long curTime = SystemClock.elapsedRealtime();
        clearIfNecessary(curTime);
        Long validTime = KEY_MILLIS_MAP.get(key);
        if (validTime == null || curTime >= validTime) {
            KEY_MILLIS_MAP.put(key, curTime + duration);
            return true;
        }
        return false;
    }

    private static void clearIfNecessary(long curTime) {
        if (KEY_MILLIS_MAP.size() < CACHE_SIZE) return;
        for (Iterator<Map.Entry<String, Long>> it = KEY_MILLIS_MAP.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Long> entry = it.next();
            Long validTime = entry.getValue();
            if (curTime >= validTime) {
                it.remove();
            }
        }
    }

    public static abstract class OnDebouncingClickListener implements View.OnClickListener {

        private static boolean mEnabled = true;

        private static final Runnable ENABLE_AGAIN = new Runnable() {
            @Override
            public void run() {
                mEnabled = true;
            }
        };

        private long mDuration;
        private boolean mIsGlobal;

        public OnDebouncingClickListener() {
            this(true, DEBOUNCING_DEFAULT_VALUE);
        }

        public OnDebouncingClickListener(final boolean isGlobal) {
            this(isGlobal, DEBOUNCING_DEFAULT_VALUE);
        }

        public OnDebouncingClickListener(final long duration) {
            this(true, duration);
        }

        public OnDebouncingClickListener(final boolean isGlobal, final long duration) {
            mIsGlobal = isGlobal;
            mDuration = duration;
        }

        public abstract void onDebouncingClick(View v);

        @Override
        public final void onClick(View v) {
            if (mIsGlobal) {
                if (mEnabled) {
                    mEnabled = false;
                    v.postDelayed(ENABLE_AGAIN, mDuration);
                    onDebouncingClick(v);
                }
            } else {
                if (isValid(String.valueOf(v.hashCode()), mDuration)) {
                    onDebouncingClick(v);
                }
            }
        }
    }

    public static abstract class OnMultiClickListener implements View.OnClickListener {

        private static final long INTERVAL_DEFAULT_VALUE = 666;

        private final int mTriggerClickCount;
        private final long mClickInterval;

        private long mLastClickTime;
        private int mClickCount;

        public OnMultiClickListener(int triggerClickCount) {
            this(triggerClickCount, INTERVAL_DEFAULT_VALUE);
        }

        public OnMultiClickListener(int triggerClickCount, long clickInterval) {
            this.mTriggerClickCount = triggerClickCount;
            this.mClickInterval = clickInterval;
        }

        public abstract void onTriggerClick(View v);

        public abstract void onBeforeTriggerClick(View v, int count);

        @Override
        public void onClick(View v) {
            if (mTriggerClickCount <= 1) {
                onTriggerClick(v);
                return;
            }
            long curTime = System.currentTimeMillis();

            if (curTime - mLastClickTime < mClickInterval) {
                mClickCount++;
                if (mClickCount == mTriggerClickCount) {
                    onTriggerClick(v);
                } else if (mClickCount < mTriggerClickCount) {
                    onBeforeTriggerClick(v, mClickCount);
                } else {
                    mClickCount = 1;
                    onBeforeTriggerClick(v, mClickCount);
                }
            } else {
                mClickCount = 1;
                onBeforeTriggerClick(v, mClickCount);
            }
            mLastClickTime = curTime;
        }
    }

    private static class OnUtilsTouchListener implements View.OnTouchListener {

        private OnUtilsTouchListener() {/**/}

        public static OnUtilsTouchListener getInstance() {
            return LazyHolder.INSTANCE;
        }

        @Override
        public boolean onTouch(final View v, MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                processScale(v, true);
                processAlpha(v, true);
            } else if (action == MotionEvent.ACTION_UP
                    || action == MotionEvent.ACTION_CANCEL) {
                processScale(v, false);
                processAlpha(v, false);
            }
            return false;
        }

        private void processScale(final View view, boolean isDown) {
            Object tag = view.getTag(PRESSED_VIEW_SCALE_TAG);
            if (!(tag instanceof Float)) return;
            float value = isDown ? 1 + (Float) tag : 1;
            view.animate()
                    .scaleX(value)
                    .scaleY(value)
                    .setDuration(200)
                    .start();
        }

        private void processAlpha(final View view, boolean isDown) {
            Object tag = view.getTag(isDown ? PRESSED_VIEW_ALPHA_TAG : PRESSED_VIEW_ALPHA_SRC_TAG);
            if (!(tag instanceof Float)) return;
            view.setAlpha((Float) tag);
        }

        private static class LazyHolder {
            private static final OnUtilsTouchListener INSTANCE = new OnUtilsTouchListener();
        }
    }

    static class ClickDrawableWrapper extends DrawableWrapper {

        private BitmapDrawable mBitmapDrawable = null;

        // 低版本ColorDrawable.setColorFilter无效，这里直接用画笔画上
        private Paint mColorPaint = null;

        public ClickDrawableWrapper(Drawable drawable) {
            super(drawable);
            if (drawable instanceof ColorDrawable) {
                mColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
                mColorPaint.setColor(((ColorDrawable) drawable).getColor());
            }
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            super.setColorFilter(cf);
            // 低版本 StateListDrawable.selectDrawable 会重置 ColorFilter
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                if (mColorPaint != null) {
                    mColorPaint.setColorFilter(cf);
                }
            }
        }

        @Override
        public void setAlpha(int alpha) {
            super.setAlpha(alpha);
            // 低版本 StateListDrawable.selectDrawable 会重置 Alpha
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                if (mColorPaint != null) {
                    mColorPaint.setColor(((ColorDrawable) getWrappedDrawable()).getColor());
                }
            }
        }

        @Override
        public void draw(Canvas canvas) {
            if (mBitmapDrawable == null) {
                Bitmap bitmap = Bitmap.createBitmap(getBounds().width(), getBounds().height(), Bitmap.Config.ARGB_8888);
                Canvas myCanvas = new Canvas(bitmap);
                if (mColorPaint != null) {
                    myCanvas.drawRect(getBounds(), mColorPaint);
                } else {
                    super.draw(myCanvas);
                }
                mBitmapDrawable = new BitmapDrawable(Resources.getSystem(), bitmap);
                mBitmapDrawable.setBounds(getBounds());
            }
            mBitmapDrawable.draw(canvas);
        }
    }

    static class DrawableWrapper extends Drawable implements Drawable.Callback {
        private Drawable mDrawable;

        public DrawableWrapper(Drawable drawable) {
            this.setWrappedDrawable(drawable);
        }

        public void draw(Canvas canvas) {
            this.mDrawable.draw(canvas);
        }

        protected void onBoundsChange(Rect bounds) {
            this.mDrawable.setBounds(bounds);
        }

        public int getChangingConfigurations() {
            return this.mDrawable.getChangingConfigurations();
        }

        public void setChangingConfigurations(int configs) {
            this.mDrawable.setChangingConfigurations(configs);
        }

        public void setDither(boolean dither) {
            this.mDrawable.setDither(dither);
        }

        public void setFilterBitmap(boolean filter) {
            this.mDrawable.setFilterBitmap(filter);
        }

        public void setAlpha(int alpha) {
            this.mDrawable.setAlpha(alpha);
        }

        public void setColorFilter(ColorFilter cf) {
            this.mDrawable.setColorFilter(cf);
        }

        public boolean isStateful() {
            return this.mDrawable.isStateful();
        }

        public boolean setState(int[] stateSet) {
            return this.mDrawable.setState(stateSet);
        }

        public int[] getState() {
            return this.mDrawable.getState();
        }

        public void jumpToCurrentState() {
            DrawableCompat.jumpToCurrentState(this.mDrawable);
        }

        public Drawable getCurrent() {
            return this.mDrawable.getCurrent();
        }

        public boolean setVisible(boolean visible, boolean restart) {
            return super.setVisible(visible, restart) || this.mDrawable.setVisible(visible, restart);
        }

        public int getOpacity() {
            return this.mDrawable.getOpacity();
        }

        public Region getTransparentRegion() {
            return this.mDrawable.getTransparentRegion();
        }

        public int getIntrinsicWidth() {
            return this.mDrawable.getIntrinsicWidth();
        }

        public int getIntrinsicHeight() {
            return this.mDrawable.getIntrinsicHeight();
        }

        public int getMinimumWidth() {
            return this.mDrawable.getMinimumWidth();
        }

        public int getMinimumHeight() {
            return this.mDrawable.getMinimumHeight();
        }

        public boolean getPadding(Rect padding) {
            return this.mDrawable.getPadding(padding);
        }

        public void invalidateDrawable(Drawable who) {
            this.invalidateSelf();
        }

        public void scheduleDrawable(Drawable who, Runnable what, long when) {
            this.scheduleSelf(what, when);
        }

        public void unscheduleDrawable(Drawable who, Runnable what) {
            this.unscheduleSelf(what);
        }

        protected boolean onLevelChange(int level) {
            return this.mDrawable.setLevel(level);
        }

        public boolean isAutoMirrored() {
            return DrawableCompat.isAutoMirrored(this.mDrawable);
        }

        public void setAutoMirrored(boolean mirrored) {
            DrawableCompat.setAutoMirrored(this.mDrawable, mirrored);
        }

        public void setTint(int tint) {
            DrawableCompat.setTint(this.mDrawable, tint);
        }

        public void setTintList(ColorStateList tint) {
            DrawableCompat.setTintList(this.mDrawable, tint);
        }

        public void setTintMode(PorterDuff.Mode tintMode) {
            DrawableCompat.setTintMode(this.mDrawable, tintMode);
        }

        public void setHotspot(float x, float y) {
            DrawableCompat.setHotspot(this.mDrawable, x, y);
        }

        public void setHotspotBounds(int left, int top, int right, int bottom) {
            DrawableCompat.setHotspotBounds(this.mDrawable, left, top, right, bottom);
        }

        public Drawable getWrappedDrawable() {
            return this.mDrawable;
        }

        public void setWrappedDrawable(Drawable drawable) {
            if (this.mDrawable != null) {
                this.mDrawable.setCallback((Callback) null);
            }

            this.mDrawable = drawable;
            if (drawable != null) {
                drawable.setCallback(this);
            }
        }
    }
}
package cn.vcamera.animationtest;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by fandong on 2015/4/1.
 */
public class FloatButton extends ImageView {
    private static final int ANIM_DURATION = 500;
    private Context context;
    private int resId;
    private int x;
    private int y;
    private int r;
    private float degree;
    private int size;

    private int dx;
    private int dy;
    private boolean isRunning;

    FrameLayout.LayoutParams params;
    ObjectAnimator openAnimator;
    ObjectAnimator closeAnimator;

    public FloatButton(Context context) {
        super(context);
    }

    public FloatButton(Context context, int resId, int x, int y, float degree, int r, int size) {
        super(context);
        this.context = context;
        this.resId = resId;
        this.x = x;
        this.y = y;
        this.degree = degree;
        this.r = r;
        this.size = size;
        initView();
    }

    private void initView() {
        setImageResource(resId);
        params = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        int sWidth = context.getResources().getDisplayMetrics().widthPixels;
        int sHeight = context.getResources().getDisplayMetrics().heightPixels;
        params.bottomMargin = Math.max(sWidth, sHeight) - y - size / 2;
        params.rightMargin = Math.min(sWidth, sHeight) - x - size / 2;
        caculate();
    }

    public void open(int betweenTimes) {
        if (openAnimator != null) {
            if (openAnimator.isStarted())
                return;
        } else {
            openAnimator = getObjectAnimator(true, betweenTimes);
        }
        isRunning = true;
        openAnimator.start();
    }

    public void close(int betweenTimes) {
        if (closeAnimator != null) {
            if (closeAnimator.isStarted())
                return;
        } else {
            closeAnimator = getObjectAnimator(false, betweenTimes);
        }
        isRunning = true;
        closeAnimator.start();
    }

    private ObjectAnimator getObjectAnimator(final boolean isOpen, int betweenTimes) {
        float _firstp = isOpen ? 0.f : 1.f;
        float _secondp = isOpen ? 1.f : 0.f;
        float _firstd = isOpen ? -180.f : 0.f;
        float _secondd = isOpen ? 0.f : -180.f;
        float _dx = isOpen ? -dx : dx;
        float _dy = isOpen ? -dy : dy;
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, _firstp, _secondp);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, _firstp, _secondp);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, _firstp, _secondp);
        PropertyValuesHolder rotation = PropertyValuesHolder.ofFloat(View.ROTATION, _firstd, -_secondd);
        PropertyValuesHolder translateX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, _dx);
        PropertyValuesHolder translateY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, _dy);
        ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(this, alpha, scaleX, scaleY, rotation, translateX, translateY);
        animation.setDuration(ANIM_DURATION);
        animation.addListener(new AnimationAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                if (isOpen && getParent() == null) {
                    ViewGroup parent = (ViewGroup) getRootView(context);
                    parent.addView(FloatButton.this, params);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isOpen && getParent() != null) {
                    ViewGroup parent = (ViewGroup) getParent();
                    parent.removeView(FloatButton.this);
                }
                isRunning = false;
            }
        });
        animation.setStartDelay(betweenTimes);
        animation.setInterpolator(isOpen ? new OvershootInterpolator() : new AnticipateOvershootInterpolator());
        return animation;
    }

    private void caculate() {
        dx = (int) (r * Math.cos(degree * Math.PI / 180));
        dy = (int) (r * Math.sin(degree * Math.PI / 180));
    }

    private View getRootView(Context context) {
        try {
            return ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        } catch (ClassCastException e) {
            throw new ClassCastException("context must instance of Activity!");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(size, size);
    }

    public static class Builder {
        private Context context;
        private int resId;
        private int x;
        private int y;
        private int r;
        private float degree;
        private int size;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setResId(int resId) {
            this.resId = resId;
            return this;
        }

        public Builder setX(int x) {
            this.x = x;
            return this;
        }

        public Builder setY(int y) {
            this.y = y;
            return this;
        }

        public Builder setR(int r) {
            this.r = r;
            return this;
        }

        public Builder setSize(int size) {
            this.size = size;
            return this;
        }

        public Builder setDegree(int degree) {
            this.degree = degree;
            return this;
        }

        public FloatButton build() {
            return new FloatButton(context, resId, x, y, degree, r, size);
        }
    }
}

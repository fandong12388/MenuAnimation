package cn.vcamera.animationtest;

import android.animation.Animator;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by fandong on 2015/4/1.
 */
public class FloatMenu {
    private Context context;
    private int radius;
    private int[] resources;
    private View target;
    private OnItemClickListener onItemClickListener;
    private FloatButton[] buttons;
    private boolean isOpen;
    private static final int LAG_BETWEEN_TIMES = 20;
    private boolean isRunning;

    private Animator.AnimatorListener lastAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            isRunning = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            isRunning = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            isRunning = false;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            isRunning = true;
        }
    };

    public FloatMenu(Context context, int radius, int[] resources, View target, //
                     OnItemClickListener onItemClickListener) {
        this.context = context;
        this.radius = radius;
        this.resources = resources;
        this.target = target;
        this.onItemClickListener = onItemClickListener;
        initView();
    }

    public void initView() {
        int length = resources.length;
        buttons = new FloatButton[resources.length];
        int centerX = getCenterX();
        int centerY = getCenterY();
        for (int i = 0; i < length; i++) {
            buttons[i] = new FloatButton.Builder(context)
                    .setDegree(90 / (length - 1) * i)
                    .setR(radius)
                    .setX(centerX)
                    .setY(centerY)
                    .setSize(target.getWidth() * 2 / 3)
                    .setResId(resources[i])
                    .build();
            if (onItemClickListener != null) {
                setOnItemClickListener(buttons[i], i);
            }
        }
    }

    private void setOnItemClickListener(View view, final int index) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(index, buttons[index]);
            }
        });
    }

    private int getCenterX() {
        int sWidth = context.getResources().getDisplayMetrics().widthPixels;
        int sHeight = context.getResources().getDisplayMetrics().heightPixels;
        int width = target.getWidth();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) target.getLayoutParams();
        return Math.min(sWidth, sHeight) - width / 2 - params.rightMargin;
    }

    private int getCenterY() {
        int sWidth = context.getResources().getDisplayMetrics().widthPixels;
        int sHeight = context.getResources().getDisplayMetrics().heightPixels;
        int height = target.getHeight();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) target.getLayoutParams();
        return Math.max(sWidth, sHeight) - height / 2 - params.bottomMargin;
    }

    public void toggle() {
        if (isRunning)
            return;
        int i = 0;
        Animator animator;
        if (isOpen) {
            for (; i < buttons.length - 1; i++) {
                buttons[i].close(i * LAG_BETWEEN_TIMES);
            }
            animator = buttons[i].close(i * LAG_BETWEEN_TIMES);
        } else {
            for (; i < buttons.length - 1; i++) {
                buttons[i].open(i * LAG_BETWEEN_TIMES);
            }
            animator = buttons[i].open(i * LAG_BETWEEN_TIMES);
        }
        if (!animator.getListeners().contains(lastAnimatorListener)) {
            animator.addListener(lastAnimatorListener);
        }
        isOpen = !isOpen;
    }

    public static class Builder {
        private Context context;
        private int radius;
        private int[] resources;
        private View target;
        private OnItemClickListener onItemClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setRadius(int radius) {
            this.radius = radius;
            return this;
        }

        public Builder setImageResource(int... params) {
            resources = params;
            return this;
        }

        public Builder attachTo(View target) {
            this.target = target;
            return this;
        }

        public Builder setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
            return this;
        }

        public FloatMenu build() {
            return new FloatMenu(context, radius, resources, target, onItemClickListener);
        }
    }

    public static interface OnItemClickListener {
        void onItemClick(int position, View view);
    }
}

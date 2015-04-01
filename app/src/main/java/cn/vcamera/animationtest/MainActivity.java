package cn.vcamera.animationtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnClickListener, FloatMenu.OnItemClickListener {
    private ImageView mButton;
    private FloatMenu floatMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (ImageView) findViewById(R.id.iv);
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (floatMenu == null) {
            floatMenu = new FloatMenu.Builder(this)
                    .setImageResource(R.drawable.adjust_brightness_press,
                            R.drawable.adjust_contrast_press,
                            R.drawable.adjust_hue_press,
                            R.drawable.adjust_saturation_press,
                            R.drawable.adjust_sharpness_press)
                    .setRadius(getRadius())
                    .setOnItemClickListener(this)
                    .attachTo(mButton)
                    .build();
        }
        floatMenu.toggle();
    }

    private int getRadius() {
        int sWidth = getResources().getDisplayMetrics().widthPixels;
        int sHeight = getResources().getDisplayMetrics().heightPixels;
        return Math.min(sWidth, sHeight) / 2 -
                (mButton.getWidth() / 2 +
                        ((RelativeLayout.LayoutParams) mButton.getLayoutParams()).rightMargin);
    }

    @Override
    public void onItemClick(int position, View view) {
        Toast.makeText(this, "Click Position ：" + position, Toast.LENGTH_SHORT).show();
    }
}

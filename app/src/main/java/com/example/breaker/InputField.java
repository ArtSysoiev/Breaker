package com.example.breaker;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.TableRow;

import androidx.core.content.ContextCompat;

public class InputField extends EditText {
    public InputField(Context context, int size, int margin) {
        super(context, null, 0, R.style.input);

        TableRow.LayoutParams params = new TableRow.LayoutParams(size, size);
        params.setMargins(margin, margin, margin, margin);
        this.setLayoutParams(params);

        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, size / 1.8f);;
    }

    public void disable(int newColor) {
        this.setEnabled(false);

        // if no such number in correct row
        if (newColor == -1) {
            Utils.animateAlpha(this, 0.7f);
            return;
        }

        Utils.animateAlpha(this, 0.9f);
//        this.setAlpha(0.9f);

        // getting current bg color from attrs/input_bg
        TypedValue v = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.input_bg, v, true);
        int oldColor = ContextCompat.getColor(getContext(), v.resourceId);

        // animating color change
        ValueAnimator colorAnimator = ValueAnimator.ofArgb(
                oldColor,
                ContextCompat.getColor(getContext(), newColor)
        );

        colorAnimator.addUpdateListener(animator -> {
            int animatedColor = (int) animator.getAnimatedValue();
            this.getBackground().setColorFilter(new PorterDuffColorFilter(animatedColor, PorterDuff.Mode.SRC_OVER));
        });

        colorAnimator.setDuration(Config.animationDuration);
        colorAnimator.start();
    }

}

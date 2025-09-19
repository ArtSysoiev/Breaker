package com.example.breaker;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams;
import androidx.transition.TransitionManager;

import com.google.android.material.button.MaterialButton;


public class StartButton extends MaterialButton {
    public View.OnClickListener tryCallback;


    public StartButton(Context context) {
        super(context);
    }

    public StartButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StartButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void disable(boolean showNotFilledError) {
        Utils.animateAlpha(this, 0.5f);
        this.setOnClickListener(v -> {
            if (showNotFilledError) {
                Utils.showToast(getContext(), R.string.error_not_filled_text);
            }
        });
    }

    public void enable() {
        Utils.animateAlpha(this, 1f);
        this.setOnClickListener(tryCallback);
    }

    public void enable(View.OnClickListener l) {
        Utils.animateAlpha(this, 1f);
        this.setOnClickListener(l);
    }

    public void move() {
        // 1st way - no animation, just change layout params
//        LayoutParams params = (LayoutParams) startBtn.getLayoutParams();
//        params.bottomToBottom = LayoutParams.PARENT_ID;
//        params.topToTop = LayoutParams.UNSET;
//        params.bottomMargin = startBtnBottomMargin;
//        startBtn.setLayoutParams(params);

        // 2nd way - with animation, but no changes to layout
//        ObjectAnimator animation = ObjectAnimator.ofFloat(startBtn, "translationY", 100f);
//        animation.setDuration(2000);
//        animation.start();

        // 3rd way - with animayion, via ConstraintSet
//        ConstraintLayout layout = findViewById(R.id.main);
//        ConstraintSet constraintSet = new ConstraintSet();
//        constraintSet.clone(layout);
//
//        constraintSet.clear(R.id.start_btn, ConstraintSet.BOTTOM);
//        constraintSet.connect(R.id.start_btn, ConstraintSet.TOP, R.id.table, ConstraintSet.BOTTOM, 100);
//
//        TransitionManager.beginDelayedTransition(layout);
//        constraintSet.applyTo(layout);

        // 4th way - with animation and LayoutParams
        ConstraintLayout layout = (ConstraintLayout) this.getParent();
        TransitionManager.beginDelayedTransition(layout);

        LayoutParams params = (LayoutParams) this.getLayoutParams();
        params.bottomToBottom = LayoutParams.UNSET;
        params.topToTop = LayoutParams.UNSET;
        params.topToBottom = R.id.table;
        params.topMargin = Config.btnTopMargin;
        this.setLayoutParams(params);
    }

    public void moveToStart() {
        ConstraintLayout layout = (ConstraintLayout) this.getParent();
        TransitionManager.beginDelayedTransition(layout);

        LayoutParams params = (LayoutParams) this.getLayoutParams();
        params.topToBottom = LayoutParams.UNSET;
        params.topMargin = 0;
        params.topToTop = LayoutParams.PARENT_ID;
        params.bottomToBottom = LayoutParams.PARENT_ID;
        this.setLayoutParams(params);
    }
}

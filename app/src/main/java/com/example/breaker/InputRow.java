package com.example.breaker;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TableRow;

import java.util.Arrays;

public class InputRow extends TableRow {
    boolean isTextChanging = false, isDeleting = false;
    private StartButton startBtn;
    private Handler handler = new Handler(Looper.getMainLooper());

    public InputRow(Context context, StartButton startButton, int[] numbers) {
        super(context);
        startBtn = startButton;

        this.setGravity(Gravity.CENTER_HORIZONTAL);

        int screenWidth = getScreenWidth(context);
        int inputSize = getInputSize(screenWidth);
        int inputMargin = getInputMargin(inputSize);

        for (int i = 0; i < Config.inputCount; i++) {
            InputField input = new InputField(context, inputSize, inputMargin);
            if (numbers.length > 0) {
                input.setBackgroundResource(R.drawable.shape_input_answer);
                input.setText(String.valueOf(numbers[i]));
                input.setEnabled(false);
                Utils.animateAlpha(input, 0.6f);
            }
            this.addView(input);
        }

        if (numbers.length == 0) {
            for (View v : this.getTouchables()) {
                InputField input = (InputField) v;

                bindOnDeleteKeyListener(input);
                bindOnFocusChange(input);
                bindOnTextChange(input);
            }

            View firstInput = this.getChildAt(0);
            handler.post(() -> Utils.focusView(firstInput));
        }
    }
    

    public boolean checkGuess(int[] numbers) {
        int correctGuesses = 0;
        for (int i = 0; i < numbers.length; i++) {
            InputField input = (InputField) this.getChildAt(i);
            String inputNumber = input.getText().toString();
            String correctNumber = String.valueOf(numbers[i]);
            int newColor;
            if (inputNumber.equals(correctNumber)) {
                newColor = R.color.input_green;
                correctGuesses++;
            } else if (Arrays.stream(numbers).anyMatch(n -> String.valueOf(n).equals(inputNumber))) {
                newColor = R.color.input_yellow;
            } else {
                newColor = R.color.input_gray;
            }
            handler.postDelayed(() -> input.disable(newColor), Config.animationDuration * (i + 1));
        }
        return correctGuesses == numbers.length;
    }

    private void checkIsFilled() {
        if (isAllFilled()) {
            startBtn.enable();
        } else {
            startBtn.disable(true);
        }
    }

    private int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    private int getInputSize(int screenWidth) {
        /*
         size of input field (inputCount = 5, inputMarginCoef = 0.25)
         _[ ]_[ ]_[ ]_[ ]_[ ]_    - 6x _, 5x [ ]
          [ ] - inputSize
          _ - margin (25% of inputSize)
         Formula: screenWidth = 5 * inputSize  +  6 * (inputSize * 0.25)
         screenWidth = inputSize * (5 + 6 * 0.25) = inputSize * (5 + 1.5)
         screenWidth = inputSize * 6.5
                               5        + (         5        + 1) *          0.25           = 6.5  */
        double coef = Config.inputCount + (Config.inputCount + 1) * Config.inputMarginCoef;
        int inputSize = (int) (screenWidth / coef);
        return inputSize;
    }

    private int getInputMargin(int inputSize) {
        /*
         _[ ]__[ ]_
          __   -margin between inputs, 25% of inputSize
          [ ]  -input
          _[ ]_   -input with margins
          _  -inputMargin (12.5% of inputSize)
         */
        int inputMargin = (int) (inputSize * Config.inputMarginCoef / 2);
        return inputMargin;
    }

    private boolean isAllFilled() {
        for (int i = 0; i < this.getChildCount(); i++) {
            EditText input = (EditText) this.getChildAt(i);
            if (input.getText().toString().isBlank() && input.getHint() == null) {
                return false;
            }
        }
        return true;
    }


    private void deleteKeyCallback() {
        EditText input = (EditText) this.getFocusedChild();
        if (!input.getText().toString().isBlank() || input.getHint() != null) {
            input.setText("");
            input.setHint(null);
            return;
        }
        int prevInputIndex = this.indexOfChild(input) - 1;
        if (prevInputIndex < 0) {
            return;
        }
        isDeleting = true;
        EditText prevInput = (EditText) this.getChildAt(prevInputIndex);
        prevInput.requestFocus();
        prevInput.setText("");
        isDeleting = false;
    }


    private void bindOnDeleteKeyListener(InputField input) {
        input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && keyCode == KeyEvent.KEYCODE_DEL) {
                    deleteKeyCallback();
                    checkIsFilled();
                    return true;
                }
                return false;
            }
        });
    }


    private void bindOnFocusChange(InputField input) {
        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                InputField input = (InputField) view;
                String text = input.getText().toString();
                String hint = input.getHint() == null ? "" : input.getHint().toString();
                isTextChanging = true;
                if (hasFocus) {
                    Utils.focusView(view);
                    if (!text.isBlank()) {
                        input.setHint(text);
                        input.setText(null);
                    }
                } else {
                    if (!isDeleting && !hint.isBlank() && text.isBlank()) {
                        input.setText(hint);
                        input.setHint(null);
                    }
                }
                isTextChanging = false;
            }
        });
    }


    private void bindOnTextChange(InputField input) {
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isTextChanging) {
                    return;
                }
                if (!s.toString().isBlank()) {
                    focusNext();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkIsFilled();
            }
        });
    }


    private void focusNext() {
         /*
          [#] - filled
          [@] - jumping from
          [ ] - blank
         */

        // if all fields are filled   [#] [#] [#] [#] [#]
        if (isAllFilled()) {
            this.clearFocus();
            Utils.hideKeyboard(this);
            return;
        }

        /*
         finding next available
         if no available forward, will focus first available on back
          on    [#] [ ] [#] [@] [_]    will focus [_]
          on    [#] [ ] [#] [@] [#]    will focus [ ]
         */
        int focusedIndex = this.indexOfChild(this.getFocusedChild());
        for (int i = focusedIndex; true; i++) {
            if (i == this.getChildCount()) {
                i = 0;
            }
            InputField v = (InputField) this.getChildAt(i);
            if (v.getText().toString().isBlank()) {
                Utils.focusView(v);
                return;
            }
        }
    }
}

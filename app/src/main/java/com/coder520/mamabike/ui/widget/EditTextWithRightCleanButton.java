package com.coder520.mamabike.ui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.EditText;


import com.coder520.mamabike.R;
import com.coder520.mamabike.utils.UIUtils;


/**
 * 带有清除按钮的输入框
 * Created by yadong on 2017/6/5.
 */

public class EditTextWithRightCleanButton extends EditText {
    public EditTextWithRightCleanButton(Context context) {
        super(context);
        setGravity(Gravity.CENTER_VERTICAL);
        addTextChangedListener(mTextWather);
        setPadding(0, 0, UIUtils.dip2px(context, 10), 0);
    }

    public EditTextWithRightCleanButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.CENTER_VERTICAL);
        addTextChangedListener(mTextWather);
        setPadding(0, 0, UIUtils.dip2px(context, 10), 0);
    }

    public EditTextWithRightCleanButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.CENTER_VERTICAL);
        addTextChangedListener(mTextWather);
        setPadding(0, 0, UIUtils.dip2px(context, 10), 0);
    }

    private enum Mode {
        NORMAL/*显示内容为空,不需要清除按钮*/, CLEAR/*显示内容不为空情况下, 需要显示清除按钮*/
    }

    private Mode mCurrentMode = Mode.NORMAL;
    private Drawable mRightImageDrawable = null;

    private TextWatcher mTextWather = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString();
            boolean cleanMode = !TextUtils.isEmpty(text);
            changeMode(cleanMode ? Mode.CLEAR : Mode.NORMAL);
        }
    };

    private void changeMode(Mode mode) {
        if (mode == Mode.NORMAL && mCurrentMode != Mode.NORMAL) {
            mCurrentMode = mode;
            setCompoundDrawables(null, null, null, null);
            setText("");
        } else if (mode == Mode.CLEAR && mCurrentMode != Mode.CLEAR) {
            if (mRightImageDrawable == null) {
                mRightImageDrawable = getResources().getDrawable(R.drawable.close_icon);
                mRightImageDrawable.setBounds(0, 0, UIUtils.dip2px(getContext(), 20),
                        UIUtils.dip2px(getContext(), 20));
            }
            mCurrentMode = mode;
            setCompoundDrawables(null, null, mRightImageDrawable, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mCurrentMode == Mode.CLEAR) {
            if (event.getRawX() >= getRight() - getCompoundDrawables()[2].getBounds().width()) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    changeMode(Mode.NORMAL);
                }
                return true;
            }
        }
        return super.onTouchEvent(event);
    }
}

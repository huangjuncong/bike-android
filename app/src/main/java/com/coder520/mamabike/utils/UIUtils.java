package com.coder520.mamabike.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Parcel;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.coder520.mamabike.R;


/**
 * Created by yadong on 2017/5/22.
 */

public class UIUtils {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 设置byte数组为imageView的显示
     * @param imageView
     * @param imageArr
     */
    public static void setImageView(ImageView imageView, byte[] imageArr) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageArr, 0, imageArr.length);
        imageView.setImageBitmap(bitmap);
    }
    /**
     * 扩大View的触摸和点击响应范围,最大不超过其父View范围
     *
     * @param view
     * @param top
     * @param bottom
     * @param left
     * @param right
     */
    public static void expandViewTouchDelegate(final View view, final int top,
                                               final int bottom, final int left, final int right) {
        ((View) view.getParent()).post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                view.setEnabled(true);
                view.getHitRect(bounds);
                bounds.top -= top;
                bounds.bottom += bottom;
                bounds.left -= left;
                bounds.right += right;
                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);
                if (View.class.isInstance(view.getParent())) {
                    ((View) view.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });
    }
    public static Dialog createHintMessageWithClick(Context context, String title, String message,
                                                    String buttonText,
                                                    View.OnClickListener clickListener) {
        return createCommenDialog(context,
                title, message, false, null, clickListener, null, buttonText);
    }

    private static Dialog createCommenDialog(Context context, String title, String message,
                                             boolean leftButton,
                                             final View.OnClickListener leftButtonClick,
                                             final View.OnClickListener rightButtonClick,
                                             String leftButtonText,
                                             String rightButtonText) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        //这里是为了让输入法能够弹出来
        dialog.setView(new EditText(context));
        View view = View.inflate(context, R.layout.dialog_hint_message, null);
        ((TextView) view.findViewById(R.id.text_title)).setText(title);
        ((TextView) view.findViewById(R.id.text_message)).setText(message);
        Button cancel = (Button) view.findViewById(R.id.btn_cancel);
        if (leftButton) {
            cancel.setVisibility(View.VISIBLE);
            cancel.setText(leftButtonText);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (leftButtonClick != null) {
                        leftButtonClick.onClick(v);
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                    }
                }
            });
        } else {
            cancel.setVisibility(View.GONE);
        }
        Button ok = (Button) view.findViewById(R.id.btn_ok);
        ok.setText(rightButtonText);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightButtonClick != null) {
                    rightButtonClick.onClick(v);
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }
            }
        });
        dialog.setView(view);
        return dialog;
    }

    public static Dialog createTwoButtonDialog(Context context, String tile, String message,
                                               String leftButtonText, String rightButtonText,
                                               View.OnClickListener leftClick,
                                               View.OnClickListener rightClick) {
        return createCommenDialog(context, tile, message, true, leftClick,
                rightClick, leftButtonText, rightButtonText);
    }

    /**
     * 创建信息提示dialog
     *
     * @param context
     * @param title
     * @param message
     * @param buttonText
     * @return
     */
    public static Dialog createHintDialog(Context context, String title, String message,
                                          String buttonText) {
        if (TextUtils.isEmpty(buttonText)) {
            buttonText = context.getString(android.R.string.ok);
        }
        return createCommenDialog(context, title, message, false, null, null, null, buttonText);
    }

    /**
     * 添加一个区别于其他颜色的spannableString
     *
     * @param text
     * @param spanText
     * @param color
     * @return
     */
    public static SpannableString addColorSpan(String text, String spanText, int color) {
        SpannableString span1 = new SpannableString(text);
        span1.setSpan(new ForegroundColorSpan(color), text.indexOf(spanText),
                text.indexOf(spanText) + spanText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span1;
    }

    /**
     * 添加一个可点击的text项
     *
     * @param text          完整的text
     * @param span          SpannableString
     * @param spanText      需要点击事件的文字
     * @param clickRunnable 点击之后的方法回调
     */
    public static void addClickSpan(String text, SpannableString span, String spanText,
                                    final Runnable clickRunnable, int foregroundColor) {
        span.setSpan(new ClickableSpan() {
                         @Override
                         public void onClick(View widget) {
                             clickRunnable.run();
                         }
                     }, text.indexOf(spanText), text.indexOf(spanText) + spanText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new NoUnderlineSpan(), text.indexOf(spanText),
                text.indexOf(spanText) + spanText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(foregroundColor), text.indexOf(spanText),
                text.indexOf(spanText) + spanText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static class NoUnderlineSpan extends UnderlineSpan {
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor);
            ds.setUnderlineText(false);
        }

        public static final Creator<NoUnderlineSpan> CREATOR = new Creator() {
            @Override
            public NoUnderlineSpan createFromParcel(Parcel source) {
                NoUnderlineSpan object = new NoUnderlineSpan();
                return object;
            }

            @Override
            public NoUnderlineSpan[] newArray(int size) {
                return new NoUnderlineSpan[size];
            }
        };

    }
}

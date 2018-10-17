package com.coder520.mamabike.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

/**
 * 点击发送验证码倒计时工具
 */
public class TimeCountUtil extends CountDownTimer {
    private Activity mActivity;
    private Button btn;
    private String tag;
    private long mStartTime;
    private static Map<String, TimeCountUtil> sCountUtilsMap = new HashMap<>(5);

    private TimeCountUtil(String tag, Activity mActivity, long millisInFuture,
                          long countDownInterval, Button btn) {
        super(millisInFuture, countDownInterval);
        this.tag = tag;
        this.mActivity = mActivity;
        this.btn = btn;
    }

    /**
     * 获取工具对象
     *
     * @param tag  根据tag创建或获取工具对象
     * @param mActivity
     * @param millisInFuture
     * @param countDownInterval
     * @param btn
     * @return
     */
    public static TimeCountUtil create(String tag, Activity mActivity, long millisInFuture,
                                       long countDownInterval, Button btn) {
        if (sCountUtilsMap.get(tag) != null) {
            TimeCountUtil timeCountUtil = sCountUtilsMap.get(tag);
            if (System.currentTimeMillis() - timeCountUtil.mStartTime < millisInFuture) {
                timeCountUtil.btn = btn;
                return timeCountUtil;
            }
        }
        TimeCountUtil timeCountUtil = new TimeCountUtil(tag, mActivity, millisInFuture,
                countDownInterval, btn);
        sCountUtilsMap.put(tag, timeCountUtil);
        return timeCountUtil;
    }

    /**
     * 启动开始CountDown
     */
    public void startRun() {
        mStartTime = System.currentTimeMillis();
        start();
    }


    @SuppressLint("NewApi")
    @Override
    public void onTick(long millisUntilFinished) {
        if (btn != null) {
            btn.setEnabled(false);// 设置不能点击
            btn.setText(millisUntilFinished / 1000 + "秒");// 设置倒计时时间
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onFinish() {
        if (btn != null) {
            btn.setText("重新发送验证码");
            btn.setEnabled(true);// 重新获得点击
        }
    }
}

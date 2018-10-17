package com.coder520.mamabike.ui.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coder520.mamabike.R;
import com.coder520.mamabike.manager.UserManager;
import com.coder520.mamabike.presenter.BasePresenter;
import com.coder520.mamabike.service.LocationService;
import com.coder520.mamabike.ui.UiControlInterface;

import org.bouncycastle.jcajce.provider.symmetric.ARC4;

/**
 * Created by huang on 2017/8/8.
 */

public abstract  class BaseActivity<T extends BasePresenter> extends Activity
        implements UiControlInterface {
    private Dialog mDialog;
    private Dialog mProgressDialog;
    protected T mPresenter;
    private LocationService mLocationService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.onCreate();
        }
        configActionBar();
    }

    private void bindLocationService() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.start(getIntent());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPresenter != null) {
            mPresenter.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    protected abstract T createPresenter();

    protected void configActionBar() {
        if(null == getActionBar()){
            return;
        }
        if (!showActionBar()) {
            getActionBar().hide();
        } else {
            getActionBar().show();
            configActionBar(getActionTitle());
        }
    }

    //配置action bar
    private void configActionBar(String title) {
        getActionBar().setDisplayHomeAsUpEnabled(false);
        if (getLeftActionClickAction() == null) {
            getActionBar().setHomeButtonEnabled(true);
        }
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);
        View actionbarLayout = LayoutInflater.from(this).inflate(
                R.layout.action_title_layout, null);
        if (getActionBarBackground() != -1) {
            actionbarLayout.setBackgroundColor(getActionBarBackground());
        }
        AppCompatImageView leftActionItem = (AppCompatImageView) actionbarLayout.findViewById(
                R.id.left_button);
        if (hideLeftActionItem()) {
            leftActionItem.setVisibility(View.GONE);
        } else if (getLeftActionClickAction() != null) {
            leftActionItem.setOnClickListener(getLeftActionClickAction());
        } else {
            leftActionItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        if (!hideLeftActionItem() && getLeftActionDrawable() != -1) {
            leftActionItem.setImageResource(getLeftActionDrawable());
        }
        ((TextView) actionbarLayout.findViewById(R.id.action_text)).setText(title);
        TextView rightView = (TextView) actionbarLayout.findViewById(R.id.text_right_button);
        if (TextUtils.isEmpty(getRightActionText())) {
            rightView.setVisibility(View.GONE);
        } else {
            rightView.setText(getRightActionText());
        }
        if (getRightActionClickAction() != null) {
            rightView.setOnClickListener(getRightActionClickAction());
        }
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL;
        getActionBar().setCustomView(actionbarLayout, layoutParams);
    }

    /**
     * 子类通过重载这个方法设置左边按钮图片
     * @return
     */
    protected int getLeftActionDrawable() {
        return -1;
    }

    /**
     * 子类通过重载这个方法控制是否隐藏左边按钮
     * @return false不隐藏  true 隐藏
     */
    protected boolean hideLeftActionItem() {
        return false;
    }

    /**
     *
     * 子类通过重载这个方法控制actionbar上左边按键的按键事件
     * @return actionbar左边按键的按键监听, 返回null将会使用默认监听
     */
    protected View.OnClickListener getLeftActionClickAction() {
        return null;
    }

    /**
     * 配置actionbar的默认颜色
     * @return -1不改变使用默认颜色.
     */
    protected int getActionBarBackground() {
        return -1;
    }

    /**
     * 子类通过重载这个方法控制actionbar右边按键的图标显示
     * @return -1不显示右键, 其他值为右键的图标
     */
    protected String getRightActionText() {
        return null;
    }

    /**
     * 子类通过重载这个方法控制actionbar上右边按键的按键事件
     * @return actionbar右边按键的按键监听
     */
    protected View.OnClickListener getRightActionClickAction() {
        return null;
    }

    /**
     * 子类通过重载这个方法显示actionbar上的title
     * @return title
     */
    protected String getActionTitle() {
        return null;
    }

    protected boolean showActionBar() {
        return true;
    }

    @Override
    public Context getContextInternal() {
        return this;
    }

    @Override
    public void launchActivityForResult(int requestCode, Intent intent) {
        //// TODO: 17/8/20
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void launchActivity(boolean loginRequire, Intent intent) {
        if (loginRequire && !UserManager.getInstance().isLogin()) {
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
    }

    @Override
    public void showAlert(Dialog dialog) {
        dismissAlert();
        mDialog = dialog;
        dialog.show();
    }

    @Override
    public void dismissAlert() {
        if (mDialog != null) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            mDialog = null;
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress(String message) {
        dismissProgress();

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.progress_dialog_custom, null);// 得到加载view
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                this, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(message);// 设置加载信息

        mProgressDialog= new Dialog(this, R.style.progressDialog);// 创建自定义样式dialog
        mProgressDialog.setCancelable(false);// 不可以用“返回键”取消
        mProgressDialog.setContentView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局

        TextPaint textPaint = tipTextView.getPaint();
        float sizePx = textPaint.measureText(message);
        setDialogLayoutParams((int) sizePx, mProgressDialog);
        mProgressDialog.show();
    }

    private void setDialogLayoutParams(int contentWidth, Dialog dialog) {
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager m = getWindowManager();
        m.getDefaultDisplay().getMetrics(dm);
        int maxWidth = (int) (dm.widthPixels * 0.85F);
        int minWidth = (int) (dm.widthPixels * 0.45F);
        if (contentWidth < minWidth) {
            contentWidth = minWidth;
        }
        if (contentWidth > maxWidth) {
            contentWidth = maxWidth;
        }
        lp.width = contentWidth;
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void dismissProgress() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            mProgressDialog = null;
        }
    }
}

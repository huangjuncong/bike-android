package com.coder520.mamabike.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.coder520.mamabike.R;
import com.coder520.mamabike.entity.vo.UserVo;
import com.coder520.mamabike.entity.vo.WalletVo;
import com.coder520.mamabike.presenter.UserSettingPresenter;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by huang on 2017/9/17.
 */

public class UserSettingActivity extends BaseActivity<UserSettingPresenter> {
    @BindView(R.id.image_head)
    ImageView imageHead;
    @BindView(R.id.text_user_name)
    TextView textUserName;
    @BindView(R.id.text_my_banlance)
    TextView textMyBanlance;

    @Override
    protected UserSettingPresenter createPresenter() {
        return new UserSettingPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        ButterKnife.bind(this);
    }

    @Override
    protected int getActionBarBackground() {
        return 0xff5fced7;
    }

    @Override
    protected String getActionTitle() {
        return getString(R.string.mama_bike);
    }

    @OnClick(R.id.text_my_trip)
    public void onMyTripClicked() {
        Intent intent = new Intent(UserSettingActivity.this, UserTripActivity.class);
        launchActivity(true, intent);
        finish();
    }

    @OnClick(R.id.image_head)
    public void onHeadImageClicked() {
        Intent intent = new Intent(UserSettingActivity.this, UserInfoActivity.class);
        launchActivity(true, intent);
        finish();
    }

    private Handler mHandler = new Handler();

    public void setUserInfo(final UserVo userInfo) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //头像
                if (!TextUtils.isEmpty(userInfo.getHeadImg())) {
                    Picasso.with(UserSettingActivity.this)
                            .load(userInfo.getHeadImg()).into(imageHead);
                } else {
                    imageHead.setImageResource(R.drawable.avatar_default_login);
                }
                //昵称
                String nikeName = userInfo.getNickname();
                if (TextUtils.isEmpty(nikeName)) {
                    nikeName = "-";
                }
                textUserName.setText(nikeName);
            }
        });
    }

    public void setWalletInfo(final WalletVo walletVo) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                textMyBanlance.setText(walletVo.getRemainSum() + "元");
            }
        });
    }

    @OnClick(R.id.btn_login_out)
    public void onLoginOutClick() {
        mPresenter.loginOut();
    }
}

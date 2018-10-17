package com.coder520.mamabike.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.coder520.mamabike.R;
import com.coder520.mamabike.entity.vo.UserVo;
import com.coder520.mamabike.presenter.UserInfoPresenter;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;

/**
 * Created by huang on 2017/9/17.
 */

public class UserInfoActivity extends BaseActivity<UserInfoPresenter> {
    @BindView(R.id.img_head)
    ImageView imgHead;
    @BindView(R.id.text_nike_name)
    TextView textNikeName;
    @BindView(R.id.text_phone)
    TextView textPhone;

    @Override
    protected UserInfoPresenter createPresenter() {
        return new UserInfoPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
    }

    private Handler mHandler = new Handler();

    public void setUserInfo(final UserVo user) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                textNikeName.setText(user.getNickname());
                textPhone.setText(user.getMobile());
                if (!TextUtils.isEmpty(user.getHeadImg())) {
                    Picasso.with(UserInfoActivity.this).load(user.getHeadImg()).into(imgHead);
                } else {
                    imgHead.setImageResource(R.drawable.avatar_default_login);
                }
            }
        });
    }

    @OnClick(R.id.text_my_head_img)
    public void onViewClicked() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(false)
                .setPreviewEnabled(false)
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    public void setImgHead(final Bitmap bitmap) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                imgHead.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                Uri uri = Uri.fromFile(new File(photos.get(0)));
                UCrop.of(uri, uri)
                        .withAspectRatio(9, 9)
                        .withMaxResultSize(120, 120)
                        .start(UserInfoActivity.this);
            }
        }
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            mPresenter.uploadHeadImage(resultUri);
            System.out.println(resultUri);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }
}

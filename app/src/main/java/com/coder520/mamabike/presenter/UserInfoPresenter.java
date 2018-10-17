package com.coder520.mamabike.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.coder520.mamabike.entity.vo.UserVo;
import com.coder520.mamabike.internet.DataListener;
import com.coder520.mamabike.internet.DefaultResponseDispatcher;
import com.coder520.mamabike.internet.InternetRequestWorker;
import com.coder520.mamabike.internet.net.UserInterface;
import com.coder520.mamabike.manager.UserManager;
import com.coder520.mamabike.ui.UiControlInterface;
import com.coder520.mamabike.ui.activity.UserInfoActivity;

import java.io.ByteArrayOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by huang on 2017/9/17.
 */

public class UserInfoPresenter extends BasePresenter {
    private UserInfoActivity mActivity;
    public UserInfoPresenter(UiControlInterface uiControlInterface) {
        super(uiControlInterface);
        this.mActivity = (UserInfoActivity) uiControlInterface;
    }

    @Override
    public void start(Intent intent) {
        UserVo user = UserManager.getInstance().getUserInfo();
        mActivity.setUserInfo(user);
    }

    public  void  uploadHeadImage(Uri resultUri) {
        getUiControlInterface().showProgress("uploading....");
        try {
            Bitmap photoBmp = null;
            if (resultUri != null) {
                photoBmp = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), resultUri);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photoBmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imageArr = baos.toByteArray();
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                    imageArr);
            final Bitmap finalPhotoBmp = photoBmp;
            InternetRequestWorker.getInstance().asyncNetwork(InternetRequestWorker.getInstance()
                    .create(UserInterface.class).uploadHeadImg(
                            MultipartBody.Part.createFormData("file", "idcard.jpg", requestBody)),
                    new DefaultResponseDispatcher(new DataListener() {
                        @Override
                        public void onSuccess(Object data, String message) {
                            getUiControlInterface().showToast("success");
                            mActivity.setImgHead(finalPhotoBmp);
                        }

                        @Override
                        public void onFailed(Throwable error, String message) {

                        }

                        @Override
                        public void onEnd() {
                            getUiControlInterface().dismissProgress();
                        }
                    }));
        } catch (Exception e) {
        }
    }
}

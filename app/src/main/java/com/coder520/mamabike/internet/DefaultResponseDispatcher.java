package com.coder520.mamabike.internet;

import com.coder520.mamabike.entity.ResponseEntityVo;

/**
 * 请求返回的默认派发器
 * Created by yadong on 2017/6/7.
 */

public class DefaultResponseDispatcher implements InternetRequestWorker.RequestCallBack {
    private DataListener mDataListener;

    public DefaultResponseDispatcher(DataListener dataListener) {
        mDataListener = dataListener;
    }

    protected void handleSuccess(ResponseEntityVo data) {
    }

    protected void handleFailed(String faild) {
    }

    @Override
    public void onSuccess(ResponseEntityVo data) {
        //派发消息之前需要把消息先拿给Manager内部处理, 做缓存等一些事情
        handleSuccess(data);
        //将消息派发
        if (mDataListener != null) {
            if (data.success()) {
                mDataListener.onSuccess(data.getData(), data.getMessage());
                mDataListener.onEnd();
            } else {
                mDataListener.onFailed(null, data.getMessage());
                mDataListener.onEnd();
            }
        }
    }

    @Override
    public void onFailed(String errorMessage) {
        handleFailed(errorMessage);
        if (mDataListener != null) {
            mDataListener.onFailed(null, errorMessage);
            mDataListener.onEnd();
        }
    }
}

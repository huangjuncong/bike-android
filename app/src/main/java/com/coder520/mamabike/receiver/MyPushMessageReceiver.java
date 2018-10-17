package com.coder520.mamabike.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



public class MyPushMessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("123456");
    }
}

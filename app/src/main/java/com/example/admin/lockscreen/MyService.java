package com.example.admin.lockscreen;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * Created by Admin on 2017/6/22.
 */

public class MyService extends Service {

    private KeyguardManager mKeyguardManager;
    private KeyguardManager.KeyguardLock mKeyguardLock;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        LogUtil.v("开启服务");
        //注册广播--开关键  开  关
        registerReceiver(mScreenOnReceiver, new IntentFilter("android.intent.action.SCREEN_ON"));
        registerReceiver(mScreenOffReceiver, new IntentFilter("android.intent.action.SCREEN_OFF"));
        registerReceiver(mScreenOffReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        registerReceiver(mScreenOffReceiver, new IntentFilter(Intent.ACTION_USER_PRESENT));

        mKeyguardManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        mKeyguardLock = mKeyguardManager.newKeyguardLock("unLock");
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mScreenOnReceiver);
        unregisterReceiver(mScreenOffReceiver);
        //再次打开服务
        LogUtil.v("再次打开服务");
        startService(new Intent(this,MyService.class));
    }


    // 屏幕变亮的广播,我们要隐藏默认的锁屏界面
    private BroadcastReceiver mScreenOnReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                LogUtil.v("----------------- android.intent.action.SCREEN_ON------");
                //打开锁屏界面
                //sendBroadcast(new Intent(Constent.OpenLockScreenViewBroadcast));
                //判断是否锁屏
                mKeyguardLock.disableKeyguard();

            }
        }
    };

    // 屏幕变暗/变亮的广播 ， 我们要调用KeyguardManager类相应方法去解除屏幕锁定
    private BroadcastReceiver mScreenOffReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.intent.action.SCREEN_OFF")) {
//                mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
//                mKeyguardLock = mKeyguardManager.newKeyguardLock("unLock");
                //mKeyguardLock.disableKeyguard();//解锁
                //OpenLockScreenView();
                OpenLockScreenView();
            }

            //监听home键
            if(action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)){
                //判断是否锁屏
                mKeyguardLock.disableKeyguard();
            }

            //屏幕解锁的刹那
            if(action.equals(Intent.ACTION_USER_PRESENT)){
                LogUtil.v("锁屏界面解锁");
            }
        }
    };

    public void OpenLockScreenView(){
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(this, LockScreenView.class);
        startActivity(intent);
    }


}

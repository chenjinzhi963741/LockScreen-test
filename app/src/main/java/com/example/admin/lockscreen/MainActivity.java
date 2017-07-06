package com.example.admin.lockscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; //需要自己定义标志

    public static Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message m){
            switch (m.what){
                case Constent.OpenLockScreenView:{
                    LogUtil.v("打开锁屏界面");
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);//关键代码
        init();
    }

    private void init(){


        //监听按钮
        findViewById(R.id.bt_openMyService).setOnClickListener(listener);
        findViewById(R.id.bt_secondActivity).setOnClickListener(listener);

        //注册广播
        registerReceiver(MyOpenActivityReceiver,new IntentFilter(Constent.OpenLockScreenViewBroadcast));
    }

    /**
     * 按钮事件监听
     */
    Button.OnClickListener listener = new Button.OnClickListener(){//创建监听对象
        public void onClick(View view){
            switch (view.getId()){
                case R.id.bt_openMyService:{
                    startService(new Intent(MainActivity.this,MyService.class));
                    //myHandler.sendEmptyMessage(0);
                    break;
                }
                case R.id.bt_secondActivity:{
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, LockScreenView.class);
                    startActivity(intent);
                    break;
                }
                default:
                    break;
            }
        }

    };

    public void OpenLockScreenView(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, LockScreenView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//注意本行的FLAG设置 这样开启时将会清除该进程空间的所有Activity。
        startActivity(intent);
    }

    private BroadcastReceiver MyOpenActivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constent.OpenLockScreenViewBroadcast)) {
                LogUtil.v("-----------------收到打开锁屏广播------");
                //打开锁屏界面
                OpenLockScreenView();
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtil.v("重写home键");
        if (KeyEvent.KEYCODE_HOME == keyCode) {
            System.out.println("HOME has been pressed yet ...");
            // android.os.Process.killProcess(android.os.Process.myPid());

            Toast.makeText(getApplicationContext(), "HOME 键已被禁用...",
                    Toast.LENGTH_LONG).show();
        }
        return super.onKeyDown(keyCode, event); // 不会回到 home 页面
    }

}

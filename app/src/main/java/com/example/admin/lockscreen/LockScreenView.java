package com.example.admin.lockscreen;

import android.app.KeyguardManager;
import android.app.Notification;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static java.security.AccessController.getContext;

public class LockScreenView extends AppCompatActivity {

    private SlidingButton mSlidingButton;
    private ClockView clockView;
    public static Handler handler;
    public static LockScreenView instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView(R.layout.activity_main2);
        setContentView(R.layout.activity_lock_screen_view2);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSlidingButton = (SlidingButton)this.findViewById(R.id.mainview_answer_1_button);

        //LogUtil.v("屏幕宽度:" + getWindowManager().getDefaultDisplay().getWidth());

        init();
    }

    public void init(){

        handler = new Handler(){
            public void handleMessage(Message msg) {
                int tab = msg.what;
                switch (tab){
                    case 1:{
                        (findViewById(R.id.mainview_answer_1)).setBackground(getResources().getDrawable(R.drawable.background));
                        break;
                    }
                    case 2:{
                        (findViewById(R.id.mainview_answer_1)).setBackground(getResources().getDrawable(R.drawable.tulips));
                        break;
                    }
                    default:
                        break;
                }
            }
        };

        findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /*
    * 滑动解锁
    * */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        LogUtil.v("button width:" + mSlidingButton.getWidth());
        //是否已经移动到解锁位置
//        if(event.getAction() == MotionEvent.ACTION_MOVE){//移动的时候
//            //判断是否到达位置
//            if(mSlidingButton.IsMaxMargin(event)){
//
//                ((TextView)findViewById(R.id.mainview_answer_1)).setBackground(getResources().getDrawable(R.drawable.tulips));
//            }
//            else{
//                ((TextView)findViewById(R.id.mainview_answer_1)).setBackground(getResources().getDrawable(R.drawable.background));
//            }
//        }

        if (mSlidingButton.handleActivityEvent(event)) {
            //Toast.makeText(SlidingButtonActivity.this, "touch", 1).show();
            LogUtil.v("111111111");
            finish();
        }

        if(event.getAction() == MotionEvent.ACTION_UP){
            mSlidingButton.setMe(false);
        }

        return super.onTouchEvent(event);
    }

}

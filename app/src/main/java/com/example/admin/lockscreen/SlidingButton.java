package com.example.admin.lockscreen;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
        import android.util.Log;
        import android.view.MotionEvent;
        import android.view.animation.AccelerateInterpolator;
        import android.view.animation.Animation;
        import android.view.animation.Interpolator;
        import android.view.animation.TranslateAnimation;
        import android.view.animation.Animation.AnimationListener;
        import android.widget.FrameLayout;
        import android.widget.LinearLayout;
import android.widget.TextView;

public class SlidingButton extends android.support.v7.widget.AppCompatButton {

    public SlidingButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SlidingButton(Context context) {
        super(context);
    }

    private int base_move_x = 0;

    private void setBase_move_x(){
        base_move_x = 0;
    }

    private boolean isMe = false;

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean isMe) {
        this.isMe = isMe;
    }

    public SlidingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            LogUtil.v("Down left:" + this.getLeft());
            isMe = true;
            //this.clearAnimation();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            isMe = false;
        }
        return false;
    }

    public boolean handleActivityEvent(MotionEvent activityEvent) {
        boolean result = false;

        if (isMe()) {
            //LogUtil.v("activity event");
            if (activityEvent.getAction() == MotionEvent.ACTION_UP) {
                LogUtil.v("activity event Up");
                LogUtil.v("frame left:" + ((FrameLayout)this.getParent().getParent()).getLeft());
                LogUtil.v("my left:" + this.getLeft());
                LogUtil.v("width :" + this.getWidth());
                LogUtil.v("event x:" + activityEvent.getX());
                LogUtil.v("parent width:" + ((FrameLayout) this.getParent().getParent()).getWidth());

                if(this.getLeft() >= ((FrameLayout)this.getParent().getParent()).getWidth() - this.getWidth()){
                    //用户完成了选择动作
                    //Log.v("yupeng", "sliding true");
                    //到达目地的就保持原状
//                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)this.getLayoutParams();
//                    lp.leftMargin = 0;
//                    this.setLayoutParams(lp);
                    this.setMe(false);
                    result = true;
                }else{
                    TranslateAnimation trans =
                            new TranslateAnimation(
                                    Animation.ABSOLUTE, 0,
                                    Animation.ABSOLUTE,-this.getLeft(),
                                    Animation.RELATIVE_TO_SELF, 0,
                                    Animation.RELATIVE_TO_SELF, 0);

//                  trans.setStartOffset(0);
                    trans.setDuration(100);
//                  trans.setFillAfter(true);
                    trans.setInterpolator(new AccelerateInterpolator());
//                    trans.setInterpolator(new Interpolator() {
//
//                        @Override
//                        public float getInterpolation(float input) {
//                            // TODO Auto-generated method stub
//                            return 0;
//                        }
//                    });
                    trans.setAnimationListener(new SlidingAnimationListener(this));
                    startAnimation(trans);
                    //trans.startNow();
                }
            } else {
                if(activityEvent.getAction() == MotionEvent.ACTION_DOWN){
                    base_move_x = (int) activityEvent.getX(); //定位初始位置
                }
                // 还在拖动
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) getLayoutParams();
                //LogUtil.v("linearLayout width:" + lp.width);
//              Log.v("yupeng", "yu" + lp.leftMargin);
                lp.leftMargin = (int) activityEvent.getX() - base_move_x;//计算移动距离
                //LogUtil.v("getX:" + (int) activityEvent.getX() + "---base X:" + base_move_x);
                int frame_width = ((FrameLayout) this.getParent().getParent()).getWidth();
                int maxMargin = frame_width - this.getWidth();
//                LogUtil.v("Max leftMargin:" + (((FrameLayout)this.getParent().getParent()).getWidth() - this.getWidth()));
//                LogUtil.v("Max leftMargin:" + lp.leftMargin);
                if (lp.leftMargin > 0 && lp.leftMargin <= maxMargin) {
                    LockScreenView.handler.sendEmptyMessage(1);//textView背景桌面
                    setLayoutParams(lp);

                } else if(lp.leftMargin > 0 && lp.leftMargin > maxMargin){
                    //在大于最大Margin的时候，则只取最大值
                    lp.leftMargin = maxMargin;
                    LockScreenView.handler.sendEmptyMessage(2);//textView背景桌面
                    setLayoutParams(lp);
                }

            }
        }
        return result;
    }

    public boolean IsMaxMargin(MotionEvent event){
        int frame_width = ((FrameLayout) this.getParent().getParent()).getWidth();
        int maxMargin = frame_width - this.getWidth();
        int event_move = (int) event.getX() - base_move_x;//计算移动距离
        if(event_move >= maxMargin){
            return true;
        }

        return false;
    }

    private static class SlidingAnimationListener implements AnimationListener {

        private SlidingButton but;

        public SlidingAnimationListener(SlidingButton button) {
            this.but = button;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            rePosition();
            but.setMe(false);
            but.clearAnimation();
        }

        private void rePosition() {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) but
                    .getLayoutParams();
            lp.leftMargin = 0;
            but.setLayoutParams(lp);

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub

        }

    }

}
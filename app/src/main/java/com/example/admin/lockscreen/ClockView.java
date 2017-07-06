package com.example.admin.lockscreen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Calendar;

/**
 * Created by Admin on 2017/7/4.
 */

public class ClockView extends SurfaceView implements SurfaceHolder.Callback {

    public Thread clockThread;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder holder;
    private float randus = 0;//半径
    private int lendth_hour = 50;
    private int lendth_min = 30;
    private int lendth_second = 10;

    public ClockView(Context context) {
        super(context);
        init();
    }

    public ClockView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    private void init(){
        this.holder = getHolder();
        this.holder.addCallback(this);//设置回调
    }

    private void working() {
        clockThread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                            canvas = holder.lockCanvas(null);
                            canvas.drawColor(Color.BLACK);//清空
                            drawClock(canvas);//开始作图
                            holder.unlockCanvasAndPost(canvas); // 解锁画布，提交画好的图像

                            Thread.sleep(1000);
                    } catch (Exception e) {
                        LogUtil.v("作图异常");
                        e.printStackTrace();
                        break; //退出Activity的时候，该线程并没有结束，所以必须结束该线程(canvas获取的值为空)
                    }
                }
            }
        };
        clockThread.start();
    }

    public void StopThread(){
        clockThread.interrupt();
    }

    /**
     * 画一个圆圈
     */
    private void drawClock(Canvas canvas){
        //LogUtil.v("开始画图");
        //背景图片
        //clock_background(canvas);
        //框架
        clock_circle(canvas);
        //刻度
        clock_kedu(canvas);
        //标识时间
        clock_text(canvas);
        //时针
        clock_hour(canvas);
        //分针
        clock_min(canvas);
        //秒针
        clock_second(canvas);
    }

    private void clock_circle(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
//        PorterDuff.Mode = PorterDuff.Mode.DST_OVER;

        //设置字体大小
        paint.setTextSize(12);

        //让画出的图形是空心的
        paint.setStyle(Paint.Style.STROKE);
        //设置画出的线的 粗细程度
        paint.setStrokeWidth(5);

        int width = getWidth();
        int heigthTmp = getHeight();
        int circle_x = width/2;
        int circle_y = heigthTmp/2;
        randus = (circle_x > circle_y ? circle_y : circle_x);
        //LogUtil.v("randus:" + randus + "x:" + circle_x + "y:" + circle_y);

        //画圆

        //LogUtil.v("getx:" + getX() + "getY:" + getY() + "width:" + getWidth() + "height:" + getHeight());
        canvas.drawCircle(getWidth()/2, getHeight()/2, randus, paint);
        //画出字符串 drawText(String text, float x, float y, Paint paint)
        // y 是 基准线 ，不是 字符串的 底部
        //canvas.drawText("apple", 60, 60, paint);
        //canvas.drawLine(0, 60, 500, 60, paint);
    }

    private void clock_kedu(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.RED);

        int length = 0;
        for (int i=0;i<60;i++){
            if(i%5==0){
                paint.setStrokeWidth(5);
                length = 10;
            }else{
                paint.setStrokeWidth(3);
                length = 5;
            }

            canvas.drawLine(this.getWidth()/2,getHeight()/2-randus+length,this.getWidth()/2,getHeight()/2-randus,paint);

            canvas.rotate(360/60,getWidth()/2,getHeight()/2);
        }
    }

    private void clock_hour(Canvas canvas){
        int hour = Calendar.getInstance().get(Calendar.HOUR);
//        LogUtil.v("hour:" + hour);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);//抗锯齿

        canvas.save();//保存原有绘图，旋转后不影响以前的绘图
        canvas.rotate(360/12*hour,getWidth()/2,getHeight()/2);
        canvas.drawLine(getWidth()/2,getHeight()/2,getWidth()/2,randus/2,paint);

        canvas.restore();
    }
    private  void  clock_min(Canvas canvas){
        int min = Calendar.getInstance().get(Calendar.MINUTE);
        //LogUtil.v("min:" + min);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(4);
        paint.setAntiAlias(true);//抗锯齿

        canvas.save();//保存原有绘图，旋转后不影响以前的绘图
        canvas.rotate(360/60*min,getWidth()/2,getHeight()/2);
        canvas.drawLine(getWidth()/2,getHeight()/2,getWidth()/2,randus/3,paint);

        canvas.restore();
    }

    private void  clock_second(Canvas canvas){
        int second = Calendar.getInstance().get(Calendar.SECOND);
        //LogUtil.v("second:" + second);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);//抗锯齿

        canvas.save();//保存原有绘图，旋转后不影响以前的绘图
        canvas.rotate(6*second,getWidth()/2,getHeight()/2);
        canvas.drawLine(getWidth()/2,getHeight()/2,getWidth()/2,randus/5,paint);
        canvas.restore();
    }

    private void clock_text(Canvas canvas){
        int frontSize = 30;
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);//抗锯齿
        paint.setTextSize(frontSize);
        paint.setTextAlign(Paint.Align.CENTER);

        canvas.drawText("0",getWidth()/2,getHeight()/2-randus+frontSize+10,paint);
        canvas.drawText("3",getWidth()/2+randus-frontSize-10,getHeight()/2+frontSize/2,paint);
        canvas.drawText("6",getWidth()/2,getHeight()/2+randus-frontSize/2-10,paint);
        canvas.drawText("9",getWidth()/2-randus+frontSize+10,getHeight()/2+frontSize/2,paint);
    }

    private void clock_background(Canvas canvas){
        Paint paint = new Paint();
        paint.setAntiAlias(true);//抗锯齿

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background_gray);
        canvas.drawBitmap(bitmap,null,new Rect(this.getLeft(),getTop(),getRight(),getBottom()),paint);
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        working();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        clockThread.interrupt();
    }
}

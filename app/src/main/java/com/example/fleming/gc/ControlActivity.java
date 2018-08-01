package com.example.fleming.gc;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ControlActivity extends AppCompatActivity{

    //设置打开界面时是否自动隐藏
    private static final boolean AUTO_HIDE = false;

    //点击按钮时自动隐藏的时间间隔
    private static final int AUTO_HIDE_DELAY_MILLIS = 500;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    //----------------------------------------------------------------------------------------------

    private SensorManager MyManage;    //新建sensor的管理器

    //新建三轴数据
    private float x;
    private float y;
    private float z;

    private TextView textView;

    //三个按键
    private ImageButton backButton;
    private ImageButton wButton;
    private ImageButton sButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        //获得系统传感器服务管理权
        MyManage = (SensorManager) getSystemService(SENSOR_SERVICE);
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        textView = findViewById(R.id.fullscreen_content);

        backButton = findViewById(R.id.backButton);
        wButton = findViewById(R.id.wButton);
        sButton = findViewById(R.id.sButton);

        //点击其他区域的监听器
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        //按钮设置监听器
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        //退出按钮点击触发show事件
        backButton.setOnClickListener(backOnClickListener);

        //w按钮按下事件：改背景、发socket
        wButton.setOnTouchListener(wOnTouchListener);

        //s按钮按下：改背景、发socket
        sButton.setOnTouchListener(sOnTouchListener);
    }

    //----------------------------------------------------------------------------------------------

    private View.OnClickListener backOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            toggle();
        }
    };

    //----------------------------------------------------------------------------------------------

    private final View.OnTouchListener wOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            //设置按下的动作
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                wButton.setBackgroundResource(R.drawable.b2w);

            //设置抬起的动作
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                wButton.setBackgroundResource(R.drawable.b1w);
            }

            return false;
        }
    };

    //----------------------------------------------------------------------------------------------

    private final View.OnTouchListener sOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            //设置按下的动作
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                sButton.setBackgroundResource(R.drawable.b2s);

                //设置抬起的动作
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                sButton.setBackgroundResource(R.drawable.b1s);
            }

            return false;
        }
    };

    //----------------------------------------------------------------------------------------------

    //当Activity彻底运行起来的回调函数
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);

        //隐藏提示；显示三个按键，开启按键

        textView.setVisibility(View.INVISIBLE);

        sButton.setVisibility(View.VISIBLE);
        wButton.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);

        sButton.setEnabled(true);
        wButton.setEnabled(true);
        backButton.setEnabled(true);
    }

    @SuppressLint("InlinedApi")
    private void show() {

        //显示提示，关闭按键，隐藏三个按键

        textView.setVisibility(View.VISIBLE);

        sButton.setEnabled(false);
        wButton.setEnabled(false);
        backButton.setEnabled(false);

        sButton.setVisibility(View.INVISIBLE);
        wButton.setVisibility(View.INVISIBLE);
        backButton.setVisibility(View.INVISIBLE);

        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    //----------------------------------------------------------------------------------------------

    @Override
    protected void onResume() {
        super.onResume();
        //注册重力感应服务
        boolean enable = MyManage.registerListener(MySensor_Gravity_listener, MyManage.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
        if (!enable) {
            MyManage.unregisterListener(MySensor_Gravity_listener);
        }
    }

    //activity暂停事件
    @Override
    protected void onPause() {
        super.onPause();
        MyManage.unregisterListener(MySensor_Gravity_listener);
    }

    //sensor监听事件
    SensorEventListener MySensor_Gravity_listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
//            if (event.sensor == null) {
//                return;
//            }
//            //新建加速度计变化事件
//            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//                //获取x，y，z三个方向的加速度值
//                x = event.values[SensorManager.DATA_X];
//                y = event.values[SensorManager.DATA_Y];
//                z = event.values[SensorManager.DATA_Z];
//                textView.setText("x="+(int)x+","+"y="+(int)y+","+"z="+(int)z);
//            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解注册
        MyManage.unregisterListener(MySensor_Gravity_listener);
    }

}
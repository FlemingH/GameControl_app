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

        //按钮设置监听器
        findViewById(R.id.dummy_button).setOnClickListener(dummyButtonOnClickListener);

        //退出按钮点击触发show事件
        backButton.setOnClickListener(backOnClickListener);

        //w按钮按下事件：改背景、发socket
        wButton.setOnTouchListener(wOnTouchListener);

        //s按钮按下：改背景、发socket
        sButton.setOnTouchListener(sOnTouchListener);
    }

    //----------------------------------------------------------------------------------------------

    //返回键事件
    private View.OnClickListener backOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            toggle();
        }
    };

    //----------------------------------------------------------------------------------------------

    //加速键事件
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

    //减速键事件
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

    //点击准备按钮，开始！
    private final View.OnClickListener dummyButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            toggle();
        }
    };

    //----------------------------------------------------------------------------------------------

    //当Activity彻底运行起来的回调函数：当页面加载后，马上调用show()
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        show();
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
# GameControl_app

## 纲要 Synopsis
App主要做的事：注册登录，检查Web登陆的情况，根据重力传感器和按键发送控制信息。<br>
What App does primarily: login, check web client is online, and send control information based on gravity sensors and button.

## 如何安装 Installation 
打包成apk，在安卓手机上安装即可。<br>
Pack it into APK and install it on Android mobile phone.<br>
(minSdkVersion 22    targetSdkVersion 28)

## 基本原理 Fundamentals
用OkHttpClient建立websocket连接。<br>
Establishing websocket connection with OkHttpClient.
```java
private void connect(){
        ControlSocketListener listener = new ControlSocketListener();
        Request request = new Request.Builder()
                .url("ws://118.25.180.193:8090/SocketHandle/control/"+username)
                .build();
        OkHttpClient client = new OkHttpClient();

        mWebSocket = client.newWebSocket(request, listener);
}
```

---

方向控制信息发送。<br>
Send direction control information.
```java
 //sensor监听事件
    SensorEventListener MySensor_Gravity_listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            socketMessage = new SocketMessage();

            if (event.sensor == null) {
                return;
            }
            //新建加速度计变化事件
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                //获取y的值
                int newY = (int) event.values[SensorManager.DATA_Y];
                if (lastY != newY){
                    //发送数据
                    lastY = newY;

                    socketMessage.setData(lastY+"");
                    socketMessage.setMessageType("dChange");
                    String json = gson.toJson(socketMessage);
                    mWebSocket.send(json);

                }

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) { }
};
```

---

加速键发送信息。<br>
Acceleration key sends information.
```java
//加速键事件
    private final View.OnTouchListener wOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            socketMessage = new SocketMessage();

            //设置按下的动作
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                wButton.setBackgroundResource(R.drawable.b2w);

                socketMessage.setData("");
                socketMessage.setMessageType("wButtonDown");
                String json = gson.toJson(socketMessage);
                mWebSocket.send(json);

            //设置抬起的动作
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                wButton.setBackgroundResource(R.drawable.b1w);

                socketMessage.setData("");
                socketMessage.setMessageType("wButtonUp");
                String json = gson.toJson(socketMessage);
                mWebSocket.send(json);

            }

            return false;
        }
};

```

## 包依赖 Dependencies
```java
dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    implementation 'com.android.support:appcompat-v7:28.0.0-beta01'
    implementation 'com.android.support.constraint:constraint-layout:1.1.2'
    testImplementation 'junit:junit:4.12'
    implementation 'com.android.support.test:runner:1.0.2'
    implementation 'com.android.support.test.espresso:espresso-core:3.0.2'
    implementation 'com.google.code.gson:gson:2.8.5'
    implementation 'com.squareup.okhttp3:okhttp:3.8.1'
}
```

## 示例图片 Sample
![example1](https://github.com/FlemingH/GameControl_app/blob/master/app_example1.jpg)
![example1](https://github.com/FlemingH/GameControl_app/blob/master/app_example2.jpg)
![example1](https://github.com/FlemingH/GameControl_app/blob/master/app_example3.jpg)
![example1](https://github.com/FlemingH/GameControl_app/blob/master/app_example4.jpg)

## 参与者介绍 Contributors 
[Fleming](https://github.com/FlemingH)-Initial work

## License
[MIT](http://opensource.org/licenses/MIT)

Copyright (c) 2018-present, Limi (Fleming) Fei

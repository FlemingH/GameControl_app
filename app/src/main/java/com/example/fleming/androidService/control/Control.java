package com.example.fleming.androidService.control;

import com.example.fleming.request.form.SocketMessage;
import com.google.gson.Gson;
import com.pusher.java_websocket.client.WebSocketClient;

public class Control {

    private Gson gson = new Gson();
    private SocketMessage socketMessage = new SocketMessage();

    /**
     * w键按下
     * @param mSocketClient
     */
    public void wButtonDown(WebSocketClient mSocketClient){
        socketMessage.setData("");
        socketMessage.setMessageType("wButtonDown");
        String json = gson.toJson(socketMessage);

        if (mSocketClient != null) {
            mSocketClient.send(json);
        }
    }

    /**
     * w键松开
     * @param mSocketClient
     */
    public void wButtonUp(WebSocketClient mSocketClient){
        socketMessage.setData("");
        socketMessage.setMessageType("wButtonUp");
        String json = gson.toJson(socketMessage);

        if (mSocketClient != null) {
            mSocketClient.send(json);
        }
    }

    /**
     * s键按下
     * @param mSocketClient
     */
    public void sButtonDown(WebSocketClient mSocketClient){
        socketMessage.setData("");
        socketMessage.setMessageType("sButtonDown");
        String json = gson.toJson(socketMessage);

        if (mSocketClient != null) {
            mSocketClient.send(json);
        }
    }

    /**
     * s键松开
     * @param mSocketClient
     */
    public void sButtonUp(WebSocketClient mSocketClient){
        socketMessage.setData("");
        socketMessage.setMessageType("sButtonUp");
        String json = gson.toJson(socketMessage);

        if (mSocketClient != null) {
            mSocketClient.send(json);
        }
    }

    /**
     * 方向改变：-10~9；-10=-9
     * @param mSocketClient
     * @param num
     */
    public void dChange(WebSocketClient mSocketClient, int num){
        socketMessage.setData(num+"");
        socketMessage.setMessageType("dChange");
        String json = gson.toJson(socketMessage);

        if (mSocketClient != null) {
            mSocketClient.send(json);
        }
    }

}

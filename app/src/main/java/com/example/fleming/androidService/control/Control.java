package com.example.fleming.androidService.control;

import com.example.fleming.request.form.SocketMessage;
import com.google.gson.Gson;

import okhttp3.WebSocket;

public class Control {

    private Gson gson = new Gson();
    private SocketMessage socketMessage = new SocketMessage();

    /**
     * w键按下
     * @param mWebSocket
     */
    public void wButtonDown(WebSocket mWebSocket){
        socketMessage.setData("");
        socketMessage.setMessageType("wButtonDown");
        String json = gson.toJson(socketMessage);

        if (mWebSocket != null) {
            mWebSocket.send(json);
        }
    }

    /**
     * w键松开
     * @param mWebSocket
     */
    public void wButtonUp(WebSocket mWebSocket){
        socketMessage.setData("");
        socketMessage.setMessageType("wButtonUp");
        String json = gson.toJson(socketMessage);

        if (mWebSocket != null) {
            mWebSocket.send(json);
        }
    }

    /**
     * s键按下
     * @param mWebSocket
     */
    public void sButtonDown(WebSocket mWebSocket){
        socketMessage.setData("");
        socketMessage.setMessageType("sButtonDown");
        String json = gson.toJson(socketMessage);

        if (mWebSocket != null) {
            mWebSocket.send(json);
        }
    }

    /**
     * s键松开
     * @param mWebSocket
     */
    public void sButtonUp(WebSocket mWebSocket){
        socketMessage.setData("");
        socketMessage.setMessageType("sButtonUp");
        String json = gson.toJson(socketMessage);

        if (mWebSocket != null) {
            mWebSocket.send(json);
        }
    }

    /**
     * 方向改变：-10~9；-10=-9
     * @param mWebSocket
     * @param num
     */
    public void dChange(WebSocket mWebSocket, int num){
        socketMessage.setData(num+"");
        socketMessage.setMessageType("dChange");
        String json = gson.toJson(socketMessage);

        if (mWebSocket != null) {
            mWebSocket.send(json);
        }
    }

}

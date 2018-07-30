package com.example.fleming.request;

import com.example.fleming.request.form.Message;
import com.example.fleming.util.Tools;

public class LoginRequest {

    /**
     * @param username
     * @param password
     * @return 登录是否成功
     */
    public static boolean IsLoginSuccess(String username,String password) {

        boolean flag = false;

        Message message = Tools.httpRequest("http://118.25.180.193:8081/LoginAction?username="+username+"&password="+password, "GET");

        if("ok".equals(message.getAjaxState())){
            flag = true;
        }

        return flag;
    }

    /**
     * @param username
     * 登录成功后向服务器发送我已准备好，请把登录信息添加到Map里
     */
    public static void IAmReady(String username) {

        Tools.httpRequest("http://118.25.180.193:8090/AppIsReady?username="+username,"GET");

    }

}

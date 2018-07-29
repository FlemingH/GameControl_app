package com.example.fleming.request;

import com.example.fleming.request.form.Message;
import com.example.fleming.util.Tools;

public class LoginRequest {

    /**
     * @param username
     * @param password
     * @return 登陆是否成功
     * 如果成功向服务器发送上线信息
     */
    public static boolean IsLoginSuccess(String username,String password) {

        boolean flag = false;

        Message message = Tools.httpRequest("http://118.25.180.193:8081/LoginAction?username="+username+"&password="+password, "GET");

        if("ok".equals(message.getAjaxState())){
            flag = true;
        }

        return flag;
    }

}

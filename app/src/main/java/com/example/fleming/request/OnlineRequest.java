package com.example.fleming.request;

import com.example.fleming.request.form.Message;
import com.example.fleming.util.Tools;

public class OnlineRequest {

    /**
     * 登录前检查自己是否在线，避免重复登录
     * @param username
     * @return 是否在线
     */
    public static boolean isAppOnline(String username){

        boolean flag = false;
        Message message = Tools.httpRequest("http://118.25.180.193:8090/CheckAppIsOnline?username="+username, "GET");

        if("ok".equals(message.getAjaxState()))       {
            flag = true;
        }

        return flag;
    }

}

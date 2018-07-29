package com.example.fleming.gc;

import com.example.fleming.request.form.Message;
import com.example.fleming.util.Tools;

import org.junit.Test;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {

        Message message = Tools.httpRequest("http://118.25.180.193:8081/LoginAction?username=admin&password=12345", "GET");
        System.out.println(message.getAjaxState());
    }
}
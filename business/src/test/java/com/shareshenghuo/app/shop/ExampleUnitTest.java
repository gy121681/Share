package com.shareshenghuo.app.shop;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        Pattern p = Pattern
                .compile("^((13[0-9])|(17[0-9])|(14[0-9])|(15[^4,\\D])|(18[0,1-9]))\\d{8}$");
        System.out.print(p.matcher("").matches());
        System.out.print(p.matcher("23156468791").matches());
        System.out.print(p.matcher("13348930210").matches());
    }
}
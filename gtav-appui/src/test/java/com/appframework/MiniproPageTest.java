package com.appframework;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * 〈test〉
 *
 * @author zhzh.yin
 * @create 2021/3/29
 */
public class MiniproPageTest {
    static Wework page;
    @BeforeAll
    static void setUp(){
        page = new Wework();
    }
    @Test
    void test(){
        page.jumpToMiniproPage().show();
    }
}

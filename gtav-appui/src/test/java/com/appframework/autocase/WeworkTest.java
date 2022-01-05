package com.appframework.autocase;

import com.appframework.page.Wework;
import org.junit.jupiter.api.*;

/**
 * 〈test wework〉
 *
 * @author zhzh.yin
 * @create 2021/3/29
 */
public class WeworkTest {
    static Wework page;
    @BeforeAll
    static void setUp(){
        page = new Wework();
    }
    @Test
    void test(){
        page.jumpToMiniproPage();
    }
}

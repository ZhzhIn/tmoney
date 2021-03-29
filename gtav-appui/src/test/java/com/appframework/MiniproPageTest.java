package com.appframework;

import com.tmoney.foundation.utils.Configuration;
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
    @Test
    void test2(){
        System.out.println(Configuration.get(Configuration.Parameter.MINIPRONAME));
    }
}

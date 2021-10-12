package com.appframework;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * 〈test wework〉
 *
 * @author zhzh.yin
 * @create 2021/3/29
 */
public class BankTest {
    static Bank page;
    @BeforeAll
    static void setUp(){
        page = new Bank();
    }
    @Test
    void test(){
        page.jumpToMiniproPage();
    }
}

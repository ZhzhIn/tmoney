package com.tengmoney.autoframework;

import com.appframework.MiniproPage;
import com.appframework.Wework;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * 〈小程序测试〉
 *
 * @author zhzh.yin
 * @create 2021/3/25
 */
public class MiniproPageTest {
    private static Wework wework= null;
    private static MiniproPage page=null;
    @BeforeAll
    static void beforeAll() {
        wework = new Wework();
        page = wework.jumpToMiniproPage();
    }
    @Test
    void testMinipro() {
        page=wework.jumpToMiniproPage();
    }
}

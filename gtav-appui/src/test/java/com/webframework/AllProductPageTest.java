package com.webframework;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

/**
 * 〈所有产品测试〉
 *
 * @author zhzh.yin
 * @create 2021/3/31
 */
public class AllProductPageTest {
    static ManagePage page ;
    @Test
    void test() throws InterruptedException {
        page = new ManagePage();
        page.jumpToAllProductPage();
//        Assert.assertNotNull(page.jumpToMorPaper().jumpToAddPaperPage().testAddPaperPage());
    }

    @AfterAll
    static void shutdown(){
        page.quit();
    }
}

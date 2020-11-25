package com.webframework;

import org.junit.jupiter.api.Test;

public class TestMainPage {
    MainPage page ;
    @Test
     void test() throws InterruptedException {
        page = new MainPage();
        page.jumpToMorPaper().jumpToAddPaperPage().addNewsTest();
//        Assert.assertNotNull(page.jumpToMorPaper().jumpToAddPaperPage().testAddPaperPage());
    }
/*    @AfterAll
    static void shutdown(){
        page.getMainPage().quit();
    }*/
}

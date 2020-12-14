package com.webframework;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

public class TestMainPage {
    static MainPage page ;
    @Test
     void test() throws InterruptedException {
        page = new MainPage();
        page.jumpToMorPaper().jumpToAddPaperPage().addNewsTest();
//        Assert.assertNotNull(page.jumpToMorPaper().jumpToAddPaperPage().testAddPaperPage());
    }
    @AfterAll
    static void shutdown(){
        page.quit();
    }
}

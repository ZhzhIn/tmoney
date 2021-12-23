package com.appframework.page;

import com.appframework.H5Page;
import com.appframework.Wework;
import lombok.extern.slf4j.Slf4j;

import org.junit.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

@Slf4j
class H5PageTest {
    private static Wework wework= new Wework();


    @BeforeAll
    static void beforeAll() {

    }
    @Test
      void test() throws InterruptedException {
        wework.jumpToH5Page().clickStation();
        wework.screenshot();
    }
    @AfterAll
    static void afterAll(){
        wework.quit();
    }
}
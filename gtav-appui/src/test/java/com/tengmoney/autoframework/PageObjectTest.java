package com.tengmoney.autoframework;/**
 * @author zhzh.yin
 * @create 2020-09-22 14:31
 */

import org.junit.Test;

import java.util.List;

/**
 * 〈pageobject test〉
 *
 * @author zhzh.yin
 * @create 2020/9/22
 */
public class PageObjectTest {
    @Test
    public void test2() {
        List<PageObjectModel> pages = BasePageFactory.create("web").loadPages("src/test/resources/test_framework").pages;
        System.out.println(pages.toString());
        //父类引用子类对象，getClass是子类的类型
    }
}

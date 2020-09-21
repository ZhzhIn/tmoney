package com.tengmoney.autoframework;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class DDTest {

    private static BasePage basePage;
    @BeforeAll
    static void beforeAll(){
        //todo: 加载通用配置
    }

    @BeforeEach
    void beforeEach(){
        //todo: 每个用例相关
    }

    @ParameterizedTest(name = "{index} {1}")
    @MethodSource
    void classic(UITestCase uiTestcase, String path){
        basePage.run(uiTestcase);
    }

    static List<Arguments> classic(){
        basePage = BasePageFactory.create("web");
        List<Arguments> all= new ArrayList<Arguments>();

        Arrays.asList(
                "/test_framework/webauto_1.yaml",
                "/test_framework/webauto_2.yaml"
        ).stream().forEach(path->{
            UITestCase uiTestcase= UITestCase.load(path);
            uiTestcase.description=path;
            all.add(arguments(uiTestcase, path));
        });
        return all;

    }
}

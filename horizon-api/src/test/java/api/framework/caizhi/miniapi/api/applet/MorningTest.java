package api.framework.caizhi.miniapi.api.applet;

import api.framework.ApiModel;
import api.framework.LoginHelper;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.BaseMatcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Execution(CONCURRENT)  //CONCURRENT表示支持多线程
@Slf4j
@Feature("早报")
@Owner("zhzh.yin")
public class MorningTest {
    ApiModel model = ApiModel.load("src/test/resources/miniapi/morningtest.yaml");

    @BeforeAll
    static void beforeAll() {
        LoginHelper.login();
    }

    @Test
    @DisplayName("一个简单的断言")
    @Story("detail.do接口")
    void test1() {
        model.runWithoutConfig("getDetail")
                .then()
                .statusCode(200)
                .body("ret", equalTo(0));
//                .body("ret", hasItem(0))
//                .body("xx.xx", hasItems(1, 2));
    };

    @ParameterizedTest(name ="早报接口：{0}-{index}")
    @CsvSource({
            "getDetail,ret,0",
            "getDetail,ret,0"
    })
    @Story("csv测试detail.do接口")
    void testPaper(String apiName, String responsePath, Integer expectValue) throws InvocationTargetException, IllegalAccessException {
        assertTrue(model.runWithoutConfig(apiName)
                .path(responsePath).equals(expectValue));
    }

    @ParameterizedTest(name ="早报接口：{0}-{index}")
    @MethodSource("paperYamlProvider")
    @Story("一大堆早报接口")
    void testPaperYaml(String apiName, HashMap<String, String> map, String responsePath, Integer expectValue) {
        assertTrue(model.get(apiName).importParam(map).run()
                .path(responsePath)
                .equals(expectValue));
    }
    static Stream<Arguments> paperYamlProvider() {
        HashMap<String, String> requestParam = new HashMap<>();
        requestParam.put("id", "f09a04b775974f98bee9aaed8c492d24");
        HashMap<String,String> jsonFileName = new HashMap<>();
        jsonFileName.put("jsonFileName","viewPaper");
        return Stream.of(
                arguments("getDetail", null, "ret", 0),
                arguments("getDetail2" , requestParam, "ret", 0),
                arguments("getDetail" , null, "ret", 0),
                arguments("getDetail" , jsonFileName, "ret", 0),
                arguments("viewPaper",null, "ret", 0),
                arguments("paperConfigAPI",null, "ret", 0)
        );
    }
    @ParameterizedTest(name = "接口-{0}-{index}")
    @MethodSource("oneApi")
    @Story("接口的另一种写法")
    void testPaper2(String apiName,HashMap map, String str, BaseMatcher matcher) {
        Object result = model.get(apiName).importParam(map).run()
                .getBody().jsonPath().get(str);
        assertThat(result, matcher);
    }
    static Stream<Arguments> oneApi() {
        HashMap<String, String> requestParam = new HashMap<>();
        requestParam.put("id", "f09a04b775974f98bee9aaed8c492d24");
        HashMap<String,String> jsonFileName = new HashMap<>();
        jsonFileName.put("jsonFileName","viewPaper");
        return Stream.of(
                arguments("getDetail",requestParam, "ret",is(0)),
                arguments("getDetail",jsonFileName, "ret",is(0))
        );
    }
}

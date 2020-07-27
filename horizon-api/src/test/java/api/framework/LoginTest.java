package api.framework;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

@Execution(CONCURRENT)  //CONCURRENT表示支持多线程
@Slf4j
@Feature("登录")
@Owner("zhzh.yin")
public class LoginTest {
    private static ApiModel model= ApiModel.load("src/test/resources/miniapi/logintest.yaml");
    @ParameterizedTest(name = "登录接口-{0}-{index}")
    @CsvSource({
                    "login, ret, 0",
            "testLoginFailure,ret,1000002"
            })
    @Story("登录用例")
    void testBaseYaml(String apiName, String assertPath, int code) {
        Response response = model.runWithoutConfig(apiName);
        assertThat("验证返回code码",response.path(assertPath).equals(code));
    }

    @ParameterizedTest(name = "登录接口-{0}-{index}")
    @CsvSource({
            "login, ret, 0",
            "testLoginFailure,ret,1000002"
    })
    @Story("并发登录")
    void testBaseYaml2(String apiName, String assertPath, int code) {
        Response response = model.runWithoutConfig(apiName);
        assertThat("验证返回code码",response.path(assertPath).equals(code));
    }
}

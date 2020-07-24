package api.framework;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Execution(CONCURRENT)  //CONCURRENT表示支持多线程
@Slf4j
public class LoginTest {
    private static ApiHelper basePO=new ApiHelper();
    private static HashMap<String,ApiContent> apis= basePO.load("src/test/resources/apiyaml/login.yaml").getContents();
    @ParameterizedTest(name="{index}...")
    @MethodSource("baseYamlProvider")
    @DisplayName("登录")
    void testBaseYaml(ApiContent apiContent,String path, int code) {
        assertTrue(ApiHelper.run(apiContent).path("ret").equals(code));
/*        assertAll(
                ()->assertTrue(response.path("ret").equals(code))
        );*/

    }

    /**
     * 接口，接口参数->response->实际值vs预期值
     * @return
     */
    static Stream<Arguments> baseYamlProvider() {
        return Stream.of(
                arguments(apis.get("login"), "ret", 0),
                arguments(apis.get("testLoginFailure"), "ret", 1000002)
        );
    }

}

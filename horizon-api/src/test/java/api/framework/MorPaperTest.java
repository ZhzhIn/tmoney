package api.framework;

import api.item.ManuData;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Execution(CONCURRENT)  //CONCURRENT表示支持多线程
@Slf4j
public class MorPaperTest {
    private static ApiHelper basePO = new ApiHelper();
    private static HashMap<String, ApiContent> apis = basePO.load("src/test/resources/apiyaml/paper.yaml").getContents();

    @BeforeAll
    static void beforeAll() {
        LoginHelper.login();
    }

    @ParameterizedTest
    @MethodSource("paperYamlProvider")
    @DisplayName("早报")
    void testPaperYaml(ApiContent api, HashMap<ManuData, HashMap<String, String>> map, String responsePath, Integer expectValue) {
        assertTrue(ApiHelper.run(api, map)
                .path(responsePath).equals(expectValue));
        /*assertAll(
                ()->assertTrue(ApiPO.parseApi(api).path("ret").equals(0))
        );*/
    }

    static Stream<Arguments> paperYamlProvider() {
        HashMap<ManuData, HashMap<String, String>> map = new HashMap<>();
        HashMap<String, String> param = new HashMap<>();
        param.put("id", "f09a04b775974f98bee9aaed8c492d24");
        map.put(ManuData.REQUESTPARAM, param);
        return Stream.of(
                arguments(apis.get("getDetail"), null, "ret", 0),
                arguments(apis.get("getDetail2"), map, "ret", 0),
                arguments(apis.get("viewPaper"),null, "ret", 0),
                arguments(apis.get("filter"),null, "ret", 0),
                arguments(apis.get("paperConfigAPI"),null, "ret", 0),
                arguments(apis.get("paperSaveAPI"),null, "ret", 0),
                arguments(apis.get("paperListAPI"),null, "ret", 0)
        );
    }



    @ParameterizedTest
    @CsvSource({
                    "getDetail,ret,0",
                    "paperConfigAPI,ret,0"
            })
    void testPaper(String apiName, String responsePath, Integer expectValue) {
        assertTrue(ApiHelper.run(apis.get(apiName))
                .path(responsePath).equals(expectValue));
    }


    @Test
    void testPaper2(){
        ApiHelper.run(apis.get("getDetail"))
                .then()
                .statusCode(200)
                .body("ret",equalTo(0))
                .body("ret",hasItem(0))
                .body("xx.xx",hasItems(1,2));
    }
}

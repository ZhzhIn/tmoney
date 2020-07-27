package api.framework.caizhi.miniapi.api.applet;/**
 * @author zhzh.yin
 * @create 2020-08-05 11:51
 */

import api.framework.ApiModel;
import api.framework.LoginHelper;
import api.item.AppType;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import util.LoadDefaultConfig;

import java.util.HashMap;

import static api.item.AppType.H5STATION;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

/**
 * 〈小站首页接口〉
 *
 * @author zhzh.yin
 * @create 2020/8/5
 */
@Execution(CONCURRENT)  //CONCURRENT表示支持多线程
@Slf4j
@Feature("早报")
@Owner("zhzh.yin")
public class StationTest {
    ApiModel model = ApiModel.load("src/test/resources/miniapi/stationtest.yaml");

    @BeforeAll
    static void beforeAll() {
        LoginHelper.login();
    }


    //    @Test
    @DisplayName("managerStation.do")
    @Story("小站首页-managerStation")
    void testManagerStation() {
        model.get("managerStation")
                .importDefaultConfig(H5STATION)
                .run()
                .then()
                .statusCode(200)
                .body("ret", equalTo(0));
//                .body("ret", hasItem(0))
//                .body("xx.xx", hasItems(1, 2));
    }

    @ParameterizedTest
    @CsvSource({
            "H5STATION",
            "MINIPRO",
            "H5STATION",
            "H5PRODUCT"
    })
    @Story("小站首页-getAuthInfo")
    void testGetAuthInfo(AppType type) {
        HashMap<String, String> map = new HashMap<>(16);
        map.put("currentCorpId", LoadDefaultConfig.getCorp().getCorpId());
        map.put("agentId", LoadDefaultConfig.getApp(type).getAgentId());
        model.get("getAuthInfo")
                .importParam(map)
                .run()
                .then()
                .statusCode(200)
                .body("ret", equalTo(0))
                .body("retdata.appId",equalTo(LoadDefaultConfig.getApp(type).getAppId()))
                .body("retdata.authCorpId",equalTo(LoadDefaultConfig.getCorp().getCorpId()))
                .body("retdata.componentAppid",equalTo(LoadDefaultConfig.getApp(type).getComponentAppid()))
        ;
    }

}

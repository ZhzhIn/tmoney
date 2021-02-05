package api.framework.caizhi.miniapi.index.h5;

import api.framework.ApiList;
import api.framework.LoginHelper;
import api.item.AppType;
import util.DefaultConfig;

import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;

public class GetAuthInfoTest {
    ApiList model = ApiList.load(this.getClass());

//    @BeforeAll
    void beforeAll() {
        LoginHelper.login();
    }
//    @ParameterizedTest
   /* @CsvSource({
            "H5STATION",
            "MINIPRO",
            "H5STATION",
            "H5PRODUCT"
    })*/
//    @Story("小站首页-getAuthInfo")
    void testGetAuthInfo(AppType type) {
        HashMap<String, Object> map = new HashMap<>(16);
        map.put("currentCorpId", DefaultConfig.getCorp().getCorpId());
        map.put("agentId", DefaultConfig.getApp(type).getAgentId());
        model.get("getAuthInfo")
                .importParam(map)
                .run()
                .then()
                .body("ret", equalTo(0))
                .body("retdata.appId",equalTo(DefaultConfig.getApp(type).getAppId()))
                .body("retdata.authCorpId",equalTo(DefaultConfig.getCorp().getCorpId()))
                .body("retdata.componentAppid",equalTo(DefaultConfig.getApp(type).getComponentAppid()))
        ;
    }

}

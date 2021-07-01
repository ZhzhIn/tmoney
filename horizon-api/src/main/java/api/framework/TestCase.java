package api.framework;

import io.restassured.response.Response;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;
import poexception.ApiNotFoundException;
import poexception.ConfigNotFoundException;
import poexception.TestCaseNeedToEditException;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static api.framework.Api.replaceVarsAndLoadConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static util.DefaultConfig.getStrFromDefaultConfig;

/**
 * 测试用例
 *
 * @author zhzh.yin
 * @create 2020/8/5
 */
@Data
@Slf4j
public class TestCase {
    /**
     * apiInfo : apiyamlName 和apiYamlAction的键值对
     */
    public String api;
    public Map<String, Object> vars;
    /**
     * - apis:
     * - apiName: latest
     * vars:
     * json: {}
     * extract:
     * paperId: ret.data.paperId
     * - apiName:  detail
     * vars:
     * json: ${paperId}
     * results:
     * describle:
     */
    public List<Map<String, Object>> apis;
    /**
     * results 期望结果，包含参数：expect,actual,matchers
     */
    public List<Map<String, Object>> results;
    public String describle;
    public String yamlName;

    public void run(Map<String, Api> map) {
        if (api != null && apis != null ||
                api == null && apis == null) {
            throw new TestCaseNeedToEditException("api和apis不能一起使用,也不能一起没有");
        } else if (api != null) {
            runApi(map);
        } else {
            runApis(map);
        }
    }

    /**
     * 按序执行apis
     *
     * @param map
     */
    public void runApis(Map<String, Api> map) {
        /**
         * 判断：map为空，apis为空，map中找不到apis的api名称时，抛异常
         */
        Map<String, Object> varsInApis = new HashMap<>();
        Set<String> keyset = new HashSet<>();
        if (apis == null || apis.size() == 0) {
            throw new ApiNotFoundException("apis列表写的有问题");
        } else if (map == null || map.size() == 0) {
            throw new ApiNotFoundException("map里面没有api");
        }
        for (Map<String, Object> api : apis) {
            keyset.add((String) api.get("api"));
        }
        if (!map.keySet().containsAll(keyset)) {
            throw new ApiNotFoundException("map中没有找到你给的api:" +
                    keyset.removeAll(map.keySet())
                    + ",map中的api有：" + map.keySet() + ",apis中的api有：" + keyset);
        }
        if (null == vars) {
            vars = new HashMap<>();
        }
        /**
         * 走到这里，map中的api名称，和apis中的api名称数据都是正常的
         * 执行前，接口传入接口加工过的varsInApis数据，然后和外部的vars一起装填
         * 这里有个遗漏，就是如果varsInApis和vars中的有重合，会再每个接口访问时都覆盖。
         * todo 可能有缺陷
         */
        for (int i = 0; i < apis.size(); i++) {
            Map<String, Object> apiAction = apis.get(i);
            String apiName = (String) apiAction.get("api");
            if (apiAction.keySet().contains("vars")
                    && apiAction.get("vars") != null
                    && apiAction.get("vars") instanceof Map) {
                Object apiVars = apiAction.get("vars");
                vars.putAll((Map<? extends String, ?>) replaceVarsAndLoadConfig(apiVars, varsInApis));
            }
            Api currentApi = map.get(apiName);
            currentApi.importVars(vars);
            Response res = currentApi.run();
            /**
             * 导出数据到apis内部的vars
             */
            if (apiAction.keySet().contains("extract")) {
                if (apiAction.get("extract") instanceof Map) {
                    Map<String, String> extract = (Map<String, String>) apiAction.get("extract");
                    for (Map.Entry<String, String> entry : extract.entrySet()) {
                        log.info("entry.getValue in res.body() is:" + res.body().path(entry.getValue()));
                        extract.replace(entry.getKey(), res.body().path(entry.getValue()));
                    }
                    vars.putAll((Map<? extends String, ?>) extract);
                } else {
                    throw new ConfigNotFoundException("用例的apis配置中的extract没有写成Map键值对");
                }
            } else {
                log.info("当前接口没有需要extract的接口");
            }
            /**
             * 验证结果
             */
            assertAll(
                    () -> {
                        results.forEach(
                                result -> {
                                    assertResult(result, res);
                                });
                    });
        }
    }

    /**
     * 在map中，寻找到apiName,并执行该接口
     *
     * @param map api名称和api对象的键值对
     */
    @Deprecated
    public void runApi(Map<String, Api> map) {
        if (map != null && map.containsKey(api)) {
            Api currentApi = map.get(api);
            currentApi.importVars(vars);
            Response res = currentApi.run();
            assertAll(
                    () -> {
                        results.forEach(
                                result -> {
                                    assertResult(result, res);
                                });
                    });
        } else {
            throw new ApiNotFoundException("没找到你说的api" + api);
        }
    }

    @Deprecated
    /**
     * 根据文件来执行，在执行内容很少时，效率比另一个要高
     * 缺点是yaml文件的名称是必传项
     */
    public void run(File file) {
        ApiListModel apiListModel = null;
        apiListModel = ApiListModel.load(file, yamlName);
        Api currentApi = apiListModel.get(this.api);
        currentApi.importVars(this.vars);
        Response res = currentApi.run();
        assertAll(
                () -> {
                    results.forEach(
                            result -> {
                                assertResult(result, res);
                            });
                });
    }

    /**
     * 查看返回的response，是否符合我们填写的result的期望值
     *
     * @param result 我们的验证值
     * @param res    返回的response
     */
    public void assertResult(Map<String, Object> result, Response res) {
        Object expect = result.get("expect");
        String matcher = (String) result.get("matcher");
        String path = (String) result.get("path");
        Class matchers = null;
        /**
         * 1.根据response的结果，提取定义的path作为actual，和传入的expect，根据matcher做断言判断，
         * 在run方法中直接完成结果断言。
         * 2.现在仅支持jsonPath断言方法，不支持path等。matcher默认使用equalTo，
         * 这里做了反射，支持传入其他的matcher断言方法，断言方法参考org.hamcrest.Matcher。
         * 3.实际结果和期望结果均为Object对象，如果yaml中传入“0”作为expect值，会识别为String对象而报错
         * ，该报错（好像？）暂未处理，可后续优化。
         * @param response 接口返回结果
         */
        try {
            matchers = Class.forName("org.hamcrest.Matchers");
            Method method = matchers.getMethod(matcher, Object.class);
            Object actual = (Object) res.jsonPath().get(path);
            log.info("path is " + path + ",actual is " + actual);
            if (expect instanceof String) {
                expect = getStrFromDefaultConfig((String) expect);
            }
            Matcher expectParam = (Matcher) method.invoke(matchers, expect);
            res.then().statusCode(200);
            assertThat(path + "数值不准确", actual, expectParam);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

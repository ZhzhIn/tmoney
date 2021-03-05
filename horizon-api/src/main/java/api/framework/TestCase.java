package api.framework;

import api.dto.TestCaseDTO;
import io.restassured.response.Response;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;

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
    public String yamlName;
    public String api;
    public HashMap<String, String> requestParam;
    /**
     * results 期望结果，包含参数：expect,actual,matchers
     */
    public List<TestCaseDTO> results;
    public String describle;

    /**
     * 测试用例里面有参数的时候，传入参数后获取返回。
     *
     * @param api
     * @return
     */
    private Api insertParam(Api api) {
        log.info("insertParam:" + api);
        HashMap<String, Object> newParam = new HashMap<>(16);
        if (requestParam != null) {
            requestParam.forEach(
                    (key, values) -> {
                        String value = getStrFromDefaultConfig(values);
                        log.info("存入key-value：" + key + "," + values);
                        newParam.put(key, value);
                    });
        }
        api.importParam(newParam);
        return api;
    }

    public void run() {
        ApiListModel apiListModel = null;
        apiListModel = ApiListModel.load(yamlName);
        Api currentApi = apiListModel.get(this.api);
        currentApi = insertParam(currentApi);
        Response res = currentApi.run();
        assertAll(
                () -> {
                    results.forEach(
                            testCaseDTO -> {
                                testCaseDTO.run(res);
                            });
                });
    }
}

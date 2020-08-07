package api.framework;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import util.HandelYaml;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * 测试用例模板
 *
 * @author zhzh.yin
 * @create 2020/8/5
 */
@Data
@Slf4j
public class ApiTestCaseModel {
    public String name;
    public String describle;
    public HashMap<String, String> param;
    public  List<TestCase> testCaseList;

    public static ApiTestCaseModel load(String yamlPath) {
        ApiTestCaseModel model = HandelYaml.getYamlConfig(yamlPath, ApiTestCaseModel.class);
        return model;
    }
    public static ApiTestCaseModel load(File file) {
        ApiTestCaseModel model = HandelYaml.getYamlConfig(file, ApiTestCaseModel.class);
        return model;
    }
    public void run() {
        testCaseList.stream().forEach(
                testCase -> {
                    if(null==testCase.yamlName
                    ||null==testCase.api
                    ||testCase.results.size()<=0){
                        log.error("testcase元素没写完整：需要填写action，api，result字段");
                    }
                    testCase.run();
                }
        );
    }
}

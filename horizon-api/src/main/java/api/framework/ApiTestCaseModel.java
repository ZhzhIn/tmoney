package api.framework;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import util.HandelYaml;

import java.io.File;
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
    public  List<TestCase> testCaseList;

    /**
     * @param yamlPath
     * @return
     */
    public static ApiTestCaseModel load(String yamlPath) {
        ApiTestCaseModel model = HandelYaml.getYamlConfig(yamlPath, ApiTestCaseModel.class);
        return model;
    }
    public static ApiTestCaseModel load(File file) {
        ApiTestCaseModel model = HandelYaml.getYamlConfig(file, ApiTestCaseModel.class);
        return model;
    }
}

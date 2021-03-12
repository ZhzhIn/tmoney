package api.framework;

import api.junit5.TimingExtension;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.util.ResourceUtils;
import poexception.ConfigNotFoundException;
import poexception.TestCaseNeedToEditException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;
import static util.HandleFile.getFileList;

@Execution(CONCURRENT)  //CONCURRENT表示支持多线程
@Slf4j
@Feature("早报")
@Owner("zhzh.yin")
@ExtendWith(TimingExtension.class)
public class TestCaseList {
    @BeforeAll
    static void setUp(){
        LoginHelper.login();
    }
    @ParameterizedTest(name = "接口：{0}-{index}")
    @MethodSource("apiTestCase")
    @Story("一大堆接口")
    public void runTestCase(TestCase testCase) {
        if (null == testCase.yamlName
                || null == testCase.api
                || testCase.results.size()<=0) {
            log.error("testcase元素没写完整：需要填写action，api，result字段");
        }
        testCase.run();
    }

    static List<TestCase> apiTestCase() {
        File file = new File(TestCaseList.class.getClassLoader().getResource("").getPath());
        List<TestCase> testcaseList = getTestCases(file);
        return testcaseList;
    }

    private static List<TestCase> getTestCases(File file) {
        File [] fileList = file.listFiles();
        if(fileList==null){ throw new ConfigNotFoundException();}
        ArrayList<File> testcaseFileList = new ArrayList<>( Arrays.asList(fileList));
        testcaseFileList=getFileList(testcaseFileList,".yaml","");
        List<ApiTestCaseModel> apitestcase = new ArrayList<>();
        List<TestCase> testcaseList = new ArrayList<>();
        testcaseFileList.forEach(
                yamlFile -> {
                    apitestcase.add(ApiTestCaseModel.load(yamlFile));
                }
        );
        apitestcase.forEach(
                apiTestCaseModel -> {
                    testcaseList.addAll(apiTestCaseModel.testCaseList);
                }
        );
        return testcaseList;
    }

    /**
     * 调试类
     */
//    @ParameterizedTest(name = "接口：{0}-{index}")
    @MethodSource("apiDebug")
    @Story("一大堆接口")
    public void debugTest(TestCase testCase) {
        if (null == testCase.yamlName
                || null == testCase.api
                || testCase.results.size()<=0) {
            log.error("testcase元素没写完整：需要填写action，api，result字段");
            throw new TestCaseNeedToEditException();
        }
        testCase.run();
    }

    static List<TestCase> apiDebug(){
        List<String> testcasePath = new ArrayList<>();
        testcasePath.add("src/test/resources/testcase/morning/morningdetailTestcase.yaml");
        List<ApiTestCaseModel> apitestcase = new ArrayList<>();
        List<TestCase> testcaseList = new ArrayList<>();
        testcasePath.forEach(
                path -> {
                    apitestcase.add(ApiTestCaseModel.load(path));
                }
        );
        apitestcase.forEach(
                apiTestCaseModel -> {
                    testcaseList.addAll(apiTestCaseModel.testCaseList);
                }
        );
        return testcaseList;
    }
}

package api.framework;

import api.junit5.TimingExtension;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import poexception.ConfigNotFoundException;
import poexception.TestCaseNeedToEditException;

import java.io.File;
import java.util.*;

import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;
import static util.HandleFile.getFileList;

@Execution(CONCURRENT)  //CONCURRENT表示支持多线程
@Slf4j
@Feature("早报")
@Owner("zhzh.yin")
@ExtendWith(TimingExtension.class)
public class TestCaseList {
    /**
     * TODO :
     * 1.har转yaml
     * 2.yapi转yaml
     */
    private Map<String,Api> map = new HashMap<>();
    {

        /**
         * api读取固定文件夹下的.yaml文件
         * map内容固定，加载完成之后每次取出来即可
         * 如果不想取全部的api存到map，可以修改Resource的路径，可以实现api只取对应文件的
         * 缺点：api的名称不能重名，因为testCase中的yamlName的设置项去掉了，去掉之后，执行时间4min~5min
         */
        File filepath = new File(this.getClass().getClassLoader().getResource("api").getPath());
        map = ApiListModel.load(filepath);
    }
    @BeforeAll
    static void setUp(){
        File file = new File(TestCaseList.class.getClassLoader().getResource("").getPath());
        Api.setGlobalCookies(file,"global","login");
    }
    /**
     * 用例读取testResource文件夹下，以testcase.yaml为后缀的文件
     * api读取resource/common/api固定文件夹下的.yaml文件
     */
    //@ParameterizedTest(name = "接口：{0}-{index}")
    @MethodSource("apiTestCase")
    @Story("一大堆接口")
    public void runTestCas2(TestCase testCase) {
        if (null == testCase.yamlName
                || null == testCase.api
                || testCase.results.size()<=0) {
            log.error("testcase元素没写完整：需要填写action，api，result字段");
        }
        testCase.run(map);
    }

    static List<TestCase> apiTestCase() {
        //加载 ApiTestCaseModel
        File file = new File(TestCaseList.class.getClassLoader().getResource("").getPath());
        File[] fileList = file.listFiles();
        if (fileList == null) {
            throw new ConfigNotFoundException();
        }
        ArrayList<File> testcaseFileList = new ArrayList<>(Arrays.asList(fileList));
        System.out.println("====="+testcaseFileList.size());
        testcaseFileList = getFileList(testcaseFileList, "testcase.yaml", "");

        log.info("====="+String.valueOf(testcaseFileList.size()));


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
        if ((null == testCase.api&& null==testCase.apis)
                || testCase.results.size()<=0) {
            log.error("testcase元素没写完整：需要填写action，api,apis，result字段");
            throw new TestCaseNeedToEditException();
        }
        File filePath = new File(this.getClass().getClassLoader().getResource("").getPath());
        log.info("+++++++++++++++++++++++"+filePath.getPath());
        testCase.run(map);
//        testCase.run(new File(this.getClass().getClassLoader().getResource("").getPath()));
//        testCase.transform();
    }

    static List<TestCase> apiDebug(){
        List<String> testcasePath = new ArrayList<>();
//        testcasePath.add("src\\test\\resources\\testcase\\common\\customer\\list1dynamicsTestcase.yaml");
//        testcasePath.add("src\\test\\resources\\testcase\\common\\customer\\listCustomerTagInGroupTestcase.yaml");
//        testcasePath.add("src\\test\\resources\\testcase\\common\\customer\\client_numTestcase.yaml");
//        testcasePath.add("src\\test\\resources\\common\\testcase\\auth\\getAuthInfoTestcase.yaml");
        testcasePath.add("src\\test\\resources\\common\\testcase\\customer\\list1shareTestcase.yaml");
        List<ApiTestCaseModel> apitestcase = new ArrayList<>();
        List<TestCase> testcaseList = new ArrayList<>();
        testcasePath.forEach(
                path -> {
                    if(ApiTestCaseModel.load(path)!=null){
                        apitestcase.add(ApiTestCaseModel.load(path));}
                }
        );
        apitestcase.forEach(
                apiTestCaseModel -> {
                    if(apiTestCaseModel!=null){
                        testcaseList.addAll(apiTestCaseModel.testCaseList);}
                }
        );
        return testcaseList;
    }
}

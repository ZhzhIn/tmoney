### 项目实现
- 构建工具： maven
- 接口调用： rest-assured
- 测试框架： junit5
- 测试用例加工： jacksonYaml,mustache
- 测试报告优化： allure

### 用例编写方式
- src/test/resources/api/ 编写对应的调用yaml文件。
- 示例：
   yaml格式：
``` yaml 
    name: 早报
    describle: 早报相关接口测试用例
    api:
        getDetail:
          method: get
          url: /caizhi_miniapi/api/applet/staffCard/detail
          requestParam:
            id: f09a04b775974f98bee9aaed8c492d24
          headers:
            Content-Type: text/plain
        filter:
          method: post
          url: "/caizhi_miniapi/api/applet/banner/filter_list.do"
        viewPaper:
          method: post
          url: /caizhi_miniapi/api/applet/morning/paper/view.do
          headers:
          Content-Type: application/json
          jsonFileName: viewPaper        
```
需要传输的参数，如果在src/main/resources/application.yaml中有，则传defaultConfig.xxxx（例如 defaultConfig.staffId).
如果希望用例不使用application.yaml中的配置，则不调用该方法，直接在json中设置，或者在调用时使用Api类的importParam(hashMap)方法。
- 如果接口中需要传输json，就在src/test/resource/json下传入，并在yaml的api参数中添加 jsonFileName字段。
``` java
{
  "id": "f09a04b775974f98bee9aaed8c492d24",
  "name": 
}
```
- 断言yaml
``` yaml
name: 早报接口
testCaseList:
  - yamlName: morningtest
    api: getDetail
    #requestParam:
     # staffId: defaultConfig.staffId
    results:
      - path: ret
        expect: "0"
        matcher: equalTo
  - yamlName: morningtest
    api: viewPaper
    requestParam:
      id: "f09a04b775974f98bee9aaed8c492d24"
    results:
      - path: ret
        expect: "0"
        matcher: equalTo
```
### testcase调试类
src/test/java/api.framework/TestCaseList
在apiDebug()中，将testcase的yaml地址修改成想要调试的testcase
然后执行debugTest方法。
``` java
/**
     * 调试类
     * @param testCase
     */
    @ParameterizedTest(name = "接口：{0}-{index}")
    @MethodSource("apiDebug")
    @Story("一大堆接口")
    public void debugTest(TestCase testCase) {
        if (null == testCase.yamlName
                || null == testCase.api
                || testCase.results.size()<=0) {
            log.error("testcase元素没写完整：需要填写action，api，result字段");
        }
        testCase.run();
    }
    static List<TestCase> apiDebug(){
        List<String> testcasePath = new ArrayList<>();
        testcasePath.add("src/test/resources/testcase/getAuthInfoTestcase.yaml");
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
```
### yaml配置编写规范
- 参数名和参数值之间需要用空格隔开
### 执行
``` shell
mvn clean test
allure serve target/allure-results
allure report
```
仅运行1个测试类
``` shell
mvn -Dtest=org.example.MyTest test
``` 
设置包含和排除
``` shell
<build>
    <plugins>
        ...
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.0.0-M5</version>
            <configuration>
                <excludes>
                    <exclude>some test to exclude here</exclude>
                </excludes>
            </configuration>
        </plugin>
    </plugins>
</build>
``` 
report地址： target/site/allure-maven-plugin/index.html
执行单个用例：mvn test -Dtest=MyTest
### 待完善
- 断言结合mybatis
- json支持传参

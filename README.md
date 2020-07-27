### 项目实现
- 构建工具： maven
- 接口调用： rest-assured
- 测试框架： junit5
- 测试用例加工： jacksonYaml,mustache
- 测试报告优化： allure

### 用例编写方式
- src/test/java/api/framework/xxx 定义业务调用 Example.java
- src/test/resources/apiyaml/xxx 编写对应的调用yaml,json文件。
- 示例：
   yaml格式：
``` yaml 
    name: 早报
    describle: 早报相关接口测试用例
    contents:
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
如果是get请求,需要传的参数在 application.yaml中配置过了，那就不用在请求中传递，在调用时使用Api类的importDefaultConfig()方法。
如果参数的app类型需要指定，则使用importDefaultConfig(AppType.xxx)方法。
如果希望用例不使用application.yaml中的配置，则不调用该方法，直接在json中设置，或者在调用时使用Api类的importParam(hashMap)方法。
- 如果接口中需要传输json，就在同yaml文件夹路径下传入对应的json文件，并在yaml的api参数中添加 jsonFileName字段。
``` java
{
  "id": "f09a04b775974f98bee9aaed8c492d24",
  "name": 
}
```
- .java断言，如果希望直接调用yaml文件中的设置，直接使用ApiModel类的runWithoutConfig(apiName)即可
如果希望调用时，对Api请求进行加工，则调用ApiModel.get(apiName)获得api之后，使用Api类的run()方法调用。
``` java
@Execution(CONCURRENT)  //CONCURRENT表示支持多线程
@Slf4j
@Feature("早报")
@Owner("zhzh.yin")
public class MorPaperTest {
    ApiModel model = ApiModel.load("src/test/resources/miniapi/paper/paper.yaml");

    @BeforeAll
    static void beforeAll() {
        LoginHelper.login(AppType.MINIPRO);
    }
    @Test
    @DisplayName("一个简单的断言")
    @Story("detail.do接口")
    void test1() {
        model.runWithoutConfig("getDetail")
                .then()
                .statusCode(200)
                .body("ret", equalTo(0));
//                .body("ret", hasItem(0))
//                .body("xx.xx", hasItems(1, 2));
    }
    @ParameterizedTest(name ="早报接口：{0}-{index}")
    @CsvSource({
            "getDetail,ret,0",
            "getDetail,ret,0"
    })
    @Story("csv测试detail.do接口")
    void testPaper(String apiName, String responsePath, Integer expectValue) throws InvocationTargetException, IllegalAccessException {
        assertTrue(model.runWithoutConfig(apiName)
                .path(responsePath).equals(expectValue));
    }
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
### 待完善
- 断言结合mybatis
- 代码优化： 异常加工，配置文件路径硬编码 
- json支持传参

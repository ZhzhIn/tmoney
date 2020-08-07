package api.dto;

import io.restassured.response.Response;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static util.DefaultConfig.getStrFromDefaultConfig;

@Data
@Slf4j
/**
 * 测试用例实体类，主要用于定义断言内容，和断言执行结果
 *
 * @author zhzh.yin
 * @create 2020/8/5
 */
public class TestCaseDTO {
    public Object expect ;
    public String matcher = "equalTo";
    public String path = "default1";

    /**
     * 1.根据response的结果，提取定义的path作为actual，和传入的expect，根据matcher做断言判断，
     * 在run方法中直接完成结果断言。
     * 2.现在仅支持jsonPath断言方法，不支持path等。matcher默认使用equalTo，
     * 这里做了反射，支持传入其他的matcher断言方法，断言方法参考org.hamcrest.Matcher。
     * 3.实际结果和期望结果均为Object对象，如果yaml中传入“0”作为expect值，会识别为String对象而报错
     * ，该报错（好像？）暂未处理，可后续优化。
     * @param response 接口返回结果
     */
    public void run(Response response) {
        Class matchers = null;
        try {
            matchers = Class.forName("org.hamcrest.Matchers");
            Method method = matchers.getMethod(matcher, Object.class);
            Object actual = (Object)response.jsonPath().get(path);
//            String actual = .toString();
            if(expect instanceof String){
                expect = getStrFromDefaultConfig((String)expect);
            }
            Matcher expectParam = (Matcher) method.invoke(matchers, expect);
            response.then().statusCode(200);
            assertThat(response.body().print(), actual, expectParam);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

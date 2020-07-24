package api.framework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import poexception.APINotFoundException;
import util.JSONTemplate;
import util.LoadDefaultConfig;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * tmoney
 * 2020/5/7 20:41
 * 解析yaml用例
 *
 * @author zhzh.yin
 **/
@Slf4j
@Data
public class ApiPO {

    public List<ApiContent> getContents(ApiModel ApiModel){
        return ApiModel.getContents();
    }
    public void run(ApiModel ApiModel) {

    }
    public ApiModel parse(String path ){
        return null;
    }
    public ApiModel load(String path) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ApiModel ApiModel = null;
        try {
            ApiModel = mapper.readValue(
//                    BasePage.class.getResourceAsStream(path),
                    new File(path),
                    ApiModel.class
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ApiModel;
    }

    public static Response parseApi(ApiContent apiContent) {
        handleJsonPath(apiContent);
        handleUrl(apiContent);
        try {
            return handleApi(apiContent);
        } catch (APINotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static RequestSpecBuilder builder;

    /**
     * 处理host
     */
    private static ApiContent handleUrl(ApiContent apiContent) {
        String host = LoadDefaultConfig.getHost();
        log.info("当前环境配置的host是："+host);
        String apiHost = apiContent.getUrl();
        if (apiHost != null) {
            String url = host + apiHost;
            apiContent.setUrl(url);
            log.info("当前api的url是："+url);
        }
        return apiContent;
    }

    /**
     * 处理jsonPath
     */
    //todo jsonPath 路径动态获取
    private static ApiContent handleJsonPath(ApiContent api) {
        String jsonPath = api.getJsonPath();
        if (jsonPath != null) {
            jsonPath = "src/test/resources/apiyaml/" + jsonPath + ".json";
//        jsonPath = yamlpath.replace("///.*?/.yaml",jsonPath)
//        .replace(".yaml",".json");
            api.setJsonPath(jsonPath);
        }
        return api;
    }


    /**
     * 解析单个api
     *
     * @param api
     * @return
     * @throws APINotFoundException
     */
    private static Response handleApi(ApiContent api) throws APINotFoundException {
        String name = api.getName();
        String url = api.getUrl();
        String method = api.getMethod();
        HashMap<String, Object> headers = api.getHeaders();
        String jsonPath = api.getJsonPath();
        HashMap requestParam = api.getRequestParam();

        if (method == "" || method.equals(null)) {
            throw new APINotFoundException("没有写入method");
        }

        //todo 参数枚举化
        RequestSpecification request = given();
        log.info("查看builder有没有值");
        if (builder != null) {
            log.info("the builder is " + builder.toString());
            RequestSpecification requestSpec = builder.build();
            request = request.spec(requestSpec);
        }
        if (requestParam != null) {
            request = request.params(requestParam);
            log.info("配置param:" + requestParam);
        }
        if (headers != null) {
            request = request.headers(api.getHeaders());
            log.info("配置header:" + headers);
        }
        if (jsonPath != null) {
            log.info("jsonPath is " + jsonPath);
            request = request
                    .contentType(ContentType.JSON)
                    .body(JSONTemplate.template(jsonPath));
        }
        if (method.equals("get")) {
            Response response = request.when()
                    .log().all()
                    .get(url)
                    .then()
                    .log().all()
                    .extract()
                    .response();
            if (null == builder) {
                builder = new RequestSpecBuilder();
                log.info(response.getCookies().keySet().toString());
                builder.addCookies(response.getCookies());
            }
            return response;
        } else if (method.equals("post")) {
            Response response = request.when()
                    .log().all()
                    .post(url)
                    .then()
                    .log().all()
                    .extract()
                    .response();
            return response;
        } else {
            throw new APINotFoundException("解析失败");
        }
    }
    public List<ApiContent> getContent(ApiModel ApiModel) {
        return ApiModel.contents;
    }

}

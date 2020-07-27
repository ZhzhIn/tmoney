package api.framework;

import api.item.ManuData;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import poexception.APINotFoundException;
import poexception.InsertValueNotInitException;
import util.JSONTemplate;
import util.LoadDefaultConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

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

public class ApiHelper {
    /**
     * 向api中传入数据
     *
     * @param api
     * @param map
     * @return
     */
    public  synchronized ApiContent  handleMap(ApiContent api, HashMap<ManuData, HashMap<String, String>> map) throws InsertValueNotInitException {
        log.info("handleMap:"+map);
        if (map != null) {
            if (map.containsKey(ManuData.REQUEST_PARAM)) {
                log.info("传参中有request_param");
                HashMap<String, String> newMap = map.get(ManuData.REQUEST_PARAM);
                if (api.getRequestParam() != null) {
                    HashMap<String, String> oldMap = api.getRequestParam();
                    oldMap.putAll(newMap);
                    //新的数据会覆盖旧的
                    api.setRequestParam(oldMap);
                } else {
                    api.setRequestParam(newMap);
                }

            }
            if (map.containsKey(ManuData.JSON_PARAM)) {
                log.info("传参中有request_param");
                HashMap<String, String> newMap = map.get(ManuData.JSON_PARAM);
                if (api.getJsonParam() != null) {
                    HashMap<String, String> oldMap = api.getJsonParam();
                    oldMap.putAll(newMap);
                    //新的数据会覆盖旧的
                    api.setJsonParam(oldMap);
                } else {
                    api.setJsonParam(newMap);
                }
            }
            if (map.containsKey(ManuData.JSON_FILE_NAME)) {
                log.info("传参中有jsonFileName");
                HashMap<String, String> jsonFileNameMap = map.get(ManuData.JSON_FILE_NAME);
                log.info("jsonFileMap is "+jsonFileNameMap);
                //map中设置key=jsonFileName
                String jsonFileName = jsonFileNameMap.get(ManuData.JSON_FILE_NAME.getType());
                //直接替换旧的
                api.setJsonFileName(jsonFileName);
            } else {
                throw new InsertValueNotInitException("测试map的数据不对");
            }
        }
        return null;
    }

    public HashMap<String, ApiContent> getContents(ApiModel ApiModel) {
        return ApiModel.getContents();
    }

    public ApiModel load(String path) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ApiModel ApiModel = null;
        try {
            ApiModel = mapper.readValue(
                    new File(path),
                    ApiModel.class
            );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ApiModel;
    }

    public  synchronized Response run(ApiContent apiContent, HashMap map) {
        if (map != null) {
            try {
                handleMap(apiContent, map);
            } catch (InsertValueNotInitException e) {
                e.printStackTrace();
            }
        } else {
            log.warn("当前传入map值为空");
        }

        return run(apiContent);
    }

    public  synchronized Response run(ApiContent apiContent) {
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
    private  synchronized ApiContent handleUrl(ApiContent apiContent) {
        String host = LoadDefaultConfig.getHost();
        log.info("当前环境配置的host是：" + host);
//        String apiHost = apiContent.getUrl();
        if (apiContent.getUrl() != null) {
            String apiHost = apiContent.getUrl();
            log.info("before handle ,the url is :"+apiHost);
            String url = host + apiHost;
            apiContent.setUrl(url);
            log.info("after handle,当前api的url是：" + url);
        }
        return apiContent;
    }

    /**
     * 处理jsonPath
     */
    //todo jsonPath 路径动态获取
    private  synchronized ApiContent handleJsonPath(ApiContent api) {
        String jsonPath = api.getJsonFileName();
        log.info("before handle ,the jsonPath is :"+jsonPath);
        if (jsonPath != null&&jsonPath!="") {
                jsonPath = "src/test/resources/apiyaml/" + jsonPath + ".json";
                log.info("after handle ,the jsonPath is :"+jsonPath);
//        jsonPath = yamlpath.replace("///.*?/.yaml",jsonPath)
//        .replace(".yaml",".json");
                api.setJsonFileName(jsonPath);
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
    private  synchronized Response handleApi(ApiContent api) throws APINotFoundException {
        String url = api.getUrl();
        String method = api.getMethod();
        HashMap<String, Object> headers = api.getHeaders();
        String jsonPath = api.getJsonFileName();
        HashMap requestParam = api.getRequestParam();
        HashMap jsonParam = api.getJsonParam();
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
            if (!jsonPath.equals("")) {
                log.info("jsonPath is " + jsonPath);
                request = request
                        .contentType(ContentType.JSON)
                        .body(JSONTemplate.template(jsonPath));
            }
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

}

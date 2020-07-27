package api.framework;

import api.item.AppType;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import poexception.ApiNotFoundException;
import util.JsonTemplate;
import util.LoadDefaultConfig;

import java.util.HashMap;

import static api.item.Manu.JSON_FILE_NAME;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

/**
 * tmoney
 * 2020/5/8 17:00
 *
 * @author zhzh.yin
 **/
@Data
@Slf4j
public class Api {
    public String url;
    public String method;
    public HashMap<String, String> headers;
    public ContentType contentType;
    public String connection;
    public String jsonFileName;
    public HashMap<String, String> requestParam;
    private int flag = 0;
    private String jsonFilePath;

    public void login() {
        log.info("登录对应类型");
    }

    /**
     * 导入默认配置
     */
    public synchronized Api importDefaultConfig() {
        log.info("做一些yaml配置的参数导入操作");
        HashMap<String, String> configMap = this.getRequestParam();
        HashMap<String, String> defaultMap = LoadDefaultConfig.getDefaultConfig();
        if (null == configMap) {
            configMap = new HashMap(16);
        }

        defaultMap.keySet().removeAll(configMap.keySet());
        configMap.putAll(defaultMap);
        this.setRequestParam(configMap);
        return this;
    }

    public synchronized Api importDefaultConfig(AppType type) {
        log.info("做一些yaml配置的参数导入操作");
        HashMap<String, String> configMap = this.getRequestParam();
        HashMap<String, String> defaultMap = LoadDefaultConfig.getDefaultConfig(type);
        if (null == configMap) {
            configMap = new HashMap(16);
        }

        defaultMap.keySet().removeAll(configMap.keySet());
        configMap.putAll(defaultMap);
        this.setRequestParam(configMap);
        return this;
    }


    /**
     * 向api中传入数据
     *
     * @param newMap
     * @return
     */
    public synchronized Api importParam(HashMap<String, String> newMap) {
        log.info("handleMap:" + newMap);
        if (newMap != null) {
            log.info("传参中有request_param");
            if (this.getRequestParam() != null) {
                HashMap<String, String> oldMap = this.getRequestParam();
                oldMap.putAll(newMap);
                //新的数据会覆盖旧的
                this.setRequestParam(oldMap);
                log.info("当前 map参数为：" + oldMap);
            } else {
                log.info("当前 map参数为：" + newMap);
                this.setRequestParam(newMap);
            }
            if (newMap.containsKey(JSON_FILE_NAME)) {
                log.info("jsonFileName is " + newMap.get(JSON_FILE_NAME));
                this.setJsonFileName(newMap.get(JSON_FILE_NAME));
            } else {
                log.error("传入的map数据不正确，不加工");
            }
        } else {
            log.warn("没有传入map值");
        }
        return this;
    }

    public synchronized Response run() {
        setApiBody();
        return sendRequest();
    }

    private static RequestSpecBuilder builder;

    /**
     * 处理host
     */
    private synchronized Api handleUrl() {
        String host = LoadDefaultConfig.getHost();
        log.info("当前环境配置的host是：" + host);
        if (getUrl() != null && flag == 0) {
            String apiHost = getUrl();
            log.info("before handle ,the url is :" + apiHost);
            String url = host + apiHost;
            setUrl(url);
            log.info("after handle,当前api的url是：" + url);
            flag = 1;
        }
        return this;
    }

    private RequestSpecification setApiBody() {
        handleUrl();
        log.info(this.toString());
        String method = this.getMethod();
        HashMap headers = this.getHeaders();
        String jsonFileName = this.getJsonFileName();
        String jsonPath = this.getJsonFilePath();
        if (method == "" || method.equals(null)) {
            log.error("没有写入method");
        }
        //todo 参数枚举化
        RequestSpecification request = given();
        log.info("查看builder有没有值");
        if (builder != null) {
            log.info("the builder is " + builder.toString());
            RequestSpecification requestSpec = builder.build();
            request = request.spec(requestSpec);
        }
        if (headers != null) {
            request = request.headers(this.getHeaders());
            log.info("配置header:" + headers);
        }
        if (jsonFileName != null) {
            log.info("jsonFileName is " + jsonFileName);
            request = request
                    .contentType(JSON)
                    .body(JsonTemplate.template(jsonPath + jsonFileName + ".json"));
        }
        request = request.when()
                .log().all();
        return request;
    }

    /**
     * 发送api请求
     *
     * @return
     * @throws ApiNotFoundException
     */
    private synchronized Response sendRequest() {
        RequestSpecification request = setApiBody();
        if (method.toUpperCase().equals(Method.GET.toString())) {
            if(requestParam!=null){request = request.params(requestParam);}
            Response response = request.get(url)
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
        } else if (method.toUpperCase().equals(Method.POST.toString())) {
            if (headers != null
                    && headers.containsKey("Content-Type")
                    && headers.get("Content-Type").toLowerCase().equals(JSON.toString())
                    && requestParam!=null) {
                request = request.contentType(JSON);
                request = request.body(requestParam);
                log.info("配置POST param:" + requestParam);
            }
            Response response = request.post(url)
                    .then()
                    .log().all()
                    .extract()
                    .response();
            return response;
        } else {
            log.error("解析失败");
            return null;
        }
    }

    @Override
    public String toString() {
        return "ApiContent{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", headers=" + headers +
                ", connection='" + connection + '\'' +
                ", jsonPath='" + jsonFileName + '\'' +
                ", requestParam=" + requestParam +
                '}';
    }
}

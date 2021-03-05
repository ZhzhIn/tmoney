package api.framework;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import poexception.ApiNotFoundException;
import util.DefaultConfig;
import util.JsonTemplate;

import java.util.HashMap;

import static api.item.Manu.JSON_FILE_NAME;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.*;
import static io.restassured.http.Method.GET;
import static io.restassured.http.Method.POST;
import static util.DefaultConfig.getStrFromDefaultConfig;

/**
 * 接口定义，支持传入url,method，headers等等接口数据。
 * 内容都在yaml中填入。
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
    public String connection;
    public String jsonFileName;
    public HashMap<String, Object> requestParam;
    private int flag = 0;
    public String describle;

    public void login() {
        log.info("登录对应类型");
    }


    /**
     * 向api中传入数据，根据yaml的requestParam来传递参数，且value中包含“defaultConfig"时，
     * 读取默认的配置，配置来自DefaultConfig。
     *
     * @param newMap
     * @return
     */
    public synchronized Api importParam(HashMap<String, Object> newMap) {
        log.info("importParam:" + newMap);
        if (newMap != null) {
            log.info("传参中有param");
            if (this.getRequestParam() != null) {
                loadParamFromDefaultConfig();
                log.info("原来得requestParam:" + this.requestParam.keySet());
                HashMap<String, Object> oldMap = this.getRequestParam();
                oldMap.putAll(newMap);
                //新的数据会覆盖旧的
                log.info("最终得requestParam:" + this.requestParam.keySet());
                this.setRequestParam(oldMap);
            } else {
                log.info("当前 map参数为：" + newMap);
                this.setRequestParam(newMap);
            }
            if (newMap.containsKey(JSON_FILE_NAME)) {
                log.info("jsonFileName is " + newMap.get(JSON_FILE_NAME));
                this.setJsonFileName(newMap.get(JSON_FILE_NAME).toString());
            } else {
                log.warn("没有传入jsonFileName,不用加工");
            }
        } else {
            log.warn("没有传入map值");
        }
        return this;
    }

    /**
     * 导入DefaultConfig的配置
     * @return
     */
    public synchronized Api loadParamFromDefaultConfig() {
        requestParam.forEach(
                (key, values) -> {
                    if(values!=null){
                        String value = getStrFromDefaultConfig(values.toString());
                        log.info("存入key-value：" + key + "," + value);
                        requestParam.put(key, value);
                    }});
        return this;
    }
    /**
     * 执行api,并返回结果。
     *
     * @return
     */
    public synchronized Response run() {
        setApiBody();
        return sendRequest();
    }

    private static RequestSpecBuilder builder;

    /**
     * 处理host，读取默认配置。
     */
    private synchronized Api handleUrl() {
        String host = DefaultConfig.getHost();
        log.info("当前环境配置的host是：" + host);
        if (getUrl() != null && flag == 0) {
            String apiHost = getUrl();
            log.info("before handle ,the url is :" + apiHost);
            String url = String.format("%s%s",host , apiHost);
            setUrl(url);
            log.info("after handle,当前api的url是：" + url);
            flag = 1;
        }
        return this;
    }

    /**
     * 读取api类，并调用restAssured装配成http请求
     * RequestSpecification 用于传递cookie值，所有api共享cookie
     *
     * @return
     */
    public RequestSpecification setApiBody() {
        handleUrl();
        log.info(this.toString());
        String method = this.getMethod();
        HashMap headers = this.getHeaders();
        String jsonFileName = this.getJsonFileName();
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

        request = request
                .log().all();
        return request;
    }

    /**
     * 发送api请求，根据json相关参数配置与否，选择是否post json.
     *
     * @return
     * @throws ApiNotFoundException
     */
    private synchronized Response sendRequest() {
        log.info("发送请求");
        RequestSpecification request = setApiBody();
        if (GET.name().equalsIgnoreCase(method)) {
            log.info("执行get方法");
            if (requestParam != null) {
                request = request.params(requestParam);
            }
            Response response = request
                    .when()
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
        } else if (POST.name().equalsIgnoreCase(method)) {
            log.info("执行post方法");
            if (headers != null){
                request = request.headers(headers);
                if(headers.containsKey("Content-Type")||headers.containsKey("content-type")){
                    ContentType contentType = fromContentType(headers.get("Content-Type"));
                    log.info("contentType is " + contentType);
                    if (JSON.matches(contentType.toString())
                            && requestParam != null) {
                        log.info("填充param作为json");
                        request.request().contentType(JSON);
                        log.info("用jsonFileName填充");
                        request = request
                                .contentType(JSON)
                                .body(requestParam);
                    } else if(URLENC.matches(contentType.toString())) {
                        request = request
                                .formParams(requestParam);
                    }else{
                        request = request.formParams(requestParam);
                    }
                }
            }else{
                request = request.body(requestParam);
            }
            if (jsonFileName != null) {
                log.info("用jsonFileName填充");
                request = request
                        .contentType(JSON)
                        .body(JsonTemplate.template(jsonFileName));
            }
            Response response = request.when().post(url)
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

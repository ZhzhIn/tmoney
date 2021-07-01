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

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    /**
     * 用法
     * vars:
     *  paramA: abc
     *  json:
     *      xxx:a
     *      yyy:b
     * requestParam:
     * paramA: ${paramA}
     * json: ${json}
     */
    public Map<String, Object> vars;
    public Map<String, Object> headers;
    public String connection;
    public String jsonFileName;
    /**
     * TODO ?
     * 去掉jsonFileName字段，变成json的参数
     * <p>
     * 这样会不会有点多余？
     * json:
     * jsonFileName: xxx/xxx/xxx
     * <p>
     * json:
     * ${json}
     * jsonFileName: xxx/xxx/xxx
     */
    public Object json;
    public Object requestParam;
    public String describle;
    private static RequestSpecBuilder builder = new RequestSpecBuilder();
    private static final String VAR_PREFIX = "${";
    private static final String VAR_SUFFIX = "}";
    private static final String CONTENT_TYPE = "Content-Type";

    /**
     * 装载所有外部传入数据
     *
     * 加工所有引用参数
     */

    /**
     * 全局参数设置，用于登录权限等的验证
     * 在所有api请求之前，都加上gloabel的返回结果
     *
     * @return
     */
    public synchronized static void setGlobalCookies(File file, String yamlName, String apiName) {
        //加载global的api文件
        //存储该response的cookie
        Map cookies = ApiListModel.load(file, yamlName).get(apiName).run().getCookies();
        for (Object entry : cookies.entrySet()) {
            log.info(entry.toString());
        }
        builder = builder.addCookies(cookies);
    }

    /**
     * 装载外部传入的变量
     *
     * @param varsFromOutside
     * @return
     */
    public synchronized Api importVars(Map<String, Object> varsFromOutside) {
        if (varsFromOutside != null) {
            log.info("传递外界参数到接口变量中");
            if (this.vars != null) {
                Map<String, Object> oldVars = this.vars;
                oldVars.putAll(varsFromOutside);
                //新的数据会覆盖旧的
                log.info("最终的vars:" + this.vars.keySet());
                this.setVars(oldVars);
            } else {
                log.info("当前 vars没有值，直接使用外部传入的vars：" + varsFromOutside);
                this.setVars(varsFromOutside);
            }
        } else {
            log.warn("外界传入的vars是空的");
        }
        return this;
    }

    /**
     * 加工数据：传入vars变量，替换默认配置项
     * 如果是字符串，直接加工后返回：有${}的从vars中取，没有的从Deafult中取
     * 如果是集合,遍历value，value是字符串，继续加工，不是字符串，继续遍历
     *
     * @param vars   变量存储集合
     * @param object 需要加工的数据
     * @return 加工后的数据
     */
    public static <T> Object replaceVarsAndLoadConfig(T object, Map<String, Object> vars) {
        if (object == null) {
            return object;
        }
        if (object instanceof String) {
            String objStr = (String) object;
            //走变量逻辑
            //提取变量，如果vars有变量，走变量逻辑
            //如果vars没有该变量，不处理，直接返回原来得数据，并error提示
            //如果有${}，处理后处理剩余得参数
            while (objStr.contains(VAR_PREFIX)
                    && objStr.contains(VAR_SUFFIX)
                    && !objStr.endsWith(VAR_PREFIX)
                    && !objStr.startsWith(VAR_SUFFIX)) {
                String varName = objStr.split("\\$\\{")[1].split(VAR_SUFFIX)[0];
                //单个${}得逻辑
                if (objStr.length() == varName.length() + 3) {
                    if (vars != null && vars.size() > 0 && vars.containsKey(varName)) {
                        return replaceVarsAndLoadConfig(vars.get(varName), vars);
                    } else {
                        return object;
                    }
                } else {
                    //可能有多个aaa${}bbb${}ccc，或为 xxx${}yyy
                    //用字符串的方式来处理
                    String returnStr = "";
                    vars.forEach(
                            (k, v) -> {
                                if (varName.equals(k)) {
                                    ((String) object).replace(VAR_PREFIX + k + VAR_SUFFIX, (String) v);
                                }

                            });
                    objStr = objStr.replace(VAR_PREFIX + varName + VAR_SUFFIX, "");
                }
            }
            return getStrFromDefaultConfig((String) object);
        } else if (object instanceof Map) {
            ((Map) object).forEach(
                    (k, v) -> {
                        ((Map) object).replace(k, replaceVarsAndLoadConfig(v, vars));
                    }
            );
            return object;
        } else if (object instanceof List) {
            for (int i = 0; i < ((List) object).size(); i++) {
                ((List) object).set(i, replaceVarsAndLoadConfig(((List) object).get(i), vars));
            }
            return object;
        } else if (object instanceof Set) {
            ((Set) object).forEach(
                    v -> {
                        Object v1 = replaceVarsAndLoadConfig(v, vars);
                        ((Set) object).remove(v);
                        ((Set) object).add(v1);
                    }
            );
            return object;
        } else {
            //没有变量值，不是集合也不是String和基本类型，不处理
            return object;
        }
    }

    /**
     * 执行api,并返回结果。
     *
     * @return
     */
    public synchronized Response run() {
        RequestSpecification req = parseRequest();
        return sendRequest(req);
    }


    /**
     * 处理host，读取默认配置。
     */
    private synchronized Api handleUrl() {
        String host = DefaultConfig.getHost();
        if (getUrl() != null) {
            setUrl(String.format("%s%s", host, getUrl()));
        }
        return this;
    }

    /**
     * 读取api类，并调用restAssured装配成http请求
     * RequestSpecification 用于传递cookie值，所有api共享cookie
     *
     * @return
     */
    public RequestSpecification parseRequest() {
        handleUrl();
        this.url = (String) replaceVarsAndLoadConfig(getUrl(), vars);
        log.info("after replace ,url is " + url);
        requestParam = replaceVarsAndLoadConfig(requestParam, vars);
        if (json != null) {
            json = replaceVarsAndLoadConfig(json, vars);
        }
        RequestSpecification request = given();
        if (method == "" || method.equals(null)) {
            log.error("没有写入method");
            throw new ApiNotFoundException("method 没有写");
        } else if (method.equalsIgnoreCase(GET.name())) {
            if (requestParam != null) {
                request = request
                        .queryParams((Map<String, ?>) requestParam);
            }
        }
        if (builder != null) {
            log.info("装填全局builder");
            request = request.spec(builder.build());
        }
        if (headers != null) {
            request = request.headers((Map<String, Object>) replaceVarsAndLoadConfig(headers, vars));

            if (headers.containsKey(CONTENT_TYPE)) {
                ContentType contentType = fromContentType((String) headers.get(CONTENT_TYPE));
                log.info("contentType is " + contentType);
                if (JSON.matches(contentType.toString())) {
                    request = request
                            .contentType(JSON);
                    //jsonFilePath不为空，传jsonFilePath
                    if (jsonFileName != null && jsonFileName != "") {
                        log.info("用jsonFileName填充" + jsonFileName);
                        request = request
                                .body(JsonTemplate.template(jsonFileName));
                    }
                    if (json != null) {
                        log.info("json is :" + json);
                        request = request
                                .body(json);
                    } else {
                        request = request
                                .body("{}");
                    }
                } else if (URLENC.matches(contentType.toString())) {
                    request = request
                            .contentType(URLENC);
                    if (requestParam != null) {
                        request = request
                                .queryParams((Map<String, ?>) requestParam);
                    }
                }
            }
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
    private synchronized Response sendRequest(RequestSpecification request) {
        log.info("发送请求");
        if (GET.name().equalsIgnoreCase(method)) {
            log.info("执行get方法");

            return request
                    .when()
                    .get(url)
                    .then()
                    .log().all()
                    .extract()
                    .response();
        }
        if (POST.name().equalsIgnoreCase(method)) {
            log.info("执行post方法");
            if (requestParam != null) {
                request = request
                        .formParams((Map<String, ?>) requestParam);
            }
            return request
                    .when().post(url)
                    .then()
                    .log().all()
                    .extract()
                    .response();
        }
        log.error("解析失败");
        throw new ApiNotFoundException("解析api失败，method方法不正确");
    }

    @Override
    public String toString() {
        return "ApiContent{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", headers=" + headers +
                ", connection='" + connection + '\'' +
                ", jsonFileName='" + jsonFileName + '\'' +
                ", requestParam=" + requestParam +
                '}';
    }
}

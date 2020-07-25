package api.framework;

import lombok.Data;

import java.util.HashMap;

/**
 * tmoney
 * 2020/5/8 17:00
 *
 * @author zhzh.yin
 **/
@Data
public class ApiContent {
    public String url;
    public String method;
    public HashMap<String, Object> headers;
    public String connection;
    public String jsonFileName;
    public HashMap<String, String> requestParam;
    public HashMap<String,String> jsonParam;

    public HashMap<String, String> getJsonParam() {
        return jsonParam;
    }

    public void setJsonParam(HashMap<String, String> jsonParam) {
        this.jsonParam = jsonParam;
    }

    public HashMap<String, String> getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(HashMap<String, String> requestParam) {
        this.requestParam = requestParam;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public HashMap<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, Object> headers) {
        this.headers = headers;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getJsonFileName() {
        return jsonFileName;
    }

    public void setJsonFileName(String jsonFileName) {
        this.jsonFileName = jsonFileName;
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

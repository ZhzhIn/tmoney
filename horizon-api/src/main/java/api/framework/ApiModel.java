package api.framework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.restassured.response.Response;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Data
@Slf4j
/**
 * tmoney
 * 2020/7/25 18:57
 * 手动传入的数据类型
 *
 * @author zhzh.yin
 **/
public class ApiModel {
    public String name;
    public String describle;
    public HashMap<String, Api> contents;

    public Api get(String apiName){
        return contents.get(apiName);
    }
    
    public Response runWithoutConfig(String apiName){
        if (contents.get(apiName) != null) {
            return contents.get(apiName).run();
        }
        return null;
    }


    public static ApiModel load(String yamlPath) {

        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        try {
            ApiModel model = objectMapper.readValue(new File(yamlPath), ApiModel.class);
            if (model.getContents().keySet().size() > 0) {
                for (String s : model.getContents().keySet()) {
                    String string = yamlPath.replace(yamlPath.split("/")[yamlPath.split("/").length - 1], "");
                    log.error("jsonFilePath is :" + string);
                    Api api = model.getContents().get(s);
                    log.info("当前api是：" + api);
                    if (api != null) {
                        api.setJsonFilePath(string);
                    }
                }
            }
            return model;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("yaml转换失败");
        }
        log.error("yaml转换失败");
        return null;
    }
}

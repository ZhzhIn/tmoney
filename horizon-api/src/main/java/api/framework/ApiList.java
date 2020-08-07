package api.framework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.restassured.response.Response;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import poexception.ApiNotFoundException;
import util.HandleFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
public class ApiList {
    public String name;
    public String describle;
    public HashMap<String, Api> api;

    public Api get(String apiName){
        if(api.get(apiName)==null){
            throw new ApiNotFoundException("在对应的yaml文件里面没找到你说的apiName:"+apiName);
        }

        return api.get(apiName);
    }

    /**
     * 以前class测试类的时候用的。现在没搞了
     * @param apiName
     * @return 接口返回值
     */
    @Deprecated
    public Response runWithoutConfig(String apiName){
        if (api.get(apiName) != null) {
            return api.get(apiName).run();
        }
        throw new ApiNotFoundException();
    }

    /**
     * 根据yaml名称来识别接口列表
     * @param yamlName
     * @return
     * @throws ApiNotFoundException
     */
    public static ApiList load(String yamlName) throws ApiNotFoundException {
        File file = new File("src/test/resources/api/");
        ArrayList<File> files = HandleFile.getFileList(Arrays.asList(file.listFiles()),".yaml",yamlName);
        if(null==files||files.size()<=0){
            log.error("没找到你说的yaml:"+yamlName);
            throw new ApiNotFoundException("没找到你说的yaml:"+yamlName);
        }
        file = files.get(0);
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        ApiList model = null;
        try {
            model = objectMapper.readValue(file, ApiList.class);
            if (null==model){
                log.error("yaml转换失败");
                throw new ApiNotFoundException("yaml转换失败，看下文件："+file.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return model;
    }

    /**
     * 手动书写class类的时候用的，现在只有LoginHelper类好像有用。
     * @param clazz
     * @return
     */
    @Deprecated
    public static ApiList load(Class clazz) {
        String yamlPath ="src/test/java/" + clazz.getCanonicalName()
                .toLowerCase()
                .replace(".", "/")
                + ".yaml";
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        ApiList model = null;
        try {
            model = objectMapper.readValue(new File(yamlPath), ApiList.class);
            if (null==model){
                log.error("yaml转换失败");
                throw new ApiNotFoundException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return model;
    }
}

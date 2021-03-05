package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

/**
 * @ClassName: ReadYAML
 * @Description: read yaml
 * @Author: zhzh.yin
 * @Date: 2020-04-23 21:43
 * @Verion: 1.0
 */
@Slf4j
public class HandelYaml{
    public static <T> T getYamlConfig(String filePath, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        try {
            T t =mapper.readValue(
                    new File(filePath),
                    clazz);
            return t;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <T> T getYamlConfig(File file, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        try {
            T t =mapper.readValue(
                    file,
                    clazz);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <T> void transObjToYaml(File file,T t){
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        try {
            mapper.writeValue(file, t);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

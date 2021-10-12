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
import java.util.*;

@Data
@Slf4j
/**
 * tmoney
 * 2020/7/25 18:57
 * 手动传入的数据类型
 *
 * @author zhzh.yin
 **/
public class ApiListModel {
    public String name = "";
    public String describle = "";
    public Map<String, Api> api;
    //    private static String currentApiPath;
    public Api get(String apiName) {
        if (api.get(apiName) == null) {
            throw new ApiNotFoundException("在对应的yaml文件里面没找到你说的apiName:" + apiName);
        }

        return api.get(apiName);
    }
    /*public String currentApiPath(){
        return currentApiPath;
    }*/

    /**
     * 以前class测试类的时候用的。现在没搞了
     *
     * @param apiName
     * @return 接口返回值
     */
    @Deprecated
    public Response runWithoutConfig(String apiName) {
        if (api.get(apiName) != null) {
            return api.get(apiName).run();
        }
        throw new ApiNotFoundException();
    }

    @Deprecated
    /**
     * 根据yaml名称来识别接口列表
     * @param yamlName
     * @return
     * @throws ApiNotFoundException
     */
    public static ApiListModel load(String yamlName) throws ApiNotFoundException {
        File file = new File("src/test/resources/api/");
        ArrayList<File> files = HandleFile.getFileList(Arrays.asList(file.listFiles()), ".yaml", yamlName);
        if (null == files || files.size() <= 0) {
            log.error("没找到你说的yaml:" + yamlName);
            throw new ApiNotFoundException("没找到你说的yaml:" + yamlName);
        }
        file = files.get(0);
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        ApiListModel model = null;
        try {
            model = objectMapper.readValue(file, ApiListModel.class);
            if (null == model) {
                log.error("yaml转换失败");
                throw new ApiNotFoundException("yaml转换失败，看下文件：" + file.toString());
            }
/**
 * currentApiPath = file.getPath();
 */

        } catch (IOException e) {
            e.printStackTrace();
        }
        return model;
    }

    /**
     * 去特定的文件夹下，加载yamlName
     *
     * @param yamlName
     * @return
     * @throws ApiNotFoundException
     */
    public static ApiListModel load(File file, String yamlName) throws ApiNotFoundException {
        if (file.isDirectory()) {
            ArrayList<File> files = HandleFile.getFileList(Arrays.asList(file.listFiles()), ".yaml", yamlName);
            if (null == files || files.size() <= 0) {
                log.error("没找到你说的yaml:" + yamlName);
                throw new ApiNotFoundException("没找到你说的yaml:" + yamlName);
            }
            file = files.get(0);
        }
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        ApiListModel model = null;
        try {
/**            log.info("xxxxx"+file.getPath());
 *
 */
            model = objectMapper.readValue(file, ApiListModel.class);
            if (null == model) {
                log.error("yaml转换失败");
                throw new ApiNotFoundException("yaml转换失败，看下文件：" + file.toString());
            }
/**            currentApiPath = file.getPath();
 *
 */
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    /**
     * 筛选file文件夹下，所有符合.yaml后缀的文件
     * @param file api所在的文件夹
     * @return
     * @throws ApiNotFoundException
     */
    public static Map<String,Api> load(File file) throws ApiNotFoundException {
        List<File> files = new ArrayList<>();
        Map<String,Api> map= new HashMap<>();
        if (file.isDirectory()) {
            files = HandleFile.getFileList(Arrays.asList(file.listFiles()), ".yaml", "");
            if (null == files || files.size() <= 0) {
                throw new ApiNotFoundException("满足条件的file文件列表为空，没找到你说的yaml");
            }
        }else{
            files.add(file);
        }
        for(int i = 0;i<files.size();i++){
            //取出.yaml结尾的文件，加载成Api类，并存储到map中
            File yamlFile = files.get(i);
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            ApiListModel model = null;
            try {
                model = objectMapper.readValue(yamlFile, ApiListModel.class);
                if (null == model) {
                    log.error("yaml转换失败");
                    throw new ApiNotFoundException("yaml转换失败，看下文件：" + yamlFile.toString());
                }
                map.putAll(model.api);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return map;
    }
    /**
     * 待完善，api正在做迁移
     *
     * @param file
     * @throws ApiNotFoundException
     */
    /*@Deprecated
    public static void loadAndTransform(File file) throws ApiNotFoundException {
        *//**
     * 加载一个ApiListModel文件，并存储为ApiListModel对象1
     * 读取里面的所有的Api
     * 转换成新的Api
     * 转换成新的ApiListModel2对象
     *//*
        System.out.println(file.getPath());
//        System.out.println(file.getParentFile().listFiles());
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        ApiListModel model = null;
        try {
            model = objectMapper.readValue(file, ApiListModel.class);
            if (null == model) {
                log.error("yaml转换失败");
                throw new ApiNotFoundException("yaml转换失败，看下文件：" + file.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, Api> apiMap = model.api;
        ApiListModel2 model2 = new ApiListModel2();
        for (String key : apiMap.keySet()) {
            System.out.println(apiMap.get(key));
            if (apiMap.get(key).jsonFileName != null) {
                Api2 api2 = new Api2();
                Api api = apiMap.get(key);
                System.out.println(apiMap.get(key).getJsonFileName());
                String json = JsonTemplate.template(apiMap.get(key).getJsonFileName());
                log.info("json is " + json);
                Map<Object, Object> jsonObject = (Map<Object, Object>) JSON.parse(json);
                //创建一个新的model2对象，把model中的所有数据都迁移
                for (Map.Entry<String, Api> entry : model.api.entrySet()) {
                    api2.setUrl(api.url);
                    if (api.describle != null) {
                        api2.setDescrible(api.describle);
                    } else {
                        api2.setDescrible(model.name + model.describle);
                    }
//                    api2.headers = api.headers;
//                    api2.json = jsonObject;
                    api2.setMethod(api.method);
                    if (api.requestParam != null) {
//                        api2.requestParam = api.requestParam;
                    }
                    if (model2.api == null) {
                        model2.api = new HashMap();
                    }
                    model2.api.put(key, api2);
                }
                model2.describle = model.describle;
                model2.name = model.name;
                HandelYaml.transObjToYaml(file, model2);
            }
        }
    }*/
}

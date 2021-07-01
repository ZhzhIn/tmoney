package util;

import com.github.mustachejava.DeferringMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheNotFoundException;
import lombok.extern.slf4j.Slf4j;
import poexception.JsonNotFoundException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @ClassName: ReadJson
 * @Description: 读取json文件
 * @Author: zhzh.yin
 * @Date: 2020-05-13 16:58
 * @Verion: 1.0
 */
@Slf4j
public class JsonTemplate {
    /**
     * todo 待删除，这里有硬编码
     * @param jsonName
     * @return
     */
    public static synchronized String template(String jsonName)   {
        File file = new File("src/test/resources/json");
        ArrayList<File>files = HandleFile.getFileList(Arrays.asList(file.listFiles()),".json",jsonName);
        if(files.size()<=0||null==files){
            log.error("没找到你说的json文件"+jsonName);
            throw new JsonNotFoundException();
        }
        file = files.get(0);
        Writer writer = new StringWriter();
        DeferringMustacheFactory mf = new DeferringMustacheFactory();
        String path = file.getPath().replace("\\","/");
        log.info("最终读取得json位置在："+path);
        Mustache mustache = mf.compile(path);
        try {
            mustache.execute(writer, new JsonTemplate())
                    .flush();
        }catch(MustacheNotFoundException e){
            log.error("未找到json文件");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }
    public static String template(File file){
        Writer writer = new StringWriter();
        DeferringMustacheFactory mf = new DeferringMustacheFactory();
        String path = file.getPath().replace("\\","/");
        log.info("最终读取得json位置在："+path);
        Mustache mustache = mf.compile(path);
        try {
            mustache.execute(writer, new JsonTemplate())
                    .flush();
        }catch(MustacheNotFoundException e){
            log.error("未找到json文件");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }
    /**
     *  todo
     *  读取file文件，并转换为json
     *  将map中的值，替换掉json文件中{{变量}}的参数
     */
    public static String template(File file ,HashMap<String,String> map) {
        return null;
    }
}

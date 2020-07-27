package util;

import com.github.mustachejava.DeferringMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheNotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
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
    public static synchronized String template(String jsonPath) {
        Writer writer = new StringWriter();
        DeferringMustacheFactory mf = new DeferringMustacheFactory();
        Mustache mustache = mf.compile(jsonPath);
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
     *  todo 还没想好怎么写
     */

    public static String template(HashMap<String,String> map) {

        return null;
    }


}

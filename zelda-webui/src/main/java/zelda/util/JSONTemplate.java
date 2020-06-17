package zelda.util;

import com.github.mustachejava.DeferringMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheNotFoundException;
import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.util.HashMap;

/**
 * @ClassName: ReadJson
 * @Description: 读取json文件
 * @Author: zhzh.yin
 * @Date: 2020-05-13 16:58
 * @Verion: 1.0
 */
@Slf4j
public class JSONTemplate {
//    private static final Logger log = LoggerFactory.getLogger(JSONTemplate.class);
    public static String template(String jsonPath, HashMap map) {
        Writer writer = new StringWriter();
        DeferringMustacheFactory mf = new DeferringMustacheFactory();
        log.info("开始加工json文件，jsonPath is :" + jsonPath);
        Mustache mustache = mf.compile(jsonPath);
        try {
            log.info("填充map的数据：" + map.keySet());
            mustache.execute(writer, map)
                    .flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    public static String template(String jsonPath) {
        Writer writer = new StringWriter();
        DeferringMustacheFactory mf = new DeferringMustacheFactory();
        Mustache mustache = mf.compile(jsonPath);
        try {
            mustache.execute(writer, new JSONTemplate())
                    .flush();
        }catch(MustacheNotFoundException e){
            log.error("未找到json文件");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }



}

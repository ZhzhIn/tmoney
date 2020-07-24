package zelda.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName: LoadDefaultConfig
 * @Description: 加载默认配置，如浏览器版本，host环境等
 * @Author: zhzh.yin
 * @Date: 2020-04-29 20:32
 * @Verion: 1.0
 */
public class LoadDefaultConfig {
    public HashMap<String, HashMap<String, String>> browserDriver = new HashMap<>();
    public HashMap<String, String> host = new HashMap<>();
    public HashMap<String, String> current = new HashMap<String, String>();
    private static String srcPath = "src/test/resources/application.yaml";

    public static List<String> getBrowserVersion() {
        LoadDefaultConfig config = ReadYAML.getYamlConfig(srcPath, LoadDefaultConfig.class);
        List list =Arrays.asList(config.current.get("browser").split("-"));
        String browserVersion = config.browserDriver.get(list.get(0)).get(list.get(1));
        list.set(1, browserVersion);
        return list;
    }

    public static String getHost() {
        LoadDefaultConfig config = ReadYAML
                .getYamlConfig(srcPath, LoadDefaultConfig.class);
        String env  =config.current.get("host");
        return config.host.get(env);
    }
}

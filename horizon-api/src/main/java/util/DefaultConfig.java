package util;

import api.dto.*;
import api.item.AppType;
import api.item.Env;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import poexception.ConfigNotFoundException;

import java.util.HashMap;

/**
 * @ClassName: loadDefaultConfig
 * @Description: 默认配置，读取application.yaml中的配置
 * @Author: zhzh.yin
 * @Date: 2020-04-24 18:30
 * @Verion: 1.0
 */
@Slf4j
public class DefaultConfig {
    public HashMap<String, Env> current = new HashMap<String, Env>();
    public HashMap<Env, String> host = new HashMap<>();
    public HashMap<Env, CorpDTO> corp = new HashMap<Env, CorpDTO>();
    public HashMap<Env, HashMap<AppType, AppDTO>> app = new HashMap<Env, HashMap<AppType, AppDTO>>();
    public HashMap<Env, StaffDTO> staff = new HashMap<Env, StaffDTO>();
    public HashMap<Env, ProductDTO> product = new HashMap<Env, ProductDTO>();
    public HashMap<Env, MorningDTO> morning = new HashMap<Env, MorningDTO>();
    /**
     * todo 优化硬编码
     */
    static String srcPath = "src/main/resources/application.yaml";
    private static DefaultConfig config = HandelYaml
            .getYamlConfig(srcPath, DefaultConfig.class);
    public static Env env = Env.fromString(System.getProperty("Env"));
    public DefaultConfig(){
        initConfig();
    }
    private void initConfig(){
        //TODO 读取数据库配置，写入配置文件

    }
    @Override
    public String toString() {
        return "LoadDefaultConfig{" +
                "current=" + current +
                ", host=" + host +
                ", corp=" + corp +
                ", app=" + app +
                ", staff=" + staff +
                '}';
    }
    //todo 参数使用反射实现
    public static String getStrFromDefaultConfig(String values) {
        if(values==null){
            return "";
        }
        log.info("getStrFromDefaultConfig");
        log.info("values:"+values);
        String[] value = values.split("\\.");
        if (value.length > 1) {
            if (value[0].equals("defaultConfig")) {
                if (value.length == 2) {
                    if (value[1].equals("corpId")) {
                        return DefaultConfig.getCorp().getCorpId();
                    } else if (value[1].equals("staffName")) {
                        return DefaultConfig.getStaff().getStaffName();
                    } else if (value[1].equals("userId")) {
                        return DefaultConfig.getStaff().getUserId();
                    } else if (value[1].equals("staffId")) {
                        return DefaultConfig.getStaff().getStaffId();
                    } else if(value[1].equals("timestamp")){
                        return getCurrentMilles()+"";
                    }else if(value[1].equals("corpName")){
                        return DefaultConfig.getCorp().getCorpName();
                    }else if(value[1].equals("corpId")){
                        return DefaultConfig.getCorp().getCorpId();
                    }else if(value[1].equals("paperId")){
                        return DefaultConfig.getMorning().getPaperId();
                    }else if(value[1].equals("productType")){
                        return DefaultConfig.getProduct().getProductType();
                    }else if(value[1].equals("productId")){
                        return DefaultConfig.getProduct().getProductId();
                    }else if(value[1].equals("morningPaperId")){
                        return DefaultConfig.getMorning().getPaperId();
                    }else if(value[1].equals("authCorpId")){
                        return DefaultConfig.getCorp().getAuthCorpId();
                    }else{
                        log.error("param参数没有在默认设置中找到");
                        throw new ConfigNotFoundException("param参数没有在默认设置中找到");
                    }
                } else if (value.length == 3) {
                    try {
                        log.info("value[1]:"+value[1]);
                        AppType type = AppType.fromString(value[1]);
                        System.out.println("=======================");
                        log.info(type.toString());
                        AppDTO app = DefaultConfig.getApp(type);
                        //todo 反射
                        if (value[2].equals("appId")) {
                            return app.getAppId();
                        } else if (value[2].equals("agentId")) {
                            return app.getAgentId();
                        } else if (value[2].equals("componentAppid")) {
                            return app.getComponentAppid();
                        } else {
                            log.error("没有你写的元素：" + value[2]);
                            throw new ConfigNotFoundException("没有你写的元素：" + value[2]);
                        }
                    } catch (Exception e) {
                        log.error("appType 必须是H5STATION,MINIPRO,H5PRODUCT等等");
                        throw new ConfigNotFoundException("appType 必须是H5STATION,MINIPRO,H5PRODUCT等等");

                    }
                } else {

                    log.error("param设置错误，虽多有两个.如defaultConfig.h5station.appId");
                    throw new ConfigNotFoundException("param设置错误，虽多有两个.如defaultConfig.h5station.appId");
                }
            }
        } else {
            return values;
        }
        return "";
    }
    public static Long getCurrentMilles(){
        String time =  System.currentTimeMillis()+"";
        return Long.valueOf(time.substring(0, time.length() - 3));
    }
    @Test
    public void test(){

        System.out.println(getCurrentMilles());
    }
    public static String getHost() {
        return config.host.get(env);
    }

    public static CorpDTO getCorp() {
        CorpDTO corp = config.corp.get(env);
        return corp;
    }

    public static AppDTO getApp(AppType appType) {
        AppDTO app = config.app.get(env).get(appType);
        return app;
    }

    public static StaffDTO getStaff() {
        StaffDTO staff = config.staff.get(env);
        return staff;
    }
    public static ProductDTO getProduct() {
        ProductDTO product = config.product.get(env);
        return product;
    }
    public static MorningDTO getMorning() {
        MorningDTO morning = config.morning.get(env);
        return morning;
    }
    public static HashMap<String, Object> getDefaultConfig() {
        HashMap<String, Object> map = new HashMap<>(16);
        map.putAll(importStaffConfig());
        map.putAll(importCorpConfig());
        return map;
    }

    public static HashMap<String, Object> getDefaultConfig(AppType type) {
        HashMap<String, Object> map = new HashMap<>(16);
        map.putAll(importStaffConfig());
        map.putAll(importCorpConfig());
        map.putAll(importAppConfig(type));
        return map;
    }

    private static HashMap<String, String> importAppConfig(AppType type) {
        HashMap<String, String> map = new HashMap<>(16);
        AppDTO app = getApp(type);
        map.put("appId", app.getAppId());
        map.put("agentId", app.getAgentId());
        map.put("componentAppid", app.getAgentId());
        return map;
    }

    private static HashMap<String, String> importStaffConfig() {
        HashMap map = new HashMap(16);
        StaffDTO staff = getStaff();
        log.info("staff is " + staff);
        map.put("userId", staff.userId);
        return map;
    }

    private static HashMap<String, String> importCorpConfig() {
        HashMap map = new HashMap(16);
        CorpDTO corp = getCorp();
        log.info("corp is " + corp);
        map.put("authCorpId", corp.corpId);
        map.put("currentCorpId", corp.corpId);
        map.put("corpId", corp.corpId);
        return map;
    }
}

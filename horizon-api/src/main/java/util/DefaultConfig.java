package util;

import api.dto.*;
import api.item.AppType;
import lombok.extern.slf4j.Slf4j;
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
    public String host;
    public CorpDTO corp;
    public HashMap<AppType, AppDTO> app = new HashMap<AppType, AppDTO>();
    public StaffDTO staff;
    public ProductDTO product;
    public MorningDTO morning;
    private static final String DEFAULT_CONFIG = "defaultConfig";
    private static final String CORP_ID = "corpId";
    private static final String STAFF_NAME = "staffName";
    private static final String USER_ID = "userId";
    private static final String STAFF_ID = "staffId";
    private static final String CORP_NAME = "corpName";
    private static final String PAPER_ID = "paperId";
    private static final String PRODUCT_TYPE = "productType";
    private static final String PRODUCT_ID = "productId";
    //    private static final String MORNING_PAPER_ID = "morningPaperId";
    private static final String AUTH_CORP_ID = "authCorpId";
    private static final String APP_ID = "appId";
    private static final String AGENT_ID = "agentId";
    private static final String COMPONENT_APP_ID = "componentAppid";
    private static final String TIMESTAMP = "timestamp";
    private static final int FIRST_INDEX = 1;
    private static final int SECOND_INDEX = 2;
    /**
     * todo 优化硬编码
     * 1.去掉current配置 √
     * 2.修改yaml的配置文件配置方式 √
     * 3.反射配置项
     */

    static String srcPath = DefaultConfig.class.getClassLoader().getResource("application.yaml").getPath();
    private static DefaultConfig config=HandelYaml.getYamlConfig(srcPath,DefaultConfig.class);;

    public DefaultConfig() {
        initConfig();
    }

    private void initConfig() {
        //TODO 读取数据库配置，写入配置文件

    }

    @Override
    public String toString() {
        return "LoadDefaultConfig{" +
                "host=" + host +
                ", corp=" + corp +
                ", app=" + app +
                ", staff=" + staff +
                '}';
    }


    //todo 参数使用反射实现

    /**
     * 调用配置类，读取application.yaml的配置
     * @param values yaml接口测试文档中的参数，比如 defaultConfig.corpId
     * @return 返回application.yaml中对应的值
     */
    public static String getStrFromDefaultConfig(String values) {
        if (values == null) {
            return "";
        }
        log.info("getStrFromDefaultConfig");

        if(values.startsWith(DEFAULT_CONFIG)){
            String[] value = values.split("\\.");
            if (value.length > 1) {
                if (value[0].equals(DEFAULT_CONFIG)) {
                    if (value.length == 2) {
                        if (value[FIRST_INDEX].equals(CORP_ID)) {
                            return DefaultConfig.getCorp().getCorpId();
                        } else if (value[FIRST_INDEX].equals(STAFF_NAME)) {
                            return DefaultConfig.getStaff().getStaffName();
                        } else if (value[FIRST_INDEX].equals(USER_ID)) {
                            return DefaultConfig.getStaff().getUserId();
                        } else if (value[FIRST_INDEX].equals(STAFF_ID)) {
                            return DefaultConfig.getStaff().getStaffId();
                        } else if (value[FIRST_INDEX].equals(TIMESTAMP)) {
                            return getCurrentMilles() + "";
                        } else if (value[FIRST_INDEX].equals(CORP_NAME)) {
                            return DefaultConfig.getCorp().getCorpName();
                        } else if (value[FIRST_INDEX].equals(CORP_ID)) {
                            return DefaultConfig.getCorp().getCorpId();
                        } else if (value[FIRST_INDEX].equals(PAPER_ID)) {
                            return DefaultConfig.getMorning().getPaperId();
                        } else if (value[FIRST_INDEX].equals(PRODUCT_TYPE)) {
                            return DefaultConfig.getProduct().getProductType();
                        } else if (value[FIRST_INDEX].equals(PRODUCT_ID)) {
                            return DefaultConfig.getProduct().getProductId();
                        } /*else if (value[1].equals("morningPaperId")) {
                            return DefaultConfig.getMorning().getPaperId();
                        } */else if (value[FIRST_INDEX].equals(AUTH_CORP_ID)) {
                            return DefaultConfig.getCorp().getAuthCorpId();
                        } else {
                            log.error("param参数没有在默认设置中找到");
                            throw new ConfigNotFoundException("param参数没有在默认设置中找到");
                        }
                    } else if (value.length == 3) {
                        try {
                            AppType type = AppType.fromString(value[1]);
                            AppDTO app = DefaultConfig.getApp(type);
                            //todo 反射
                            if (value[SECOND_INDEX].equals(APP_ID)) {
                                return app.getAppId();
                            } else if (value[SECOND_INDEX].equals(AGENT_ID)) {
                                return app.getAgentId();
                            } else if (value[SECOND_INDEX].equals(COMPONENT_APP_ID)) {
                                return app.getComponentAppid();
                            }else if (value[SECOND_INDEX].equals(AUTH_CORP_ID)) {
                                return app.getAuthCorpId();
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
        }else{
            return values;
        }
        return values;
    }

    public static Long getCurrentMilles() {
        String time = System.currentTimeMillis() + "";
        return Long.valueOf(time.substring(0, time.length() - 3));
    }

    public static String getHost() {
        return config.host;
    }

    public static CorpDTO getCorp() {
        CorpDTO corp = config.corp;
        return corp;
    }

    public static AppDTO getApp(AppType appType) {
        AppDTO app = config.app.get(appType);
        return app;
    }

    public static StaffDTO getStaff() {
        StaffDTO staff = config.staff;
        return staff;
    }

    public static ProductDTO getProduct() {
        ProductDTO product = config.product;
        return product;
    }

    public static MorningDTO getMorning() {
        MorningDTO morning = config.morning;
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

package api.item;

/**
 * tmoney
 * 2020/7/25 18:57
 * 手动传入的数据类型
 *
 * @author zhzh.yin
 **/
public enum Manu {
    /**
     * REQUEST_PARAM 请求参数类型
     * JSON_FILE_NAME json文件名称
     */
    REQUEST_PARAM("requestParam"),
    JSON_FILE_NAME("jsonFileName");
    private final String type;
    private Manu(String type)
    {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

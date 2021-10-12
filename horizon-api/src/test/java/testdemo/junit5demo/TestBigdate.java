package testdemo.junit5demo;

import org.junit.jupiter.api.Test;

/**
 * @author zhzh.yin
 * @create 2021-07-26 14:19
 */

public class TestBigdate {

    @Test
    void test(){
        String h5data  = "title\": \"黄色双预警继续发布！\",\"staffId\": \"3f0d48196a77442b8c1094b84393da13\",\"type\": 2,\"id\": \"f9b8ab2aec0b4c49bce3ee441e550665\",\"corpId\": \"ww8c83d949a80b562d\",\"context\": \"\",\"optionType\": 2,\"channel\": 1,\"source\": 16,\"buriedPointType\": 0,\"sourceContentId\": \"\"}";
        String bigdata = "{\"properties\":{\"external\":null,\"corp_id\":\"ww8c83d949a80b562d\",\"staff_id\":\"3f0d48196a77442b8c1094b84393da13\"},\"event\":{\"source\":16,\"external\":null,\"staff_id\":\"3f0d48196a77442b8c1094b84393da13\",\"opt_type\":2,\"content_id\":\"f9b8ab2aec0b4c49bce3ee441e550665\",\"content_type\":2,\"content_title\":\"黄色双预警继续发布！\",\"link_id\":null,\"send_channel\":null,\"source_content_id\":\"\"},\"create_time\":1627271380778}";
        for( String str :bigdata.split(h5data)) {
            System.out.println(str);
        };
    }
}
class ShareData{
    String title;
    String staffId;
    int type;
    String id ;
    String corpId;
    String context;
    int optionType;
    String channel;
    String source;
    String buriedPointType;
    String sourceContentId;
}
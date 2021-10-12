package api.dto;/**
 * @author zhzh.yin
 * @create 2020-08-05 16:05
 */

import lombok.Data;

/**
 * 〈自建应用信息
 *
 * @author zhzh.yin
 * @create 2020/8/5
 */
@Data
public class AppDTO {
    public String corpId;
    public String appId;
    public String appName;
    public String agentId;
    public String componentAppid;
    public String authCorpId;
}

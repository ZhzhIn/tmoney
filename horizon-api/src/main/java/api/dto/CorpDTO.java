package api.dto;/**
 * @author zhzh.yin
 * @create 2020-08-05 16:21
 */

import lombok.Data;

import java.util.List;

/**
 * 〈公司信息〉
 *
 * @author zhzh.yin
 * @create 2020/8/5
 */
@Data
public class CorpDTO {
    public String corpName;
    public String corpId;
    public List<String> appIdList;
}

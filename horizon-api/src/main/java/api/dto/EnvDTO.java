package api.dto;

import api.item.AppType;
import lombok.Data;

import java.util.HashMap;
@Data
/**
 * 〈环境信息〉
 *
 * @author zhzh.yin
 * @create 2020/8/5
 */
public class EnvDTO {
    public String host;
    public CorpDTO corp;
    public HashMap<AppType,AppDTO> app;
    public StaffDTO staff;
}

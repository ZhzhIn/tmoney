package api.dto;/**
 * @author zhzh.yin
 * @create 2020-08-05 16:25
 */
import lombok.Data;
/**
 * 〈员工实体类〉
 *
 * @author zhzh.yin
 * @create 2020/8/5
 */
@Data
public class StaffDTO {
    public String staffName;
    public String staffId;
    public String userId;
    public String corpId;
}
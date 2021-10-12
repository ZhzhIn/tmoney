package util;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

/**
 * 〈测试数据库语句〉
 *
 * @author zhzh.yin
 * @create 2021/3/5
 */
@Slf4j
//TODO 启动前读取数据库，自动填充不同环境下的yaml配置！！！！
public class MySqlData {
    SqlSession sqlSession =null;
    public String getMorningPaperId(){
        try {
            sqlSession = SqlSessionFactoryUtil.openSqlSession();
            PaperMapper roleMapper = sqlSession.getMapper(PaperMapper.class);
            String paperid=roleMapper.getPaperId(DefaultConfig.getCorp().getCorpId());
            log.info("paperId =>" + paperid);
            return paperid;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            sqlSession.rollback();
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
        return "";
    }
}
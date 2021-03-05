package util;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

@Slf4j
public class SqlSessionFactoryUtil {
    private static SqlSessionFactory sqlSessionFactory = null;
    //类线程锁，避免sqlSession多线程下多次初始化造成对象不唯一
    private static final Class CLASS_LOCK = SqlSessionFactoryUtil.class;
    private SqlSessionFactoryUtil(){}
    public static SqlSessionFactory initSqlSessionFactory() {
        String resource = "mybatis-config.xml";
        InputStream is=null;
        try {
            is = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
            log.info("build sqlSessionFactory success");
            return sqlSessionFactory;
        } catch (Exception e) {
            log.error( SqlSessionFactoryUtil.class.getName(),e.getMessage());
            e.printStackTrace();
        }
        synchronized (CLASS_LOCK){
            if(sqlSessionFactory==null){
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
            }
        }
        return sqlSessionFactory;
    }

    /**
     * 打开SqlSession
     */
    public static SqlSession openSqlSession(){
        if(sqlSessionFactory == null){
            initSqlSessionFactory();

        }
        return sqlSessionFactory.openSession();
    }

}

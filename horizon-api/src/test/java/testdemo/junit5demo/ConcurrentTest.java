package testdemo.junit5demo;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
//CorpStaffDataAnalyseServiceImpl
//还有一个解决方案，手动加锁
@Slf4j
@Feature("SimpleDateFormat并发不安全示例")
@Owner("zhzh.yin")
public class ConcurrentTest {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    //StringBuffer是线程安全的，也就是多线程修改同一个StringBuffer对象的时候，过程是同步的，当然这就导致了StringBuffer的效率降低，毕竟如果要提升安全性，就必须要损失一定的效率。
    //synchronized
    //加锁
    private static final ThreadLocal<SimpleDateFormat> THREAD_LOCAL = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
//    @RepeatedTest(500)
    @Story("并发报错：simpleDateFormat线程不安全")
    @Execution(ExecutionMode.CONCURRENT)
    //解决方案：用DateTimeFormatter
    public void testFailure() throws ParseException, InterruptedException {
        String dateString = simpleDateFormat.format(new Date());
        log.info(dateString);
        Date time = simpleDateFormat.parse(dateString);
        String dateString2 = simpleDateFormat.format(time);
        assertEquals(dateString, dateString2);
    }
//    @RepeatedTest(500)
    @Story("解决方案：局部变量")
    @Execution(ExecutionMode.CONCURRENT)
    //解决方案：局部变量
    public void testSuc2() throws ParseException, InterruptedException {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = simpleDateFormat1.format(new Date());
        log.info(dateString);
        Date time = simpleDateFormat1.parse(dateString);
        String dateString2 = simpleDateFormat1.format(time);
        assertEquals(dateString, dateString2);
    }
//    @RepeatedTest(500)
    @Story("解决方案：ThreadLocal")
    @Execution(ExecutionMode.CONCURRENT)
    //解决方案：使用ThreadLocal，每个线程都拥有自己的SimpleDateFormat对象副本。
    public void testSuc3() throws ParseException, InterruptedException {
        SimpleDateFormat simpleDateFormat2 = THREAD_LOCAL.get();
        if (simpleDateFormat2 == null) {
            simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }String dateString = simpleDateFormat2.format(new Date());
        log.info(dateString);
        Date time = simpleDateFormat2.parse(dateString);
        String dateString2 = simpleDateFormat2.format(time);
        assertEquals(dateString, dateString2);
    }
//    @RepeatedTest(500)
    @Story("解决方案：使用DateTimeFormatter")
    @Execution(ExecutionMode.CONCURRENT)
    public void testSuc1() throws ParseException, InterruptedException {
        String dateNow = LocalDateTime.now().format(dtf);
        String dateNow2=LocalDateTime.parse(dateNow,dtf).format(dtf);
        assertEquals(dateNow, dateNow2);
    }

}

package testdemo.junit5demo;/**
 * @author zhzh.yin
 * @create 2021-07-09 16:52
 */

import org.junit.jupiter.api.RepeatedTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * 〈execute command〉
 *
 * @author zhzh.yin
 * @create 2021/7/9
 */
public class ExeCmd {
    @RepeatedTest(1)
    void test() throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec("ipconfig ");
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;
        while((line=br.readLine())!=null){
            System.out.println(new String(line.getBytes(), Charset.defaultCharset()));
        }
        br.close();
        System.out.println("over");

    }
}

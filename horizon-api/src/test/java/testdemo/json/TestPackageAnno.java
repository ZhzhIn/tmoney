package testdemo.json;/**
 * @author zhzh.yin
 * @create 2020-11-06 15:32
 */

//import org.junit.Test;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.IncludePackages;
import org.junit.runner.RunWith;

/**
 * 〈xx〉
 *
 * @author zhzh.yin
 * @create 2020/11/6
 */
@RunWith(JUnitPlatform.class)
@IncludePackages("junit5demo")
@DisplayName("JUnit Platform Suite Demo")
public class TestPackageAnno {
    @Test
    public void test(){
        System.out.println("package");

    }


}

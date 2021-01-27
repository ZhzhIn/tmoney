package testdemo.junit5demo;

import org.junit.jupiter.api.*;

import java.util.HashMap;


public class LoginTest {

    protected  static HashMap<String,Object> dataMap=new HashMap<>();

    @Test
    void login(){
        System.out.println("登录成功");
        dataMap.put("login","登录成功");

    }


    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class payTest{
        @BeforeAll
        void beforeAll(){
            System.out.println("beforeAll");
        }
        @Test
        void payTest(){
            if(null!=dataMap.get("buy")){
                System.out.println("aaa");
                System.out.println(dataMap.get("buy"));
                System.out.println("支付成功");
            }else{
                System.out.println("请先下单购买产品");
            }
        }
        @AfterAll
        void afterAll(){
            System.out.println("AfterAll");
        }
    }


    @Nested
    class buyTest{
        @Test
        void buyTest(){
            if("登录成功".equals(dataMap.get("login"))){
                System.out.println("登录成功，可以购买产品了");
                dataMap.put("buy","购买产品成功，请支付");
            }else{
                System.out.println("请先进行登录");
            }


        }
    }
}

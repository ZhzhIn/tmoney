package testdemo.json;

import org.junit.Test;

import java.util.HashMap;

public class TestMap {
    @Test
   public void test(){
        HashMap<String,String>map = new HashMap();
        HashMap<String,String>map2 = new HashMap();

        map.put("a","a");
        map.put("b","b");
        map.put("c","c");




        map2.put("a","aa");
        map2.put("b","bb");
        map2.put("d","d");

        map2.keySet().removeAll(map.keySet());


        System.out.println(map);
        System.out.println(map2);


        map.putAll(map2);
        System.out.println("+++++++++++++++++");
        System.out.println(map);
        System.out.println(map2);
    }
}


package com.tengmoney.autoframework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import util.HandelYaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
/**
 *  将所有page放入集合，按照用例步骤进行page的调用和执行
 */
public class PageHandler {
    List<PageObjectModel> pages = new ArrayList<>();
    public UITestCase load(String path){
        UITestCase uiTestCase = HandelYaml
                .getYamlConfig(path,UITestCase.class);
        return uiTestCase;
    }
    public void run(UITestCase uiTestcase) {
        uiTestcase.steps.stream().forEach(
                step -> {
                action(step);
        });
    }

    /**
     * 执行每个用例中的步骤,如果是page层面的，就调用对应的page和对应方法
     * 如果是操作层面的，就调用pageObject里面的操作处理
     * 如果是断言层面的，就调用断言方法处理
     * @param map
     */
    public void action(HashMap map){

    }
    public void loadPages(String dir) {
        Stream.of(new File(dir).list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.contains("page");
            }
        })).forEach(path -> {
            path = dir + "/" + path;
            System.out.println(path);
            pages.add(loadPage(path));
        });
    }
    /**
     * 加载所有的pageObject
     * @param path
     * @return
     */
    public   PageObjectModel loadPage( String path) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        PageObjectModel pom = null;
        try {
            pom = mapper.readValue(
//                    Thread.currentThread().getStackTrace()[2].getClass().getResourceAsStream(path),
                    new File(path),
                    PageObjectModel.class
            );
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pom;
    }
    /**
     * 单个操作的判定和执行
     */
    public void click(HashMap map){
        log.info("click");
    }
    public void sendKeys(HashMap map){
        log.info("sendKeys");
    }
    public void quit() {}
    public void sendKeys(By by, String content){};
    public void click(String text) {}
    public void click(By by) {}
    public void upload(By by, String path){}
}

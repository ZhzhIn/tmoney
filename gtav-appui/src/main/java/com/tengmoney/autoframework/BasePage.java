package com.tengmoney.autoframework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

//抽象工厂模式
@Slf4j
public abstract class BasePage {

    List<PageMethod> pages = new ArrayList<>();
    public void click(HashMap<String, Object> map) {
        log.info("click");
    }
    public void sendKeys(HashMap<String, Object> map) {
        log.info("sendKeys");
    }

    /**
     * 除了官方提供的元素操作之外，还需要定义一些自己的操作
     * @param map
     */
    public void action(HashMap<String, Object> map) {
        log.info("action");

//        如果是page级别的关键字
        if (map.containsKey("page")) {
            String action = map.get("action").toString();
            String pageName = map.get("page").toString();
            pages.forEach(pom-> log.info(pom.name));

            pages.stream()
                    .filter(pom -> pom.name.equals(pageName))
                    .findFirst()
                    .get()
                    .methods.get(action).forEach(step -> {
                action(step);
            });
        } else {

//            page内部操作
            if (map.containsKey("click")) {
                HashMap<String, Object> by = (HashMap<String, Object>) map.get("click");
                click(by);
            }
            if (map.containsKey("sendkeys")) {
                HashMap<String, Object> by = (HashMap<String, Object>) map.get("sendkeys");
                sendKeys(by);
            }

        }


    }

    public void find() {

    }

    public void getText() {

    }

    public void run(UITestCase uiTestcase) {
        uiTestcase.steps.stream().forEach(
                step -> {
                action(step);
        });

    }

    public UITestCase load(String path) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        UITestCase uiTestcase = null;
        try {
            uiTestcase = mapper.readValue(
                    BasePage.class.getResourceAsStream(path),
                    UITestCase.class
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uiTestcase;

    }

    public PageMethod loadPage(String path) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        PageMethod pom = null;
        try {
            pom = mapper.readValue(
                    new File(path),
                    PageMethod.class
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pom;
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
}

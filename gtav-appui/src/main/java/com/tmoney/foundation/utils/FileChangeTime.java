package com.tmoney.foundation.utils;/**
 * @author zhzh.yin
 * @create 2020-11-25 15:18
 */

import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * 〈return the file last change time〉
 *
 * @author zhzh.yin
 * @create 2020/11/25
 */
@Slf4j
public class FileChangeTime {
    /**
     * 获取文件上次修改的时间
     * @param file
     * @return
     */
    public static synchronized  long getChangeTime(File file) {
        try {
            BasicFileAttributes bAttributes = Files.readAttributes(file.toPath(),
                    BasicFileAttributes.class);
            return bAttributes.lastModifiedTime().toMillis();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("获取文件上次更新时间失败");
        return 0;
    }
}

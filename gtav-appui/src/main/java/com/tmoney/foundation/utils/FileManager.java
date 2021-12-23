package com.tmoney.foundation.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.tmoney.foundation.utils.Configuration;
import com.tmoney.foundation.utils.Configuration.Parameter;

/**
 * 〈file manager〉
 *
 * @author zhzh.yin
 * @create 2021/12/23
 */
@Slf4j
public class FileManager
{


    public static void removeDirRecurs(String directory)
    {
        File dir = new File(directory);
        if (dir.exists() && dir.isDirectory())
        {
            try
            {
                FileUtils.deleteDirectory(dir);
            }
            catch (IOException e)
            {
                log.error(e.getMessage());
            }
        }
    }

    public static List<File> getFilesInDir(File directory)
    {
        List<File> files = new ArrayList<File>();
        try
        {
            File[] fileArray = directory.listFiles();

            for (int i = 0; i < fileArray.length; i++)
            {
                files.add(fileArray[i]);
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
        }
        return files;
    }

    public static void createFileWithContent(String filePath, String content)
    {
        File file = new File(filePath);
        try
        {
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            fw.write(content);
            fw.close();
        }
        catch (IOException e)
        {
            log.error(e.getMessage());
        }
    }

    public static File createLogRootFolder(String name)
    {
        createFolder(System.getProperty("user.dir") + "/" + Configuration.get(Parameter.ROOT_REPORT_DIRECTORY));
        createFolder(System.getProperty("user.dir") + "/" + Configuration.get(Parameter.PROJECT_REPORT_DIRECTORY));
        return createFolder(System.getProperty("user.dir") + "/" + Configuration.get(Parameter.PROJECT_REPORT_DIRECTORY) + "/" + name);
    }

    private static File createFolder(String name)
    {
        File folder = new File(name);
        if (!folder.exists())
        {
            folder.mkdir();
        }
        return folder;
    }
}

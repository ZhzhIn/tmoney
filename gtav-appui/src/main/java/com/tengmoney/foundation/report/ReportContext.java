package com.tengmoney.foundation.report;

import com.tmoney.foundation.utils.Configuration;
import com.tmoney.foundation.utils.Configuration.Parameter;
import com.tmoney.foundation.utils.FileManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 〈report〉
 *
 * @author zhzh.yin
 * @create 2021/12/23
 */
@Slf4j
public class ReportContext
{

    private static File baseDirectory;

    private static File tempDirectory;

    private static long rootID;

    /**
     * Crates new screenshot directory at first call otherwise returns created
     * directory. Directory is specific for any new test suite launch.
     *
     * @return root screenshot folder for test launch.
     */
    public static synchronized File getBaseDir()
    {
        if (baseDirectory == null)
        {
            File reportRoot = new File(String.format("%s/%s", System.getProperty("user.dir"),
                    Configuration.get( Parameter.ROOT_REPORT_DIRECTORY)));
            if (!reportRoot.exists())
            {
                reportRoot.mkdir();
            }

            File projectRoot = new File(String.format("%s/%s", System.getProperty("user.dir"),
                    Configuration.get(Parameter.PROJECT_REPORT_DIRECTORY)));
            if (!projectRoot.exists())
            {
                projectRoot.mkdir();
            }

            rootID = System.currentTimeMillis();
            String directory = String.format("%s/%s/%d", System.getProperty("user.dir"),
                    Configuration.get(Parameter.PROJECT_REPORT_DIRECTORY), rootID);
            baseDirectory = new File(directory);
            baseDirectory.mkdir();
        }
        return baseDirectory;
    }

    public static synchronized File getTempDir()
    {
        if (tempDirectory == null)
        {
            tempDirectory = new File(getBaseDir().getAbsolutePath() + "/temp");
            tempDirectory.mkdir();
        }
        return tempDirectory;
    }

    /**
     * Crates new screenshot directory at first call otherwise returns created
     * directory. Directory is specific for any new test launch.
     *
     * @param test
     *            = name of test.
     * @return test screenshot folder.
     */
    public static File getTestDir(String test)
    {
        String directory = String.format("%s/%s", getBaseDir(), test.replaceAll("[^a-zA-Z0-9.-]", "_"));
        File screenDir = new File(directory);
        if (!screenDir.exists())
        {
            screenDir.mkdir();
            File thumbDir = new File(screenDir.getAbsolutePath() + "/thumbnails");
            thumbDir.mkdir();
        }
        return screenDir;
    }

    /**
     * Removes oldest screenshots directories according to history size defined
     * in config.
     */
    public static void removeOldReports()
    {
        File baseDir = new File(String.format("%s/%s", System.getProperty("user.dir"),
                Configuration.get(Parameter.PROJECT_REPORT_DIRECTORY)));

        if (baseDir.exists())
        {
            List<File> files = FileManager.getFilesInDir(baseDir);
            List<File> screenshotFolders = new ArrayList<File>();
            for(File file : files)
            {
                if(file.isDirectory() && !file.getName().startsWith("."))
                {
                    screenshotFolders.add(file);
                }
            }

            int maxHistory = Configuration.getInt(Parameter.MAX_SCREENSHOOT_HISTORY);

            if (screenshotFolders.size() > maxHistory)
            {
                Comparator<File> comp = new Comparator<File>()
                {
                    @Override
                    public int compare(File file1, File file2)
                    {
                        return file2.getName().compareTo(file1.getName());
                    }
                };
                Collections.sort(screenshotFolders, comp);
                for (int i = maxHistory - 1; i < screenshotFolders.size(); i++)
                {
                    try
                    {
                        FileUtils.deleteDirectory(screenshotFolders.get(i));
                    }
                    catch (IOException e)
                    {
                        log.error(e.getMessage());
                    }
                }
            }
        }
    }

    public static void removeTestReport(String test)
    {
        try
        {
            File toDelete = new File(ReportContext.getBaseDir() + "/" + test.replaceAll("[^a-zA-Z0-9.-]", "_"));
            FileUtils.deleteDirectory(toDelete);
        }
        catch (IOException e)
        {
            log.error(e.getMessage());
        }
    }

    /**
     * Returns URL for test screenshot folder.
     *
     * @param test
     * @return - URL for test screenshot folder.
     */
    public static String getTestScreenshotsLink(String test)
    {
        return String.format("%s/%d/%s/report.html", Configuration.get(Parameter.REPORT_URL), rootID, test.replaceAll("[^a-zA-Z0-9.-]", "_"));
    }

    /**
     * Returns URL for test log.
     *
     * @param test
     * @return - URL to test log folder.
     */
    public static String getTestLogLink(String test)
    {
        return String.format("%s/%d/%s/test.log", Configuration.get(Parameter.REPORT_URL), rootID, test.replaceAll("[^a-zA-Z0-9.-]", "_"));
    }

    /**
     * Returns URL for performance report.
     *
     * @return - URL to test log folder.
     */
    public static String getPerformanceReportLink()
    {
        return String.format("%s/%d/report.html", Configuration.get(Parameter.REPORT_URL), rootID);
    }
}


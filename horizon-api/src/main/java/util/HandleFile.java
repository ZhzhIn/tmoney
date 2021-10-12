package util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhzh.yin
 * @create 2021/3/5
 */
@Slf4j
public class HandleFile {
    /**
     * 筛选当前文件夹以及所有子文件夹中，符合条件的文件
     *
     * @param files         要筛选的文件夹
     * @param desFileSuffix 要查找的文件的文件后缀
     * @param desFileName   要查找的文件的文件名
     * @return 筛选出的符合条件的文件集合
     */
    public static ArrayList<File> getFileList(List<File> files, String desFileSuffix, String desFileName) {
        ArrayList<File> fileResult = new ArrayList<File>();

        for (File file : files) {
            boolean desFileNameNotNull = desFileName != "" && desFileName != null;
            boolean desFileNameIsNull = desFileName == "" || desFileName == null;
            boolean fileEqualsDesFileNameAndSuffix = file.getName().equalsIgnoreCase(desFileName + desFileSuffix);
            boolean fileEndWithSuffix = file.getName().toLowerCase().endsWith(desFileSuffix.toLowerCase());
            if (file.isDirectory()) {
                ArrayList<File> arrays = new ArrayList<File>(Arrays.asList(file.listFiles()));
                arrays = getFileList(arrays, desFileSuffix, desFileName);
                fileResult.addAll(arrays);
            } else if (fileEndWithSuffix && desFileNameIsNull
                    || desFileNameNotNull && fileEqualsDesFileNameAndSuffix) {
                fileResult.add(file);
            }
        }
        return fileResult;
    }
}

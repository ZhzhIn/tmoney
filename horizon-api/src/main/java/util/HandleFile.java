package util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Slf4j
public class HandleFile {
    /**
     * 筛选当前文件夹以及所有子文件夹中，符合条件的文件
     * @param files 要筛选的文件夹
     * @param endWord 文件的结尾字符
     * @return 筛选出的符合条件的文件集合
     */
    public static ArrayList<File> getFileList(List<File> files, String endWord,String containWord){
        ArrayList<File> fileResult= new ArrayList<File>();
        for(File file:files){
            log.info("筛选"+file.getName()+"以"+endWord+"结尾，包含"+containWord);
            if(file.getName().toLowerCase().endsWith(endWord.toLowerCase())
                    && file.getName().toLowerCase().contains(containWord.toLowerCase())){
                log.info("添加"+file.getName());
                fileResult.add(file);
            }else if(file.isDirectory()){
                log.info("当前是文件夹"+file.getName());
                ArrayList<File> arrays = new ArrayList<File>(Arrays.asList(file.listFiles()));
                arrays=getFileList(arrays,endWord,containWord);
                fileResult.addAll(arrays);
            }
        }
        return fileResult;
    }
}

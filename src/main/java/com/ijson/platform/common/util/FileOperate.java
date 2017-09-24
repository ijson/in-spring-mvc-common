package com.ijson.platform.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

@Slf4j
public class FileOperate {

    private static FileOperate instance = null;

    private FileOperate() {

    }

    public synchronized static FileOperate getInstance() {
        if (null == instance)
            instance = new FileOperate();
        return instance;
    }

    /**
     * 检查文件或文件夹是否存在
     *
     * @param folderPath 文件或文件夹路径
     * @return true为存在，false不存在
     */
    public boolean checkFolder(String folderPath) {
        boolean result = false;
        if (Validator.isNotNull(folderPath)) {
            File newFilePath = new File(folderPath);
            result = newFilePath.exists();
        }
        return result;
    }

    /**
     * 新建目录操作
     *
     * @param folderPath 文件夹路径
     */
    public boolean newCreateFolder(String folderPath) {
        boolean result = false;
        try {
            if (Validator.isNotNull(folderPath)) {
                File newFilePath = new File(folderPath);
                if (!newFilePath.exists()) {
                    result = newFilePath.mkdirs();
                }
            }
        } catch (Exception e) {
           log.error("newCreateFolder 新建目录操作出错ERROR KEY:" , e.getMessage());
        }
        return result;
    }

    /**
     * 创建新的文件
     *
     * @param filePathAndName 文件名称
     * @param fileContent     文件内容
     */
    public boolean newCreateFile(String filePathAndName, String fileContent) {
        boolean result = false;
        try {
            if (Validator.isNotNull(filePathAndName)) {
                File newFilePath = new File(filePathAndName);
                if (!newFilePath.exists()) {
                    newFilePath.createNewFile();
                }
                FileOutputStream fo = new FileOutputStream(filePathAndName);//"content_page.js");
                OutputStreamWriter osw = new OutputStreamWriter(fo, "UTF-8");
                PrintWriter out1 = new PrintWriter(osw);
                out1.println(fileContent);
                out1.close();
                osw.close();
                fo.close();
                result = true;
            }
        } catch (Exception e) {
            log.error("newCreateFile 新建文件操作出错ERROR KEY:{}" ,filePathAndName, e.getMessage());
        }
        return result;
    }

    /**
     * 创建新的文件
     *
     * @param filePathAndName 文件名称
     * @param fileContent     文件内容
     */
    public boolean newCreateFile(String filePathAndName, String fileContent, String coding) {
        boolean result = false;
        try {
            if (Validator.isNotNull(filePathAndName)) {
                File newFilePath = new File(filePathAndName);
                if (!newFilePath.exists()) {
                    newFilePath.createNewFile();
                }
                FileOutputStream fo = new FileOutputStream(filePathAndName);//"content_page.js");
                OutputStreamWriter osw = new OutputStreamWriter(fo, coding);
                PrintWriter out1 = new PrintWriter(osw);
                out1.println(fileContent);
                out1.close();
                osw.close();
                fo.close();
                result = true;
            }
        } catch (Exception e) {

            log.error("newCreateFile 新建文件操作出错ERROR KEY:{}" ,filePathAndName, e.getMessage());
        }
        return result;
    }

    /**
     * description: 获取指定文件夹下的文件列表
     *
     * @param filePath 文件夹路径
     * @return
     */
    public File[] getFileList(String filePath) {
        File file = new File(filePath);
        return file.listFiles();
    }

    /**
     * 返回文件名
     *
     * @param fileName
     * @return
     */
    public String getFileName(String fileName) {
        File file = new File(fileName);
        return file.getName();
    }

    /**
     * 格式化文件路径
     *
     * @param path 文件路径
     * @return
     */
    public String formatFilePath(String path) {
        if (Validator.isNull(path))
            return "";
        path = path.replace("\\", "/");
        path = path.replace("\\\\", "/");
        return path.replace("//", "/");
    }

}

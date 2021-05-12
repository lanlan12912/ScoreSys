package com.yelanlan.scoremanagersystem.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class FileUtils {
    private final static Logger logger = LogManager.getLogger();
    public static boolean writeImg(String imgFile,String imgType,String name,String path){
        try {
            File file =new File(path);
            if  (!file .exists()  && !file .isDirectory())
            {
                file .mkdirs();
            }
            String fullPath = path + "/" + name + imgType;
            BASE64Decoder decoder = new BASE64Decoder();
            String imgBase64 = imgFile.replace("data:image/png;base64,", "")
                    .replace("data:image/jpeg;base64,","")
                    .replace("data:image/jpg;base64,","");
            byte[] b = decoder.decodeBuffer(imgBase64);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(fullPath);
            out.write(b);
            out.flush();
            out.close();
            logger.info("保存了文件   "+fullPath);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteImg(String path,String name){
        try {
            File filePath =new File(path);
            //数组指向文件夹中的文件和文件夹
            File[] files=filePath.listFiles();
            //遍历文件和文件夹
            for(File file : files)
            {
                //如果是文件夹，递归查找
                if(file.isDirectory())
                    deleteImg(path,name);
                else if(file.isFile()){
                    //是文件的话，把文件名放到一个字符串中
                    String filename=file.getName();
                    //删除
                    if(filename.equals(name))
                    {
                        logger.info("文件 "+filename+" 被删除 ");
                        file.delete();
                        break;
                    }
                }
            }

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}

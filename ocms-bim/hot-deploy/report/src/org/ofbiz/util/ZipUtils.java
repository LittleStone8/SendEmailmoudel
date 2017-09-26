package org.ofbiz.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    
//    public static void doCompress__(File file,OutputStream destout) throws IOException{
//    		ZipOutputStream out = new ZipOutputStream(destout);
//            byte[] buffer = new byte[1024];
//            FileInputStream fis = new FileInputStream(file);
//            out.putNextEntry(new ZipEntry(file.getName()));
//            int len = 0 ;
//            // 读取文件的内容,打包到zip文件    
//            while ((len = fis.read(buffer)) > 0) {
//                out.write(buffer, 0, len);
//            }
//            out.write(0);
//            out.flush();
//            out.closeEntry();
//            fis.close();
//    }
//    
    
    
    public static boolean doCompress(File sourceFile,OutputStream destout){  
        boolean flag = false;  
        FileInputStream fis = null;  
        BufferedInputStream bis = null;  
        FileOutputStream fos = null;  
        ZipOutputStream zos = null;  
          
        if(sourceFile.exists() == false){  
            System.out.println("待压缩的文件目录："+sourceFile+"不存在.");  
        }else{  
            try {  
                        zos = new ZipOutputStream(destout);  
                        byte[] bufs = new byte[1024*10];  
                            //创建ZIP实体，并添加进压缩包  
                            ZipEntry zipEntry = new ZipEntry(sourceFile.getName());  
                            zos.putNextEntry(zipEntry);  
                            //读取待压缩的文件并写进压缩包里  
                            fis = new FileInputStream(sourceFile);  
                            bis = new BufferedInputStream(fis, 1024*10);  
                            int read = 0;  
                            while((read=bis.read(bufs, 0, 1024*10)) != -1){  
                                zos.write(bufs,0,read);  
                            }  
                        flag = true;  
            } catch (FileNotFoundException e) {  
                e.printStackTrace();  
                throw new RuntimeException(e);  
            } catch (IOException e) {  
                e.printStackTrace();  
                throw new RuntimeException(e);  
            } finally{  
                //关闭流  
                try {  
                    if(null != bis) bis.close();  
                    if(null != zos) zos.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                    throw new RuntimeException(e);  
                }  
            }  
        }  
        return flag;  
    }  
    
    
    
    
}
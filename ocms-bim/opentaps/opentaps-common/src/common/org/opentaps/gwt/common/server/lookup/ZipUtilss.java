package com.opensourcestrategies.crmsfa.content;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtilss {
    
  
  
  public static boolean doCompress(File sourceFile,OutputStream destout){  
      boolean flag = false;  
      FileInputStream fis = null;  
      BufferedInputStream bis = null;  
      ZipOutputStream zos = null;  
        
      if(sourceFile.exists() == false){  
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
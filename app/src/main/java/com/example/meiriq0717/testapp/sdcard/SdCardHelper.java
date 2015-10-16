package com.example.meiriq0717.testapp.sdcard;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * SdCard帮助类  用于保存用户头像（读、写操作）
 * Created by chenzhenwen@meiriq.com on 2015/10/9.
 */
public class SdCardHelper {
    private Context context;
    //应用程序缓存地址
    private static String appCache_Path = "";
    private File file;

    public SdCardHelper(Context context) {
        this.context = context;
        appCache_Path = context.getExternalCacheDir().getAbsolutePath();
    }

    //判断SdCard是否挂载
    public boolean hasSDcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    //将byte[]类型的数据保存在应用程序缓存目录下
    public boolean saveData2SdCard(byte[] data, String fileName) {
        file = new File(appCache_Path, fileName);
        BufferedOutputStream bos = null;
        if (hasSDcard() && !file.exists()) {
            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bos.write(data, 0, data.length);
                bos.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    //从SdCard中读取数据,返回byte[] 类型
    public byte[] loadDataFromSdCard(String fileName) {
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (hasSDcard()) {
            File file = new File(appCache_Path, fileName);
            if(file.exists()){
                try {
                    bis = new BufferedInputStream(new FileInputStream(file));
                    int len = 0;
                    byte[] bytes = new byte[1024 * 4];
                    while ((len = bis.read(bytes)) != -1) {
                        baos.write(bytes, 0, len);
                        baos.flush();
                    }
                    return baos.toByteArray();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                return null;
            }

            return  null;
        } else {
            return null;
        }
    }
}

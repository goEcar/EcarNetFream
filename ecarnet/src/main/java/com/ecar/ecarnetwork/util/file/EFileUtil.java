package com.ecar.ecarnetwork.util.file;

import android.app.Application;
import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okio.Buffer;

/**
 * 文件读写类
 *
 * @author Administrator
 */
public class EFileUtil {


    /**
     * 字符串转成流
     */
    public static InputStream[] getInputStreams(String... keys) {
        if (keys == null || keys.length == 0) {
            return null;
        }
        int size = keys.length;
        InputStream[] inputStreams = new InputStream[size];
        for (int i = 0; i < size; i++) {
            inputStreams[i] = new Buffer().writeUtf8(keys[i]).inputStream();
        }
        return inputStreams;
    }

    public static InputStream[] getAssetsInputStream(Context mContext, String... keys) {
        try {
            if (keys == null || keys.length == 0) {
                return null;
            }
            int size = keys.length;
            InputStream[] inputStreams = new InputStream[size];
            for (int i = 0; i < size; i++) {
                inputStreams[i] = mContext.getAssets().open(keys[i]);
            }
            return inputStreams;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 读取文件中字符串
     *
     * @param file 目标文件
     * @return 结果字符
     */
    public static String readTextFile(File file) throws Exception {
        StringBuilder result = new StringBuilder();
        InputStream instream = new FileInputStream(file);
        if (instream != null) {
            InputStreamReader inputreader = new InputStreamReader(instream);
            BufferedReader buffreader = new BufferedReader(inputreader);
            String line;
            //分行读取
            while ((line = buffreader.readLine()) != null) {
                result.append(line + "\n");
            }
            instream.close();
        }
        return result.toString();
    }

    /**
     * 读取文件中字符串
     *
     * @param instream 目标流
     * @return 结果字符
     */
    public static String readTextInputStream(InputStream instream) throws Exception {
        StringBuilder result = new StringBuilder();
        if (instream != null) {
            InputStreamReader inputreader = new InputStreamReader(instream);
            BufferedReader buffreader = new BufferedReader(inputreader);
            String line;
            //分行读取
            while ((line = buffreader.readLine()) != null) {
                result.append(line + "\n");
            }
            instream.close();
        }
        return result.toString();
    }

    /**
     * 向文件写入数据
     *
     * @param file 目标文件
     * @param data 字符数据
     */
    public static void writeTextFile(File file, String data) throws Exception {
        FileWriter filerWriter = new FileWriter(file, false);//后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
        BufferedWriter bufWriter = new BufferedWriter(filerWriter);
        bufWriter.write(data);
        bufWriter.newLine();
        bufWriter.close();
        filerWriter.close();
    }

    /**
     * 向文件写入数据
     *
     * @param path 目标文件
     * @param data 字符数据
     */
//    public final String File_Debug_Cache_Path = "/oneall/cache/debuglog";
//
//    public static synchronized void writeDebugTextFile(String data) {
//        FileWriter filerWriter = null;
//        BufferedWriter bufWriter = null;
//        try {
//            File cacheDir = new File(android.os.Environment.getExternalStorageDirectory(),
//                    "/oneall/cache/debuglog");// 有SD卡的
//            if (!cacheDir.exists())
//                cacheDir.mkdirs();
//            File file = new File(cacheDir, DateDeserializer.getCurrTime() + "_debug.txt");
//            filerWriter = new FileWriter(file, false);//后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
//            bufWriter = new BufferedWriter(filerWriter);
//            bufWriter.write(data);
//            bufWriter.flush();
//            bufWriter.newLine();
//        } catch (Exception e) {
//            e.printStackTrace();
//            TagUtil.showLogError("debug异常写入文件失败");
//        } finally {
//            try {
//                bufWriter.close();
//                filerWriter.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//                TagUtil.showLogError("debug异常写入文件失败");
//            }
//        }
//    }
}

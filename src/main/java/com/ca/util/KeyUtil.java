package com.ca.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class KeyUtil {

    /**
     * 从文件中输入流中加载公钥
     *
     * @throws Exception 加载公钥时产生的异常
     */
    public static String loadPublicKeyByFile(String file_path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file_path));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("公钥数据流读取错误");
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("公钥输入流为空");
        }
        return null;
    }

    /**
     * 从文件中加载私钥
     *
     * @return 是否成功
     * @throws Exception
     */
    public static String loadPrivateKeyByFile(String file_path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file_path));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("私钥数据读取错误");
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("私钥输入流为空");
        }
        return null;
    }
}

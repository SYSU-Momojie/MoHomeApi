package com.cn.momojie.blog;

import org.junit.Test;
import sun.security.provider.MD5;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JustTest {

    private static final SimpleDateFormat format = new SimpleDateFormat("ddyyyyMM");

    private static final String id = "20180604173605";

    @Test
    public void test() {
        String convertDate = format.format(new Date());
        String md5 = StringToMd5(id + convertDate + "1201811230010");
        System.out.println(md5);
    }


    public static String StringToMd5(String psw) {
        {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(psw.getBytes("UTF-8"));
                byte[] encryption = md5.digest();

                StringBuffer strBuf = new StringBuffer();
                for (int i = 0; i < encryption.length; i++) {
                    if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                        strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                    } else {
                        strBuf.append(Integer.toHexString(0xff & encryption[i]));
                    }
                }

                return strBuf.toString();
            } catch (Exception e) {
                return "";
            }
        }
    }
}

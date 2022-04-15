package com.faith.mygateway.security.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

/**
 * @author 86183
 */
public class RandomUtil {

    /**
     * 根据指定长度生成字母和数字的随机数
     * 0~9的ASCII为48~57
     * A~Z的ASCII为65~90
     * a~z的ASCII为97~122
     *
     * @param length length
     * @return 随机字符串和数字的组合
     */
    public static String randLetterNum(int length) {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        int data;
        for (int i = 0; i < length; i++) {
            int index = new Random().nextInt(3);
            switch (index) {
                case 0:
                    // 仅仅会生成0~9
                    data = rand.nextInt(10);
                    sb.append(data);
                    break;
                case 1:
                    // 保证只会产生65~90之间的整数
                    data = rand.nextInt(26) + 65;
                    sb.append((char) data);
                    break;
                case 2:
                    // 保证只会产生97~122之间的整数
                    data = rand.nextInt(26) + 97;
                    sb.append((char) data);
                    break;
                default:
                    break;
            }
        }
        return sb.toString();
    }


    /**
     * 随机生成 6 位随机验证码
     */
    public static String ran6Num() {
        StringBuilder num = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            num.append((int) (Math.random() * 9));
        }
        return num.toString();
    }

    /**
     * 用 org.apache.commons.lang 下的 RandomStringUtils 类生成随机字符串
     *
     * @param length length
     * @return 字符和数字随机组合的字符串
     */
    public static String randAlphaNum(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
}

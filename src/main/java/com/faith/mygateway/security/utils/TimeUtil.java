package com.faith.mygateway.security.utils;

/**
 * @author Leeko
 * @date 2022/3/29
 **/
public class TimeUtil {

    /**
     * 获取多少分之后的毫秒数
     *
     * @param min min
     * @return 毫秒数
     */
    public static long futureMillByMin(int min) {
        return Math.addExact(System.currentTimeMillis(), Math.multiplyExact(min, 60 * 1000));
    }

    public static void main(String[] args) {
        System.out.println(Math.addExact(Math.multiplyExact(3, 5), 15));
    }
}

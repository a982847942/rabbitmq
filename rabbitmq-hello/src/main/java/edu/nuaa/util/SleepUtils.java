package edu.nuaa.util;

import java.util.concurrent.TimeUnit;

/**
 * @author brain
 * @version 1.0
 * @date 2023/5/6 19:55
 */
public class SleepUtils {
    public static void sleep(int second){
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}

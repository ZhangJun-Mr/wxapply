package com.datax;

import java.time.LocalTime;

/**
 * @author Administrator
 */
public class EngineTest {

    public static void main(String[] args) {
        System.setProperty("datax.home", getCurrentClasspath());
        // 替换job中的占位符
        System.setProperty("now", LocalTime.now().toString());
        String[] datxArgs = {"-job", getCurrentClasspath() + "/job/stream2stream.json", "-mode", "standalone", "-jobid", "-1"};
        try {
            // Engine.entry(datxArgs);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static String getCurrentClasspath() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String currentClasspath = classLoader.getResource("").getPath();
        // 当前操作系统
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows")) {
            // 删除path中最前面的/
            currentClasspath = currentClasspath.substring(1);
        }
        return currentClasspath;
    }
}

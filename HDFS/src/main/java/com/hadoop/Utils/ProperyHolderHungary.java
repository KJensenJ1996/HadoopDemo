package com.hadoop.Utils;

import java.io.IOException;
import java.util.Properties;

//单例设计模式，方式一：饿汉单例
public class ProperyHolderHungary {
    private static Properties prop =new Properties();

    static {
        try {
            prop.load(ProperyHolderHungary.class.getResourceAsStream("collect.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Properties getProps() throws IOException {
        return prop;
    }
}

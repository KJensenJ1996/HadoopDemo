package com.hadoop.Utils;

import java.io.IOException;
import java.util.Properties;

public class ProperyHolderLazy {
    private static Properties prop =null;

    public static  Properties getProps() throws IOException {
        if(prop==null){
            synchronized (ProperyHolderLazy.class){
                if(prop==null){
                    prop=new Properties();
                    prop.load(ProperyHolderHungary.class.getResourceAsStream("collect.properties"));
                }
            }
        }
        return prop;
    }
}

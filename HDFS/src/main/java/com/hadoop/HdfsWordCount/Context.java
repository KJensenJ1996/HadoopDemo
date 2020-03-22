package com.hadoop.HdfsWordCount;

import java.util.HashMap;

public class Context {

    private HashMap<Object,Object> contextMap =new HashMap<Object, Object>();

    public void write(Object key,Object value){
        contextMap.put(key,value);
    }

    public Object get(Object key) {
        return contextMap.get(key);
    }

    public HashMap<Object,Object> getContextMap(){
        return contextMap;
    }
}

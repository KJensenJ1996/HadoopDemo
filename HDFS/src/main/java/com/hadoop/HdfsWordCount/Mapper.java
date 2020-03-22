package com.hadoop.HdfsWordCount;

public interface Mapper {
    public void map(String line, Context context);
}

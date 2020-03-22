package com.hadoop.HdfsWordCount;

public class CaseIgnorWordCountMapper implements Mapper{
    public void map(String line, Context context) {
        String[] words = line.toUpperCase().split(" ");
        for (String word : words) {
            Object value = context.get(word);
            if (null == value) {
                context.write(word, 1);
            } else {
                Integer v=(Integer) value;
                context.write(word, v+1);
            }
        }
    }
}

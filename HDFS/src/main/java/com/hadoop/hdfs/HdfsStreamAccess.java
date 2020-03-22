package com.hadoop.hdfs;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.Before;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URI;

public class HdfsStreamAccess {
    /*
    *用流的方式来操作hdfs上的文件
    * 可以实现读取指定偏移量范围的数据
     */


    FileSystem fs=null;

    @Before
    public void init() throws Exception {
        Configuration conf=new Configuration();
        conf.set("dfs.blocksize","64m");
        fs = FileSystem.get(new URI("hdfs://hdp-01:9000/"), conf, "root");
    }

    //查看文件内容：
    public void testOpen() throws Exception{
        BufferedReader bf=new BufferedReader(new InputStreamReader(fs.open(new Path("/"))));
        String s;
        while ((s=bf.readLine())!=null){
            System.out.println(s);
        }
    }

    //通过流的方式上传文件
    public void testSteamupload() throws Exception{
        FileInputStream inputStream=new FileInputStream("D:/you.txt");
        FSDataOutputStream outputStream=fs.create(new Path("/you.txt"),false);
        IOUtils.copy(inputStream,outputStream);

    }

}

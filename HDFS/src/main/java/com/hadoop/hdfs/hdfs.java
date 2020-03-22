package com.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.Before;
import org.junit.Test;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.net.URI;
import java.sql.SQLOutput;
import java.util.logging.Logger;


public class hdfs {

    FileSystem fs=null;
    Logger log=Logger.getLogger("logRollingFile");

    public static void main(String[] args) throws Exception {

    }

    @Before
    public void init() throws Exception {
        Configuration conf=new Configuration();
        conf.set("dfs.blocksize","64m");
        fs = FileSystem.get(new URI("hdfs://hdp-01:9000/"), conf, "root");
    }

    //从hdfs把文件拷贝下来
    @Test
    public void testGet() throws IOException {
        fs.copyToLocalFile(new Path("/a.txt"),new Path("D:/"));
        fs.close();
    }

    //从本地把文件拷贝到HDFS中
    @Test
    public void testSend() throws IOException {
        fs.copyFromLocalFile(new Path("D:/a.txt"),new Path("/"));
        fs.close();
    }

    //查看文件信息
    @Test
    public void testListFile() throws IOException {
       RemoteIterator<LocatedFileStatus> remoteIterator=fs.listFiles(new Path("/"),true);
        while (remoteIterator.hasNext()){
            LocatedFileStatus file=remoteIterator.next();
            System.out.println("文件名:"+file.getPath());
            System.out.println("块"+file.getBlockLocations());
            System.out.println("大小"+file.getLen());
        }
    }

    //在hdfs中改名移动=改名
    public void testRename() throws IOException {
        fs.rename(new Path("/a.txt"),new Path("/abc.txt"));
        fs.close();
    }

    //创建文件夹
    public void testMkdirs() throws IOException {
        fs.mkdirs(new Path("/a/b/c"));
        fs.close();
    }

    //删除文件
    public void testDelet() throws Exception{
        //是否递归删除
        fs.delete(new Path("/a"),false);
    }

    //查看当前目录下的文件
    public void testList() throws IOException{
        FileStatus[] ll=fs.listStatus(new Path("/a/"));
        for(FileStatus status:ll){
            System.out.println("文件全路径"+status.getPath());
            System.out.println(status.isDirectory()?"这是一个目录":"这是文件");
        }
    }

    //读取hdfsf的文件的内容
    @Test
    public void testReadData() throws IOException {
        FSDataInputStream fsDataInputStream = fs.open(new Path("/a.txt"));
        byte[] bytes=new byte[1024];
        fsDataInputStream.read(bytes);
        System.out.println(new String(bytes));

        //一次读一行（只能从文件的最开始读）
        BufferedReader br=new BufferedReader(new InputStreamReader(fsDataInputStream));
        String s=null;
        while((s=br.readLine())!=null){
            System.out.println(s);
        }

        //从文件的某个位置开始读，可以定义读的长度
        //从偏移量为12开始读（0开始）
        fsDataInputStream.seek(12);
        byte[] bytes1=new byte[1024];
        fsDataInputStream.read(bytes1);
        System.out.println(new String(bytes1));

        //作业题：用本例知识，实现读取一个文本文件中的指定block块中的所有数据

        br.close();
        fsDataInputStream.close();
        fs.close();
    }

    //往hdfs写数据
    @Test
    public void testWriteData() throws IOException {
        //是否覆盖
        FSDataOutputStream fsDataOutputStream = fs.create(new Path("/yy.jpg"), true);
        byte[] bytes=new byte[1024];
        fsDataOutputStream.write(bytes);

        //从本地磁盘读了之后，写到hdfs中
        //BufferedReader bf(字符流)

        //字节流
        FileInputStream inputStream = new FileInputStream("D/yy.jpg");
        byte[] bytes1=new byte[1024];
        int read=0;
        while ((read=inputStream.read())!=-1){
            fsDataOutputStream.write(bytes1,0,read);
        }

        inputStream.close();
        fsDataOutputStream.close();
        fs.close();
    }


}

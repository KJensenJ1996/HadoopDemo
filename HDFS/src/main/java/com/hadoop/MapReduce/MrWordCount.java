package com.hadoop.MapReduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.util.Iterator;

/**
 * @Author: Jensen
 * @Date: 2020/3/23 11:47
 */
public class MrWordCount {

    static FileSystem fs=null;

    public static void main(String[] args) throws Exception{
        fs=getFileSystem();

        Configuration conf=new Configuration();
        conf.set("mapreduce.framwork.name","yarn");
        conf.set("yarn.resourcemanager.hostname","indata-172-19-221-68.indata.com");

        Job job = Job.getInstance(conf);

        //1、封装参数：jar包所在的位置
        //job.setJar("d:/wc.jar");
        job.setJarByClass(MrWordCount.class);

        //2、封装参数：本次job调用的Mapper实现类，Reduce实现类产生的结果数据的key,value类型
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        //3、封装参数：本次job调用的Mapper实现类，Reduce实现类产生的结果数据的key,value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        //4、本次job要处理的输入数据所在路径

        Path outputPath = new Path("/wordcount/output");
        if(!fs.exists(outputPath)){
            fs.mkdirs(outputPath);
        }

        //org.apach.hadoop.mapreduce.lib.input
        FileInputFormat.setInputPaths(job,new Path("/wordcount/input"));
        FileOutputFormat.setOutputPath(job,outputPath);

        //5、封装参数：想要启动的reducetask的数量（决定了输出的文件的多少）
        job.setNumReduceTasks(2);

        //向yarn提交
        //job.submit();//提交之后就不管
        boolean wait = job.waitForCompletion(true);  //客户端等着跑
        if(wait){
            System.exit(0);
        }
        else {
            System.exit(-1);
        }
    }

    public static FileSystem getFileSystem() throws Exception{

        String principal="jkyl-realm@INDATA.COM";
        Configuration conf=new Configuration();
        System.out.println(conf.get("fs.defaultFS"));
        conf.set("fs.defaultFS","hdfs://cluster1");
        conf.set("hadoop.security.authentication","kerberos");
        conf.set("hadoop.security.authorization","true");
        String krbStr=Thread.currentThread().getContextClassLoader().getResource("krb5.conf").getFile();
        String userKeytab=Thread.currentThread().getContextClassLoader().getResource("jkyl-realm.keytab").getFile();

        System.clearProperty("java.security.krb5.conf");

        System.setProperty("java.security.krb5.conf",krbStr);

        System.out.println("current user principle   ===" + principal);
        System.out.println("current user keytab path ===" + userKeytab);
        System.out.println("current user krb5 path   ===" + krbStr);


        UserGroupInformation.setConfiguration(conf);
        UserGroupInformation.loginUserFromKeytab(principal,userKeytab);

        fs = FileSystem.get(conf);
        return fs;
    }

    public static class WordCountMapper extends Mapper<LongWritable,Text,Text,IntWritable>{
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] words = value.toString().split(" ");
            for(String word:words){
                context.write(new Text(word),new IntWritable(1));
            }
        }
    }

    public static class WordCountReducer extends Reducer{
        @Override
        protected void reduce(Object key, Iterable values, Context context) throws IOException, InterruptedException {
            int sum=0;
            Iterator<IntWritable> iterator=values.iterator();
            while (iterator.hasNext()){
                IntWritable next = iterator.next();
                sum+=next.get();
            }
            context.write(key,new IntWritable(sum));
        }
    }

}

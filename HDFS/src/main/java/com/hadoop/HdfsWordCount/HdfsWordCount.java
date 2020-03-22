package com.hadoop.HdfsWordCount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class HdfsWordCount {

    public static void main(String[] args) throws Exception {
        /*去hdfs中读文件：一次读一行
            调用一个方法对每一行进行业务处理
            将这一行的处理结果放入一个缓存
            调用一个方法将缓存中的结果数据输出到HDFS结果文件
        */

        Properties props=new Properties();
        props.load(HdfsWordCount.class.getClassLoader().getResourceAsStream("job.properties"));

        Path input=new Path(props.getProperty("INPUT_PATH"));
        Path output=new Path(props.getProperty("OUTPUT_PATH"));

        Class<?> mapper_class = Class.forName(props.getProperty("MAPPER_CLASS"));
        Mapper mapper=(Mapper)mapper_class.newInstance();

        Context context =new Context();

        FileSystem fs = FileSystem.get(new URI("hdfs://hdp-01:9000"), new Configuration(), "root");
        RemoteIterator<LocatedFileStatus> iter = fs.listFiles(input,false);

        while (iter.hasNext()){
            LocatedFileStatus file =iter.next();
            FSDataInputStream in = fs.open(file.getPath());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line=null;
            //逐行读取
            while ((line=br.readLine())!=null){
                //调用一个方法对每一行进行业务处理
                mapper.map(line,context);
            }
            br.close();
            in.close();
        }
        //输出结果
        HashMap<Object,Object> contextMap=context.getContextMap();

        if(!fs.exists(output)){
            fs.mkdirs(output);
        }
        else{
            throw new RuntimeException("指定的输出目录已存在，请更换");
        }

        FSDataOutputStream outputStream = fs.create(new Path(output,new Path("res.dat")));

        Set<Map.Entry<Object,Object>> entrySet=contextMap.entrySet();
        for(Map.Entry<Object,Object> entry:entrySet){
            outputStream.write((entry.getKey().toString()+"\t"+entry.getValue()+"\n").getBytes());
        }

        outputStream.close();
        fs.close();

        System.out.println("数据统计完成");


    }
}

package com.hadoop.CollectTask;

import com.hadoop.Utils.Constants;
import com.hadoop.Utils.ProperyHolderLazy;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CollectTask extends TimerTask {

    @Override
    public void run() {
        /*
        定时探测日志目录
        获取需要采集文件
        移动这些文件到待上传临时目录
        遍历待上传目录中的各文件，逐一传输到HDFS的目标路径，同时将传输完成的文件移动到备份目录
         */
        try {
        //构造一个log4j日志对象
        Logger logger=Logger.getLogger("logRollingFile");


        Properties props= ProperyHolderLazy.getProps();


        //获取本次采集的日期：
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH");
        String date = simpleDateFormat.format(new Date());

        File srcDir=new File(props.getProperty(Constants.LOG_SOURCE_DIR));
        File[] listFiles=srcDir.listFiles(new FilenameFilter() {

            public boolean accept(File dir,String name) {
                if(name.startsWith("access.log")){
                    return true;
                }
                return false;
            }
        });

        logger.info("探测到如下文件需要采集："+ Arrays.toString(listFiles));

        //移动到待上传目录
        File toUploadDir=new File(props.getProperty(Constants.LOG_TOUPLOAD_DIR));
        for(File file:listFiles){
            file.renameTo(toUploadDir);
        }
        logger.info("上述文件上传到了："+toUploadDir.getAbsolutePath());

        //构造一个hdfs客户端对象

            FileSystem fs=FileSystem.get(new URI(props.getProperty(Constants.HDFS_URI)),new Configuration(),"root");

            //检查HDFS中的日期目录是否存在
            Path hdfsDestPath = new Path(props.getProperty(Constants.HDFS_DEST_BASE_DIR) + date);
            if(!fs.exists(hdfsDestPath)){
                fs.mkdirs(hdfsDestPath);
            }

            //检查本地的备份目录是否存在
            File backupDir = new File(props.getProperty(Constants.LOG_BACKUP_BASE_DIR) + date+"/");
            if(!backupDir.exists()){
                backupDir.mkdirs();
            }

            File[] toUploadFiles=toUploadDir.listFiles();
            for(File file:toUploadFiles){
                //上传到hdfs并改名
                Path destPath = new Path(hdfsDestPath + props.getProperty(Constants.HDFS_FILE_PREFIX) + UUID.randomUUID() + props.getProperty(Constants.HDFS_FILE_SUFFIX));
                fs.copyFromLocalFile(new Path(file.getAbsolutePath()), destPath);
                logger.info("文件传输到HDFS完成："+file.getAbsolutePath()+"--->"+destPath);

                //将传输完成的文件移动到备份目录
                FileUtils.moveFileToDirectory(file,backupDir,true);
                //file.renameTo(backupDir);

                logger.info("文件传输到备份目录完成："+file.getAbsolutePath()+"--->"+backupDir);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

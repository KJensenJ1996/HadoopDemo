package com.hadoop.CollectTask;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;
import java.util.logging.Logger;

public class BackupCleanTask extends TimerTask {

    @Override
    public void run() {

        Logger logger=Logger.getLogger("loggerRollingFile");

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd-HH");
        long now=new Date().getTime();
        //探测备份目录
        File backupBaseDir=new File("d:/logs/backup/");
        File[] dayBackDir=backupBaseDir.listFiles();
        for(File dir:dayBackDir){
            try {
                long time=date.parse(dir.getName()).getTime();
                if (now-time>24*60*60*1000){
                    FileUtils.deleteDirectory(dir);
                    logger.info("以下目录已经删除："+dir.getAbsolutePath());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}

package com.hadoop.CollectTask;

import java.util.Timer;

public class DataCollectMain {
    public static void main(String[] args) {
        Timer timer=new Timer();
        //数据采集任务  开启之后立即执行，没隔一小时执行一次
        timer.schedule(new CollectTask(),0,60*60*1000);

        //备份数据清理任务
        timer.schedule(new BackupCleanTask(),0,60*60*1000);
    }
}

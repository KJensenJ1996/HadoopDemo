package com.hadoop.Utils;

public class Constants {

    /*
    #本地配置
LOG_SOURCE_DIR=d:/logs/acesslog/
LOG_TOUPLOAD_DIR=d:/logs/toupload/
LOG_BACKUP_BASE_DIR=d:/logs/backup/
LOG_BACKUP_TIMEOUT=24
LOG_LEAGAL_PRIFIX=access.log.

#HDFS配置
HDFS_URI=hdfs://hdp-01:9000/
HDFS_DEST_BASE_DIR=/logs/
HDFS_FILE_PREFIX=access_log_
HDFS_FILE_SUFFIX=.log

     */

    public static final String LOG_SOURCE_DIR="LOG_SOURCE_DIR";
    public static final String LOG_TOUPLOAD_DIR="LOG_TOUPLOAD_DIR";
    public static final String LOG_BACKUP_BASE_DIR="LOG_BACKUP_BASE_DIR";
    public static final String LOG_BACKUP_TIMEOUT="LOG_BACKUP_TIMEOUT";
    public static final String LOG_LEAGAL_PRIFIX="LOG_LEAGAL_PRIFIX";
    public static final String HDFS_URI="HDFS_URI";
    public static final String HDFS_DEST_BASE_DIR="HDFS_DEST_BASE_DIR";
    public static final String HDFS_FILE_PREFIX="HDFS_FILE_PREFIX";
    public static final String HDFS_FILE_SUFFIX="HDFS_FILE_SUFFIX";
}

package com.my.utils;

import java.util.Date;
import java.util.Random;

/**
 * ID生成
 */
public class IdGenUtil {
    private long workerId;
    private long datacenterId;
    private long sequence = 0L;
    private long twepoch = 1288834974657L; //Thu, 04 Nov 2010 01:42:54 GMT
    private long workerIdBits = 5L; //节点ID长度
    private long datacenterIdBits = 5L; //数据中心ID长度
    private long maxWorkerId = -1L ^ (-1L << workerIdBits); //最大支持机器节点数0~31，一共32个
    private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits); //最大支持数据中心节点数0~31，一共32个
    private long sequenceBits = 12L; //序列号12位
    private long workerIdShift = sequenceBits; //机器节点左移12位
    private long datacenterIdShift = sequenceBits + workerIdBits; //数据中心节点左移17位
    private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits; //时间毫秒数左移22位
    private long sequenceMask = -1L ^ (-1L << sequenceBits); //4095
    private long lastTimestamp = -1L;

    private static class IdGenHolder {
        private static final IdGenUtil instance = new IdGenUtil();
    }

    @Deprecated
    public static IdGenUtil get() {
        return IdGenHolder.instance;
    }

    private IdGenUtil() {
        this(0L, 0L);
    }

    private IdGenUtil(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException("worker Id can't be greater than %d or less than 0 " + maxWorkerId);
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException("datacenter Id can't be greater than %d or less than 0 " + maxDatacenterId);
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 获取分布式全局唯一ID
     * @return
     */
    @Deprecated
    public synchronized long nextId() {
        long timestamp = timeGen(); //获取当前毫秒数
        //如果服务器时间有问题(时钟后退) 报错。
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    "Clock moved backwards.  Refusing to generate id for %d milliseconds" + (lastTimestamp - timestamp));
        }
        //如果上次生成时间和当前时间相同,在同一毫秒内
        if (lastTimestamp == timestamp) {
            //sequence自增，因为sequence只有12bit，所以和sequenceMask相与一下，去掉高位
            sequence = (sequence + 1) & sequenceMask;
            //判断是否溢出,也就是每毫秒内超过4095，当为4096时，与sequenceMask相与，sequence就等于0
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp); //自旋等待到下一毫秒
            }
        } else {
            sequence = 0L; //如果和上次生成时间不同,重置sequence，就是下一毫秒开始，sequence计数重新从0开始累加
        }
        lastTimestamp = timestamp;
        return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift) | sequence;
    }

    /**
     * 获取分布式全局唯一ID
     * @return
     */
    public static long getId(){
       return get().nextId();
    }

    /**
     * 生成第三方流水，格式为yyMMddHHmmss+n位随机数
     * @param ranBit 随机位数，必须>=1
     * @return
     */
    public static String genThirdRequestSeq(int ranBit){
        String  yyMMddHHmmss = DateUtil.format(new Date(),DateUtil.shortFullPattern);
        String format = "%0"+ranBit+"d";
        if(ranBit<=0){
            ranBit=1;
        }
        return yyMMddHHmmss+String.format(format,new Random().nextInt((int)Math.pow(10, ranBit)) );
    }

    /**
     * 日期[yyyymmdd]+系统编码[2]+业务编码[2]+分库标识[2]+分表标识[2]+全局唯一id[18]
     * @param sysCode
     * @param bizCode
     * @param dbCode
     * @param tbCode
     * @return
     */
    public static String genOrderId(String sysCode,String bizCode,String dbCode,String tbCode){
        return DateUtil.getCurrent(DateUtil.PATTERN_YYYYMMDD)+sysCode+bizCode+dbCode+tbCode+IdGenUtil.get().nextId();
    }

    /**
     * 日期[yyyymmdd]+系统编码[2]+业务编码[2]+全局唯一id[18]
     * @param sysCode
     * @param bizCode
     * @return
     */
    public static String genOrderId(String sysCode,String bizCode){
        return genOrderId(sysCode,bizCode,"00","00");
    }


    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }
}
package com.lpy.news.service;


/**
 * 雪花算法生成id
 */
public class SnowService {
    /**
     * 机器占用位数，范围是0-63
     */
    private long machineBit = 5L;
    /**
     * 业务占用位数，范围0-255
     */
    private long businessBit = 7L;
    /**
     * 序列占用位数，范围0-2047
     */
    private long sequenceBit = 10L;
    /**
     * 传入的机器数值
     */
    private long machineNo;
    /**
     * 传入业务数值
     */
    private long businessNo;
    /**
     * 同一毫秒序列号
     */
    private long sequenceNo;
    /**
     * 最后获取id的时间戳
     */
    private long lastTimeStamp = -1L;
    /**
     * 时间位需要左移的位数
     * 等于机器位数+业务位数+序列位数
     * 即等于低位的总位数
     */
    private long timeStampShift = machineBit + businessBit + sequenceBit;
    /**
     * 机器位需要左移的位数
     * 等于业务位+序列位
     */
    private long machineShift = businessBit + sequenceBit;
    /**
     * 业务位需要左移的位数
     * 等于序列位
     */
    private long businessShift = sequenceBit;
    /**
     * sequence的最大值
     */
    private long maxSequence = -1L ^ (-1L << sequenceBit);
    /**
     * 开始使用时间
     */
    private long startStamp = 1624348225278L;
    public SnowService(long machineNo, long businessNo) {
        this.machineNo = machineNo;
        this.businessNo = businessNo;
    }

    public synchronized long getId() {
        long nowStamp = System.currentTimeMillis();
        //当前时间小于上次处理时间，表示时间回拨，直接抛出异常
        if(nowStamp < lastTimeStamp) {
            throw new RuntimeException("时间小于上次获取id时间");
        }
        if(nowStamp == lastTimeStamp) {
            //超过最大值把sequenceNo重置为0
            sequenceNo = (sequenceNo + 1) & maxSequence;
            if(sequenceNo == 0) {
                nowStamp = waitNextMillis(nowStamp);
            }
        } else {
            sequenceNo = nowStamp & 1;
            //sequenceNo =0;这种每毫秒都从0开始会导致如果没有冲突生成的id都是偶数
        }
        lastTimeStamp = nowStamp;
        long id = (nowStamp - startStamp) << timeStampShift |
                machineNo << machineShift |
                businessNo << businessShift |
                sequenceNo;
        return id;
    }

    /**
     * 等待到下一毫秒
     * @param nowStamp
     * @return
     */
    private long waitNextMillis(long nowStamp) {
        long newStamp = System.currentTimeMillis();
        while(nowStamp >= newStamp) {
            newStamp = System.currentTimeMillis();
        }
        return newStamp;
    }

//    public static void main(String[] args) throws InterruptedException {
//        SnowService snowService = new SnowService(1, 1);
//        for(int i = 0; i < 100; i++) {
//            Thread t = new Thread(() -> {
//                System.out.println(snowService.getId());
//            });
//            t.start();
//        }
//    }
}


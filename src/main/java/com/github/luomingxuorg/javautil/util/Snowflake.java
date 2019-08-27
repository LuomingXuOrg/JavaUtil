/*
 *  Copyright 2018-2019 LuomingXuOrg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  Author : Luoming Xu
 *  File Name : SnowFlake.java
 *  Url: https://github.com/LuomingXuOrg/JavaUtil
 */

package com.github.luomingxuorg.javautil.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * Adapted from Twitter's Snowflake
 * http://github.com/twitter/snowflake
 */
@Slf4j
public class Snowflake
{
    //2019-08-27 14:05:13 +0800
    //应当设为项目开始时间
    private static long epoch = 1566885913535L;

    private static final long workerIdBits = 5L;
    private static final long dataCenterIdBits = 5L;
    private static final long sequenceBits = 12L;

    private static final long workerIdShift = sequenceBits;
    private static final long dataCenterIdShift = sequenceBits + workerIdBits;
    private static final long timestampShift = sequenceBits + workerIdBits + dataCenterIdBits;

    private static final long sequenceMask = ~(-1L << sequenceBits);
    private static final long workerIdMask = ~(-1L << workerIdBits);
    private static final long dataCenterIdMask = ~(-1L << dataCenterIdBits);

    private final long workerId;
    private final long dataCenterId;

    private long sequence = 0L;
    private long lastTimestamp = -1L;

    /**
     * sequence随机种子(兼容低并发下, sequence均为0的情况)
     */
    private static final Random RANDOM = new Random();

    /**
     * 构造函数
     *
     * @param dataCenterId      可以使用ip地址来标识
     * @param workerId          machineID, 最好是对工作的机器有一个统一的编号, 使用这个编号作为工作ID
     * @param projectStartEpoch 设置为项目开始的时候的时间戳, 则可以在此时间之后使用69年不会有重复ID
     */
    public Snowflake(final long dataCenterId, final long workerId, final long projectStartEpoch)
    {
        this.dataCenterId = dataCenterId & dataCenterIdMask;
        this.workerId = workerId & workerIdMask;
        epoch = projectStartEpoch;
    }

    public synchronized long nextId()
    {
        long timestamp = timestamp();

        if (lastTimestamp == timestamp)
        {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0)
            {
                log.info("rollover");
                sequence = RANDOM.nextInt(100);
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        else
        {
            sequence = RANDOM.nextInt(100);
        }

        if (timestamp < lastTimestamp)
        {
            log.warn("Clock is moving backwards. Rejecting requests until {}.", lastTimestamp);

            if (timeBackwardsCallback(timestamp, lastTimestamp))
            {
                return nextId();
            }
        }

        lastTimestamp = timestamp;
        return ((timestamp - epoch) << timestampShift)
                | (dataCenterId << dataCenterIdShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    private long tilNextMillis(long lastTimestamp)
    {
        long timestamp = timestamp();
        while (timestamp <= lastTimestamp)
        {
            timestamp = timestamp();
        }
        return timestamp;
    }

    private long timestamp()
    {
        return System.currentTimeMillis();
    }

    /**
     * default callback
     * 可以自定义在子类中修改, 只需结果为true即可生成nextId()
     *
     * @param timestamp     当前时间
     * @param lastTimestamp 上次生成Id的时间
     * @return callback是否处理成功
     */
    protected boolean timeBackwardsCallback(long timestamp, long lastTimestamp)
    {
        long delay = lastTimestamp - timestamp;
        log.warn("Will wait {}ms to see if it could generate. ", delay);
        //如果偏差比较小, 则等待, 设定的最大偏差为618ms
        if (delay < 618)
        {
            try
            {
                Thread.sleep(delay);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        timestamp = timestamp();

        //如果还没好, 报警
        if (timestamp < lastTimestamp)
        {
            log.error("Wait {}ms is not enough. Refusing to generate id.", delay);
            throw new IllegalStateException("Clock moved backwards. Refusing to generate id for " + (lastTimestamp - timestamp) + " milliseconds");
        }
        else
        {
            return true;
        }
    }
}

package com.sequoia.util;

import com.sequoia.infrastructure.common.ProducerPromise;
import com.sequoia.infrastructure.service.impl.TinyUrlStore;
import com.sequoia.infrastructure.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberModifier;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

/**
 * Descript:
 * File: com.sequoia.util.SnowflakeIdWorkerTest
 * Author: daishengkai
 * Date: 2022/3/30 23:00
 * Copyright (c) 2022,All Rights Reserved.
 */
@Slf4j
public class SnowflakeIdWorkerTest {

    @Test
    public void idGenerateTest() {
        log.info("当前时间:{}", System.currentTimeMillis());
        long startTime = System.nanoTime();
        for (int i = 0; i < 50000; i++) {
            long id = SnowflakeIdWorker.nextId();
            log.info("生成的id: {}", id);
        }
        log.info("生成id耗时: {}", (System.nanoTime()-startTime)/1000000+"ms");
    }

    @Test
    public void setMachineIdTest() {
        log.info("当前时间:{}", System.currentTimeMillis());
        long startTime = System.nanoTime();
        SnowflakeIdWorker.setMachineId(10001L);
        for (int i = 0; i < 10; i++) {
            long id = SnowflakeIdWorker.nextId();
            log.info("生成的id: {}", id);
        }
        SnowflakeIdWorker.setMachineId(-1L);
        log.info("生成id耗时: {}", (System.nanoTime()-startTime)/1000000+"ms");
    }

    @Test
    public void testcompleteExceptionally() {
        ProducerPromise<String> promise = new ProducerPromise<>(Thread.currentThread());
        promise.shouldProduce();

        promise.shouldProduce();

        Executors.newFixedThreadPool(5).execute(() -> {
            promise.shouldProduce();
        });

        promise.completeExceptionally(new RuntimeException());
    }

    @BeforeClass
    public void initBeforetestError() {
        MemberModifier.stub(PowerMockito.method(InetAddress.class, "getLocalHost")).toThrow(new UnknownHostException());
    }

    @Test
    public void testError() {
        try {
            long id = SnowflakeIdWorker.nextId();
        } catch (Exception e) {
            log.error("异常", e);
        }

        MemberModifier.stub(PowerMockito.method(SnowflakeIdWorker.class, "getNowSeconds")).toReturn(-100L);
        SnowflakeIdWorker.nextId();
    }
}

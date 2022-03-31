package com.sequoia.service;

import com.google.common.collect.Sets;
import com.sequoia.infrastructure.service.impl.CodeGenerator;
import com.sequoia.infrastructure.service.impl.TinyUrlStore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Descript:
 * File: com.sequoia.service.TinyUrlStoreTest
 * Author: daishengkai
 * Date: 2022/3/31
 * Copyright (c) 2022,All Rights Reserved.
 */
@Slf4j
@SpringBootTest
public class TinyUrlStoreTest {

    @SpyBean
    private TinyUrlStore tinyUrlStore;

    @SpyBean
    private ICodeGenerator codeGenerator;

    @Test
    public void testgetTinyUrlFuture() {
        for (String originUrl : Sets.newHashSet(null, "null", "test.com", "test.cn/fsggssg11")) {
            try {
                String tinyCode = tinyUrlStore.getTinyUrlFuture(originUrl).get();
                log.info("{}  -  {}", originUrl, tinyCode);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    public void testError() throws ExecutionException, InterruptedException {
        String originUrl = "test.com";
        Mockito.when(codeGenerator.generateTinyCode(originUrl)).thenThrow(new RuntimeException());
        try {
            String tinyCode = tinyUrlStore.getTinyUrlFuture(originUrl).get();
        } catch (Exception e) {
            log.error("异常", e);
        }
    }

    @Test
    public void testsaveTinyOriginCodeMapping() throws InvocationTargetException, IllegalAccessException, InterruptedException {
        String originUrl = "test.com";
        Lock lock = Mockito.mock(Lock.class);
        Mockito.when(tinyUrlStore.getLock(Mockito.anyString())).thenReturn(lock);
        Mockito.when(lock.tryLock(1000, TimeUnit.MILLISECONDS)).thenReturn(false);
        try {
            String tinyCode = tinyUrlStore.getTinyUrlFuture(originUrl).get();
        } catch (Exception e) {
            log.error("异常", e);
        }
    }

    @Test
    public void testError1() throws ExecutionException, InterruptedException {
        String originUrl = "test.com";
        Lock lock = Mockito.mock(Lock.class);
        Mockito.when(tinyUrlStore.getLock(Mockito.anyString())).thenReturn(lock);

        Mockito.when(lock.tryLock(1000, TimeUnit.MILLISECONDS)).thenReturn(false);
        try {
            String tinyCode = tinyUrlStore.getTinyUrlFuture(originUrl).get();
        } catch (Exception e) {
            log.error("异常", e);
        }

        Mockito.when(lock.tryLock(1000, TimeUnit.MILLISECONDS)).thenThrow(new InterruptedException());
        try {
            String tinyCode = tinyUrlStore.getTinyUrlFuture(originUrl).get();
        } catch (Exception e) {
            log.error("异常", e);
        }

        CompletableFuture<String> future = new CompletableFuture<>();
        future.completeExceptionally(new InterruptedException());
        MemberModifier.stub(PowerMockito.method(TinyUrlStore.class, "generateTinyCodeFuture")).toReturn("originUrl");

        try {
            String tinyCode = tinyUrlStore.getTinyUrlFuture(originUrl).get();
        } catch (Exception e) {
            log.error("异常", e);
        }

        MemberModifier.stub(PowerMockito.method(TinyUrlStore.class, "getOriginUrl")).toReturn(null);
        MemberModifier.stub(PowerMockito.method(StringUtils.class, "equals", String.class, String.class)).toReturn(false);
        try {
            String tinyCode = tinyUrlStore.getTinyUrlFuture(originUrl).get();
        } catch (Exception e) {
            log.error("异常", e);
        }

        MemberModifier.stub(PowerMockito.method(TinyUrlStore.class, "getOriginUrl")).toReturn(originUrl);
        MemberModifier.stub(PowerMockito.method(StringUtils.class, "equals", String.class, String.class)).toReturn(true);
        try {
            String tinyCode = tinyUrlStore.getTinyUrlFuture(originUrl).get();
        } catch (Exception e) {
            log.error("异常", e);
        }

        MemberModifier.stub(PowerMockito.method(TinyUrlStore.class, "getOriginUrl")).toReturn(null);
        MemberModifier.stub(PowerMockito.method(StringUtils.class, "equals", String.class, String.class)).toReturn(false);
        MemberModifier.stub(PowerMockito.method(TinyUrlStore.class, "saveTinyOriginCodeMapping")).toReturn(false);

        try {
            String tinyCode = tinyUrlStore.getTinyUrlFuture(originUrl).get();
        } catch (Exception e) {
            log.error("异常", e);
        }

    }

    @Test
    public void testgetTinyUrlFutureSame() throws ExecutionException, InterruptedException {
        String originUrl = "test.com";
        String tinyCode1 = tinyUrlStore.getTinyUrlFuture(originUrl).get();

        String tinyCode2 = tinyUrlStore.getTinyUrlFuture(originUrl).get();
        log.info("originUrl:{} -> {}  -  {}", originUrl, tinyCode1, tinyCode2);

        Assertions.assertEquals(tinyCode1, tinyCode2);
    }

    @Test
    public void testgenerateTinyCodeFuture() throws ExecutionException, InterruptedException {
        String originUrl = "originUrl";
        MemberModifier.stub(PowerMockito.method(CodeGenerator.class, "generateTinyCode")).toReturn("tinyCode");
        MemberModifier.stub(PowerMockito.method(TinyUrlStore.class, "getOriginUrl")).toReturn("originUrl");

        tinyUrlStore.getTinyUrlFuture(originUrl).get();

        MemberModifier.stub(PowerMockito.method(TinyUrlStore.class, "getOriginUrl")).toReturn("originUrl-other");
        tinyUrlStore.getTinyUrlFuture(originUrl).get();
    }

    @Test
    public void testgetOriginUrlFuture() throws ExecutionException, InterruptedException {
        String originUrl = tinyUrlStore.getOriginUrlFuture(null).get();
        Assertions.assertEquals(null, originUrl);

        originUrl = tinyUrlStore.getOriginUrlFuture("null").get();
        Assertions.assertEquals(null, originUrl);
    }
}

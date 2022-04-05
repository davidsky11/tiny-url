package com.sequoia.service;

import com.google.common.collect.Sets;
import com.sequoia.infrastructure.common.exception.LockException;
import com.sequoia.infrastructure.service.impl.CodeGenerator;
import com.sequoia.infrastructure.service.impl.TinyUrlStore;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;


/**
 * Descript:
 * File: com.sequoia.service.TinyUrlStoreMock
 * Author: daishengkai
 * Date: 2022/3/31
 * Copyright (c) 2022,All Rights Reserved.
 */
@Slf4j
@SpringBootTest
//@RunWith(MockitoJUnitRunner.class)
public class TinyUrlStoreTest {

    @SpyBean
    private TinyUrlStore tinyUrlStore;

    @SpyBean
    private ICodeGenerator codeGenerator;

    @Test
    public void testGetTinyUrlFuture() {
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
    public void testgetOriginUrlFuture() throws ExecutionException, InterruptedException {
        String originUrl = tinyUrlStore.getOriginUrlFuture(null).get();
        Assertions.assertEquals(null, originUrl);

        originUrl = tinyUrlStore.getOriginUrlFuture("null").get();
        Assertions.assertEquals(null, originUrl);
    }

    @Test
    public void testSaveTinyOriginCodeMapping(){
        String originUrl = "test.com";
//        Lock lock = Mockito.mock(Lock.class);
//        Mockito.when(tinyUrlStore.getLock(Mockito.anyString())).thenReturn(lock);
//
//        try {
//            Mockito.when(lock.tryLock(1000, TimeUnit.MILLISECONDS)).thenThrow(new InterruptedException());
//            String tinyCode = tinyUrlStore.getTinyUrlFuture(originUrl).get();
//        } catch (Exception e) {
//            log.error("异常", e);
//        }
//
//        try {
//            TinyUrlStore store = new TinyUrlStore();
//
//            Method method = TinyUrlStore.class.getDeclaredMethod("saveTinyOriginCodeMapping", String.class, String.class);
//            method.setAccessible(true);
//
//            method.invoke(store, "test", "test");
//            method.invoke(store, "test", "test");
//        } catch (Exception e) {
//            log.error("异常", e);
//        }

        try {
            CodeGenerator codeGenerator = Mockito.mock(CodeGenerator.class);
            Mockito.when(codeGenerator.generateTinyCode(originUrl))
                    .thenThrow(new LockException("中断异常"));
//                    .thenReturn("test");


            CompletableFuture<String> future = new CompletableFuture<>();
            future.completeExceptionally(new RuntimeException("中断异常"));

//            doAnswer((InvocationOnMock invocation) -> {
//                ((Runnable)invocation.getArguments()[0]).run();
//                return null;
//            });

//            TinyUrlStore store = new TinyUrlStore();
//            Method method = TinyUrlStore.class.getDeclaredMethod("generateTinyCodeFuture", String.class);
//            method.setAccessible(true);
//            CompletableFuture<String> future = new CompletableFuture<>();
//            future.completeExceptionally(new RuntimeException("中断异常"));
//            method.invoke(tinyUrlStore, originUrl);

            String tinyCode = tinyUrlStore.getTinyUrlFuture(originUrl).get();

//            TinyUrlStore store = new TinyUrlStore();
//            Method method = TinyUrlStore.class.getDeclaredMethod("putOriginUrl", String.class, String.class);
//            method.setAccessible(true);
//            method.invoke(tinyUrlStore, "test", originUrl);
//
//            method = TinyUrlStore.class.getDeclaredMethod("generateTinyCodeFuture", String.class);
//            method.setAccessible(true);
//            method.invoke(tinyUrlStore, originUrl);
        } catch (Exception e) {
            log.error("异常", e);
        }
    }
}

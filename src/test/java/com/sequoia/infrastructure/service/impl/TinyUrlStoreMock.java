package com.sequoia.infrastructure.service.impl;

import com.alibaba.testable.core.annotation.MockInvoke;
import com.alibaba.testable.core.model.MockScope;
import com.alibaba.testable.processor.annotation.EnablePrivateAccess;
import com.sequoia.infrastructure.service.impl.CodeGenerator;
import com.sequoia.infrastructure.service.impl.TinyUrlStore;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static com.alibaba.testable.core.tool.PrivateAccessor.invoke;

/**
 * TinyUrlStoreMock
 *
 * @author KVLT
 * @date 2022-04-05.
 */
@Slf4j
@EnablePrivateAccess(srcClass = TinyUrlStore.class)
public class TinyUrlStoreMock {

    private TinyUrlStore store = new TinyUrlStore();

    public static class Mock {

        @MockInvoke(targetClass = TinyUrlStore.class)
        private String tinyCode(String originUrl) {
            return "test";
        }

        @MockInvoke(targetClass = TinyUrlStore.class, targetMethod = "getOriginUrl")
        private String getOriginUrl(String tinyCode) {
            return "originUrl";
        }

        @MockInvoke(targetClass = TinyUrlStore.class)
        private CompletableFuture<String> generateTinyCodeFuture(String originUrl){
            CompletableFuture<String> future = new CompletableFuture<>();
            future.completeExceptionally(new InterruptedException());
            return future;
        }
    }

    @Test
    public void testGenerateTinyCodeFuture() throws Exception {
        try {
            CompletableFuture<String> future = new CompletableFuture<>();
            future.completeExceptionally(new InterruptedException());

            CompletableFuture<String> resultFuture = store.generateTinyCodeFuture("test");
            resultFuture.get();
        } catch (Exception e) {
            log.error("异常", e);
        }
    }

    @Test
    public void testGetTinyUrlFuture() {
        String originUrl = "www.sq.com";
        try {
            CompletableFuture<String> future = store.getTinyUrlFuture(originUrl);
            future.get();
        } catch (Exception e) {
            log.error("异常", e);
        }

        String tinyCode = store.generateTinyCode(originUrl);
    }

    @Test
    public void testSaveTinyOriginCodeMapping() {
        store.putOriginUrl("test", "originUrl");
        store.saveTinyOriginCodeMapping("test", "originUrl");
    }
}

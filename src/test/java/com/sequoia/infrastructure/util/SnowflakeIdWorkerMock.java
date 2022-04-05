package com.sequoia.infrastructure.util;

import com.alibaba.testable.core.annotation.MockInvoke;
import com.alibaba.testable.processor.annotation.EnablePrivateAccess;
import com.sequoia.infrastructure.service.impl.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.net.UnknownHostException;

/**
 * SnowflakeIdWorkerMock
 *
 * @author KVLT
 * @date 2022-04-05
 *  */
@Slf4j
@EnablePrivateAccess(srcClass = SnowflakeIdWorker.class)
public class SnowflakeIdWorkerMock {

    private SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker();

    public static class Mock {

        @MockInvoke(targetClass = SnowflakeIdWorker.class)
        private String getHostName() throws UnknownHostException {
            throw new UnknownHostException("未知host");
        }
    }

    @Test
    public void testInitMachineId() {
        snowflakeIdWorker.initMachineId();
    }
}

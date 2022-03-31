package com.sequoia.service;

import com.sequoia.infrastructure.service.impl.CodeGenerator;
import com.sequoia.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * Descript:
 * File: com.sequoia.service.CodeGeneratorTest
 * Author: daishengkai
 * Date: 2022/3/30 23:07
 * Copyright (c) 2022,All Rights Reserved.
 */
@Slf4j
public class CodeGeneratorTest {

    @Test
    public void generateTinyCodeTest() {
        CodeGenerator codeGenerator = new CodeGenerator();
        log.error(codeGenerator.generateTinyCode(Constant.ORIGIN_URL));
    }

    @Test
    public void testGenerateTinyCodeMore() {
        CodeGenerator codeGenerator = new CodeGenerator();

        for (int i = 0; i < 1000; i++) {
            log.error(codeGenerator.generateTinyCode(i+""));
        }
    }
}

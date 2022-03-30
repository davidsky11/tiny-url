package com.sequoia.service.impl;

import com.sequoia.domain.TinyUrl;
import com.sequoia.service.ICodeGenerator;
import com.sequoia.service.ITinyUrlGenerator;

import javax.annotation.Resource;

/**
 * TinyUrlGenerator
 *
 * @author KVLT
 * @date 2022-03-30.
 */
public class TinyUrlGenerator implements ITinyUrlGenerator {

    @Resource
    private ICodeGenerator codeGenerator;

    @Override
    public TinyUrl generateShortUrl(String originUrl) {
        return null;
    }

}

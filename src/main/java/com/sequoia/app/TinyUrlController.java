package com.sequoia.app;

import com.sequoia.service.ITinyUrlGenerator;

import javax.annotation.Resource;

/**
 * TinyUrlController
 *
 * @author KVLT
 * @date 2022-03-30.
 */
public class TinyUrlController {

    @Resource
    private ITinyUrlGenerator tinyUrlGenerator;

}

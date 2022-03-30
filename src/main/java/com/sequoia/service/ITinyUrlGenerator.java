package com.sequoia.service;

import com.sequoia.domain.TinyUrl;

/**
 * ITinyUrlGenerator
 *
 * @author KVLT
 * @date 2022-03-30.
 */
public interface ITinyUrlGenerator {

    public TinyUrl generateShortUrl(String originUrl);

}

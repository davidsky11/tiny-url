package com.sequoia.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * TinyUrl
 *
 * @author KVLT
 * @date 2022-03-30.
 */
@Getter
@AllArgsConstructor
@ToString
public class TinyUrl {

    /**
     * 短链接
     */
    private String code;

    /**
     * 原始长链接
     */
    private String originUrl;

}

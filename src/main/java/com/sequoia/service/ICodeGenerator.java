package com.sequoia.service;

/**
 * ICodeGenerator
 *
 * @author KVLT
 * @date 2022-03-30.
 */
public interface ICodeGenerator {

    public String nextCode();

    public String nextCode(int bizId);

}

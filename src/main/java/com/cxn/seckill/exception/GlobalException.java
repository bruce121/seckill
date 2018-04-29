package com.cxn.seckill.exception;

import com.cxn.seckill.result.CodeMsg;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-28 17:29
 * @Version v1.0
 */
public class GlobalException extends RuntimeException{

    private CodeMsg codeMsg;

    public GlobalException(CodeMsg codeMsg) {
        super(codeMsg.toString());
        this.codeMsg = codeMsg;
    }

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }

}

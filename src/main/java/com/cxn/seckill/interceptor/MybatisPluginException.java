package com.cxn.seckill.interceptor;

/**
 * 业务处理异常
 *
 */
public class MybatisPluginException extends RuntimeException {
    private static final long serialVersionUID = -7034897190745766939L;

    /**
     * 空构造器
     */
    public MybatisPluginException() {
        super();
    }

    /**
     * 构造器
     *
     * @param message 错误消息
     */
    public MybatisPluginException(String message) {
        super(message);
    }

    /**
     * 构造器
     *
     * @param message 错误消息
     * @param cause   错误原因
     */
    public MybatisPluginException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造器
     *
     * @param cause 错误原因
     */
    public MybatisPluginException(Throwable cause) {
        super(cause);
    }

}

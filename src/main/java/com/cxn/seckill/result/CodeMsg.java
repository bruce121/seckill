package com.cxn.seckill.result;

/**
 * @program: seckill
 * @description: ${description}
 * @author: cxn
 * @create: 2018-04-25 22:06
 * @Version v1.0
 */
public class CodeMsg {

    private int code;
    private String msg;
    // 通用异常
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    // 登录模块 500200

    // 商品模块 500300

    // 订单模块 500400

    // 秒杀模块 500500


    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


    @Override
    public String toString() {
        return "CodeMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}

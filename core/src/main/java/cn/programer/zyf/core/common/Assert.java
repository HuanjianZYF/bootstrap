package cn.programer.zyf.core.common;

/**
 * @Author wb-zyf471922
 * @Date 2019/11/13 20:14
 **/
public class Assert {

    /**
     * 断言
     *
     * @param condition 条件
     * @param msg 报错信息
     */
    public static void assertTrue(boolean condition, String msg) {

        if (!condition) {
            throw new AssertException(msg);
        }
    }
}

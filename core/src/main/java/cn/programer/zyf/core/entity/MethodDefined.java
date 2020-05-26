package cn.programer.zyf.core.entity;

import lombok.Data;

/**
 * @author wb-wc362584
 * 医保接口方法定义
 */
@Data
public class MethodDefined {

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 返回参数类型 只支持3类 int, void , char*
     */
    private String returnType;

    /**
     * 形式参数定义 只支持3类 int, void , char*
     */
    private String[] methodTypes;

}

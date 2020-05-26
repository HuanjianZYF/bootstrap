package cn.programer.zyf.core.entity;

import lombok.Data;

/**
 * @Author wb-zyf471922
 * @Date 2019/10/23 12:58
 **/
@Data
public class GeneratorClassInfo {

    /**
     * 类文件名字
     */
    private String classFileName;

    /**
     * 全类名
     */
    private String fullClassName;
}

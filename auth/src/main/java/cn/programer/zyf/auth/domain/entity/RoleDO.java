package cn.programer.zyf.auth.domain.entity;

import cn.programer.zyf.core.entity.BaseDO;
import lombok.Data;

/**
 * @author zyf
 * @date 2020-09-01 13:41
 **/
@Data
public class RoleDO extends BaseDO {

    private String code;
    private String name;
    private String desc;
}

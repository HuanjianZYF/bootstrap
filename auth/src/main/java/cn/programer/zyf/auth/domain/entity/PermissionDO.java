package cn.programer.zyf.auth.domain.entity;

import cn.programer.zyf.core.entity.BaseDO;
import lombok.Data;

/**
 * @author zyf
 * @date 2020-09-01 14:09
 **/
@Data
public class PermissionDO extends BaseDO {
    private String code;
    private String name;
    private String desc;
}

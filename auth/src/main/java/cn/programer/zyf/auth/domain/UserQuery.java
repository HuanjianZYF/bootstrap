package cn.programer.zyf.auth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zyf
 * @date 2020-09-01 12:21
 **/
@Data
public class UserQuery {

    @ApiModelProperty(value = "用户名")
    private String userName;
}

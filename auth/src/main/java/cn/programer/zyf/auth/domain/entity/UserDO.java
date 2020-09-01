package cn.programer.zyf.auth.domain.entity;

import cn.programer.zyf.core.entity.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author zyf
 * @date 2020-09-01 11:46
 **/
@Data
public class UserDO extends BaseDO {

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "微信")
    private String weixin;

    @ApiModelProperty(value = "电子邮箱")
    private String email;

    @ApiModelProperty(value = "qq")
    private String qq;

    @ApiModelProperty(value = "密码 加盐后的MD5")
    private String password;

    @ApiModelProperty(value = "盐")
    private String salt;

    @ApiModelProperty(value = "是否试用")
    private Integer trial;

    @ApiModelProperty(value = "是否是管理员")
    private Integer isAdmin;

    @ApiModelProperty(value = "上次登录时间")
    private Date lastedLoginAt;

    @ApiModelProperty(value = "当前appid")
    private Integer currentAppId;

    @ApiModelProperty(value = "过期时间")
    private Date expiredAt;

    @ApiModelProperty(value = "是否冻结")
    private Integer freeze;
}

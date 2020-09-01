package cn.programer.zyf.core.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author zyf
 * @date 2020-09-01 11:46
 **/
@Data
public class BaseDO {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "创建人，由于乐其的这个字段是String，所以就只能定义成string了 但愿是统一的")
    private String createdUser;

    @ApiModelProperty(value = "更新人")
    private String updatedUser;

    @ApiModelProperty(value = "创建时间")
    private Date createdAt;

    @ApiModelProperty(value = "更新时间")
    private Date updatedAt;

    @ApiModelProperty(value = "状态 乐其的这个字段是这样命名的，所以就只能这样了")
    private Integer status;
}

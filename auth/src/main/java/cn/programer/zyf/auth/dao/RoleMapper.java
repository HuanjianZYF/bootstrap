package cn.programer.zyf.auth.dao;

import cn.programer.zyf.auth.domain.entity.RoleDO;
import cn.programer.zyf.auth.domain.query.RoleQuery;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author zyf
 * @date 2020-09-01 14:10
 **/
@Mapper
public interface RoleMapper {

    List<RoleDO> listUserRoles(RoleQuery query);
}

package cn.programer.zyf.auth.dao;

import cn.programer.zyf.auth.domain.entity.PermissionDO;
import cn.programer.zyf.auth.domain.query.PermissionQuery;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author zyf
 * @date 2020-09-01 14:24
 **/
@Mapper
public interface PermissionMapper {

    List<PermissionDO> listRolePermission(PermissionQuery query);
}

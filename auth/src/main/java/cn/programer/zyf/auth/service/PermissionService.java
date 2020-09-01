package cn.programer.zyf.auth.service;

import cn.programer.zyf.auth.domain.entity.PermissionDO;

import java.util.List;

/**
 * @author zyf
 * @date 2020-09-01 14:31
 **/
public interface PermissionService {

    List<PermissionDO> listRolePermission(String roleCode);
}

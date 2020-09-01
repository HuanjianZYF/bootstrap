package cn.programer.zyf.auth.service;

import cn.programer.zyf.auth.domain.entity.RoleDO;

import java.util.List;

/**
 * @author zyf
 * @date 2020-09-01 14:16
 **/
public interface RoleService {

    List<RoleDO> listUserRole(Long userId);
}

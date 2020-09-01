package cn.programer.zyf.auth.service;

import cn.programer.zyf.auth.domain.entity.UserDO;

/**
 * @author zyf
 * @date 2020-09-01 11:44
 **/
public interface UserService {

    UserDO getByUserName(String userName);
}

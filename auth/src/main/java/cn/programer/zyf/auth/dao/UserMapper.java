package cn.programer.zyf.auth.dao;

import cn.programer.zyf.auth.domain.query.UserQuery;
import cn.programer.zyf.auth.domain.entity.UserDO;
import org.mapstruct.Mapper;

/**
 * @author zyf
 * @date 2020-09-01 12:16
 **/
@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户
     * @param query
     * @return
     */
    UserDO queryByUserName(UserQuery query);
}

package cn.programer.zyf.auth.service.impl;

import cn.programer.zyf.auth.dao.UserMapper;
import cn.programer.zyf.auth.domain.query.UserQuery;
import cn.programer.zyf.auth.domain.entity.UserDO;
import cn.programer.zyf.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zyf
 * @date 2020-09-01 12:30
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDO getByUserName(String userName) {
        UserQuery query = new UserQuery();
        query.setUserName(userName);
        return userMapper.queryByUserName(query);
    }
}

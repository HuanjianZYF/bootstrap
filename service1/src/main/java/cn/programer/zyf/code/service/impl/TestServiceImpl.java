package cn.programer.zyf.code.service.impl;

import cn.programer.zyf.code.domain.CouponDO;
import cn.programer.zyf.code.mapper.TestMapper;
import cn.programer.zyf.code.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author wb-zyf471922
 * @Date 2019/11/13 16:35
 **/
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestMapper testMapper;

    @Override
    public List<CouponDO> listCoupon() {
        return testMapper.listCoupon();
    }
}

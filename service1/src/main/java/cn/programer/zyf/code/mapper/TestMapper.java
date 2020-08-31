package cn.programer.zyf.code.mapper;

import cn.programer.zyf.code.domain.CouponDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author wb-zyf471922
 * @Date 2019/11/13 16:37
 **/
@Mapper
public interface TestMapper {

    List<CouponDO> listCoupon();
}

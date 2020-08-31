package cn.programer.zyf.code.controller;

import cn.programer.zyf.code.service.TestService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author wb-zyf471922
 * @Date 2019/11/13 16:32
 **/
@RestController
@Api(description = "测试控制器")
public class TestController {

    @Autowired
    private TestService testService;

    @ApiOperation(value = "测试一下")
    @RequestMapping(value = "/test2")
    public String test2() {
        return JSONObject.toJSONString(testService.listCoupon());
    }

    @ApiOperation(value = "测试一下")
    @RequestMapping(value = "/test")
    public String test() {

        return "test";
    }
}

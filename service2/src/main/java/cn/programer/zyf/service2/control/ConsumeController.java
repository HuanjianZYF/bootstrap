package cn.programer.zyf.service2.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @Author wb-zyf471922
 * @Date 2020/1/10 10:43
 **/
@RestController
public class ConsumeController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(value = "/hello")
    public String hello() {
        return restTemplate.getForEntity("http://service1/test2", String.class).getBody();
    }

    @GetMapping(value = "/hello2")
    public String hello2() {

        return restTemplate.postForEntity("http://test-service/test", "", String.class).getBody();
    }

}

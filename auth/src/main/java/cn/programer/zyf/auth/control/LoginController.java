package cn.programer.zyf.auth.control;

import cn.programer.zyf.auth.domain.UsernamePassword;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zyf
 * @date 2020-09-01 11:01
 **/
@RestController
public class LoginController {

    @PostMapping(value = "/login")
    public String login(@RequestBody UsernamePassword usernamePassword) {
        UsernamePasswordToken token = new UsernamePasswordToken(usernamePassword.getUserName(), usernamePassword.getPassword());
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);

        return "haha";
    }
}

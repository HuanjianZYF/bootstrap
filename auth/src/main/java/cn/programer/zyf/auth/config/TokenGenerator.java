package cn.programer.zyf.auth.config;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;

import java.util.Map;

/**
 * @Author wb-zyf471922
 * @Date 2019/11/29 13:47
 **/
public class TokenGenerator extends DefaultAuthenticationKeyGenerator {

    @Override
    public String extractKey(OAuth2Authentication authentication) {
        return super.extractKey(authentication);
    }

    @Override
    protected String generateKey(Map<String, String> values) {
        return super.generateKey(values);
    }
}

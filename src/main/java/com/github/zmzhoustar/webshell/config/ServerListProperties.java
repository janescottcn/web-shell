package com.github.zmzhoustar.webshell.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 服务器列表配置
 *
 * @author scott
 * @date 2021/06/19 17:54
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Component
@ConfigurationProperties(prefix = "webshell.server-list")
public class ServerListProperties {
    /**
     * 服务器地址
     */
    private List<String> hosts;
    /**
     * 服务器密码
     */
    private String password;
    /**
     * 服务器账号
     */
    private String username;
    /**
     * 加密私钥
     */
    private String key;
}

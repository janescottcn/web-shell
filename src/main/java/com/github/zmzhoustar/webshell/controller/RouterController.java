/*
 * Copyright © 2020-present zmzhou-star. All Rights Reserved.
 */

package com.github.zmzhoustar.webshell.controller;

import com.alibaba.fastjson.JSON;
import com.github.zmzhoustar.webshell.config.ServerListProperties;
import com.github.zmzhoustar.webshell.utils.AESUtils;
import com.github.zmzhoustar.webshell.utils.EhCacheUtils;
import com.github.zmzhoustar.webshell.utils.SftpUtils;
import com.github.zmzhoustar.webshell.utils.WebShellUtils;
import com.github.zmzhoustar.webshell.vo.WebShellData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * 路由控制类
 *
 * @author zmzhou
 * @version 1.0
 * @title RouterController
 * @date 2021/1/30 23:32
 */
@Slf4j
@Controller
public class RouterController {
    @Resource
    private ServerListProperties properties;

    /**
     * index
     *
     * @author zmzhou
     * @date 2021/1/30 23:33
     */
    @GetMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("hosts", properties.getHosts());
        model.addAttribute("username", properties.getUsername());
        model.addAttribute("password", AESUtils.encrypt(properties.getPassword(), properties.getKey()));
        return "index";
    }

    /**
     * sftp
     *
     * @author zmzhou
     * @date 2021/2/26 16:40
     */
    @GetMapping("/sftp")
    public String sftp(String params, Model model) {
        String sessionId = WebShellUtils.getSessionId();
        log.info("sessionId：{}", sessionId);
        WebShellData sshData = JSON.parseObject(params, WebShellData.class);
        // 存放ssh连接信息
        if (sshData != null) {
            EhCacheUtils.put(sessionId, sshData);
        } else {
            sshData = EhCacheUtils.get(sessionId);
        }
        if (sshData != null) {
            SftpUtils sftpUtils = new SftpUtils(sshData);
            boolean login = sftpUtils.login();
            // 登录成功状态
            model.addAttribute("login", login);
            model.addAttribute("host", sshData.getHost());
            sftpUtils.logout();
        }
        return "sftp";
    }
}

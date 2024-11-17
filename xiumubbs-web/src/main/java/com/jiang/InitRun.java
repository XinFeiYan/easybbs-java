package com.jiang;

import com.jiang.service.SysSettingService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
//添加spring管理,把bean注入进去
//继承这个启动类
@Component
public class InitRun implements ApplicationRunner {
    @Resource
    private SysSettingService sysSettingService;
    //服务器启动就开始加载
    public void run(ApplicationArguments arg)throws Exception{
        sysSettingService.refreshCache();
    }

}

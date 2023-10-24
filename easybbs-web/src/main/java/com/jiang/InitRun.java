package com.jiang;

import com.jiang.service.SysSettingService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
//添加spring管理
@Component
public class InitRun implements ApplicationRunner {
    @Resource
    private SysSettingService sysSettingService;

    public void run(ApplicationArguments arg)throws Exception{
        sysSettingService.refreshCache();
    }

}

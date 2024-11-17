package com.jiang;

import com.jiang.service.SysSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

//服务器启动自动执行
@Component
public class InitRun implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(InitRun.class);

    @Resource
    private SysSettingService sysSettingService;

    public void run(ApplicationArguments args){

        //刷新缓存成功
        sysSettingService.refreshCache();
        logger.info("服务启动成功");
    }

}

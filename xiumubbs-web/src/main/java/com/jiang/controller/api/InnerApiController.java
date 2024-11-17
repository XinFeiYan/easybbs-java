package com.jiang.controller.api;

import com.jiang.Enums.ResponseCodeEnum;
import com.jiang.Exception.BusinessException;
import com.jiang.Utils.StringTools;
import com.jiang.annotation.GlobalInterceptor;
import com.jiang.annotation.VerifyParam;
import com.jiang.controller.base.BaseController;
import com.jiang.entity.config.WebConfig;
import com.jiang.entity.dto.SysSettingDto;
import com.jiang.entity.vo.ResponseVO;
import com.jiang.service.SysSettingService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("innerApiController")
@RequestMapping("/innerApi")
public class InnerApiController extends BaseController {
    @Resource
    private WebConfig webConfig;

    @Resource
    private SysSettingService sysSettingService;

    @RequestMapping
    @GlobalInterceptor(checkParams = true)
    public ResponseVO refresSysSetting(@VerifyParam(required = true)String appKey,
                                       @VerifyParam(required = true)Long timestamp,
                                       @VerifyParam(required = true)String sign){
        if(!webConfig.getInnerApiAppKey().equals(appKey)){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        //时间戳小于10分钟
        if(System.currentTimeMillis()-timestamp>1000*10){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        //没有看懂什么原理
        String mySign = StringTools.encodeMd5(appKey+timestamp+webConfig.getInnerApiAppKey());
        if(!mySign.equals(sign)){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        //调用这个后，就是将配置文件写入到这个文件里面，有个两个tomcat
        sysSettingService.refreshCache();
        return getSuccessResponseVO(null);

    }

}

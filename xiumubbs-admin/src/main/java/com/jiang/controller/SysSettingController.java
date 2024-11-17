package com.jiang.controller;


import com.jiang.annotation.VerifyParam;
import com.jiang.controller.base.BaseController;
import com.jiang.entity.config.AdminConfig;
import com.jiang.entity.dto.*;
import com.jiang.entity.vo.ResponseVO;
import com.jiang.Exception.BusinessException;
import com.jiang.service.SysSettingService;
import com.jiang.Utils.JsonUtils;
import com.jiang.Utils.OKHttpUtils;
import com.jiang.Utils.StringTools;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/setting")
public class SysSettingController extends BaseController {
    @Resource
    private SysSettingService settingService;

    @Resource
    private AdminConfig adminConfig;

    @RequestMapping("/getSetting")
    public ResponseVO getSetting() throws BusinessException {
        return getSuccessResponseVO(settingService.refreshCache());
    }

    @RequestMapping("/saveSetting")
    public ResponseVO saveSetting(@VerifyParam(required = true) SysSetting4AuditDto auditDto,
                                  @VerifyParam(required = true)SysSetting4CommentDto commentDto,
                                  @VerifyParam(required = true)SysSetting4EmailDto emailDto,
                                  @VerifyParam(required = true)SysSetting4LikeDto likeDto,
                                  @VerifyParam(required = true)SysSetting4PostDto postDto,
                                  @VerifyParam(required = true)SysSetting4RegisterDto registerDto) throws BusinessException {
        SysSettingDto sysSettingDto = new SysSettingDto();
        sysSettingDto.setAuditSetting(auditDto);
        sysSettingDto.setCommentSetting(commentDto);
        sysSettingDto.setEmailSetting(emailDto);
        sysSettingDto.setLikeSetting(likeDto);
        sysSettingDto.setPostSetting(postDto);
        sysSettingDto.setRegisterSetting(registerDto);
        settingService.saveSetting(sysSettingDto);
        return getSuccessResponseVO(null);
    }
}

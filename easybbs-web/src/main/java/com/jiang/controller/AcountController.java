package com.jiang.controller;

import com.jiang.entity.vo.ResponseVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AcountController extends BaseController{
    @RequestMapping("/test1")
    public ResponseVO test(Integer userid){
        return getSuccessResponseVO(userid);
    }
}

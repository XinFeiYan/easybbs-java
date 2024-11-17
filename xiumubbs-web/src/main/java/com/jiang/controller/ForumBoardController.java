package com.jiang.controller;

import com.jiang.controller.base.BaseController;
import com.jiang.entity.vo.ResponseVO;
import com.jiang.service.ForumBoardService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/board")
public class ForumBoardController extends BaseController {

    @Resource
    private ForumBoardService forumBoardService;
    @RequestMapping("/loadBoard")
    public ResponseVO loadBoard(){
        return getSuccessResponseVO(forumBoardService.getBoardTree(null));
    }












}

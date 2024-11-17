package com.jiang.controller;

import com.jiang.Enums.FileUploadTypeEnum;
import com.jiang.Utils.FileUtils;
import com.jiang.annotation.GlobalInterceptor;
import com.jiang.annotation.VerifyParam;
import com.jiang.controller.base.BaseController;
import com.jiang.entity.constants.Constants;
import com.jiang.entity.dto.FileUploadDto;
import com.jiang.entity.po.ForumBoard;
import com.jiang.entity.vo.ResponseVO;
import com.jiang.service.ForumBoardService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/board")
public class ForumBoardController extends BaseController {

    @Resource
    private ForumBoardService forumBoardService;
    @Resource
    private FileUtils fileUtils;

    @RequestMapping("/loadBoard")
    public ResponseVO loadBoard(){
        return getSuccessResponseVO(forumBoardService.getBoardTree(null));
    }

    @RequestMapping("/saveBoard")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO saveBoard(Integer boardId,
                                @VerifyParam(required = true) Integer pBoardId,
                                @VerifyParam(required = true)String boardName,
                                String boardDesc,
                                Integer postType,
                                MultipartFile cover){
        ForumBoard forumBoard = new ForumBoard();
        forumBoard.setBoardDesc(boardDesc);
        forumBoard.setBoardId(boardId);
        forumBoard.setPBoardId(pBoardId);
        forumBoard.setBoardName(boardName);
        forumBoard.setPostType(postType);
        if(cover!=null){
           FileUploadDto uploadDto =  fileUtils.uploadFile2local(cover, Constants.FILE_FOLDER_IMAGE,FileUploadTypeEnum.ARTICLE_COVER);
           forumBoard.setCover(uploadDto.getLocalPath());
        }
        forumBoardService.saveForumBoard(forumBoard);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/delBoard")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO delBoard(@VerifyParam(required = true) Integer boardId){

        forumBoardService.deleteByBoardId(boardId);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/changeBoardSort")
    @GlobalInterceptor(checkParams = true)
    public ResponseVO changeBoardSort(@VerifyParam(required = true) String  boardIds){

        forumBoardService.changeSort(boardIds);
        return getSuccessResponseVO(null);
    }




}

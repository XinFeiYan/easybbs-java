package com.jiang.controller;

import com.jiang.Enums.ResponseCodeEnum;
import com.jiang.Exception.BusinessException;
import com.jiang.Utils.StringTools;
import com.jiang.annotation.GlobalInterceptor;
import com.jiang.controller.base.BaseController;
import com.jiang.entity.config.WebConfig;
import com.jiang.entity.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/file")
public class FileController extends BaseController {
    public static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Resource
    private WebConfig webConfig;

    @RequestMapping("/getImage/{imageFolder}/{imageName}")
    @GlobalInterceptor(checkParams = true)
    public void getImage(HttpServletResponse response,
                          @PathVariable("imageFolder") String imageFolder,
                         @PathVariable("imageName") String imageName){
        readImage(response,imageFolder,imageName);

    }

    @RequestMapping("/getAvatar/{userId}")
    @GlobalInterceptor(checkParams = true)
    public void getAvatar(HttpServletResponse response,
                          @PathVariable("userId")  String userId){
        //路径名
        String avatarFolderName = Constants.FILE_FOLDER_FILE+Constants.FILE_FOLDER_AVATAR_NAME;
        //文件名
        String avatarPath = webConfig.getProjectFolder()+avatarFolderName+userId+Constants.AVATAR_SUFFIX;

        File avatarFolder = new File(avatarFolderName);
        if(!avatarFolder.exists()){
            avatarFolder.mkdirs();
        }
        //看是否上传过图片，没有就读取默认
        File file = new File(avatarPath);
        String imageName = userId+Constants.AVATAR_SUFFIX;
        if(!file.exists()){
            imageName = Constants.AVATAR_DEFAULT;
        }

        readImage(response,Constants.FILE_FOLDER_AVATAR_NAME,imageName);

    }

    //读取文件
    private void readImage(HttpServletResponse response, String imageFolder, String imageName) {
        ServletOutputStream sos = null;
        FileInputStream in = null;
        ByteArrayOutputStream baos = null;
        try {
            if (StringTools.isEmpty(imageFolder) || StringUtils.isBlank(imageName)) {
                return;
            }
            String imageSuffix = StringTools.getFileSuffix(imageName);
            String filePath = webConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_IMAGE + imageFolder +"/"+ imageName;
            if (Constants.FILE_FOLDER_TEMP.equals(imageFolder)||imageFolder.contains(Constants.FILE_FOLDER_AVATAR_NAME)){
                filePath = webConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + imageFolder + imageName;
            }
            File file = new File(filePath);
            if (!file.exists()) {
                return;
            }
            //头像不设置缓存
            imageSuffix = imageSuffix.replace(".", "");
            if (!Constants.FILE_FOLDER_AVATAR_NAME.equals(imageFolder)) {
                response.setHeader("Cache-Control", "max-age=2592000");
            }
            response.setContentType("image/" + imageSuffix);
            in = new FileInputStream(file);
            sos = response.getOutputStream();
            baos = new ByteArrayOutputStream();
            int ch = 0;
            while (-1 != (ch = in.read())) {
                baos.write(ch);
            }
            sos.write(baos.toByteArray());
        } catch (Exception e) {
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sos != null) {
                try {
                    sos.close();
                } catch (IOException e) {
                    throw new BusinessException(ResponseCodeEnum.CODE_500);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new BusinessException(ResponseCodeEnum.CODE_500);
                }
            }
        }
    }



}

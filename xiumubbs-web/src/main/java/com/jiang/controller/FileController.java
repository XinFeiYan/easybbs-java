package com.jiang.controller;

import com.jiang.Enums.ResponseCodeEnum;
import com.jiang.Enums.UserOperFrequencyTypeEnum;
import com.jiang.Exception.BusinessException;
import com.jiang.Utils.StringTools;
import com.jiang.annotation.GlobalInterceptor;
import com.jiang.controller.base.BaseController;
import com.jiang.entity.config.WebConfig;
import com.jiang.entity.constants.Constants;
import com.jiang.entity.vo.ResponseVO;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

    //上传图片
    @RequestMapping("/uploadImage")
    @GlobalInterceptor(checkLogin = true,frequencyType = UserOperFrequencyTypeEnum.IMAGE_UPLOAD)
    public ResponseVO uploadImage(MultipartFile file){
        if(file == null){
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        }

        String fileName = file.getOriginalFilename();
        //获得图片后缀
        String fileExtName = StringTools.getFileSuffix(fileName);
        if(!ArrayUtils.contains(Constants.IMAGE_SUFFIX,fileExtName)){
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        }
        //上传服务器，返回路径
        String path = copyFile(file);
        Map<String,String> fileMap= new HashMap<>();
        //返回路径
        fileMap.put("fileName",path);
        return getSuccessResponseVO(fileMap);
    }

    private String copyFile(MultipartFile file){
        try{
            String fileName = file.getOriginalFilename();
            String fileExtName = StringTools.getFileSuffix(fileName);

            //服务器的文件名
            String fileRealName = StringTools.getRandomString(Constants.LENGTH_30)+fileExtName;
            //先传入到临时目录
            String folderPath = webConfig.getProjectFolder()+Constants.FILE_FOLDER_FILE+Constants.FILE_FOLDER_TEMP;
            File folder = new File(folderPath);
            //如果目录不存在创建目录
            if(!folder.exists()){
                folder.mkdirs();
            }
            //真正的服务器路径与文件
            File uploadFile = new File(folderPath+fileRealName);
            //写入文件
            file.transferTo(uploadFile);
            return Constants.FILE_FOLDER_TEMP+fileRealName;
        }catch (Exception e){
            logger.error("上传文件失败",e);
            throw new BusinessException("上传文件失败");
        }
    }

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

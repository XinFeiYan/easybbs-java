package com.jiang.Utils;

import com.jiang.Enums.DateTimePatternEnum;
import com.jiang.Enums.FileUploadTypeEnum;
import com.jiang.Exception.BusinessException;
import com.jiang.entity.config.AppConfig;
import com.jiang.entity.constants.Constants;
import com.jiang.entity.dto.FileUploadDto;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;

@Component
public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    @Resource
    private AppConfig appConfig;
    @Resource
    private ImageUtils imageUtils;

    @Transactional(rollbackFor = Exception.class)
    public FileUploadDto uploadFile2local(MultipartFile file, String folder, FileUploadTypeEnum uploadTypeEnum){
        try{
            FileUploadDto fileUploadDto = new FileUploadDto();
            String originalFileName = file.getOriginalFilename();
            String fileSuffix = StringTools.getFileSuffix(originalFileName);
            if(originalFileName.length()> Constants.LENGTH_200){
                originalFileName = StringTools.getFileName(originalFileName).substring(0,Constants.LENGTH_190)+fileSuffix;
            }
            if(!ArrayUtils.contains(uploadTypeEnum.getSuffixArray(),fileSuffix)){
                throw new BusinessException("文件类型不正确");
            }

            String month = DateUtil.format(new Date(), DateTimePatternEnum.YYYYMM.getPattern());
            String baseFolder = appConfig.getProjectFolder()+Constants.FILE_FOLDER_FILE;
            File targetFileFolder = new File(baseFolder+folder+month+"/");

            String fileName = StringTools.getRandomString(Constants.LENGTH_15)+fileSuffix;
            File targetFile = new File(targetFileFolder.getPath()+"/"+fileName);
            String localPath = month+"/"+fileName;

            //头像上传,这folder就是用户id
            if(uploadTypeEnum==FileUploadTypeEnum.AVATAR){
                targetFileFolder = new File(baseFolder+Constants.FILE_FOLDER_AVATAR_NAME);
                targetFile = new File(targetFileFolder.getPath()+"/"+folder+Constants.AVATAR_SUFFIX);
                localPath = folder+Constants.AVATAR_SUFFIX;
            }

            if(!targetFileFolder.exists()){
                targetFileFolder.mkdirs();
            }
            file.transferTo(targetFile);
            //压缩图片
            if(uploadTypeEnum==FileUploadTypeEnum.COMMENT_IMAGE){
                String thumbnailName = targetFile.getName().replace(".","_.");
                File thumbnail = new File(targetFile.getParent()+"/"+thumbnailName);
                Boolean thumbnailCreated = imageUtils.createThumbnail(targetFile,Constants.LENGTH_200,Constants.LENGTH_200,thumbnail);
                if(!thumbnailCreated){
                    //不用压缩同样复制一个
                    org.apache.commons.io.FileUtils.copyFile(targetFile,thumbnail);
                }
            }else if(uploadTypeEnum==FileUploadTypeEnum.AVATAR||uploadTypeEnum==FileUploadTypeEnum.ARTICLE_COVER){
                imageUtils.createThumbnail(targetFile,Constants.LENGTH_200,Constants.LENGTH_200,targetFile);
            }
            fileUploadDto.setLocalPath(localPath);
            fileUploadDto.setOriginalFileName(originalFileName);
            return fileUploadDto;
        }catch (BusinessException e){
            logger.error("文件上传失败",e);
            throw e;
        }catch (Exception e){
            logger.error("文件上传失败",e);
            throw new BusinessException("文件上传失败");
        }
    }
}

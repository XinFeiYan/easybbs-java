package com.jiang.Utils;

import com.jiang.Enums.DateTimePatternEnum;
import com.jiang.entity.config.AppConfig;
import com.jiang.entity.constants.Constants;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ImageUtils {
    private final static Logger logger = LoggerFactory.getLogger(ImageUtils.class);
    @Resource
    private AppConfig appConfig;

    //压缩图片
    public Boolean createThumbnail(File file, Integer thumbnailWidth, Integer thumbnailHeight, File targetFile) {
        try {
            BufferedImage src = ImageIO.read(file);
            int sorceW = src.getWidth();
            int sorceH = src.getHeight();

            //如果图片过小，放回图片
            if (sorceW < thumbnailWidth) {
                return false;
            }
            int height = sorceH;
            if (sorceW > thumbnailWidth) {
                height = thumbnailWidth * sorceH / sorceW;
            } else {
                thumbnailWidth = sorceW;
                height = sorceH;
            }
            BufferedImage dst = new BufferedImage(thumbnailWidth, height, BufferedImage.TYPE_INT_RGB);
            Image scaleImage = src.getScaledInstance(thumbnailWidth, height, Image.SCALE_SMOOTH);
            Graphics2D g = dst.createGraphics();
            g.drawImage(scaleImage, 0, 0, thumbnailWidth, height, null);
            g.dispose();

            int resultH = dst.getHeight();
            if (resultH > thumbnailHeight) {
                resultH = thumbnailHeight;
                dst = dst.getSubimage(0, 0, thumbnailWidth, resultH);
            }
            ImageIO.write(dst, "JPEG", targetFile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //重新复制图片
    public String resetImageHtml(String html) {
        String month = DateUtil.format(new Date(), DateTimePatternEnum.YYYYMM.getPattern());
        List<String> imageList = getImageList(html);
        for (String img : imageList) {
            resetImage(img, month);
        }
        return month;
    }


    private String resetImage(String imagePath, String month) {
        if (StringTools.isEmpty(imagePath) || !imagePath.contains(Constants.FILE_FOLDER_TEMP)) {
            return imagePath;
        }
        imagePath = imagePath.replace(Constants.READ_IMAGE_PATH, "");
        if (StringTools.isEmpty(month)) {
            month = DateUtil.format(new Date(), DateTimePatternEnum.YYYYMM.getPattern());
        }
        String imageFileName = month + "/" + imagePath.substring(imagePath.lastIndexOf("/") + 1);
        File targetFile = new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_IMAGE + imageFileName);
        try {
            File tempFile = new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + imagePath);
            //将本地的存在的文件复制给目标文件
            FileUtils.copyFile(tempFile, targetFile);
            //复制完成后删除图片
            tempFile.delete();
        } catch (IOException e) {
            logger.error("复制图片失败",e);
            return imagePath;
        }
        return imageFileName;
    }

    //读取图片路径
    private List<String> getImageList(String content) {
        List<String> imageList = new ArrayList<>();
        String regEx_img = "(<img.*src\\s*=\\s*(.*?)[^>]*?>)";
        Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        Matcher m_image = p_image.matcher(content);
        while (m_image.find()) {
            String img = m_image.group();
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)")
                    .matcher(img);
            while (m.find()) {
                String imageUrl = m.group(1);
                imageList.add(imageUrl);
            }
        }
        return imageList;
    }

}

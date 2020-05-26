package com.cafuc.graduation.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * <p>
 * 文件工具类
 * </p>
 *
 * @author flyeat
 * @date 2020/5/25 11:35
 */
public class FileUtil {

    public static String ByteToFile(byte[] bytes, String saveUrl) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        BufferedImage bi1 = ImageIO.read(bais);
        String newUrl = null;
        try {
            int dont = saveUrl.lastIndexOf(".");
            newUrl = saveUrl.substring(0, dont) + "_analysed.png";
            File w2 = new File(newUrl);
            ImageIO.write(bi1, "png", w2);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bais.close();
        }
        return newUrl;
    }
}

package com.cafuc.graduation.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

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


    /**
     * <p>
     * http 方式下载图片
     * </p>
     *
     * @param url 图片url
     * @return {@link ResponseEntity<byte[]> }
     * @author shijintao@supconit.com
     * @date 2020/5/26 14:11
     */
    public static ResponseEntity<byte[]> httpDownloadFile(String url) {
        byte[] body;
        try {
            body = getBytesOfFile(url);
        } catch (Exception e) {
            e.printStackTrace();
            // 返回500
            return new ResponseEntity<>(null, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 获取字节数组成功
        if (body.length > 0) {
            HttpHeaders headers = new HttpHeaders();
            String fileName = getFileName(url);
            headers.add("Content-Disposition", "attachment;filename=" + fileName);
            return new ResponseEntity<>(body, headers, HttpStatus.OK);
        }

        // 返回结果为空
        return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }


    //------------------------------------------------------------------
    //        private 方法，仅供内部调用
    //------------------------------------------------------------------

    /**
     * <p>
     * 通过文件路径获取字节数组
     * </p>
     *
     * @param url 文件路径
     * @return {@link byte[] }
     * @author shijintao@supconit.com
     * @date 2020/5/26 14:09
     */
    private static byte[] getBytesOfFile(String url) throws IOException {
        File analysedPhoto = new File(url);
        byte[] body = null;
        InputStream in = new FileInputStream(analysedPhoto);
        body = new byte[in.available()];
        int read = in.read(body);
        return body;
    }

    /**
     * <p>
     * 获取图片名
     * </p>
     *
     * @param url 图片url
     * @return {@link String }
     * @author flyeat
     * @date 2020/5/26 13:59
     */
    private static String getFileName(String url) {
        int index = url.lastIndexOf("/") + 1;
        return url.substring(index);
    }

}

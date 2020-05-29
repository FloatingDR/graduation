package com.cafuc.graduation.util;

import com.cafuc.graduation.exception.BaseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * <p>
 * 文件工具类
 * </p>
 *
 * @author flyeat
 * @date 2020/5/25 11:35
 */
public class FileUtil {

    public static String ByteToFile(byte[] bytes, String saveUrl, String suffix) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        BufferedImage bi1 = ImageIO.read(bais);
        String newUrl = null;
        try {
            int dont = saveUrl.lastIndexOf(".");
            newUrl = saveUrl.substring(0, dont) + String.format("_%s.png", suffix);
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

    /**
     * <p>
     * 保存图片
     * </p>
     *
     * @param fileName 文件名
     * @param savePath 保存路径
     * @param file     文件
     * @return {@link String }
     * @author shijintao@supconit.com
     * @date 2020/5/29 09:34
     */
    public static String saveFile(String fileName, String savePath, MultipartFile file) throws IOException {
        return saveFile0(fileName, savePath, file);
    }

    /* 预先检查文件 */
    public static void checkImgValid(MultipartFile originFile) throws Exception {
        //校验文件格式
        if (originFile == null) {
            throw new BaseException("未检测到文件");
        }
        if (originFile.getOriginalFilename() == null) {
            throw new BaseException("文件名不能为空");
        }
        String suffixs = ".bmp.jpg.png.BMP.JPG.PNG.jpeg.JPEG";
        String suffix = originFile.getOriginalFilename().substring(originFile.getOriginalFilename()
                .lastIndexOf("."));
        if (!suffixs.contains(suffix)) {
            throw new BaseException("图片格式有误，必须为bmp, jpg, png，jpeg图片格式中的一种");
        }
    }

    /* 生成uuid */
    public static String uuid() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    /* 获取文件类型 */
    public static String getFileType(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        int i = originalFilename.lastIndexOf(".");
        return originalFilename.substring(i + 1);
    }

    //------------------------------------------------------------------
    //        private 方法，仅供内部调用
    //------------------------------------------------------------------

    private static String saveFile0(String fileName, String savePath, MultipartFile file) throws IOException {
        Path p = Paths.get(savePath + fileName);
        Path newFile = null;
        if (!Files.exists(p)) {
            newFile = Files.createFile(p);
        }
        InputStream in = file.getInputStream();
        assert newFile != null;
        Files.copy(in, newFile, StandardCopyOption.REPLACE_EXISTING);
        return savePath + fileName;
    }

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

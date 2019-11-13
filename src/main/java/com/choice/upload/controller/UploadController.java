package com.choice.upload.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
// import org.springframework.web.multipart.MultipartHttpServletRequest;
// import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
// import java.util.List;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

@RestController
public class UploadController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);
    private static final String token = "eST6mPO1KYvmjnmDPsjy5W2xe3LC60gC";

    @GetMapping("/upload")
    public String upload() {
        return "请上传ZIP文件，file: zip , path：/upload , method: post";
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestHeader("x-token") String myToken,
            @RequestParam("file") MultipartFile file) throws ZipException {
        if (file.isEmpty()) {
            return new ResponseEntity<>("上传失败，请选择文件", HttpStatus.FORBIDDEN);
        }

        if (!myToken.equals(token)) {
            return new ResponseEntity<>("x-token 验证失败", HttpStatus.FORBIDDEN);
        }

        String fileName = file.getOriginalFilename();
        String filePath = "D:\\temp\\";
        String distPath = "D:\\dist\\";
        File dest = new File(filePath + fileName);

        try {
            file.transferTo(dest);
            LOGGER.info("上传成功 " + dest);
            ZipFile zipFile = new ZipFile(dest);
            try {
                zipFile.extractAll(distPath);
                LOGGER.info("完成解压 " + dest + " ==> " + distPath);
            } catch (Exception e) {
                LOGGER.error(e.toString(), e);
                return new ResponseEntity<>("解压失败！请上传ZIP格式文件", HttpStatus.FORBIDDEN);
            }

            // 解压后删除上传的zip文件
            if (dest.delete()) {
                LOGGER.info("删除成功 " + dest);
            } else {
                LOGGER.info("删除失败 " + dest);
            }
            System.out.println(myToken);
            return new ResponseEntity<>("上传成功!", HttpStatus.OK);
        } catch (IOException e) {
            LOGGER.error(e.toString(), e);
            return new ResponseEntity<>("上传失败！", HttpStatus.BAD_GATEWAY);
        }

    }

    // @GetMapping("/multiUpload")
    // public String multiUpload() {
    // return "multiUpload";
    // }

    // @PostMapping("/multiUpload")
    // public String multiUpload(HttpServletRequest request) {
    // List<MultipartFile> files = ((MultipartHttpServletRequest)
    // request).getFiles("file");
    // String filePath = "/Users/itinypocket/workspace/temp/";
    // for (int i = 0; i < files.size(); i++) {
    // MultipartFile file = files.get(i);
    // if (file.isEmpty()) {
    // return "上传第" + (i++) + "个文件失败";
    // }
    // String fileName = file.getOriginalFilename();

    // File dest = new File(filePath + fileName);
    // try {
    // file.transferTo(dest);
    // LOGGER.info("第" + (i + 1) + "个文件上传成功");
    // } catch (IOException e) {
    // LOGGER.error(e.toString(), e);
    // return "上传第" + (i++) + "个文件失败";
    // }
    // }

    // return "上传成功";

    // }

}

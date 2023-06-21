package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${reggie.path}")
    private String path;
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        //获取文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + suffix;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //file是一个临时文件，需要将其保存到指定的目录下
        file.transferTo(new File(path + fileName));
        return R.success(fileName);
    }
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws FileNotFoundException, IOException{
        //输入流
        FileInputStream fileInputStream = new FileInputStream(new File(path + name));

        ServletOutputStream outputStream = response.getOutputStream();
        int len = 0;
        byte[] buffer = new byte[1024];
        while ((len = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer,0,len);
        }
        response.setContentType("image/jpeg");
        fileInputStream.close();
        outputStream.close();

    }


}

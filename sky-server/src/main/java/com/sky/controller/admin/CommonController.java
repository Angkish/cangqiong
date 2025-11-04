package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 通用Controller
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传：{}",file);
        try {
            //获取原始文件名
            String originalFilename = file.getOriginalFilename();

            //新的文件名
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName = UUID.randomUUID().toString() + extension;

            //保存文件
            file.transferTo(new File("D:/CangQiong/images/"+newFileName));

            /**
             * 根本原因
             * 前后端分离架构中的端口不匹配问题
             *
             * 具体原因分析
             * 前端运行在8081端口：开发服务器端口
             * 后端运行在8080端口：Spring Boot应用端口
             * 静态资源在后端：图片文件实际存储在8080端口的服务器上
             * 前端使用相对路径：当使用 /images/xxx.jpg 时，浏览器会自动在当前域名（8081）下寻找资源
             * 资源不存在：8081端口没有图片资源，所以404
             */
            //返回包含端口的完整URL
            String fileUrl = "http://localhost:8080/images/" + newFileName;
            return Result.success(fileUrl);

        } catch (IOException e) {
            log.error("文件上传失败"+ e);
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
    }
}

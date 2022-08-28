package io.renren.modules.takeout.controller;

import io.renren.common.annotation.LogOperation;
import io.renren.common.properties.MinioProperties;
import io.renren.common.redis.RedisKeys;
import io.renren.common.redis.RedisUtils;
import io.renren.common.utils.MinioUtils;
import io.renren.common.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * 文件上传
 * @author David 321740709@qq.com
 */
@RestController
@RequestMapping("takeout/common")
@Api(tags="文件上传")
public class CommonController {
    @Autowired
    private MinioProperties minioProperties;

    @Autowired
    private MinioUtils minioUtils;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    @LogOperation("文件上传")
    @RequiresPermissions("takeout:common:upload")
    public Result<String> upload(MultipartFile file){
        boolean flag = false;
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        //.jpg
        String extention = originalFilename.substring(index - 1);
        String fileName = UUID.randomUUID().toString() + extention;
        try {

            //将文件上传到Minio
            minioUtils.upload(file, fileName, minioProperties.getBucketName());

            //将文件上传到七牛云服务器
            //QiniuUtils.upload2Qiniu(file.getBytes(),fileName);
            //jedisPool.getResource().sadd(RedisConstant.FOOD_PIC_RESOURCES,fileName);
            //redisUtils.save2Qiniu(fileName);
            redisTemplate.opsForSet().add(RedisKeys.getFoodPicResources(),fileName);
            //redisTemplate.opsForSet().add(RedisConstant.FOOD_PIC_RESOURCES,fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<String>().error("图片上传失败！");
        }
        return new Result<String>().ok(fileName);
    }
}

package io.renren.modules.job.task;

import io.renren.common.properties.MinioProperties;
import io.renren.common.redis.RedisKeys;
import io.renren.common.utils.MinioUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 定时清理垃圾图片任务
 * @author Yangx 321740709@qq.com
 * @DATE 2022/8/27 20:13
 **/
@Slf4j
@Component("cleanUpImagesTask")
public class CleanUpImagesTask implements ITask{
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MinioProperties minioProperties;
    @Autowired
    private MinioUtils minioUtils;

    @Override
    public void run(String params) {
        Set<String> set = redisTemplate.opsForSet().difference(RedisKeys.getFoodPicResources(), RedisKeys.getFoodPicDbResources());
        if (set!=null){
            for (String picName : set){
                minioUtils.removeObject(minioProperties.getBucketName(),picName );
                redisTemplate.opsForSet().remove(RedisKeys.getFoodPicResources(),picName);
                log.info("定时任务执行成功！清理垃圾图片:{}",picName);
            }
        }

    }
}

/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.common.constant.Constant;
import io.renren.common.exception.ErrorCode;
import io.renren.common.oss.service.SysOssService;
import io.renren.common.service.SysParamsService;
import io.renren.common.utils.Result;
import io.renren.common.oss.cloud.OSSFactory;
import io.renren.common.oss.entity.SysOssEntity;
import io.renren.common.oss.service.SysOssService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传
 * 
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("mp/oss")
@Api(tags="文件上传")
public class ApiOssController {
	@Autowired
	private SysOssService sysOssService;
    @Autowired
    private SysParamsService sysParamsService;

    private final static String KEY = Constant.CLOUD_STORAGE_CONFIG_KEY;



	@PostMapping("upload")
	@ApiOperation(value = "上传文件")
	@Login
	public Result<Map<String, Object>> upload(@RequestParam("file") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			return new Result<Map<String, Object>>().error(ErrorCode.UPLOAD_FILE_EMPTY);
		}

		//上传文件
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		String url = OSSFactory.build().uploadSuffix(file.getBytes(), extension);

		//保存文件信息
		SysOssEntity ossEntity = new SysOssEntity();
		ossEntity.setUrl(url);
		ossEntity.setCreateDate(new Date());
		sysOssService.insert(ossEntity);

		Map<String, Object> data = new HashMap<>(1);
		data.put("src", url);

		return new Result<Map<String, Object>>().ok(data);
	}

	@DeleteMapping
	@ApiOperation(value = "删除")
	@Login
	public Result delete(@RequestBody Long[] ids){
		for (Long id : ids) {
			String url = sysOssService.selectById(id).getUrl();
			String fileName = url.substring(url.lastIndexOf("/",url.lastIndexOf("/")- 1) + 1);
			System.out.println(fileName);
			OSSFactory.build().removeObject(fileName);
			sysOssService.deleteById(id);
		}
		return new Result();
	}

}
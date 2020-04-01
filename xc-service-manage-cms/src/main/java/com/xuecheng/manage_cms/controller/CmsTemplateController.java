package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsTemplateControllerApi;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.CmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cms/template")
public class CmsTemplateController implements CmsTemplateControllerApi {


    @Autowired
    CmsTemplateService cmsTemplateService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, CmsTemplate queryPageRequest) {
        return cmsTemplateService.findList(page, size, queryPageRequest);
    }

    @Override
    @PostMapping("/upload")
    public ResponseResult upload(@RequestParam("file") MultipartFile file, HttpServletRequest req) {
        return cmsTemplateService.uploadTemplateFile(file,req);
    }


}

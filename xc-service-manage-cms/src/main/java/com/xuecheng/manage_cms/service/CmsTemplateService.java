package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class CmsTemplateService {

    @Autowired
    CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    GridFsTemplate gridFsTemplate;

    /**
     * 分页查询模板列表
     * @param page
     * @param size
     * @param queryPageRequest
     * @return
     */
    public QueryResponseResult findList(int page, int size, CmsTemplate queryPageRequest){
        //分页参数
        if(page <=0){
            page = 1;
        }
        page = page -1;
        if(size<=0){
            size = 10;
        }
        //条件匹配器
        //页面名称模糊查询，需要自定义字符串的匹配器实现模糊查询
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher("templateName", ExampleMatcher.GenericPropertyMatchers.contains());
        //排序
//        Sort sort = new Sort();
        //创建条件实例
        Pageable pageable = PageRequest.of(page,size);
        Example<CmsTemplate> example = Example.of(queryPageRequest, exampleMatcher);
        Page<CmsTemplate> all = cmsTemplateRepository.findAll(example,pageable);
        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());//数据列表
        queryResult.setTotal(all.getTotalElements());//数据总记录数
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }

    /**
     * 上传模板
     * @param file
     * @return
     */
    public ResponseResult uploadTemplateFile(MultipartFile file, HttpServletRequest req) {
        if (file.isEmpty()) {
            ExceptionCast.cast(CmsCode.CMS_TEMPLATEFILE_NOTEXISTS);
        }
        String fileName = file.getOriginalFilename();
        String realPath = req.getServletContext() + fileName;
        //定义输入流
        FileInputStream inputStram = null;
        try {
            inputStram = (FileInputStream) file.getInputStream();
        } catch (FileNotFoundException e) {
            new ResponseResult(CommonCode.UPLOAD_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //向GridFS存储文件
        ObjectId objectId = gridFsTemplate.store(inputStram, fileName, "");
        //得到文件ID
        String fileId = objectId.toString();
        //模板表中插入数据
        CmsTemplate cmsTemplate = new CmsTemplate();
        cmsTemplate.setSiteId("5a751fab6abb5044e0d19ea1");
        cmsTemplate.setTemplateFileId(fileId);
        cmsTemplate.setTemplateName(fileName);
        ResponseResult responseResult = saveTemplate(cmsTemplate);
        return responseResult;
    }

    /**
     * 保存模板数据
     * @param cmsTemplate
     * @return
     */
    public ResponseResult saveTemplate(CmsTemplate cmsTemplate){
        CmsTemplate save = cmsTemplateRepository.save(cmsTemplate);
        return new ResponseResult(CommonCode.SUCCESS);
    }
}

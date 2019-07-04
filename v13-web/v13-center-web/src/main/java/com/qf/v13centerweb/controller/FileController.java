package com.qf.v13centerweb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.qf.v13.common.pojo.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author maizifeng
 * @Date 2019/6/13
 */
@Controller
@RequestMapping("file")
public class FileController {

    @Autowired
    private FastFileStorageClient client;

    @Value("${image.server}")
    private String imageServer;

    @PostMapping("upload")
    @ResponseBody
    public ResultBean fileUpload(MultipartFile file){
        String filename = file.getOriginalFilename();

        String suffixName = filename.substring(filename.lastIndexOf(".") + 1);
        try {
            StorePath storePath = client.uploadFile(file.getInputStream(),file.getSize() ,suffixName,null );
            String path=new StringBuilder(imageServer).append(storePath.getFullPath()).toString();
            return new ResultBean("200", path);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResultBean("404", "上传失败");
        }

    }
    @PostMapping("upload2")
    @ResponseBody
    public String fileUpload2(MultipartFile file){
        String filename = file.getOriginalFilename();

        String suffixName = filename.substring(filename.lastIndexOf(".") + 1);
        JSONObject json=new JSONObject();
        JSONArray array=new JSONArray();
        try {
            StorePath storePath = client.uploadFile(file.getInputStream(),file.getSize() ,suffixName,null );
            String path =new StringBuilder(imageServer).append(storePath.getFullPath()).toString();

            array.add(path);
            json.put("errno", 0);
            json.put("data", array);
            return json.toString();
        } catch (IOException e) {
            e.printStackTrace();
            json.put("errno", 1);
            json.put("data", null);
            return json.toString();
        }
    }

}

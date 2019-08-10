package cn.edu.swpu.exam.controller;


import cn.edu.swpu.exam.service.FileService;
import cn.edu.swpu.exam.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.Callable;

@Slf4j
@RestController
@RequestMapping("/file")
@CrossOrigin(origins = "http://192.168.8.100:5488")
public class FileController {

    @Autowired
    private FileService fileService;


    /**
     * 服务器位置
     **/

    @RequestMapping("/upload")
    public Callable<ResultVo> singleFileUpload(
            HttpServletRequest request,
            @RequestParam("file") MultipartFile file) throws Exception {

        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding("UTF-8");
        }

        log.info("upload 主线程开始");

        Callable<ResultVo> resultVoCallable = () -> {

            log.info("fileService.uploadFile 副线程开始");
            ResultVo resultVo = fileService.uploadFile(request,file);
            log.info("fileService.uploadFile 副线程返回");

            return resultVo;
        };

        log.info("upload 主线程返回");

        return resultVoCallable;
    }



    @RequestMapping(value ="/submit",produces = { "application/json;charset=UTF-8" })
    public ResultVo submitFile(HttpServletRequest request,
                               @RequestParam("file") MultipartFile file){
        return fileService.submitFile(request,file);
    }

    @GetMapping("/downloadAllFiles")
    public ResultVo downloadAllFiles(HttpServletResponse response){
        return fileService.getZipFile(response);
    }

}

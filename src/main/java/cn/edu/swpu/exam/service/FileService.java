package cn.edu.swpu.exam.service;

import cn.edu.swpu.exam.vo.ResultVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface FileService {

    ResultVo uploadFile(HttpServletRequest request, MultipartFile multipartFile);


    /**
     * 上传结果
     * @param request
     * @param multipartFile
     * @return
     */
    ResultVo submitFile(HttpServletRequest request, MultipartFile multipartFile);

    public ResultVo getExcel (HttpServletResponse response);

    public ResultVo getZipFile(HttpServletResponse response);
}

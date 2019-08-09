package cn.edu.swpu.jdata_exam.service;

import cn.edu.swpu.jdata_exam.vo.ResultVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface FileService {

    ResultVo uploadFile(HttpServletRequest request, MultipartFile multipartFile);


    ResultVo submitFile(HttpServletRequest request, MultipartFile multipartFile);

    public ResultVo getExcel (HttpServletResponse response);

    public ResultVo getZipFile(HttpServletResponse response);
}

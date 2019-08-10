package cn.edu.swpu.exam.service;


import cn.edu.swpu.exam.vo.ResultVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface VerifyCodeService {

    ResultVo createVerifyCode(HttpServletRequest request, HttpServletResponse response);

    ResultVo checkVerifyCode(HttpServletRequest request, String userCode);



}

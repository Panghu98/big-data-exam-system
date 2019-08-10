package cn.edu.swpu.exam.service;

import cn.edu.swpu.exam.entity.RegisterUser;
import cn.edu.swpu.exam.entity.dto.UserLogin;
import cn.edu.swpu.exam.entity.dto.UserUpdatePw;
import cn.edu.swpu.exam.vo.ResultVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserBasicInfoService {

    /**
     * 用户登录
     * @param userLogin
     * @param request
     * @param response
     * @return
     */
    ResultVo userLogin(UserLogin userLogin, HttpServletRequest request, HttpServletResponse response);

    /**
     * 用户修改密码
     * @param userUpdatePw
     * @param request
     * @return
     */
    ResultVo updateUserPw(UserUpdatePw userUpdatePw, HttpServletRequest request);

    /**
     * 用户注册
     * @param registerUser
     * @return
     */
    ResultVo register(RegisterUser registerUser);



}

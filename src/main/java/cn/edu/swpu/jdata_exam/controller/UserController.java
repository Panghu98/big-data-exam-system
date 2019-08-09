package cn.edu.swpu.jdata_exam.controller;

import cn.edu.swpu.jdata_exam.entity.RegisterUser;
import cn.edu.swpu.jdata_exam.entity.dto.UserLogin;
import cn.edu.swpu.jdata_exam.entity.dto.UserUpdatePw;
import cn.edu.swpu.jdata_exam.service.UserBasicInfoService;
import cn.edu.swpu.jdata_exam.service.VerifyCodeService;
import cn.edu.swpu.jdata_exam.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author panghu
 */
@Slf4j
@RequestMapping(value = "/user",produces = { "application/json;charset=UTF-8" })
@RestController
@CrossOrigin(origins = "http://192.168.8.100:5488")
public class UserController {

    @Autowired
    private UserBasicInfoService userBasicInfoService;

    @Autowired
    private VerifyCodeService verifyCodeService;

    /**
     * 用户登录
     * @param userLogin
     * @param request
     * @param response
     * @return
     */
    @PostMapping(value = "/login")
    public ResultVo login(@RequestBody UserLogin userLogin, HttpServletRequest request, HttpServletResponse response){
        log.info("进入/login服务");
        return userBasicInfoService.userLogin(userLogin,request,response);
    }


    /**
     * 用户修改密码
     * @param userUpdatePw
     * @return
     */
    @RequestMapping(value = "/updatePw")
    public ResultVo updatePw(@RequestBody UserUpdatePw userUpdatePw, HttpServletRequest request){

        log.info("进入/updatePw");

        return userBasicInfoService.updateUserPw(userUpdatePw,request);



    }



    /**
     * 获取验证码接口
     * @param request
     * @param response
     */
    @GetMapping(value = "/getCode")
    public ResultVo getVerifyCode(HttpServletRequest request, HttpServletResponse response){
        return verifyCodeService.createVerifyCode(request,response);

    }

    @PostMapping(value = "/register")
    public ResultVo register(RegisterUser registerUser){
        log.error("进入注册");
        return userBasicInfoService.register(registerUser);

    }


}

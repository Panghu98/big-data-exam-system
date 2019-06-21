package cn.edu.swpu.jdata_exam.controller;

import cn.edu.swpu.jdata_exam.entity.RegisterUser;
import cn.edu.swpu.jdata_exam.entity.dto.UserLogin;
import cn.edu.swpu.jdata_exam.entity.dto.UserUpdatePw;
import cn.edu.swpu.jdata_exam.service.UserBasicInfoService;
import cn.edu.swpu.jdata_exam.service.UserScoreService;
import cn.edu.swpu.jdata_exam.service.VerifyCodeService;
import cn.edu.swpu.jdata_exam.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequestMapping("/user")
@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private UserBasicInfoService userBasicInfoService;

    @Autowired
    private UserScoreService userScoreService;

    @Autowired
    private VerifyCodeService verifyCodeService;

    /**
     * 用户登录
     * @param userLogin
     * @param request
     * @param response
     * @return
     */
    @PostMapping(value = "/login",produces = { "application/json;charset=UTF-8" })
    public ResultVo login(@RequestBody UserLogin userLogin, HttpServletRequest request, HttpServletResponse response){
        log.info("进入/login服务");
        return userBasicInfoService.userLogin(userLogin,request,response);
    }


    /**
     * 用户修改密码
     * @param userUpdatePw
     * @return
     */
    @RequestMapping(value = "/updatePw",produces = { "application/json;charset=UTF-8" })
    public ResultVo updatePw(@RequestBody UserUpdatePw userUpdatePw, HttpServletRequest request){

        log.info("进入/updatePw");

        return userBasicInfoService.updateUserPw(userUpdatePw,request);



    }


    /**
     * 用户查询个人最好成绩
     * @param
     * @return
     */
    @RequestMapping(value = "/select/score",produces = { "application/json;charset=UTF-8" })
    public ResultVo selectScore(){
        log.info("进入/select/score服务");

        return userScoreService.getScoreById();

    }


    /**
     * 获取验证码接口
     * @param request
     * @param response
     */
    @GetMapping(value = "/getCode",produces = { "application/json;charset=UTF-8" })
    public ResultVo getVerifyCode(HttpServletRequest request, HttpServletResponse response){
        return verifyCodeService.createVerifyCode(request,response);

    }

    @PostMapping(value = "/register",produces = { "application/json;charset=UTF-8" })
    public ResultVo register(RegisterUser registerUser){
        log.error("进入注册");
        return userBasicInfoService.register(registerUser);

    }


}

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
@CrossOrigin(allowedHeaders = "*")
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
    @PostMapping("/login")
    public ResultVo login(@RequestBody UserLogin userLogin, HttpServletRequest request, HttpServletResponse response){

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept,Authorization");
        response.addHeader("Access-Control-Expose-Headers", "Origin, X-Requested-With, Content-Type, Accept,Authorization");


        log.info("进入/login服务");
        log.error(userLogin.toString());
        return userBasicInfoService.userLogin(userLogin,request,response);
    }


    /**
     * 用户修改密码
     * @param userUpdatePw
     * @return
     */
    @RequestMapping("/updatePw")
    public ResultVo updatePw(@RequestBody UserUpdatePw userUpdatePw, HttpServletRequest request){

        log.info("进入/updatePw");

        ResultVo resultVo = userBasicInfoService.updateUserPw(userUpdatePw,request);

        return resultVo;



    }


    /**
     * 用户查询个人最好成绩
     * @param
     * @return
     */
    @RequestMapping("/select/score")
    public ResultVo selectScore(){
        log.info("进入/select/score服务");

        ResultVo resultVo = userScoreService.getScoreById();

        return resultVo;

    }


    /**
     * 获取验证码接口
     * @param request
     * @param response
     */
    @GetMapping("/getCode")
    public ResultVo getVerifyCode(HttpServletRequest request, HttpServletResponse response){

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept,Authorization");
        response.addHeader("Access-Control-Expose-Headers", "Origin, X-Requested-With, Content-Type, Accept,Authorization");

        return verifyCodeService.createVerifyCode(request,response);

    }

    @PostMapping("/register")
    public ResultVo register(RegisterUser registerUser, HttpServletResponse response, HttpServletRequest request){
        log.error("进入注册");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept,Authorization");
        response.addHeader("Access-Control-Expose-Headers", "Origin, X-Requested-With, Content-Type, Accept,Authorization");
        return userBasicInfoService.register(registerUser);

    }










}

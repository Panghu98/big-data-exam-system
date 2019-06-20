package cn.edu.swpu.jdata_exam.controller;

import cn.edu.swpu.jdata_exam.service.FileService;
import cn.edu.swpu.jdata_exam.service.UserScoreService;
import cn.edu.swpu.jdata_exam.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author panghu
 * @title: TestController
 * @projectName jdata_exam
 * @date 19-6-17 上午9:53
 */

@RequestMapping("/info")
@RestController
@CrossOrigin(allowedHeaders = "*")
public class InfoController {

    @Autowired
    private FileService fileService;

    @Autowired
    private UserScoreService userScoreService;

    @GetMapping("/info")
    public String test(){
        return "for test";
    }

    @GetMapping(value = "/getExcel",produces = { "application/json;charset=UTF-8" })
    public void getExcel(HttpServletResponse response) throws IOException {
        fileService.getExcel(response);
    }

    @GetMapping(value = "/getRankingList",produces = { "application/json;charset=UTF-8" })
    public ResultVo getRankingList(){
        return userScoreService.getRankingList();
    }

}

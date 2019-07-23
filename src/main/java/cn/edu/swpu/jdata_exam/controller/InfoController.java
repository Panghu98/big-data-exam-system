package cn.edu.swpu.jdata_exam.controller;

import cn.edu.swpu.jdata_exam.service.FileService;
import cn.edu.swpu.jdata_exam.service.UserScoreService;
import cn.edu.swpu.jdata_exam.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author panghu
 * @title: TestController
 * @projectName jdata_exam
 * @date 19-6-17 上午9:53
 */
@Slf4j
@RequestMapping(value = "/info",produces = { "application/json;charset=UTF-8" })
@RestController
@CrossOrigin(origins = "http://192.168.8.100:5488")
public class InfoController {

    @Autowired
    private UserScoreService userScoreService;

    @Autowired
    private FileService fileService;

    @GetMapping("/info")
    public String test(){
        return "for test";
    }

    @GetMapping(value = "/getExcel")
    public void getExcel(HttpServletResponse response) throws IOException {
        fileService.getExcel(response);
    }

    /**
     * 获取排行榜频繁查询,放入redis
     * 定时将最新的排行榜写入缓存
     * @return
     */
    @GetMapping(value = "/getRankingList")
    public ResultVo getRankingList(){
        return userScoreService.getRankingList();
    }

    /**
     * 用户查询个人最好成绩
     * @param
     * @return
     */
    @GetMapping(value = "/score")
    public ResultVo selectScore(){
        log.info("进入/info/score服务");
        return userScoreService.getScoreById();

    }

}

package cn.edu.swpu.jdata_exam.controller;

import cn.edu.swpu.jdata_exam.dao.UserScoreDAO;
import cn.edu.swpu.jdata_exam.entity.UserScore2;
import cn.edu.swpu.jdata_exam.service.FileService;
import cn.edu.swpu.jdata_exam.utils.ResultVoUtil;
import cn.edu.swpu.jdata_exam.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author panghu
 * @title: TestController
 * @projectName jdata_exam
 * @date 19-6-17 上午9:53
 */
@Slf4j
@RestController
@CrossOrigin(allowedHeaders = "*",origins = "http://47.107.61.232:8666")
public class InfoController {

    @Autowired
    private FileService fileService;

    @Autowired
    private UserScoreDAO userScoreDAO;

    @GetMapping("/info")
    public String test(){
        return "for test";
    }

    @GetMapping(value = "/info/getExcel",produces = { "application/json;charset=UTF-8" })
    public void getExcel(HttpServletResponse response) throws IOException {
        fileService.getExcel(response);
    }

    @GetMapping(value = "/info/getRankingList",produces = { "application/json;charset=UTF-8" })
    public ResultVo getRankingList(){
        List<UserScore2> list = userScoreDAO.getRankingList(userScoreDAO.getUserList());
        return ResultVoUtil.success(list);
    }


}

package cn.edu.swpu.jdata_exam.controller;

import cn.edu.swpu.jdata_exam.entity.UserScore2;
import cn.edu.swpu.jdata_exam.service.FileService;
import cn.edu.swpu.jdata_exam.service.UserScoreService;
import cn.edu.swpu.jdata_exam.utils.ResultVoUtil;
import cn.edu.swpu.jdata_exam.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author panghu
 * @title: TestController
 * @projectName jdata_exam
 * @date 19-6-17 上午9:53
 */
@Slf4j
@RestController
@CrossOrigin(allowedHeaders = "*",origins = "*")
public class InfoController {

    @Autowired
    private FileService fileService;

    @GetMapping("/info")
    public String test(){
        return "for test";
    }

    @GetMapping(value = "/info/getExcel",produces = { "application/json;charset=UTF-8" })
    public void getExcel(HttpServletResponse response) throws IOException {
        fileService.getExcel(response);
    }

    @ResponseBody
    @GetMapping(value = "/info/getRankingList",produces = { "application/json;charset=UTF-8" })
    public ResponseEntity<ResultVo> getRankingList(HttpServletResponse response, HttpServletRequest request){
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept,Authorization");
        response.addHeader("Access-Control-Expose-Headers", "Origin, X-Requested-With, Content-Type, Accept,Authorization");
        ResultVo resultVo;
        try {
            List<UserScore2> userScore2List = new ArrayList<>();
            resultVo = ResultVoUtil.success(userScore2List);
        }catch (Exception e ){
            resultVo = ResultVoUtil.error(1,e.getMessage());
            e.printStackTrace();
        }
        return ResponseEntity.ok(resultVo);
    }

    @RequestMapping(value = "/*",method = RequestMethod.OPTIONS)
    public ResponseEntity.HeadersBuilder<?> handleOptions(){
        return ResponseEntity.noContent();
    }

}

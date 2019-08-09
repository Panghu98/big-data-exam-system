package cn.edu.swpu.jdata_exam.service;


import cn.edu.swpu.jdata_exam.vo.ResultVo;

public interface UserScoreService {

    ResultVo getScoreById();

    ResultVo getRankingList();

}

package cn.edu.swpu.exam.service;


import cn.edu.swpu.exam.vo.ResultVo;

public interface UserScoreService {

    ResultVo getScoreById();

    ResultVo getRankingList();

}

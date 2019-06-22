package cn.edu.swpu.jdata_exam.dao;


import cn.edu.swpu.jdata_exam.entity.UserScore;
import cn.edu.swpu.jdata_exam.entity.UserScore2;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户成绩信息操作
 */

@Repository
@Mapper
public interface UserScoreDAO {

    //根据id查询成绩信息
    UserScore2 getScoreById(String userId);

    //添加成绩
    int addScore(UserScore userScore);

    List<UserScore> selectByTime();

    //更新成绩
//    int updateScore(UserScore userScore);

    List<UserScore2> getRankingList(List<Long> list);


    List<Long> getUserList();


}

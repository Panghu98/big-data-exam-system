package cn.edu.swpu.exam.dao;


import cn.edu.swpu.exam.entity.UserScore;
import cn.edu.swpu.exam.entity.UserScore2;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户成绩信息操作
 */

@Repository
@Mapper
public interface UserScoreDAO {

    /**
     * 根据id查询成绩信息
     * @param userId  用户ID
      */
    UserScore2 getScoreById(String userId);

    /**
     * 添加成绩
     * @param userScore
     * @return
     */
    void addScore(UserScore userScore);

    List<UserScore> selectByTime();


    List<UserScore2> getRankingList();


    List<Long> getUserList();


}

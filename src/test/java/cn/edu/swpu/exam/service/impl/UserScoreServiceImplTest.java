package cn.edu.swpu.exam.service.impl;

import cn.edu.swpu.exam.service.UserScoreService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author panghu
 * @title: UserScoreServiceImplTest
 * @projectName exam
 * @date 2019/7/28 下午4:02
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserScoreServiceImplTest {

    private final static String PREFIX = "score-hash";


    @Autowired
    private UserScoreService userScoreService;

    @Test
    public void getRankingList() {

        System.out.println(userScoreService.getRankingList());

    }
}
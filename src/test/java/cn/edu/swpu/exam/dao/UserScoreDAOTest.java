package cn.edu.swpu.exam.dao;

import cn.edu.swpu.exam.entity.UserScore2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author panghu
 * @title: UserScoreDAOTest
 * @projectName exam
 * @date 19-6-24 上午8:34
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserScoreDAOTest {

    @Autowired
    private UserScoreDAO userScoreDAO;

    @Test
    public void getRankingList() {

        List<UserScore2> list = userScoreDAO.getRankingList();
        for (UserScore2 score2:list
             ) {
            System.out.print(score2.toString());
        }

    }

    @Test
    public void getUsers(){
        List<Long> list = userScoreDAO.getUserList();
        System.out.println(list);
    }

}
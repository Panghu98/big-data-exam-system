package cn.edu.swpu.jdata_exam.dao;

import cn.edu.swpu.jdata_exam.entity.RegisterUser;
import cn.edu.swpu.jdata_exam.entity.UserBasicInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *用户信息操作
 */
@Repository
@Mapper
public interface UserBasicInfoDAO {

    //根据id查询信息
    UserBasicInfo getUserById(String userId);

    //添加用户
    int addUser(RegisterUser registerUser);

    //修改密码
    int updatePw(UserBasicInfo userBasicInfo);

    List<Long> getUserList();

}

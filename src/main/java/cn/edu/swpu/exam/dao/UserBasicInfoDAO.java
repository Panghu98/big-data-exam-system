package cn.edu.swpu.exam.dao;

import cn.edu.swpu.exam.entity.RegisterUser;
import cn.edu.swpu.exam.entity.UserBasicInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *用户信息操作
 */
@Repository
@Mapper
public interface UserBasicInfoDAO {

    /**
     * 根据id查询信息
     * @param userId  用户ID
     * @return 用户信息
     */
    UserBasicInfo getUserById(String userId);

    /**
     * 添加用户
     * @param registerUser  注册用户
     */
    void addUser(RegisterUser registerUser);

    /**
     * 修改密码
     * @param userBasicInfo 用户信息
     */
    int updatePw(UserBasicInfo userBasicInfo);

    /**
     * 获取用户列表
     * @return  用户列表
     */
    List<Long> getUserList();

}

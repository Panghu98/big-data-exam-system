package cn.edu.swpu.jdata_exam.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户分数成绩
 */

@Data
public class UserScore implements Serializable {


    //学生学号
    private String userId;

    private String username;

    //班级名称
    private Integer className;

    //准确率
    private String accuracy;


}

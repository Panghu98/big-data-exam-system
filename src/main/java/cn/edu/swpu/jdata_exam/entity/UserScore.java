package cn.edu.swpu.jdata_exam.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户分数成绩
 */

@Data
public class UserScore implements Serializable {


    //学生学号
    private String userId;

    //提交时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateTime;

    //提交次数
    private Integer frequency;

    //准确率
    private String accuracy;


}

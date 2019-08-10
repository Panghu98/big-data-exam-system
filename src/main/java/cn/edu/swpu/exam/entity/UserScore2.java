package cn.edu.swpu.exam.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author panghu
 * @title: UserScore2
 * @projectName exam
 * @date 19-6-18 下午12:52
 */
@Data
public class UserScore2 implements Serializable {

    private static final long serialVersionUID = 2534576269111424941L;

    private String username;

    private String userId;

    private String accuracy;

//    private Integer ranking;

}

package cn.edu.swpu.exam.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 用户基本信息表
 */

@Data
public class UserBasicInfo {


    @NotNull
    private String userId;

    @NotNull
    private String userPassword;



}

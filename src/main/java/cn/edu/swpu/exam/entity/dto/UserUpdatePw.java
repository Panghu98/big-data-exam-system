package cn.edu.swpu.exam.entity.dto;

import lombok.Data;

@Data
public class UserUpdatePw {

    private String userId;

    private String oldPassword;

    private String newPassword;

    private String verifyCode;

}

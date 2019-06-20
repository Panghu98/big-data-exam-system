package cn.edu.swpu.jdata_exam.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserLogin {

    @NotNull
    private String userId;

    @NotNull
    private String userPassword;

    @NotNull
    private String verifyCode;
}

package cn.edu.swpu.exam.entity.dto;

import lombok.Data;

@Data
public class VerifyCodeDTO {

    private String verifyKey;

    private String verifyCode;

}

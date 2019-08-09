package cn.edu.swpu.jdata_exam.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author panghu
 * @title: RegisterUser
 * @projectName jdata_exam
 * @date 19-6-14 下午12:54
 */
@Data
public class RegisterUser {

    @NotNull
    private String userId;

    @NotNull
    private String userPassword;

    @NotNull

    /**
     * 1.闵帆 2.李平 3.陈雁1  4.陈雁2
     */
    private String className;

    @NotNull
    private String username;

}

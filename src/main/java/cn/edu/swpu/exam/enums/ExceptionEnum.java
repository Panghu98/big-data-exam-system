package cn.edu.swpu.exam.enums;


import lombok.Getter;

@Getter
public enum ExceptionEnum {

    /**
     *
     */
    USER_NOT_EXIT(1,"用户名不存在"),
    PLEASE_LOGIN_FIRST(2,"请先登录"),
    PASSWORD_ERROR(3,"密码错误"),
    GET_USERDATA_ERROR(4,"登录中获取用户详情出错"),
    UPDATE_SCORE_FAIL(5,"更新成绩失败"),
    CANNOT_ACCESS_OTHER(6,"你不能查看他人的成绩"),
    REPEAT_LOGIN(7,"请重新登录"),
    DATA_SHOULD_BE_POSITIVE_INTEGER(9,"Label中的数值不正确"),
    FILE_EMPTY(10,"文件为空,请重新上传"),
    FILE_UPLOAD_FAILED(11,"文件上传失败,请重新上传"),
    FILE_DATA_FORMAT_ERROR(12,"文件数据格式错误"),
    TITLE_ERROR(13,"文件标题不正确"),
    SSH_UPLOAD_FAILED(14,"ssh上传失败,请重新上传"),
    FILE_NAME_ERROR(15,"文件命名错误"),
    IO_EXCEPTION(16,"文件传输出错,请重试"),
    GET_CODE_ERROR(17,"获取验证码失败"),
    FILE_SIZE_NOT_CORRECT(18,"文件大小不正确"),
    MOUNT_OF_DATA_ERROR(19,"数据数量不正确"),
    VERIFY_CODE_ERROR(20,"验证码错误"),
    FILE_HAS_UPLOADED(21,"每天只能提交一次结果，你今天已经提交过了"),
    NOT_PERMISSION(22,"权限不够，无法访问"),
    SUBMIT_NOT_OPEN(23,"该通道尚未开通"),
    DONOT_MODIFY_OTHER(24,"你不能修改别人的密码"),
    OLD_PASSWORD_ERROR(25,"旧密码错误"),




    ;

    private Integer code;

    private String message;

    ExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}

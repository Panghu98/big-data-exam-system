package cn.edu.swpu.exam.exception;


import cn.edu.swpu.exam.enums.ExceptionEnum;
import lombok.Data;

@Data
public class JdataExamException extends RuntimeException {

    private Integer code;

    public JdataExamException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public JdataExamException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.code = exceptionEnum.getCode();
    }
}

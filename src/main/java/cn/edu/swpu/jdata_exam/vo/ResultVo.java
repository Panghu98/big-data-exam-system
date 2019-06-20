package cn.edu.swpu.jdata_exam.vo;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
@JsonSerialize
public class ResultVo<T> {

    //错误码
    private Integer code;
    //错误信息
    private String message;
    //具体内容
    private T data;

    public ResultVo(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResultVo() {
    }
}

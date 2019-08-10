package cn.edu.swpu.exam.utils;

import cn.edu.swpu.exam.enums.CodeEnum;
import cn.edu.swpu.exam.enums.ExceptionEnum;
import cn.edu.swpu.exam.vo.ResultVo;

public class ResultVoUtil {
    //成功返回，携带内容
    public static ResultVo success(Object object){

        ResultVo resultVo = new ResultVo();
        resultVo.setCode(CodeEnum.SUCCESS.getCode());
        resultVo.setMessage(CodeEnum.SUCCESS.getMessage());
        resultVo.setData(object);
        return resultVo;
    }

    //成功返回，携带内容
    public static ResultVo success(String message,Object object){

        ResultVo resultVo = new ResultVo();
        resultVo.setCode(CodeEnum.SUCCESS.getCode());
        resultVo.setMessage(message);
        resultVo.setData(object);
        return resultVo;
    }

    //成功返回，无携带内容
    public static ResultVo success(){

        return success(null);
    }

    //返回错误，携带错误信息及错误码
    public static ResultVo error(Integer code,String message){

        ResultVo resultVo = new ResultVo();
        resultVo.setCode(code);
        resultVo.setMessage(message);
        return resultVo;
    }

    public static ResultVo error(ExceptionEnum exceptionEnum){
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(exceptionEnum.getCode());
        resultVo.setMessage(exceptionEnum.getMessage());
        return resultVo;
    }


}

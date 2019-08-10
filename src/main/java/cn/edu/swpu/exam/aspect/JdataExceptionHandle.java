package cn.edu.swpu.exam.aspect;


import cn.edu.swpu.exam.enums.CodeEnum;
import cn.edu.swpu.exam.exception.JdataExamException;
import cn.edu.swpu.exam.utils.ResultVoUtil;
import cn.edu.swpu.exam.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author panghu
 */
@Slf4j
@Aspect
@ControllerAdvice
public class JdataExceptionHandle {

    @ResponseBody
    @ExceptionHandler(JdataExamException.class)
    public ResultVo handlerJdataException(JdataExamException je){

        log.info(je.getStackTrace().toString());

        ResultVo resultVo = null;
        if(je.getCode() == 2){
            resultVo = ResultVoUtil.error(je.getCode(),je.getMessage());
        }else {
            resultVo = ResultVoUtil.error(CodeEnum.ERROR.getCode(), je.getMessage());
        }
        return resultVo;
    }

}

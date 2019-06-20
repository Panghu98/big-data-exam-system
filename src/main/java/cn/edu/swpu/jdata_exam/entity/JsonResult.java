package cn.edu.swpu.jdata_exam.entity;

import lombok.Data;

/**
 * @author panghu
 * @title: JsonResult
 * @projectName jdata_exam
 * @date 19-6-20 下午8:39
 */
@Data
public class JsonResult {

    private String status;

    private Object result;

}

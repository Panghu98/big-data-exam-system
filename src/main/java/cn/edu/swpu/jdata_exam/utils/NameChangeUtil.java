package cn.edu.swpu.jdata_exam.utils;

/**
 * @author panghu
 * @title: NameChangeUtil
 * @projectName jdata_exam
 * @date 19-6-14 下午4:25
 */
public class NameChangeUtil {

    public static String getRealName(String userClass){
        switch (userClass){
            case "1":
                return "闵帆";
            case "2":
                return "李平";
            case "3":
                return "陈雁1";
            case "4":
                return "陈雁2";
            default:
                return "未指定";
            }
    }

}

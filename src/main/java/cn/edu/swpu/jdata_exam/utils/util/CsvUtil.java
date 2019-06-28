package cn.edu.swpu.jdata_exam.utils.util;

import cn.edu.swpu.jdata_exam.enums.ExceptionEnum;
import cn.edu.swpu.jdata_exam.exception.JdataExamException;
import cn.edu.swpu.jdata_exam.utils.AuthenticationUtil;
import cn.edu.swpu.jdata_exam.utils.ResultVoUtil;
import cn.edu.swpu.jdata_exam.vo.ResultVo;
import com.csvreader.CsvReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * @author cyg
 * @date 18-5-29 下午9:59
 **/
@Slf4j
public class CsvUtil {

    /**
     * 临时文件夹  用于文件的检测  检测通过之后上传到正确位置
     **/
    public static String UPLOADED_LOCAL_FOLDER = "/root/home/panghu/Project/exam/temp/";

//    public static String UPLOADED_LOCAL_FOLDER =  "/home/panghu/IdeaProjects/big-data-exam/src/main/resources/temp/";

    public static String UPLOADED_LOCAL_FOLDER2 = "/root/home/panghu/Project/exam/file/";



    //数据条数限制
    private static Integer number = 15000;

    private static Integer fileNameLength = 16;


    /**
     * 判断是否为正整数
     * @param str  字符串
     * @return true为是
     */
    private static boolean isNumeric(String str){
        for(int i=str.length();--i>=0;){
            int chr=str.charAt(i);
            if(chr<48 || chr>57) {
                return false;
            }
        }
        return true;
    }

    public static ResultVo read(String filePath, String name) {


        //记录ID 的长度
        List<Integer> list = new ArrayList<>();
        HashSet<Integer> hashSets = new HashSet<>();

        //Label的长度
        List<String> labelList = new ArrayList<>();
        int cs = 0;

        /**判断文件名是否正确**/
        if (!name.contains(".csv")) {
            log.info("[文件命名错误]");
            return ResultVoUtil.error(ExceptionEnum.FILE_NAME_ERROR);
        }


        //文件格式不是 16位
        if (name.length()!=fileNameLength){
            return ResultVoUtil.error(ExceptionEnum.FILE_NAME_ERROR);
        }

        /**得到文件名中的学号**/
        try {
            String schoolNumber = name.substring(0, 12);
//            todo 判断学号是否为用户本人
            String authUserId = AuthenticationUtil.getAuthUserId();

            if (authUserId == null){
                throw new JdataExamException(ExceptionEnum.REPEAT_LOGIN);
            }

            if (!schoolNumber.equals(authUserId)){

                log.info("文件名学号与用户本人不符合。文件名学号={}，用户本人学号={}",schoolNumber,authUserId);

                return ResultVoUtil.error(ExceptionEnum.FILE_NAME_ERROR);
            }
        }catch (Exception e){

            log.error(e.getMessage());
        }


        try {
            /**创建CSV读对象**/
            CsvReader csvReader = new CsvReader(filePath, ',', Charset.forName("utf-8"));
            while (csvReader.readRecord()) {
                /**读一整行**/
                String[] raws = csvReader.getRawRecord().split(",");
                /**判断第一行**/
                if (cs == 0) {
                    cs++;
                    String value1 = raws[0];
                    String value2 = raws[1];
                    /**解析后带上空格**/
                    if (!(value1.equals("Id") && value2.equals("Label"))) {
                        System.err.println("标题名称不对");
                        log.info("[标题名称不对]");
                        return ResultVoUtil.error(ExceptionEnum.TITLE_ERROR);
                    }
                } else {

                    /**第一行数据**/
                    for (int i = 0; i < raws.length; i++) {
                        String x = raws[i];
                        if (i == 0) {
                            /**防止ID为空**/
                            if (x != null && !"".equals(x)) {
                                try {
                                    hashSets.add(Integer.parseInt(x));
                                    list.add(Integer.parseInt(x));
                                }catch (Exception e){
                                    return ResultVoUtil.error(ExceptionEnum.FILE_DATA_FORMAT_ERROR);
                                }
                            }
                        }

                        String value = raws[1];
                        //判断数值是否为空
                        if (value==null|| "".equals(value)){
                            return ResultVoUtil.error(ExceptionEnum.FILE_DATA_FORMAT_ERROR);
                        }

                        if (!isNumeric(value)){
                            return ResultVoUtil.error(ExceptionEnum.DATA_SHOULD_BE_POSITIVE_INTEGER);
                        }
                        labelList.add(value);

                    }
                }
            }
        } catch (IOException e) {
            log.info("[文件格式存在语法错误]");
            return ResultVoUtil.error(ExceptionEnum.FILE_DATA_FORMAT_ERROR);
        }



        /**判断数据量**/
        if (list.size()!=number || labelList.size()!=number*2) {
            log.info("[数据数目不对,文件有" + list.size() + "条数据]");
            return ResultVoUtil.error(ExceptionEnum.MOUNT_OF_DATA_ERROR);
        }
        /**判断Id是否重复**/
        // list.size() --条数
        //
        if (list.size() != hashSets.size()) {
            log.info("[id重复]");
            return ResultVoUtil.error(ExceptionEnum.MOUNT_OF_DATA_ERROR);
        }

        return ResultVoUtil.success();
    }


}

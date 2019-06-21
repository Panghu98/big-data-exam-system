package cn.edu.swpu.jdata_exam.utils.util;

import cn.edu.swpu.jdata_exam.enums.ExceptionEnum;
import cn.edu.swpu.jdata_exam.exception.JdataExamException;
import cn.edu.swpu.jdata_exam.utils.AuthenticationUtil;
import com.csvreader.CsvReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author cyg
 * @date 18-5-29 下午9:59
 **/
@Slf4j
public class CsvUtil {

    /**
     * 本机位置
     **/
    public static String UPLOADED_LOCAL_FOLDER = "/root/home/panghu/Project/exam/data/backup/";

    private static Integer number = 85443;//数据条数限制
//    private static Integer number = 4;//测试

    public static boolean read(String filePath, String name) {


        List<Integer> list = new ArrayList<>();
        HashSet<Integer> hashSets = new HashSet<>();
        int cs = 0;

        /**判断文件名是否正确**/
        if (!name.contains(".csv")) {
            log.info("[文件命名错误]");
            return false;
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

                return false;
            }
        }catch (Exception e){

            log.error(e.getMessage());
        }


        try {
            /**创建CSV读对象**/
            CsvReader csvReader = new CsvReader(filePath, ',', Charset.forName("utf-8"));
            while (csvReader.readRecord()) {
                /**读一整行**/
                System.out.println(csvReader.getRawRecord());
                String raws[] = csvReader.getRawRecord().split(",");
                /**判断第一行**/
                if (cs == 0) {
                    cs++;
                    String value1 = raws[0];
                    String value2 = raws[1];
                    /**解析后带上空格**/
                    if (!(value1.equals("Id") && value2.equals("Label"))) {
                        log.info("[标题名称不对]");
                        return false;
                    }
                } else {
                    /**判断非第一行**/
                    for (int i = 0; i < raws.length; i++) {
                        String x = raws[i];
                        if (i == 0) {
                            /**防止为空**/
                            if (x != null && x != "") {
                                hashSets.add(Integer.parseInt(x));
                                list.add(Integer.parseInt(x));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.info("[文件格式存在语法错误]");
            e.printStackTrace();
            return false;
        }



        /**判断数据量**/
        if (list.size() == 0) {
            log.info("[数据数目不对,文件有" + list.size() + "条数据]");
            return false;
        }
        /**判断user_id是否重复**/
        // list.size() --条数
        //
        if (list.size() != hashSets.size()) {
            log.info("[id重复]");
            return false;
        }
        return true;
    }


}

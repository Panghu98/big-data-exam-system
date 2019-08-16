package cn.edu.swpu.exam.service.impl;

import cn.edu.swpu.exam.dao.UserScoreDAO;
import cn.edu.swpu.exam.entity.UserScore;
import cn.edu.swpu.exam.enums.BackMessageEnum;
import cn.edu.swpu.exam.enums.ExceptionEnum;
import cn.edu.swpu.exam.exception.JdataExamException;
import cn.edu.swpu.exam.service.FileService;
import cn.edu.swpu.exam.utils.AuthenticationUtil;
import cn.edu.swpu.exam.utils.NameChangeUtil;
import cn.edu.swpu.exam.utils.ResultVoUtil;
import cn.edu.swpu.exam.utils.util.CsvUtil;
import cn.edu.swpu.exam.utils.util.LocalExecute;
import cn.edu.swpu.exam.utils.util.SshUtil;
import cn.edu.swpu.exam.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author panghu
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private final static String prefixKey = "username--";

    private final static long MAX_SIZE = 180*1024;

    private final static long MIN_SIZE = 100*1024;

    private RedisTemplate<String, String> redisTemplate;

    private UserScoreDAO userScoreDAO;

    @Value( value = "${file.temp}")
    private String tempFolder;

    @Value(value = "${file.evidence}")
    private String evidenceFolder;

    @Autowired
    public FileServiceImpl(UserScoreDAO userScoreDAO, RedisTemplate<String, String> redisTemplate) {
        this.userScoreDAO = userScoreDAO;
        this.redisTemplate = redisTemplate;
    }

    //上传CSV  结果  应该先上传文件到本地（用于验证文件是否符合要求），再上传到服务器  这样保证文件的正确性
    //但是这里的本地其实就是服务器，有点矛盾

    @Override
    public ResultVo uploadFile(HttpServletRequest request, MultipartFile file) {

        String userId = prefixKey+AuthenticationUtil.getAuthUserId();


        //如果存在则不进行上传
        if (searchInCache(userId)){
            return ResultVoUtil.error(ExceptionEnum.FILE_HAS_UPLOADED.getCode(),
                            ExceptionEnum.FILE_HAS_UPLOADED.getMessage());
        }


        /**判断是否为空**/
        if (file.isEmpty()) {
            throw new JdataExamException(ExceptionEnum.FILE_EMPTY);
        }
        String filePath = tempFolder + file.getOriginalFilename();



        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(filePath);

            Files.write(path, bytes);
        } catch (Exception e) {

            System.out.println("上传本地失败！");

            throw new JdataExamException(ExceptionEnum.FILE_UPLOAD_FAILED);

        }


        //如果  文件逻辑判断不通过  直接返回错误结果
        ResultVo result = CsvUtil.read(filePath,file.getOriginalFilename());
        if (result.getCode()!=0){
            return result;
        }



        long size = file.getSize();

        if ((size>MAX_SIZE  || size <  MIN_SIZE  )){
            log.error("文件大小不正确");
            throw new JdataExamException(ExceptionEnum.FILE_SIZE_NOT_CORRECT);
        }






        //先上传到服务器中的临时文件夹，格式验证通过之后转发到带有脚本的文件夹中，保证正确性


        try{

            if (!SshUtil.putFile(filePath)){

                log.info("转发服务器出错");

                throw new JdataExamException(ExceptionEnum.FILE_UPLOAD_FAILED);

            }
            log.info("ssh上传文件成功,删除本地临时文件...");

            LocalExecute.removeFile(filePath);

        }catch (Exception e){

            log.error("ssh上传文件失败,或删除文件失败");

            throw new JdataExamException(ExceptionEnum.SSH_UPLOAD_FAILED);
        }

        //将用户的学放入缓存当中，缓存you效则不能上传
        saveInCache(userId);
        log.info("本地上传成功");


        return ResultVoUtil.success(BackMessageEnum.FILE_UPLOAD_SUCCESS);

    }

    @Override
    public ResultVo submitFile(HttpServletRequest request, MultipartFile file) {

        boolean flag = checkTime();
        if (!flag){
            throw new JdataExamException(ExceptionEnum.SUBMIT_NOT_OPEN);
        }

        String filePath = evidenceFolder + file.getOriginalFilename();
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(filePath);

            System.out.println(file.getOriginalFilename());

            Files.write(path, bytes);
        } catch (Exception e) {
            System.out.println("上传本地失败！");

            e.printStackTrace();

            throw new JdataExamException(ExceptionEnum.FILE_UPLOAD_FAILED);

        }
        System.out.println("上传本地成功！");


        return ResultVoUtil.success(BackMessageEnum.FILE_UPLOAD_SUCCESS);
    }

    @Override
    public ResultVo getExcel(HttpServletResponse response) {
        List<UserScore> userScoreList = userScoreDAO.selectByTime();

        // 1.创建HSSFWorkbook，一个HSSFWorkbook对应一个Excel文件
        XSSFWorkbook wb = new XSSFWorkbook();
        // 2.在workbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = wb.createSheet("sheet1");
        // 3.设置表头，即每个列的列名
        String[] title = {"学生ID","学生姓名","所在班级","准确率"};
        // 3.1创建第一行
        XSSFRow row = sheet.createRow(0);
        // 此处创建一个序号列
        row.createCell(0).setCellValue("序号");
        for (int i = 0; i < title.length; i++) {
            // 给列写入数据,创建单元格，写入数据
            row.createCell(i+1).setCellValue(title[i]);
        }

        // 写入正式数据
        for (int i = 0; i < userScoreList.size(); i++) {
            // 创建行
            row = sheet.createRow(i+1);
            // 序号
            row.createCell(0).setCellValue(i+1);
            // 医院名称
            row.createCell(1).setCellValue(userScoreList.get(i).getUserId());
            // 业务类型
            row.createCell(2).setCellValue(userScoreList.get(i).getUsername());
            // 异常信息
            row.createCell(3).setCellValue(userScoreList.get(i).getClassName());
            // 数量
            row.createCell(4).setCellValue(userScoreList.get(i).getAccuracy());
        }

        //默认行高
        sheet.setDefaultRowHeight((short)(16.5*20));

        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-disposition", "attachment;filename=score.xlsx");
        try {
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        }catch (IOException e){
            throw new JdataExamException(ExceptionEnum.IO_EXCEPTION);
        }
        return ResultVoUtil.success();
    }

    @Override
    public ResultVo getZipFile(HttpServletResponse response) {
        return null;
    }



    public boolean checkTime(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int month = calendar.get(Calendar.MONTH)+1;
        if (month == 7){
            if (day >= 3 && hours >= 12){
                return false;
            }
            else {
                return day < 3;
            }
        }
        return false;
    }

    private boolean searchInCache(String userId){
        return redisTemplate.hasKey(userId);
    }

    /**
     *     判断用户是否在同一天进行操作
     * @param userId  用户ID
     */
    private void saveInCache(String userId){
        redisTemplate.opsForValue().set(userId,"用户文件上传时间未过期",
                                        getTimeExpire(), TimeUnit.MILLISECONDS);
    }


    //获取缓存的毫秒数
    private static Long getTimeExpire(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        //获取的月数比实际的少一个月
        //System.currentTimeMillis()这个同样少一个月
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year,month,day,0,0,0);
        long startOfToday = calendar.getTimeInMillis()+24*60*60*1000;
        return (startOfToday-System.currentTimeMillis());
    }


}




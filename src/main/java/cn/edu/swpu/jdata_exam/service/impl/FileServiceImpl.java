package cn.edu.swpu.jdata_exam.service.impl;

import cn.edu.swpu.jdata_exam.dao.UserScoreDAO;
import cn.edu.swpu.jdata_exam.entity.UserScore;
import cn.edu.swpu.jdata_exam.enums.BackMessageEnum;
import cn.edu.swpu.jdata_exam.enums.ExceptionEnum;
import cn.edu.swpu.jdata_exam.exception.JdataExamException;
import cn.edu.swpu.jdata_exam.service.FileService;
import cn.edu.swpu.jdata_exam.utils.AuthenticationUtil;
import cn.edu.swpu.jdata_exam.utils.NameChangeUtil;
import cn.edu.swpu.jdata_exam.utils.ResultVoUtil;
import cn.edu.swpu.jdata_exam.utils.util.CsvUtil;
import cn.edu.swpu.jdata_exam.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    public final static String prefixKey = "username--";

    private RedisTemplate<String, String> redisTemplate;

    private UserScoreDAO userScoreDAO;

    @Autowired
    public FileServiceImpl(UserScoreDAO userScoreDAO, RedisTemplate<String, String> redisTemplate) {
        this.userScoreDAO = userScoreDAO;
        this.redisTemplate = redisTemplate;
    }

    //上传CSV  结果
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
        String filePath = CsvUtil.UPLOADED_LOCAL_FOLDER + file.getOriginalFilename();



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


        if(!CsvUtil.read(filePath,file.getOriginalFilename())){

            log.info("csv验证不通过。格式内容错误");
            throw new JdataExamException(ExceptionEnum.FILE_FORMAT_ERROR);
        }


        //将用户的学放入缓存当中，缓存you效则不能上传
        saveInCache(userId);
        log.info("本地上传成功");





        //不使用SSH进行上传
//
//        try{
//
//            if (!SshUtil.putFile(filePath)){
//
//                log.info("转发服务器出错");
//
//                throw new JdataExamException(ExceptionEnum.FILE_UPLOAD_FAILED);
//
//            }
//            log.info("ssh上传文件成功,删除本地临时文件...");
//
//            LocalExecute.removeFile(filePath);
//
//        }catch (Exception e){
////            e.printStackTrace();
//
//            log.error("ssh上传文件失败,或删除文件失败");
//
//            throw new JdataExamException(ExceptionEnum.SSH_UPLOAD_FAILED);
//        }


        return ResultVoUtil.success(BackMessageEnum.FILE_UPLOAD_SUCCESS);

    }

    @Override
    public ResultVo submitFile(HttpServletRequest request, MultipartFile file) {

        boolean flag = checkTime();
        if (flag == false){
            throw new JdataExamException(ExceptionEnum.SUBMIT_NOT_OPEN);
        }

        String filePath = CsvUtil.UPLOADED_LOCAL_FOLDER2 + file.getOriginalFilename();
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

//
//        try {
//
//            if (!SshUtil.putFile(filePath)){
//
//                throw new JdataExamException(ExceptionEnum.FILE_UPLOAD_FAILED);
//
//            }
//            log.info("ssh上传文件成功,删除本地临时文件...");
//
//            LocalExecute.removeFile(filePath);
//
//        }catch (Exception e){
//
//            log.error("ssh上传文件失败,或删除文件失败");
//
//            throw new JdataExamException(ExceptionEnum.SSH_UPLOAD_FAILED);
//        }


        return ResultVoUtil.success(BackMessageEnum.FILE_UPLOAD_SUCCESS);
    }

    @Override
    public void getExcel(HttpServletResponse response) throws IOException {
        List<UserScore> userScoreList = userScoreDAO.selectByTime();

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet =wb.createSheet();
        HSSFRow row;

        row = sheet.createRow(1);
        row.setHeight((short)(26.25*20));
        row.createCell(0).setCellValue("学生成绩表");
        //设置样式
        row.getCell(0).setCellStyle(getStyle(wb,0));
        for(int i = 1;i <= 3;i++){
//            row.createCell(i).setCellStyle(getStyle(wb,0));
        }
        CellRangeAddress rowRegion = new CellRangeAddress(0,0,0,3);
        sheet.addMergedRegion(rowRegion);

        CellRangeAddress columnRegion = new CellRangeAddress(1,4,0,0);
        sheet.addMergedRegion(columnRegion);

        row = sheet.createRow(1);
//        row.createCell(0).setCellStyle(getStyle(wb,3));
        row.setHeight((short)(22.50*20));
        row.createCell(1).setCellValue("学生Id");
        row.createCell(2).setCellValue("准确率");
        row.createCell(3).setCellValue("学生所在班级");
        row.createCell(4).setCellValue("提交时间");
//        for(int i = 1;i <= 4;i++){
//            row.getCell(i).setCellStyle(getStyle(wb,1));
//        }

        for(int i = 0;i<userScoreList.size();i++){
            row = sheet.createRow(i+2);
            UserScore userScore = userScoreList.get(i);
            row.createCell(1).setCellValue(userScore.getUserId());
            row.createCell(2).setCellValue(userScore.getAccuracy());
            String className = NameChangeUtil.getRealName(String.valueOf(userScore.getClassName()));
            row.createCell(3).setCellValue(className);
            Date date = userScore.getDateTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formatDate = simpleDateFormat.format(date);
            System.out.println(formatDate);
            row.createCell(4).setCellValue(formatDate);
            for(int j = 1;j <= 4;j++){
                row.getCell(j).setCellStyle(getStyle(wb,2));
            }
        }

        //默认行高
        sheet.setDefaultRowHeight((short)(16.5*20));
        //列宽自适应
        for(int i=0;i<=13;i++){
            sheet.autoSizeColumn(i);
        }


        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-disposition", "attachment;filename=score.xls");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }

    /**
     * 获取样式
     * @param hssfWorkbook
     * @param styleNum
     * @return
     */
    public HSSFCellStyle getStyle(HSSFWorkbook hssfWorkbook, Integer styleNum){
        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        //右边框
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);

        HSSFFont font = hssfWorkbook.createFont();
        //设置字体为微软雅黑
        font.setFontName("微软雅黑");

        switch (styleNum){
            case(0):{
                //跨列居中
                style.setAlignment(HorizontalAlignment.CENTER_SELECTION);
                //粗体`
                font.setBold(true);
                //字体大小
                font.setFontHeightInPoints((short) 14);
                style.setFont(font);
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }
            break;
            case(1):{
                //粗体
                font.setBold(true);
                //字体大小
                font.setFontHeightInPoints((short) 11);
                style.setFont(font);
            }
            break;
            case(2):{
                font.setFontHeightInPoints((short)10);
                style.setFont(font);
            }
            break;
            case(3):{
                style.setFont(font);
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }
            break;
            default:
                break;
        }

        return style;
    }

    public boolean checkTime(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int month = calendar.get(Calendar.MONTH)+1;
        //如果为6月
        if (month == 6){
            if (day == 26 && hours >= 10){
                return true;
            }else {
                return day > 26;
            }
        }else if (month == 7){
            return day == 1 && hours <= 12;
        }
        return false;
    }

    private boolean searchInCache(String userId){
        return redisTemplate.hasKey(userId);
    }

    //判断用户是否在同一天进行操作
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




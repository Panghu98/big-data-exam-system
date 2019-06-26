package cn.edu.swpu.jdata_exam.utils.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Date;

@Slf4j
public class SshUtil {
    public static String ip = "localhost";

    public static String uploadPath = "/root/home/panghu/Project/exam/data/";

    public static String username = "root";

    public static String password  = "Abc123456";
    //实质上是file
    public static boolean putFile(String localFilePath) throws IOException {
        //目标服务器地址
        Connection conn = new Connection(ip);
        conn.connect();
        boolean isAuthenticated = conn.authenticateWithPassword(username, password);
        if (isAuthenticated == false){
            throw new IOException("Authentication failed.");
        }

        SCPClient client = new SCPClient(conn);
        //put方法用来将本地文件上传到目标服务器
        client.put(localFilePath,uploadPath);

        log.info("文件完成转发");

        ch.ethz.ssh2.Session session = conn.openSession();
        conn.close();
        session.close();
        return true;
    }


}

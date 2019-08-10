package cn.edu.swpu.exam.utils.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author panghu
 */
@Slf4j
@Component
public class SshUtil {

    private static String ip;

    private static String uploadPath;

    private static String username;

    private static String password;

    @Value(value = "${remote.ip}")
    private void setIp(String remoteIp){
        ip = remoteIp;
    }

    @Value(value = "${file.real}")
    public void setUploadPath(String path){
        uploadPath = path;
    }

    @Value(value = "${remote.username}")
    private void setUsername(String name){
        username = name;
    }

    @Value(value = "${remote.password}")
    private void setPassword(String pwd){
        password = pwd;
    }



    /**
     *
     * @param localFilePath  file文件
     * @return
     * @throws IOException
     */
    public static boolean putFile(String localFilePath) throws IOException {
        //目标服务器地址
        Connection conn = new Connection(ip);
        conn.connect();
        boolean isAuthenticated = conn.authenticateWithPassword(username, password);
        if (!isAuthenticated){
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

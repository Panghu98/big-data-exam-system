package cn.edu.swpu.exam.utils.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;


/**
 * @author panghu
 */
@Component
public class RemoteExecute {

    @Value("${file.real}")
    public static String uploadPath;

    @Value(value = "${remote.ip}")
    public  String ip;

    @Value(value = "${remote.username}")
    public  String username;

    @Value(value = "${remote.password}")
    public  String password;
    //字符编码默认是utf-8
    private static String DEFAULTCHART = "UTF-8";
    private static Connection conn;

    /**
     * 连接远程服务器主机
     * @return
     */
    public Boolean login() {

        boolean flg = false;
        try {
            conn = new Connection(ip,22);
            conn.connect();//连接
            //认证
            flg = conn.authenticateWithPassword(username,password);
            if (flg) {
                System.out.println("服务器认证成功！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flg;
    }

    //执行命令
    public String execute(String cmd) {
        String result = "";
        try {
            if (login()) {

                //打开一个会话
                Session session = conn.openSession();
                //执行命令
                session.execCommand(cmd);
                result = processStdout(session.getStdout(), DEFAULTCHART);

                //如果得到标准输出为空，说明脚本执行出错了
                if (StringUtils.isEmpty(result)) {
                    System.out.println("无返回值");
                    result = processStdout(session.getStderr(), DEFAULTCHART);
                }
                conn.close();
                session.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String processStdout(InputStream in, String charset) {
        InputStream stdout = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();

        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(stdout, charset));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\n");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    //创建文件夹
    public static String mkdir(String dirName){
        RemoteExecute remoteExecute = new RemoteExecute();
        String result = remoteExecute.execute("mkdir "+ uploadPath+dirName);
        conn.close();
        return result;
    }

}

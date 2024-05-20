package com.api.wq.helper;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author wuqian
 */
public class RemoteCommandUtil {

    private static final Logger log = LoggerFactory.getLogger(RemoteCommandUtil.class);
    private static String DEFAULTCHART = "UTF-8";

    /**
     * 登录主机
     *
     * @return 登录成功返回true，否则返回false
     */
    public static Connection login(String ip,
                                   String userName,
                                   String userPwd) {

        boolean flg = false;
        Connection conn = null;
        try {
            conn = new Connection(ip);
            //连接
            conn.connect();
            //认证
            flg = conn.authenticateWithPassword(userName, userPwd);
            if (flg) {
                log.info("=========登录成功=========" + conn);
                return conn;
            }
        } catch (IOException e) {
            log.error("=========登录失败=========" + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 远程执行shll脚本或者命令
     *
     * @param cmd 即将执行的命令
     * @return 命令执行完后返回的结果值
     */
    public static String execute(Connection conn, String cmd) {
        String result = "";
        Session session = null;
        try {
            if (conn != null) {
                //打开一个会话
                session = conn.openSession();
                //执行命令
                session.execCommand(cmd);
                result = processStdout(session.getStdout(), DEFAULTCHART);
                //如果为得到标准输出为空，说明脚本执行出错了
                if (StringUtils.isBlank(result)) {
                    log.info("得到标准输出为空,链接conn:" + conn + ",执行的命令：" + cmd);
                    result = processStdout(session.getStderr(), DEFAULTCHART);
                } else {
                    log.info("执行命令成功,链接conn:" + conn + ",执行的命令：" + cmd);
                }
            }
        } catch (IOException e) {
            log.info("执行命令失败,链接conn:" + conn + ",执行的命令：" + cmd + "  " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (session != null) {
                session.close();
            }
        }
        return result;
    }

    /**
     * 解析脚本执行返回的结果集
     *
     * @param in      输入流对象
     * @param charset 编码
     * @return 以纯文本的格式返回
     */
    private static String processStdout(InputStream in, String charset) {
        InputStream stdout = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();
        ;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\n");
            }
        } catch (UnsupportedEncodingException e) {
            log.error("解析脚本出错：" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error("解析脚本出错：" + e.getMessage());
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 支持查看GBK编码远程文件
     * @param conn
     * @param path
     * @return
     */
    public static String readRemoteFile(Connection conn, String path) {
        String fileLines = "";
        boolean fileExist = false;
        if (conn != null) {
            Session session = null;
            try {
                session = conn.openSession();
                session.execCommand("ls -l ".concat(path));
                log.info("执行命令：" + "ls -l ".concat(path));
                InputStream inputStream = new StreamGobbler(session.getStdout());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = bufferedReader.readLine();
                if (line != null) {
                    if (line.startsWith("-")) {
                        log.info("file " + path + " exists!");
                        fileExist = true;
                    }
                } else {
                    log.error("file " + path + " doesn't exists!");
                    fileExist = false;
                }
            } catch (IOException e) {
                log.error("查看文件出错：" + e.getMessage());
                e.printStackTrace();
            } finally {
                if (session != null) {
                    session.close();
                }
            }
        }
        if (fileExist) {
            if (conn != null) {
                Session session = null;
                try {
                    session = conn.openSession();
                    session.execCommand("cat ".concat(path));
                    log.info("执行命令：" + "cat ".concat(path));
                    InputStream inputStream = new StreamGobbler(session.getStdout());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
                    while (true) {
                        String line = "";
                        if ((line = bufferedReader.readLine()) != null) {
                            fileLines = new StringBuilder().append(fileLines).append(line).append("\n").toString();
                        } else {
                            break;
                        }
                    }
                } catch (IOException e) {
                    log.error("解析文件出错：" + e.getMessage());
                    e.printStackTrace();
                } finally {
                    if (session != null) {
                        session.close();
                    }
                }
            }
        }
        return fileLines;
    }
}



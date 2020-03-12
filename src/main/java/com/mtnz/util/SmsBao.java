package com.mtnz.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/*
    Created by xxj on 2018\3\26 0026.  
 */
public class SmsBao {
    public static void main(String[] args) throws Exception {
        SmsBao smsBao=new SmsBao();
        smsBao.sendSMS("17638567709","你的账号已创建成功,账号为17638567709初始密码为123456，请赶快修改密码");

    }

    public String sendSMS(String testPhone, String testContent) throws Exception{
        //登录账号密码13526524092     821143
        // 用户名
        String name="13526524092";
        // 密码
        String pwd="6ACD05E026F8945A23A86ACC906D";
        // 电话号码字符串，中间用英文逗号间隔
        StringBuffer mobileString=new StringBuffer(testPhone);
        // 内容字符串
        StringBuffer contextString=new StringBuffer(testContent);
        // 签名
        String sign="喜开单";
        // 追加发送时间，可为空，为空为及时发送
        String stime="";
        // 扩展码，必须为数字 可为空
        StringBuffer extno=new StringBuffer();
        return doPost(name, pwd, mobileString, contextString, sign, stime, extno);
    }


    public String sendSMSstord(String testPhone, String testContent,String sign) throws Exception{
        //登录账号密码13526524092     821143
        // 用户名
        String accesskey="13526524092";
        // 密码
        String secret="6ACD05E026F8945A23A86ACC906D";
        // 电话号码字符串，中间用英文逗号间隔
        StringBuffer mobileString=new StringBuffer(testPhone);
        // 内容字符串
        StringBuffer contextString=new StringBuffer(testContent);
        // 签名
        // 追加发送时间，可为空，为空为及时发送
        String stime="";
        // 扩展码，必须为数字 可为空
        StringBuffer extno=new StringBuffer();
        return doPost(accesskey, secret, mobileString, contextString, sign, stime, extno);
    }


    /**
     * 发送短信
     *
     * @param name			用户名
     * @param pwd			密码
     * @param mobileString	电话号码字符串，中间用英文逗号间隔
     * @param contextString	内容字符串
     * @param sign			签名
     * @param stime			追加发送时间，可为空，为空为及时发送
     * @param extno			扩展码，必须为数字 可为空
     * @return
     * @throws Exception
     */
    public static String doPost(String accesskey, String secret,
                                StringBuffer mobileString, StringBuffer contextString,
                                String sign, String stime, StringBuffer extno) throws Exception {
        StringBuffer param = new StringBuffer();
        param.append("accesskey="+accesskey);
        param.append("&secret="+secret);
        param.append("&mobile=").append(mobileString);
        param.append("&content=").append(URLEncoder.encode(contextString.toString(),"UTF-8"));
        param.append("&scheduleSendTime="+stime);
        param.append("&sign=").append(URLEncoder.encode("【"+"喜开单"+"】","UTF-8"));
     //   param.append("&type=pt");
     //   param.append("&extno=").append(extno);
        URL localURL = new URL("http://api.1cloudsp.com/api/v2/send");
       // URL localURL = new URL("http://web.wasun.cn/asmx/smsservice.aspx?");
        URLConnection connection = localURL.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;

        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(param.length()));

        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        String resultBuffer = "";

        try {
            outputStream = httpURLConnection.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream);

            outputStreamWriter.write(param.toString());
            outputStreamWriter.flush();

            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }

            inputStream = httpURLConnection.getInputStream();
            resultBuffer = convertStreamToString(inputStream);

        } finally {

            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }

            if (outputStream != null) {
                outputStream.close();
            }

            if (reader != null) {
                reader.close();
            }

            if (inputStreamReader != null) {
                inputStreamReader.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }

        }

        return resultBuffer;
    }

    /**
     * 转换返回值类型为UTF-8格式.
     * @param is
     * @return
     */
    public static String convertStreamToString(InputStream is) {
        StringBuilder sb1 = new StringBuilder();
        byte[] bytes = new byte[4096];
        int size = 0;

        try {
            while ((size = is.read(bytes)) > 0) {
                String str = new String(bytes, 0, size, "UTF-8");
                sb1.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb1.toString();
    }
}

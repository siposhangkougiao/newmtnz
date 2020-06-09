package com.mtnz.controller.app.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtnz.controller.base.BaseController;
import com.mtnz.service.system.recharge.RechargeService;
import com.mtnz.service.system.store.StoreService;
import com.mtnz.util.AlipayConfig;
import com.mtnz.util.DateUtil;
import com.mtnz.util.PageData;
import com.mtnz.util.utils.GetWxOrderno;
import com.mtnz.util.utils.RequestHandler;
import com.mtnz.util.utils.TenpayUtil;
import com.mtnz.util.weixin.Sha1Util;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/*
    Created by xxjon 2 018\4\21 0021.
 */
@Controller
@RequestMapping(value = "app/pay",produces = "text/html;charset=UTF-8")
public class PayComtroller extends BaseController{
    @Resource(name = "rechargeService")
    public RechargeService rechargeService;
    @Resource(name = "storeService")
    public StoreService storeService;

    @RequestMapping(value = "/wxPay_snatch", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String wxPay_snatch1(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logBefore(logger, "微信支付");
        String partner = "1482116722";
        String appid = "wx3e8ef0db80a0580f";
        String appsecret = "278fc286264c9d8a96571752f88c3eb7";
        String partnerkey = "5BCB0B1B9455219FF9628FA9DED938A2";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String s = String.valueOf((int) ((Math.random() * 9 + 1) * 100));
        Date now = new Date();
        String no = s + sdf1.format(now).substring(2); // 订单号
        String ORDER_NUMBER = no;
        String MONEY = request.getParameter("money");
        String code = "1";
        String flag = request.getParameter("count")+","+request.getParameter("uid")+","+request.getParameter("store_id");

        logBefore(logger,"================微信APP支付数值"+flag);
        // 获取openId后调用统一支付接口https://api.mch.weixin.qq.com/pay/unifiedorder
        String currTime = TenpayUtil.getCurrTime();
        // 8位日期
        String strTime = currTime.substring(8, currTime.length());
        // 四位随机数
        String strRandom = TenpayUtil.buildRandom(4) + "";
        // 10位序列号,可以自行调整。
        String strReq = strTime + strRandom;

        // 商户号
        String mch_id = partner;
        // 随机数
        String nonce_str = strReq;
        // 商品描述根据情况修改
        String body = "xiKaiDan";
        // 商户订单号
        String out_trade_no = ORDER_NUMBER;
        // 总金额以分为单位，不带小数点
        // Integer total_fee1=Integer.valueOf(MONEY)*100;
        Double moneys=Double.valueOf(MONEY)*100;
        DecimalFormat df = new DecimalFormat("######0"); //四色五入转换成整数
        System.out.println(String.valueOf(df.format(moneys)));
        String total_fee=String.valueOf(df.format(moneys));
        // 订单生成的机器 IP
        String spbill_create_ip = request.getRemoteAddr();
        // 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
        String notify_url = "http://115.28.210.33:9090/manage/app/pay/wxPayNotify";
        String trade_type = "APP";

        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", appid);
        packageParams.put("attach", flag);
        packageParams.put("mch_id", mch_id);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", body);
        packageParams.put("out_trade_no", out_trade_no);
        packageParams.put("total_fee", total_fee);
        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("notify_url", notify_url);
        packageParams.put("trade_type", trade_type);

        RequestHandler reqHandler = new RequestHandler(request, response);
        reqHandler.init(appid, appsecret, partnerkey);

        String sign = reqHandler.createSign(packageParams);
        String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<attach>" + flag + "</attach>" + "<mch_id>" + mch_id
                + "</mch_id>" + "<nonce_str>" + nonce_str + "</nonce_str>" + "<sign>" + sign + "</sign>" +
                // "<body>"+body+"</body>"+
                "<body><![CDATA[" + body + "]]></body>" + "<out_trade_no>" + out_trade_no + "</out_trade_no>"
                + "<total_fee>" + total_fee + "</total_fee>" + "<spbill_create_ip>" + spbill_create_ip
                + "</spbill_create_ip>" + "<notify_url>" + notify_url + "</notify_url>" + "<trade_type>" + trade_type
                + "</trade_type>" + "</xml>";

        System.out.println(xml);
        xml = new String(xml.getBytes(),"ISO8859-1");
        String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String prepay_id = "";
        try {
            prepay_id = new GetWxOrderno().getPayNo(createOrderURL, xml);
            System.out.println(prepay_id);
            if (prepay_id.equals("")) {
                // request.setAttribute("ErrorMsg", "统一支付接口获取预支付订单出错");
                System.out.println("ErrorMsg  统一支付接口获取预支付订单出错");
                // response.sendRedirect("error.jsp");
                code = "2";
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        SortedMap<String, String> finalpackage = new TreeMap<String, String>();
        String appid2 = appid;
        String timestamp = Sha1Util.getTimeStamp();
        String nonceStr2 = nonce_str;
        String prepay_id2 = "prepay_id=" + prepay_id;
        String packages = prepay_id;
        finalpackage.put("appId", appid2);
        finalpackage.put("timeStamp", timestamp);
        finalpackage.put("nonceStr", nonceStr2);
        finalpackage.put("package", packages);
        finalpackage.put("signType", "MD5");
        finalpackage.put("code", code);
        SortedMap<String, String> finalpackage1 = new TreeMap<String, String>();
        finalpackage1.put("appid", appid);
        finalpackage1.put("timestamp", timestamp);
        finalpackage1.put("noncestr", nonce_str);
        finalpackage1.put("partnerid", partner);
        finalpackage1.put("package", "Sign=WXPay");
        finalpackage1.put("prepayid", prepay_id);
        String finalsign1 = reqHandler.createSign(finalpackage1);
        System.out.println(finalsign1);

        finalpackage.put("sign", finalsign1);
        ObjectMapper mapper = new ObjectMapper();
        String str = null;
        try {
            str = mapper.writeValueAsString(finalpackage);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    @RequestMapping(value = "/wxPayNotify", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String wxPayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logBefore(logger, "微信回调");
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        System.out.println(sb);
        Map<String, String> map = GetWxOrderno.doXMLParse(sb.toString());
        String attach = map.get("attach");
        String result_code = map.get("result_code");
        String ORDER_NUMBER = map.get("out_trade_no");
        logBefore(logger,"================微信APP支付回调数值"+attach);
        String xml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                + "<return_msg><![CDATA[签名失败]]></return_msg>" + "</xml>";
        if ("SUCCESS".equals(result_code)) {
            String str[]=attach.split(",");
            PageData od=new PageData();
            od.put("store_id",str[2]);
            od.put("count",str[0]);
            od.put("uid",str[2]);
            od.put("money",result_code);
            od.put("date", DateUtil.getTime());
            od.put("status","1");
            od.put("out_trade_no",ORDER_NUMBER);
            rechargeService.save(od);
            storeService.updateJiaNumber(od);
            System.out.println("~~~~~~~~~~~~~~~~付款成功~~~~~~~~~");
            xml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>"
                    + "</xml>";
        }
        System.out.println(xml);
        return xml;
    }



    @ResponseBody
    @RequestMapping(value = "/alipay", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST,
            RequestMethod.GET })
    public String alipay(String money,String count,String uid,String store_id) throws Exception {
        logBefore(logger, "支付宝支付");
        PageData pd1 = new PageData();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String s = String.valueOf((int) ((Math.random() * 9 + 1) * 100));
        Date now = new Date();
        String no = s + sdf1.format(now).substring(2); // 订单号
        pd1.put("ORDER_NUMBER", no);
        PageData pd = new PageData();
        /* if(MONEY.equals(pd1.getString("MONEY"))){ */
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                AlipayConfig.app_id, AlipayConfig.private_key, "json", "utf-8", AlipayConfig.alipay_public_key, "RSA");
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        // SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        // model.setPassbackParams(URLEncoder.encode(body.toString()));; //描述信息
        // 添加附加数据
        model.setSubject("喜开单"); // 商品标题
        model.setOutTradeNo(no); // 商家订单编号
        model.setTimeoutExpress("30m"); // 超时关闭该订单时间
        model.setTotalAmount(money); // 订单总金额
        model.setProductCode("QUICK_MSECURITY_PAY"); // 销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
        model.setBody(count+","+uid+","+store_id);
        request.setBizModel(model);
        logBefore(logger,"================支付宝APP支付数值"+count+","+uid+","+store_id);
        // request.setNotifyUrl("http://m.nongjike.cn/NJK/app/notify_url"); //
        // 回调地址
        request.setNotifyUrl("http://115.28.210.33:9090/manage/app/pay/alipaynotify_url"); // 回调地址
        String orderStr = "";
        try {
            // 这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            orderStr = response.getBody();
            System.out.println(orderStr);// 就是orderString 可以直接给客户端请求，无需再做处理。
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        pd.put("code", "1");
        pd.put("message", "支付成功");
        // pd.put("data", json4.toString());
        pd.put("data", orderStr);
        /*
         * }else{ pd.put("code", "2"); pd.put("message", "支付异常");
         * //pd.put("data", json4.toString()); pd.put("data", ""); }
         */
        JSONObject json = JSONObject.fromObject(pd);
        System.out.println(json.toString());
        return json.toString();
        /* return orderString; */

        // String
        // a="partner="+AlipayConfig.app_id+"&seller_id=""&out_trade_no="1000000124"&total_fee="0.02"&notify_url="http://wxej.cckelifang.com/web/api/mkt/order/getAlipayResult"&service="mobile.securitypay.pay"&payment_type="1"&_input_charset="utf-8"&it_b_pay="30m"&show_url="m.alipay.com"&sign="0SON%2FcHuokUIeYLUrhcB13XbLiczU65Rz%2F5yzUEUisKxx7LupD2T85Fw2WBcti9%2FYPpcVTzf5JoK8SB20V9uPjdvjKKqNShOPUiE%2F0AwpZqE8m%2BRf3dKjaI%2BL7FsbSjgw%2BTrTGfGwfsqH0MgqYa9rjlCwnEeScGEfnMI2%2F6KTpo%3D"&sign_type="RSA""
    }


    @ResponseBody
    @RequestMapping(value = "/alipaynotify_url", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST,
            RequestMethod.GET })
    public String notify_url(HttpServletRequest request) throws Exception {
        // 接收支付宝返回的请求参数
        logBefore(logger,"支付宝回调");
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }

        System.out.println("====================================================");
        System.out.println(params.toString());
        System.out.println("支付宝回调地址！");
        String trade_status = request.getParameter("trade_status");
        String total_amount = request.getParameter("total_amount");
        String out_trade_no = params.get("out_trade_no");
        String body=request.getParameter("body");
        logBefore(logger,"================支付宝回调Body数值"+body);
        if (trade_status.equals("TRADE_SUCCESS")) {
            String str[]=body.split(",");
            PageData od=new PageData();
            od.put("store_id",str[2]);
            od.put("count",str[0]);
            od.put("uid",str[2]);
            od.put("money",total_amount);
            od.put("date", DateUtil.getTime());
            od.put("status","1");
            od.put("out_trade_no",out_trade_no);
            rechargeService.save(od);
            storeService.updateJiaNumber(od);
            return "success";
        } else {

            return "success";
        }
    }

}

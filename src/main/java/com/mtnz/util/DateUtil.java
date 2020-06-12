package com.mtnz.util;



import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DateUtil {
	private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

	private final static SimpleDateFormat sdfYue = new SimpleDateFormat("yyyy-MM");

	private final static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");

	private final static SimpleDateFormat sdfDay1 = new SimpleDateFormat("MM-dd");

	private final static SimpleDateFormat sdfDays = new SimpleDateFormat("yyyyMMdd");

	private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	/**
	 * 获取YYYY格式
	 * 
	 * @return
	 */
	public static String getYear() {
		return sdfYear.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD格式
	 * 
	 * @return
	 */
	public static String getDay() {
		return sdfDay.format(new Date());
	}

	/**
	 * 转换成YYYY-MM-DD格式
	 */
	public static String getDay2(Date data) {
		return sdfDay.format(data);
	}

	/**
	 * 转换成MM-DD格式
	 */
	public static String getDay3() {
		return sdfDay1.format(new Date());
	}

	/**
	 * 获取YYYY-MM格式
	 * 
	 * @return
	 */
	public static String getYue() {
		return sdfYue.format(new Date());
	}

	/**
	 * 获取YYYYMMDD格式
	 * 
	 * @return
	 */
	public static String getDays() {
		return sdfDays.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 * 
	 * @return
	 */
	public static String getTime() {
		return sdfTime.format(new Date());
	}

	/**
	 * 格式化日期
	 * 
	 * @return
	 */
	public static Date fomatDate(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 校验日期是否合法
	 * 
	 * @return
	 */
	public static boolean isValidDate(String s) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fmt.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}

	public static int getDiffYear(String startTime, String endTime) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			long aa = 0;
			int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(startTime).getTime()) / (1000 * 60 * 60 * 24))
					/ 365);
			return years;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return 0;
		}
	}

	public static String dateFormat(Date date){
		SimpleDateFormat sdf1 = new SimpleDateFormat("d日");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy.MM.dd HH:mm");
		SimpleDateFormat sdf3 = new SimpleDateFormat("MM.dd HH:mm");
		String dateTime = "";
			if(MyTimesUtil.isThisYear(date)){
				dateTime = sdf3.format(date);
			}else {
				dateTime = sdf2.format(date);
			}
		return dateTime;
	}


	/**
	 * <li>功能描述：时间相减得到天数
	 * 
	 * @param beginDateStr
	 * @param endDateStr
	 * @return long
	 * @author Administrator
	 */
	public static long getDaySub(String beginDateStr, String endDateStr) {
		long day = 0;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date beginDate = null;
		Date endDate = null;

		try {
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
		// System.out.println("相隔的天数="+day);

		return day;
	}

	/**
	 * 得到n天之后的日期
	 * 
	 * @param days
	 * @return
	 */
	public static String getAfterDayDate(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdfd.format(date);

		return dateStr;
	}

	/**
	 * 得到n天之后是周几
	 * 
	 * @param days
	 * @return
	 */
	public static String getAfterDayWeek(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("E");
		String dateStr = sdf.format(date);

		return dateStr;

		/*
		 * view.loadUrl(
		 * "javascript:function setTop(){document.getElementById('saveBtn').style.display=\"none\";}setTop();"
		 * ); view.loadUrl(
		 * "javascript:function setTop1(){document.getElementsByClassName('smart-header')[2].style.display=\"none\";}setTop1();"
		 * ); view.loadUrl(
		 * "javascript:function setTop12(){document.getElementById('div-gpt-ad-1466480176223-0').style.display=\"none\";}setTop12();"
		 * );
		 */
	}

	/**
	 * @Title: compareDate
	 * @Description: TODO(日期比较，如果s>=e 返回true 否则返回false)
	 * @param s
	 * @param e
	 * @return boolean
	 * @throws @author
	 *             luguosui
	 */
	public static boolean compareDate(String s, String e) {
		if (fomatDate(s) == null || fomatDate(e) == null) {
			return false;
		}
		return fomatDate(s).getTime() >= fomatDate(e).getTime();
	}





	public static String delHTMLTag(String htmlStr) {
		String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
		String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
		String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

		Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签

		return htmlStr.trim(); // 返回文本字符串
	}



	public static String times(String time) {
		String times = "";
		try {
			SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// @SuppressWarnings("unused")
			// long lcc = Long.valueOf(time);

			int i = Integer.parseInt(time);
			times = sdr.format(new Date(i * 1000L));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return times;

	}

	// 获取前一天
	public static String getSpecifiedDayBefore(String specifiedDay,Integer days) {
		// SimpleDateFormat simpleDateFormat = new
		// SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - days);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}


	//获取本月第一天和最后一天
	public static Map<String,Object> ThisMonth(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
        
        //获取前月的第一天
        Calendar   cal_1=Calendar.getInstance();//获取当前日期 
        cal_1.add(Calendar.MONTH, -1);
        String firstDay = format.format(cal_1.getTime());

        
        //获取前月的最后一天
        Calendar cale = Calendar.getInstance();
		cale.add(Calendar.MONTH, -3);
        String lastDay = format.format(cale.getTime());



		//获取前月的最后一天
		Calendar cale_a = Calendar.getInstance();
		cale_a.add(Calendar.MONTH, -6);
		String first = format.format(cale_a.getTime());

		//获取前月的最后一天
		Calendar cale_aa = Calendar.getInstance();
		cale_aa.add(Calendar.MONTH, -12);
		String nian = format.format(cale_aa.getTime());



        Map<String,Object> map=new HashMap();
        map.put("firstDay", firstDay+" 00:00:00");
        map.put("lastDay", lastDay+" 00:00:00");
        map.put("first", first+" 00:00:00");
		map.put("nian", nian+" 00:00:00");
		return map;
	}

	public static String dealZeroToNine() {
		Calendar calendar = Calendar.getInstance();
		int num=calendar.get(Calendar.HOUR_OF_DAY);
		if (num >= 1 && num <= 9) {
			return "0" + num;
		}
		return "" + num;
	}


	public static String doubleTrans(double d){
		if(Math.round(d)-d==0){
			return String.valueOf((long)d);
		}
		return String.valueOf(d);
	}


	/**
	 * 根据月份查询本月最后一天
	 * @param year 年
	 * @param month 月
	 * @return
	 */
	public static String getLastDayOfMonth(int year,int month) {

		Calendar cal = Calendar.getInstance();

		//设置年份

		cal.set(Calendar.YEAR,year);

		//设置月份

		cal.set(Calendar.MONTH, month-1);

		//获取某月最大天数

		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

		//设置日历中月份的最大天数

		cal.set(Calendar.DAY_OF_MONTH, lastDay);

		//格式化日期

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String lastDayOfMonth = sdf.format(cal.getTime());

		return lastDayOfMonth;

	}


	/**

	 * 获取某月的第一天

	 * @Title:getLastDayOfMonth

	 * @Description:

	 * @param:@param year

	 * @param:@param month

	 * @param:@return

	 * @return:String

	 * @throws

	 */

	public static String getFirstDayOfMonth(int year,int month) {

		Calendar cal = Calendar.getInstance();

		//设置年份

		cal.set(Calendar.YEAR,year);

		//设置月份

		cal.set(Calendar.MONTH, month-1);

		//获取某月最大天数

		int lastDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);

		//设置日历中月份的最大天数

		cal.set(Calendar.DAY_OF_MONTH, lastDay);

		//格式化日期

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String lastDayOfMonth = sdf.format(cal.getTime());

		return lastDayOfMonth;

	}



	/**

	 * 获取某月的第一天

	 * @Title:getLastDayOfMonth

	 * @Description:

	 * @param:@param year

	 * @param:@param month

	 * @param:@return

	 * @return:String

	 * @throws

	 */

	public static String getFirstDayYue(int years) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Calendar c = Calendar.getInstance();
		//过去一月
		c.setTime(new Date());
		c.add(Calendar.MONTH, -years);
		Date m = c.getTime();
		String mon = format.format(m);

		return mon;



	}

	public static String getFirstDayNian(int nian) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Calendar c = Calendar.getInstance();
		//过去一年
		c.setTime(new Date());
		c.add(Calendar.YEAR, -nian);
		Date y = c.getTime();
		String year = format.format(y);
		return year;
	}


	public static void main(String[] args) throws IOException {

	}



	/*public static void aaa() throws IOException {
		//创建workbook
		HSSFWorkbook workbook = new HSSFWorkbook();
		//创建sheet页
		HSSFSheet sheet = workbook.createSheet("学生表");
		HSSFCellStyle style = workbook.createCellStyle(); // 样式对象 
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直 
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平 


		//创建单元格
		HSSFRow row = sheet.createRow(0);
		HSSFCell c0 = row.createCell(0);
		c0.setCellValue(new HSSFRichTextString("客户名称"));
		c0.setCellStyle(style);

		HSSFCell c1 = row.createCell(1);
		c1.setCellValue(new HSSFRichTextString("手机号"));
		c1.setCellStyle(style);

		HSSFCell c2 = row.createCell(2);
		c2.setCellValue(new HSSFRichTextString("商品明细"));
		c2.setCellStyle(style);

		HSSFCell c3 = row.createCell(9);
		c3.setCellValue(new HSSFRichTextString("合计金额"));
		c3.setCellStyle(style);

		HSSFCell c4 = row.createCell(10);
		c4.setCellValue(new HSSFRichTextString("结算方式"));
		c4.setCellStyle(style);

		HSSFCell c5 = row.createCell(11);
		c5.setCellValue(new HSSFRichTextString("实收"));
		c5.setCellStyle(style);

		HSSFCell c6 = row.createCell(12);
		c6.setCellValue(new HSSFRichTextString("优惠"));
		c6.setCellStyle(style);

		HSSFCell c7 = row.createCell(13);
		c7.setCellValue(new HSSFRichTextString("余欠"));
		c7.setCellStyle(style);

		HSSFCell c8 = row.createCell(14);
		c8.setCellValue(new HSSFRichTextString("开单人"));
		c8.setCellStyle(style);

		HSSFCell c9 = row.createCell(15);
		c9.setCellValue(new HSSFRichTextString("开单时间"));
		c9.setCellStyle(style);


		HSSFRow row1 = sheet.createRow(1);
		HSSFCell cc1 = row1.createCell(2);
		cc1.setCellValue(new HSSFRichTextString("商品名称"));
		cc1.setCellStyle(style);

		HSSFCell cc2 = row1.createCell(3);
		cc2.setCellValue(new HSSFRichTextString("规格单位"));
		cc2.setCellStyle(style);

		HSSFCell cc3 = row1.createCell(4);
		cc3.setCellValue(new HSSFRichTextString("单价"));
		cc3.setCellStyle(style);

		HSSFCell cc4 = row1.createCell(5);
		cc4.setCellValue(new HSSFRichTextString("单价单位"));
		cc4.setCellStyle(style);

		HSSFCell cc5 = row1.createCell(6);
		cc5.setCellValue(new HSSFRichTextString("销售数量"));
		cc5.setCellStyle(style);

		HSSFCell cc6 = row1.createCell(7);
		cc6.setCellValue(new HSSFRichTextString("销售单位"));
		cc6.setCellStyle(style);

		HSSFCell cc7 = row1.createCell(8);
		cc7.setCellValue(new HSSFRichTextString("单品总价"));
		cc7.setCellStyle(style);











		// 四个参数分别是：起始行，起始列，结束行，结束列 
		Region region0 = new Region(0, (short)0, 1, (short)0);
		Region region1 = new Region(0, (short)1, 1, (short)1);
		Region region2 = new Region(0, (short)2, 0, (short)8);
		Region region3 = new Region(0, (short)9, 1, (short)9);
		Region region4 = new Region(0, (short)10, 1, (short)10);
		Region region5 = new Region(0, (short)11, 1, (short)11);
		Region region6 = new Region(0, (short)12, 1, (short)12);
		Region region7 = new Region(0, (short)13, 1, (short)13);
		Region region8 = new Region(0, (short)14, 1, (short)14);
		Region region9 = new Region(0, (short)15, 1, (short)15);
		sheet.addMergedRegion(region0);
		sheet.addMergedRegion(region1);
		sheet.addMergedRegion(region2);
		sheet.addMergedRegion(region3);
		sheet.addMergedRegion(region4);
		sheet.addMergedRegion(region5);
		sheet.addMergedRegion(region6);
		sheet.addMergedRegion(region7);
		sheet.addMergedRegion(region8);
		sheet.addMergedRegion(region9);




		int num= 2;
		for (int i=2;i<5;i++){
			HSSFRow rowss = sheet.createRow(i);
			HSSFCell cs2 = rowss.createCell(2);
			cs2.setCellValue(new HSSFRichTextString("邢宣聚"+i));
			cs2.setCellStyle(style);
		}

		HSSFRow rows = sheet.createRow(num);
		HSSFCell cs0 = rows.createCell(0);
		cs0.setCellValue(new HSSFRichTextString("邢宣聚"));
		cs0.setCellStyle(style);

		HSSFCell cs1 = rows.createCell(1);
		cs1.setCellValue(new HSSFRichTextString("18736083059"));
		cs1.setCellStyle(style);

		// 四个参数分别是：起始行，起始列，结束行，结束列 
		Region regions0 = new Region(2, (short)0, 4, (short)0);
		Region regions1 = new Region(2, (short)1, 4, (short)1);
		//Region regions2 = new Region(num, (short)1, num, (short)1);

		sheet.addMergedRegion(regions0);
		sheet.addMergedRegion(regions1);






		//sheet.addMergedRegion(regions2);
		*//*for(int i=0;i<3;i++){
			HSSFRow rowss = sheet.createRow(num);
			HSSFCell cs3 = rowss.createCell(2);
			cs3.setCellValue(new HSSFRichTextString("欣喜"+i));
			cs3.setCellStyle(style);

			HSSFCell cs4 = rowss.createCell(3);
			cs4.setCellValue(new HSSFRichTextString("1L"+i));
			cs4.setCellStyle(style);
			num++;
		}*//*



		FileOutputStream stream = new FileOutputStream("E:/studentss.xls");
		workbook.write(stream);
	}*/




}

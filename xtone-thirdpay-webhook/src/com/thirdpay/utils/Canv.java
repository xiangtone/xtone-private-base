package com.thirdpay.utils;

import java.util.HashMap;

public class Canv {
	
	public static HashMap<String, String> parm;
	
	static{
		parm = new HashMap<>();
		//调用界面统计
		parm.put("10", "PayGuiShow");
		parm.put("11", "PayGuiCancel");
		//支付宝统计
		parm.put("201", "AlipayClick");
		parm.put("202", "AlipayCancel");
		parm.put("203", "AlipaySuccess");
		parm.put("204", "AlipayFail");
		//银联统计
		parm.put("301", "UnionpayClick");
		parm.put("302", "UnionpayCancel");
		parm.put("303", "UnionpaySuccess");
		parm.put("304", "UnionpayFail");
		//微信统计
		parm.put("401", "WeChatpayClick");
		parm.put("402", "WeChatpayCancel");
		parm.put("403", "WeChatpaySuccess");
		parm.put("404", "WeChatpayFail");
		//百度统计
		parm.put("501", "BaidupayClick");
		parm.put("502", "BaidupayCancel");
		parm.put("503", "BaidupaySuccess");
		parm.put("504", "BaidupayFail");
		
		
		
	}
	

}

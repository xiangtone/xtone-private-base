package com.thirdpay.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.message.BasicNameValuePair;
import org.common.util.ConfigManager;
import org.common.util.ConnectionService;
import org.common.util.GenerateIdService;
import org.common.util.ThreadPool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thirdpay.domain.ForwardsyncBean;
import com.thirdpay.domain.PayInfoBean;
import com.thirdpay.utils.CheckCPInfo;

public class testInsert {

	public static void main(String[] args) {

		// while(true){
		// ThreadPool.mThreadPool
		// .execute(new PayInfoBean(0, "cbl", "cbl", "cbl", "cbl", "cbl", "cbl",
		// "cbl", "cbl", "cbl", 0));
		// }

		// ThreadPool.mThreadPool.execute(new PayInfoBean(0, "cbl", "cbl",
		// "cbl", "cbl", "cbl", "cbl", "cbl", "cbl", "cbl", "cbl", 0));

		// PreparedStatement ps = null;
		// Connection con = null;
		// con = ConnectionService.getInstance().getConnectionForLocal();
		// try {
		// ps = con.prepareStatement("SELECT * FROM tbl_thirdpay_cp_information
		// WHERE appKey=" + "'" + "zgt" + "'");
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// ThreadPool.mThreadPool.execute(new ForwardsyncBeanTest(1001,
		// "123456", "0","3000", "0", "url", "200", "zgt", "appkey"));

		// StringBuilder MyStringBuilder = new StringBuilder("Hello World!");
		// MyStringBuilder.append(" What a beautiful day.");
		// MyStringBuilder.insert(0, "cbl");
		// MyStringBuilder.deleteCharAt(MyStringBuilder.length()-1);
		// System.out.println(MyStringBuilder);

		// 测试用数据
		// String payInfo = "";
		// String[] cbl = { "aa" };
		// String[] zgt = { "cc" };
		// String[] cdf = { "ee" };
		// Map<String, String[]> map = new HashMap<String, String[]>();
		// map.put("cbl", cbl);
		// map.put("zgt", zgt);
		// map.put("cdf", cdf);
		// List<BasicNameValuePair> formparams = new
		// ArrayList<BasicNameValuePair>();
		//
		// Iterator<Entry<String, String[]>> iterator =
		// map.entrySet().iterator();
		//
		// while (iterator.hasNext()) {
		// Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>)
		// iterator.next();
		//
		// String key = entry.getKey(); // key为参数名称
		// if (key != "cbl") {
		// String[] value = map.get(key); // value为参数值
		//
		// for (int i = 0; i < value.length; i++) {
		//
		// payInfo += "\"" + key + "\"" + ":" + "\"" + value[i] + "\"" + ",";
		//
		// }
		// }
		//
		// }
		// System.out.println("payInfo = " + payInfo);
		// StringBuilder builder = new StringBuilder(payInfo);
		// builder.deleteCharAt(builder.length() - 1);
		// builder.insert(0, "{");
		// builder.append("}");
		// System.out.println(builder);
		
//		String url = CheckCPInfo.CheckInfo("zgt").getNotify_url();
//		ThreadPool.mThreadPool
//				.execute(new ForwardsyncBeanTest(1001, "123456", "0", "3000", "0", url, "200", "zgt", "appkey"));

		String str = "{\"buyNum\":0,\"coinNum\":0,\"price\":1,\"productDesc\":\"商品名称是苹果商品id为123456\",\"productId\":\"12345\",\"productName\":\"苹果\",\"ratio\":0,\"roleLevel\":0,\"uid\":\"7d07ccb3-8d83-4ebc-b2e9-2f37b120fa0d\",\"webOrderid\":\"1462867117426032111\"}";
		
//		System.out.println(str.replace("\\", ""));
		
		JSONObject payParamsjson =  JSON.parseObject(str.replace("\\", "")); 
		
		
	}

}

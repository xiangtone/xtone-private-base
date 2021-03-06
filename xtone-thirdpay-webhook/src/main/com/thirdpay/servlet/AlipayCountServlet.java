package com.thirdpay.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.common.util.ThreadPool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thirdpay.domain.PayInfoBean;
import com.thirdpay.utils.CheckPayInfo;
import com.thirdpay.utils.payConstants;

/**
 * 支付宝统计servlet
 */
@WebServlet("/AlipayCountServlet")
public class AlipayCountServlet extends HttpServlet {
	private static final Logger LOG = Logger.getLogger(AlipayCountServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AlipayCountServlet() {
		super();
		// TODO Auto-generated constructor stub

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		requestPostData(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/**
	 * 打印数据
	 * 
	 * @param request
	 * @return
	 */
	public static String requestPostData(HttpServletRequest request, HttpServletResponse response) {
		LOG.info("--------------------调用了AlipayCountServlet ------------------------ ");
		
		String xx_notifyData = request.getParameter("xx_notifyData");// 自定义参数
		
		JSONObject json = JSON.parseObject(xx_notifyData); // 解析自定义参数

		int price = (int) ((Float.parseFloat(request.getParameter("price"))) * 100); // 支付宝转换单位:元→分
		String payChannel = json.getString("platform");// 支付通道channel

		String ip = request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP") : request.getRemoteAddr();// 来源ip

		String releaseChannel = json.getString("channel");// 发行通道ID，一般从payInfo中解析出
		String appKey = json.getString("appkey");
		String payChannelOrderId = request.getParameter("out_trade_no");// 支付通道的订单号，一般从payInfo中解析出

		String ownUserId = request.getParameter("ownUserId");// 付费用户ID，待用
		String ownItemId = request.getParameter("ownItemId");// 购买道具ID，待用

		String ownOrderId = json.getString("OrderIdSelf");// 原始订单号ID
		String cpOrderId = json.getString("OrderIdCp"); // cp方订单号
		int payStatus = payConstants.paytestStatus;// 是否是测试信息
		String payInfo = getPayInfo(request); // 从支付通道获取的原始内容



		
		//版本兼容
		if (payChannel == null) {
			payChannel = json.getString("p");
		}
		if (releaseChannel == null) {
			releaseChannel = json.getString("a");
		}
		if (appKey == null) {
			appKey = json.getString("k");
		}
		if (ownOrderId == null) {
			ownOrderId = json.getString("s");
		}
		if (cpOrderId == null) {
			cpOrderId = json.getString("c");
		}

		String payChannelUserID  = "";//支付渠道的付费用户标识
		
		String IMEIforwardString = CheckPayInfo.CheckInfoIMEI(ownOrderId);
		String payUserIMEI ="";
		if(!"".equals(IMEIforwardString)){
			LOG.info("IMEIforwardString --------- "+IMEIforwardString);
			JSONObject IMEIjson = JSON.parseObject(IMEIforwardString); // 解析自定义参数
			 payUserIMEI = IMEIjson.getString("imei");//支付用户IMEI //2016/09/20
			LOG.info("payUserIMEI --------- "+payUserIMEI);
		}
		
		
		// wait_buyer_pay是创建订单成功的时候发送的
		// trade_success是交易支付成功的时候发送的
		String trade_status = request.getParameter("trade_status"); // 支付状态

		if (trade_status.equals("TRADE_SUCCESS")) {
			// 数据库写入操作
			ThreadPool.mThreadPool.execute(new PayInfoBean(price, payChannel, ip, payInfo, releaseChannel, appKey,
					payChannelOrderId, ownUserId, ownItemId, ownOrderId, cpOrderId, payStatus,payChannelUserID,payUserIMEI));
		}

		try {
			response.getWriter().append("success"); // 返回success
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 发送 post请求访问本地应用并根据传递参数不同返回不同结果
	 */
	public static void post(String url, List<BasicNameValuePair> formparams) {

		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.createDefault();

		// 创建httppost
		HttpPost httppost = new HttpPost(url);
		// 创建参数队列

		UrlEncodedFormEntity uefEntity;

		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			System.out.println("executing request" + httppost.getURI());
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					System.out.println("--------------------------------------");
					System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
					System.out.println("--------------------------------------");
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 得到所有的参数与参数值
	 * 
	 * @param request
	 * @return
	 */
	public static String getPayInfo(HttpServletRequest request) {
		String payInfo = "";
		// 测试用数据
		Map<String, String[]> map = request.getParameterMap();

		Iterator<Entry<String, String[]>> iterator = map.entrySet().iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) iterator.next();

			String key = entry.getKey(); // key为参数名称
			if (!key.equals("xx_notifyData")) {
				String[] value = map.get(key); // value为参数值

				for (int i = 0; i < value.length; i++) {

					payInfo += "\"" + key + "\"" + ":" + "\"" + value[i] + "\"" + ",";

				}

			}

		}

		return payInfo;
	}

}

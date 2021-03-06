package com.epplus.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils; 


//import com.alipay.sdk.app.PayTask;
/**
 *  支付宝支付
 * @author zgt
 *
 */
public class AlipayUtils {

	// 商户PID
	private  static final String PARTNER = "2088021159783167";
	// 商户收款账号
	private static final String SELLER = "xtgame@bjxiangtone.com";
	// 商户私钥，pkcs8格式
	private static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAM4reycyjGhOM/uEvTsva0fZbCAMD8zD+n+BMRtLAVNDczIsyThCby8Awrhr7tVa1r8TvtTDbcpMHBEMkDSiTP0r19uTLiyt+ei8GK7q/2v1xnfDJfsiRjUwdd9xSPlCLRACd/9iklbKZT60gHzcBov7S/8OvgebLVtCANoGVdDzAgMBAAECgYBN31qK+arTEwbLb93R5x1MbDFNAYFORI/vbSrRNklv28A9KXFvkJhSVqU15360k7UdQyYHUzG7AXhwcCBf4RXWtFIFRu1YTX+vtwOJoxo5q27byh9rOKg+4J4b5ojt78dAjYmmj59GhdUcr9gSBi11gDw5DbM80ytbzCuP6biM4QJBAPxT+O0NZRNBquP5PF0aTR6Ex2Hp4KByaNRDFXn0lnSX0zWAcdYnw9v0MKG7wT6K360SnxPPYmH3NDFhvwFSQesCQQDRK4zYB+0S/pgpdUxNxwnkWAO5arXW8hAm3bZUr71+NT3ozg+NTrDpO6+kpD2L6E5nz//tPJiffVshVyWODuMZAkBSJKgZy82Gyk7urlmXWZOXhtQ9rNyifvxfdYNNU3GTfUWV2j204Pci6MjYLf5H9P/CIRjGYzH9AHPuS4rZzESHAkBMVze1VNc62n7QisYJkP5UP6dEUeUCCSDJ/ptgNy/S0z3ALQzSBwlcZnNJhMQNvwB2tRx0CmytsQPEnFjRiy9pAkBfIoi7dj3n9M5iW31DAUvhLFxm5uBGgXg5yCQUSmqNSApYF7GhiHr3Cf227iglgHD3OO3ngCp8jHQ3MbA+zumH";
	// 支付宝公钥
	//private static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCdA+n4L9+INfZZ7yfJyzbsknMgW2CsW3BFplPR L8/rXdk9v2lRNAEewJZxLhqcINCJ/dOu7Wi7WFqm293U76QLR3LgAXf18xogJLhHH9P/U/KFkalU 2byEJrqpt809N51G2RakdD4sx/yvpmm+Rk0a8W6y0AzL40KPzB70oEStQwIDAQAB";
	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;
	
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					alipayHandler.aliPaySuccess(resultInfo, resultStatus);
					//Toast.makeText(activity, "支付成功",Toast.LENGTH_SHORT).show();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						alipayHandler.aliPayConfirmed(resultInfo, resultStatus);
						//Toast.makeText(activity, "支付结果确认中",Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						alipayHandler.aliPayFailed(resultInfo, resultStatus);
						//Toast.makeText(activity, "支付失败",Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				//Toast.makeText(activity, "检查结果为：" + msg.obj,Toast.LENGTH_SHORT).show();
				alipayHandler.aliPayCheck((Boolean)msg.obj);
				break;
			}
			default:
				break;
			}
		};
	};

	
	private Activity activity;
	private AlipayHandler alipayHandler;
	

	
	private String notify_url;
	
	
	
	
	public AlipayUtils(Activity activity,String OrderIdSelf,String OrderIdCp,AlipayHandler alipayHandler) {
		this.activity =activity;
		this.alipayHandler = alipayHandler;
		//String baseUrl = ConfigUtils.Notify_Url_Alipy;//"http://thirdpay-webhook.n8wan.com:29141/thirdpayCountServlet";
		
		this.notify_url = URLUtils.notifyUrlAlipy(activity,OrderIdSelf,OrderIdCp);//baseUrl+"?"+ConfigUtils.xx_notifyData+"="+ConfigUtils.getNotifyJsonData(activity,ConfigUtils.ALIPAY);
		
	}
	

	
	/**
	 * call alipay sdk pay. 调用SDK支付
	 * @param subject 商品名称
	 * @param body   商品描述
	 * @param price  商品价格  eg: 0.01
	 */
	public void pay(final String subject, final String body, final String price) {
		
		// 订单
		String orderInfo = getOrderInfo(subject, body, price);

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				try {
//					String SERVICE_ALIPAY = "http://192.168.0.111:8080/alipaySign/AlipaySign";
//					
//					String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//			        Map<String, String> map = new HashMap<String, String>();
//			        map.put("subject", subject);
//			        map.put("body", body);
//			        map.put("price", price);
//			        //自定义数据
//			        map.put("notify_url",notify_url);
//		 	        String mPayInfo = HttpUtils.post(SERVICE_ALIPAY, map);
					
					Class PayTaskClass = Class.forName("com.alipay.sdk.app.PayTask");
					Constructor constructor = PayTaskClass.getDeclaredConstructor(Activity.class);
					Object PayTask_ = constructor.newInstance(activity);
					
					Method pay = PayTaskClass.getDeclaredMethod("pay", String.class);
					String result = (String) pay.invoke(PayTask_, payInfo);
					
					Message msg = new Message();
					msg.what = SDK_PAY_FLAG;
					msg.obj = result;
					mHandler.sendMessage(msg);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
//				// 构造PayTask 对象
//				PayTask alipay = new PayTask(activity);
//				// 调用支付接口，获取支付结果
//				String result = alipay.pay(payInfo);
//
//				Message msg = new Message();
//				msg.what = SDK_PAY_FLAG;
//				msg.obj = result;
//				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 */
	public void check() {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				
				
				try {
					Class PayTaskClass = Class.forName("com.alipay.sdk.app.PayTask");
					Constructor constructor = PayTaskClass.getDeclaredConstructor(Activity.class);
					Object PayTask_ = constructor.newInstance(activity);
					
					Method checkAccountIfExist = PayTaskClass.getDeclaredMethod("checkAccountIfExist");
					
					boolean isExist = (Boolean) checkAccountIfExist.invoke(PayTask_);
					Message msg = new Message();
					msg.what = SDK_CHECK_FLAG;
					msg.obj = isExist;
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
//				// 构造PayTask 对象
//				PayTask payTask = new PayTask(activity);
//				// 调用查询接口，获取查询结果
//				boolean isExist = payTask.checkAccountIfExist();
//
//				Message msg = new Message();
//				msg.what = SDK_CHECK_FLAG;
//				msg.obj = isExist;
//				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public String getSDKVersion(Activity activity) {
		
		try {
			Class PayTaskClass = Class.forName("com.alipay.sdk.app.PayTask");
			Constructor constructor = PayTaskClass.getDeclaredConstructor(Activity.class);
			Object PayTask_ = constructor.newInstance(activity);
			
			Method getVersion = PayTaskClass.getDeclaredMethod("getVersion");
			String version = (String) getVersion.invoke(PayTask_);
			return version;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
//		PayTask payTask = new PayTask(activity);
//		String version = payTask.getVersion();
//		return version;
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	private  String getOrderInfo(String subject, String body, String price) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		//orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
		orderInfo += "&notify_url=" + "\"" + notify_url
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	private  String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}


	/**
	 * 支付回调
	 * @author Administrator
	 *
	 */
	public static abstract class AlipayHandler{
		/**
		 * 支付成功
		 * @param resultInfo  支付宝返回此次支付结果
		 * @param resultStatus  支付宝返回此次支付状态
		 */
		public abstract void aliPaySuccess(String resultInfo,String resultStatus);
		/**
		 * 支付失败
		 * @param resultInfo 支付宝返回此次支付结果
		 * @param resultStatus 支付宝返回此次支付状态
		 */
		public abstract void aliPayFailed(String resultInfo,String resultStatus);
		/**
		 * 支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
		 * @param resultInfo 支付宝返回此次支付结果
		 * @param resultStatus 支付宝返回此次支付状态
		 */
		public void aliPayConfirmed(String resultInfo,String resultStatus){
			
		}
		
		/**
		 * 查询终端设备是否存在支付宝认证账户 
		 * @param isExist 检查结果
		 */
		public void aliPayCheck(boolean isExist){
			
		}
	}

	
	
}

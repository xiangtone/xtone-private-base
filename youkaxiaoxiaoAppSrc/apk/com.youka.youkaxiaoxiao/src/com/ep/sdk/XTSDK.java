package com.ep.sdk;

import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.account.bean.UserInfo;
import com.account.util.AccountService;
import com.account.util.CallBack;
import com.cmnpay.api.Payment;
import com.ep.sdk.YKSDKComponents.IYKSDKComCall;
import com.epplus.publics.EPPayHelper;
import com.epplus.view.PayParams;

public class XTSDK {
	
	private static XTSDK xtsdk;
	
	private boolean isInit;
	
	private UserInfo userInfo;
	
	private Handler mHandler;
	
	private XTSDK(){
		isInit = false;
	} 
	
	/**
	 * 获取XTSDK
	 * @return
	 */
	public static XTSDK getInstance(){
		if(xtsdk==null){
			xtsdk = new XTSDK();
		}
		return xtsdk;
	}
	
	/**
	 * 初始化
	 */
	public void init(final Activity ac,String payContact,Handler handler){
		this.mHandler = handler;
		EPPayHelper.getInstance(ac).initPay(true,payContact);//"4001059566"
		EPPayHelper.getInstance(ac).setPayListen(handler);
		Payment.init(ac);
		isInit = true;
		
		//如果用户登录过自动登陆
		AtomicBoolean initialized = new AtomicBoolean(false);
		if(initialized.compareAndSet(false, true)){
		   //自动登陆
			AccountService.getInstances().autoLogin(ac, new CallBack() {
				@Override
				public void loginSuccess(UserInfo arg0) {
					userInfo = arg0;
					Toast.makeText(ac, "自动登陆", Toast.LENGTH_SHORT).show();
					Message msg = mHandler.obtainMessage();
					msg.what = 4011;
					msg.obj =arg0; 
					mHandler.sendMessage(msg);
				}
			});
		}
		
	}
	
	
	/**
	 * 用户登录
	 */
	public void login(final Activity ac){
		if(isInit){
		
		AccountService.getInstances().showWebDialog(ac, new CallBack() {
			
			@Override
			public void loginSuccess(UserInfo arg0) {
				userInfo = arg0;
				Toast.makeText(ac, "登陆成功", Toast.LENGTH_SHORT).show();
				Message msg = mHandler.obtainMessage();
				msg.what = 4012;
				msg.obj =arg0; 
				mHandler.sendMessage(msg);
			}
		});
		}
	}
	
	
	/**
	 * 支付 
	 */
	public boolean pay(final Activity ac,final PayParams params){
		
		if(!ac.isFinishing()){
			if(userInfo!=null&&!TextUtils.isEmpty(userInfo.getUserID())&&isInit){
				
				
				if(userInfo!=null&&!TextUtils.isEmpty(userInfo.getUserID())&&isInit){
					if(YKSDKComponents.checkYKSDK()){
						
						YKSDKComponents.instances(ac).activities(new IYKSDKComCall() {
							
							@Override
							public void getDataSuccess() {
								YKSDKComponents.instances(ac).dismiss();
								params.setUid(userInfo.getUserID());
								EPPayHelper.getInstance(ac).pay(params);
							}
						});
						
					}else {
						params.setUid(userInfo.getUserID());
						EPPayHelper.getInstance(ac).pay(params);
					}
				}
					
//				params.setUid(userInfo.getUserID());
//				EPPayHelper.getInstance(ac).pay(params);
				return true;
			}else {
				return false;
			}
		}
		return false;
		
	}
	
	/**
	 * 登出
	 */
	public void logout(){
		userInfo = null;
		AccountService.getInstances().logout();
		
	}
	
	/**
	 * 退出
	 */
	public void exit(Activity ac){
		try {
			EPPayHelper.getInstance(ac).exit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 支付回调结果
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void payCallResult(Activity ac,int requestCode, int resultCode, Intent data){
		EPPayHelper.getInstance(ac).onActivityResult(requestCode, resultCode, data);
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}
	
	
	

}

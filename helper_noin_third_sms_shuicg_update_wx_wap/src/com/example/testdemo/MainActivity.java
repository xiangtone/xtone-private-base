package com.example.testdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.epplus.view.PayParams;


public class MainActivity extends Activity{
	
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			System.out.println(msg);
			switch (msg.what) {
			
			case 1070:
				Toast.makeText(MainActivity.this, "失败-"+msg.obj.toString(), 1000).show();
				break;
			case 1078:
				Toast.makeText(MainActivity.this, "失败*"+msg.what, 1000).show();
				break;
			case 4001:
				Toast.makeText(MainActivity.this, msg.what+"", 1000).show();
				break;
			case 4002:
				Toast.makeText(MainActivity.this, msg.what+"", 1000).show();
				break;
			case 4010:
				Toast.makeText(MainActivity.this, "初始化成功*"+msg.what, 1000).show();
				break;
			case 4011:
				Toast.makeText(MainActivity.this, "自动登陆成功*"+msg.what, Toast.LENGTH_SHORT).show();
				break;
			case 4012:
			default:
//				Toast.makeText(MainActivity.this, "未知原因*"+msg.what, 1000).show();
				break;
			}
			
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		XTSDK.getInstance().init(this,"4001059566",handler);
		
		Log.e("zgt", "onCreate");
	}
	
	
	public void onMyclick(View view){
		switch (view.getId()) {
		case R.id.button1:
			break;
		case R.id.button2:
			break;
		case R.id.button3:
			Toast.makeText(this, "退出", Toast.LENGTH_SHORT).show();
			break;
		case R.id.button4:
			PayParams pa = new PayParams(1, "12345", "苹果", "商品名称是苹果商品id为123456");
			 XTSDK.getInstance().pay(this,pa);
			
			break;

		default:
			break;
		}
	}
	
	
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			XTSDK.getInstance().payCallResult(this,requestCode, resultCode, data);
			super.onActivityResult(requestCode, resultCode, data);
		}
	 
	 @Override  
	    protected void onDestroy() {  
	        super.onDestroy(); 
	        try {
	        	XTSDK.getInstance().exit(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
	    }  

}

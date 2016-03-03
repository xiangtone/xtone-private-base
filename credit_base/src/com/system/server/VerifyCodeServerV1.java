package com.system.server;

import com.system.constant.Constant;
import com.system.model.ApiOrderModel;
import com.system.model.BaseResponseModel;
import com.system.util.StringUtil;

import net.sf.json.JSONObject;

public class VerifyCodeServerV1
{
	public String handleVerifyCode(String verifyCode,String transParams)
	{
		BaseResponseModel response = new BaseResponseModel();
		
		response.setStatus(Constant.CP_VERIFY_CODE_API_NOT_EXIST);
		
		if(StringUtil.isNullOrEmpty(verifyCode) 
				|| StringUtil.isNullOrEmpty(transParams) 
				|| transParams.length()<6)
		{
			return StringUtil.getJsonFormObject(response);
		}
		
		String tableName = transParams.substring(0,6);
		
		String apiId = transParams.substring(6); 
				
		ApiOrderModel apiOrderModel = new RecordServer().getApiOrderById(tableName, apiId);
		
		if(apiOrderModel==null)
		{
			return StringUtil.getJsonFormObject(response);
		}
		
		apiOrderModel.setCpVerifyCode(verifyCode);
		
		String result = NetServer.sendNextApiOrder(apiOrderModel);
		
		JSONObject jo = null;
		JSONObject command = null;
		JSONObject apiJson = null;
		ApiOrderModel resultModel = null;
		
		try
		{
			jo = JSONObject.fromObject(result);
			command = jo.getJSONObject("Command");
			apiJson = jo.getJSONObject("Request");
			resultModel = (ApiOrderModel)JSONObject.toBean(apiJson, ApiOrderModel.class);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		if(resultModel!=null)
		{
			apiOrderModel = resultModel;
			response.setResultJson(command);
			response.setStatus(apiOrderModel.getStatus());
		}
		else
		{
			response.setStatus(Constant.CP_VERIFY_CODE_ERROR);
			apiOrderModel.setStatus(response.getStatus());
			apiOrderModel.setCpVerifyCode(verifyCode);
			updateRequest(apiOrderModel,tableName);
			return StringUtil.getJsonFormObject(response);
		}
		
		apiOrderModel.setCpVerifyCode(verifyCode);
		
		updateRequest(apiOrderModel,tableName);
		
		return StringUtil.getJsonFormObject(response);	
	}
	
	private void updateRequest(ApiOrderModel model,String tableName)
	{
		new RecordServer().updateVeryCode(model,tableName);
	}
	
	public static void main(String[] args)
	{
		System.out.println("201601124".substring(6));
	}
}

package com.thirdpay.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.common.util.ConnectionService;
import org.common.util.ThreadPool;

import com.thirdpay.domain.ForwardLogBean;



/**
 * 查询cp信息 返回cpInfoBean对象
 * 
 * @author 28518
 *
 */
public class CheckPayInfo {
	private static final Logger LOG = Logger.getLogger(CheckPayInfo.class);
	
	/**
	 * 返回转发数据
	 * @param ownOrderId
	 * @return
	 */
	public static String CheckInfo(String ownOrderId) {
		// TODO Auto-generated method stub
		// ForwardsyncBean forwardsyncBean = new ForwardsyncBean();
		String jsonString = "";
		PreparedStatement ps = null; 
		Connection con = null;

		try {
			// DbKey 选择使用的数据库
			con = ConnectionServicethirdpayCount.getInstance().getConnectionForLocal(); // DbKey选择使用config.properties
			ps = con.prepareStatement("SELECT * FROM log_async_generals WHERE para05 = " + "'"+ownOrderId+"'");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				jsonString = rs.getString("para04");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return jsonString;
	}
		
public static String CheckInfoIMEI(String ownOrderId){

	// TODO Auto-generated method stub
	// ForwardsyncBean forwardsyncBean = new ForwardsyncBean();
	String jsonString = "";
	PreparedStatement ps = null; 
	Connection con = null;

	try {
		// DbKey 选择使用的数据库
		con = ConnectionServicethirdpayCount.getInstance().getConnectionForLocal(); // DbKey选择使用config.properties
		ps = con.prepareStatement("SELECT * FROM log_async_generals WHERE para05 = " + "'"+ownOrderId+"'");
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			jsonString = rs.getString("para03");
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	return jsonString;

}
	
	public static void Updata1001(String ownOrderId,String notify_url,String forwardString) {
		// TODO Auto-generated method stub
		
//	 	ForwardsyncBean forwardsyncBean = new ForwardsyncBean();
//		String jsonString = "";
		PreparedStatement ps = null;
		Connection con = null;

		try {
			// DbKey 选择使用的数据库
			con = ConnectionServicethirdpayCount.getInstance().getConnectionForLocal(); // DbKey选择使用config.properties
			ps = con.prepareStatement("UPDATE `log_async_generals` SET para02 = 'syncSuccess'"+",para04 = 'sendCount1'"+ " WHERE para01 = "+"'" + ownOrderId +"'"+" AND logId = 1001");
			
			if(!ps.execute()){
				LOG.info(ownOrderId+" -- 1001     数据状态更新成功为1");
				CheckPayInfo.Insert1002(ownOrderId, notify_url,forwardString);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.info(ownOrderId+" -- 1001  状态更新syncSuccess异常报错");
			e.printStackTrace();
			
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	
		
	}
	
	public static void Insert1002(String ownOrderId,String notify_url,String forwardString){
		String send_time = "send_time";
		
		ThreadPool.mThreadPool.execute(new ForwardLogBean(Contents.forwardsuccess, ownOrderId, notify_url, "200",
				"success", send_time, "rsp_time", "send_url", "send_header", "body", "rsp_status", "rsp_header",
				"rsp_body", "task_id", "key_type", "id_type",forwardString));
		
	}
	
	public static void Updata1001Time(String ownOrderId,String responseContent) {
		
		PreparedStatement ps = null;
		Connection con = null;
		String next_forwardTime = "sendNextTime=180000";
		String count = "sendCount1";
		String syncFail="syncFail";
		try {

			// DbKey 选择使用的数据库
			con = ConnectionServicethirdpayCount.getInstance().getConnectionForLocal(); // DbKey选择使用config.properties
			
//			//找出上次查询次数
//			ps = con.prepareStatement("SELECT * FROM `log_async_generals` WHERE para01 = " + ownOrderId + " AND logId = 1001");
//			ResultSet rs = ps.executeQuery();
//			while (rs.next()) {
//			 count = rs.getString("para04");
//			 next_forwardTime = rs.getString("para03");
//			}
//			count ++ ;
//			
//			//设置转发时间
//			if(count == 1){
//				next_forwardTime = 180000; 
//			}else if(count == 2){
//				next_forwardTime = 600000;
//			}else if(count == 3){
//				next_forwardTime = 1800000;
//			}else if(count == 4){
//				next_forwardTime = 3600000;
//			}else if(count == 5){
//				next_forwardTime = 7200000;
//			}else{
//				next_forwardTime = 9999999;
//			}
			
			//更新查询次数
			ps = con.prepareStatement(
					"UPDATE `log_async_generals` SET para03 = " + "'"+ next_forwardTime +"'" + ",para04 = "+ "'"+count+"'"+ ",para02 = 'syncFail'"+ ",para12 = "+"'"+responseContent+"'"+" WHERE para01 = "+"'" + ownOrderId +"'"+" AND logId = 1001");
			if (!ps.execute()) {
				LOG.info(ownOrderId + " 距离下次转发时间更新为  " + next_forwardTime + "" +"s" + " 已处理次数为  " + count );
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			LOG.info(ownOrderId + "--状态更新syncFail异常报错");
			
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}

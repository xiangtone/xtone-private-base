<%@page import="org.demo.info.CpChannelUser"%>
<%@page import="org.demo.json.DataInfo"%>
<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.demo.service.UserService"%>
<%@page import="org.demo.info.Code"%>
<%@page import="org.common.util.ConnectionService"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.demo.json.CodeRsp"%>
<%@page import="com.google.gson.LongSerializationPolicy"%>
<%@page import="com.google.gson.GsonBuilder"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="org.demo.utils.EscapeUnescape"%>
<%@ include file="inc-receive-body.jsp"%>
<%
	request.setCharacterEncoding("UTF-8");
	request.getSession(true);
	//User user = (User) session.getAttribute("user");
	//	String info = new String(EscapeUnescape.unescape(request.getParameter("info")));
	GsonBuilder gsonBuilder = new GsonBuilder();
	gsonBuilder.setLongSerializationPolicy(LongSerializationPolicy.STRING);
	Gson gson = gsonBuilder.create();
	CpChannelUser cpChannelUser = gson.fromJson(info, CpChannelUser.class);
	PreparedStatement ps = null;
	Connection con = null;
	ResultSet rs = null;
	int updates = 0;
	Date date = new Date();
	String sql = "";
	try {
		
		con = ConnectionService.getInstance().getConnectionForLocal();
		if(cpChannelUser.getType()==2){
			sql = "SELECT pwd FROM `tbl_thirdpay_cp_channel_users` WHERE id=?";
			ps = con.prepareStatement(sql);
			ps.setLong(1, cpChannelUser.getId());
			rs = ps.executeQuery();
			if(rs.next()){
				if(cpChannelUser.getPwd().equals(rs.getString("pwd"))){
					sql = "UPDATE `tbl_thirdpay_cp_channel_users` SET username=?,pwd=?,email=?,isAdmin=?,isAvail=?,cpId=?,reduceBeginAmoumt=?,reduceScale=? WHERE id=?";
				}else{
					sql = "UPDATE `tbl_thirdpay_cp_channel_users` SET username=?,pwd=md5(?),email=?,isAdmin=?,isAvail=?,cpId=?,reduceBeginAmoumt=?,reduceScale=? WHERE id=?";
				}
			}
			
			ps = con.prepareStatement(sql);
	
			int m = 1;
			ps.setString(m++, cpChannelUser.getUser());
			ps.setString(m++, cpChannelUser.getPwd());
			ps.setString(m++, cpChannelUser.getEmail());
			ps.setInt(m++, cpChannelUser.getAdmin());
			ps.setInt(m++, cpChannelUser.getStatus());
			ps.setLong(m++, cpChannelUser.getCpId());
			ps.setInt(m++, cpChannelUser.getRbamoumt());
			ps.setInt(m++, cpChannelUser.getRedscale());
			ps.setLong(m++, cpChannelUser.getId());
			
			updates = ps.executeUpdate();
	
			if (updates != 0) {
				CodeRsp codeRsp = new CodeRsp();
				String rsp = gson.toJson(cpChannelUser);
				out.print(rsp);
			} else {
				out.print("{\"status\":\"error\",\"data\":\"" + info + "\"}");
			}
		}else if(cpChannelUser.getType()==1){
			
			sql = "SELECT releaseChannel FROM `tbl_thirdpay_cp_channel_users` WHERE releaseChannel=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, cpChannelUser.getRelchannel());
			rs = ps.executeQuery();
			if(rs.next()){
				DataInfo datainfo = new DataInfo();
				datainfo.setStatue("error");
				datainfo.setData("发布渠道已存在!");
				String rsp = gson.toJson(datainfo);
				out.print(rsp);
			}else{
				sql = "INSERT INTO `tbl_thirdpay_cp_channel_users`(username,pwd,email,isAdmin,isAvail,lastLogin,addTime,cpId,releaseChannel,reduceBeginAmoumt,reduceScale) VALUES(?,md5(?),?,?,?,?,?,?,?,?,?)";
				ps = con.prepareStatement(sql);
				
				int m = 1;
				ps.setString(m++, cpChannelUser.getUser());
				ps.setString(m++, cpChannelUser.getPwd());
				ps.setString(m++, cpChannelUser.getEmail());
				ps.setInt(m++, cpChannelUser.getAdmin());
				ps.setInt(m++, cpChannelUser.getStatus());
				ps.setLong(m++, date.getTime());
				ps.setLong(m++, date.getTime());
				ps.setLong(m++, cpChannelUser.getCpId());
				ps.setString(m++, cpChannelUser.getRelchannel());
				ps.setInt(m++, cpChannelUser.getRbamoumt());
				ps.setInt(m++, cpChannelUser.getRedscale());
				if(!ps.execute()){
					CodeRsp codeRsp = new CodeRsp();
					String rsp = gson.toJson(cpChannelUser);
					out.print(rsp);
				}else {
					out.print("{\"status\":\"error\",\"data\":\"" + info + "\"}");
				}			
			}
		}
		

	} catch (Exception e) {
		// TODO Auto-generated catch block
		out.print("{\"status\":\"error\",\"data\":\"" + info + "\"}");
		e.printStackTrace();
	} finally {
		if (con != null) {
			try {
				con.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
%>
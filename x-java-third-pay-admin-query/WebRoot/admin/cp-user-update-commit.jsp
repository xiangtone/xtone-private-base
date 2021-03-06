<%@page import="org.demo.info.CpUser"%>
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
	CpUser cpUser = gson.fromJson(info, CpUser.class);
	PreparedStatement ps = null;
	Connection con = null;
	ResultSet rs = null;
	int updates = 0;
	Date date = new Date();
	String sql = "";
	try {
		
		con = ConnectionService.getInstance().getConnectionForLocal();
		if(cpUser.getType()==2){
			sql = "SELECT pwd FROM `tbl_thirdpay_cp_users` WHERE id=?";
			ps = con.prepareStatement(sql);
			ps.setLong(1, cpUser.getId());
			rs = ps.executeQuery();
			if(rs.next()){
				if(cpUser.getPwd().equals(rs.getString("pwd"))){
					sql = "UPDATE `tbl_thirdpay_cp_users` SET username=?,pwd=?,email=?,isAdmin=?,isAvail=?,cpId=? WHERE id=?";
				}else{
					sql = "UPDATE `tbl_thirdpay_cp_users` SET username=?,pwd=md5(?),email=?,isAdmin=?,isAvail=?,cpId=? WHERE id=?";
				}
			}
			
			
			ps = con.prepareStatement(sql);
	
			int m = 1;
			ps.setString(m++, cpUser.getUser());
			ps.setString(m++, cpUser.getPwd());
			ps.setString(m++, cpUser.getEmail());
			ps.setInt(m++, cpUser.getAdmin());
			ps.setInt(m++, cpUser.getStatus());
			ps.setLong(m++, cpUser.getCpId());
			ps.setLong(m++, cpUser.getId());
			
			updates = ps.executeUpdate();
	
			
			if (updates != 0) {
				CodeRsp codeRsp = new CodeRsp();
				String rsp = gson.toJson(cpUser);
				out.print(rsp);
			} else {
				out.print("{\"status\":\"error\",\"data\":\"" + info + "\"}");
			}
		}else if(cpUser.getType()==1){
			sql = "INSERT INTO `tbl_thirdpay_cp_users`(username,pwd,email,isAdmin,isAvail,lastLogin,addTime,cpId) VALUES(?,md5(?),?,?,?,?,?,?)";
			ps = con.prepareStatement(sql);
			
			int m = 1;
			ps.setString(m++, cpUser.getUser());
			ps.setString(m++, cpUser.getPwd());
			ps.setString(m++, cpUser.getEmail());
			ps.setInt(m++, cpUser.getAdmin());
			ps.setInt(m++, cpUser.getStatus());
			ps.setLong(m++, date.getTime());
			ps.setLong(m++, date.getTime());
			ps.setLong(m++, cpUser.getCpId());
			if(!ps.execute()){
				CodeRsp codeRsp = new CodeRsp();
				String rsp = gson.toJson(cpUser);
				out.print(rsp);
			}else {
				out.print("{\"status\":\"error\",\"data\":\"" + info + "\"}");
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
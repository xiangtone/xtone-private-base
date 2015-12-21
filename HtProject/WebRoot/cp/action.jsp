<%@page import="com.system.model.CpModel"%>
<%@page import="com.system.server.CpServer"%>
<%@page import="com.system.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int id = StringUtil.getInteger(request.getParameter("id"), -1);
	String fullName = StringUtil.getString(request.getParameter("full_name"), "");
	String shortName = StringUtil.getString(request.getParameter("short_name"), "");
	String contractPerson = StringUtil.getString(request.getParameter("contract_person"), "");
	String qq = StringUtil.getString(request.getParameter("qq"), "");
	String email = StringUtil.getString(request.getParameter("email"), "");
	String phone = StringUtil.getString(request.getParameter("phone"), "");
	String address = StringUtil.getString(request.getParameter("address"), "");
	String contractStartDate = StringUtil.getString(request.getParameter("contract_start_date"), StringUtil.getDefaultDate());
	String contractEndDate = StringUtil.getString(request.getParameter("contract_end_date"), StringUtil.getDefaultDate());
	String query = request.getQueryString();
	
	CpModel model = new CpModel();
	model.setId(id);
	model.setFullName(fullName);
	model.setShortName(shortName);
	model.setContactPerson(contractPerson);
	model.setQq(qq);
	model.setMail(email);
	model.setPhone(phone);
	model.setAddress(address);
	model.setContractStartDate(contractStartDate);
	model.setContractEndDate(contractEndDate);
	
	if(id>0)
	{
		new CpServer().updateCp(model);
	}
	else
	{
		new CpServer().addCp(model);
	}
	
	response.sendRedirect("cp.jsp?"+query);
	
%>

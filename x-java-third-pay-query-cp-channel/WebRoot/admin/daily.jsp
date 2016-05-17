<%@page import="org.demo.utils.ConnectionServiceConfig"%>
<%@page import="org.demo.info.Daily"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.TimeZone"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.text.SimpleDateFormat"%>

<html lang="en">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">

<title>日统计</title>

<!-- Bootstrap core CSS -->
<link rel="stylesheet"
	href="http://cdn.bootcss.com/bootstrap/3.3.1/css/bootstrap.min.css">
<!-- code.jquery.com -->
<script src="../js-css/jquery-1.7.js"></script>
<script src="../js-css/ie-emulation-modes-warning.js"></script>

<link href="../js-css/bootstrap-datetimepicker.min.css" rel="stylesheet"
	media="screen">
<script type="text/javascript"
	src="../js-css/bootstrap-datetimepicker.js" charset="UTF-8"></script>

<!-- DataTables -->
<link rel="stylesheet" type="text/css"
	href="http://cdn.datatables.net/1.10.4/css/jquery.dataTables.css">
<script type="text/javascript" charset="utf8"
	src="http://cdn.datatables.net/1.10.4/js/jquery.dataTables.js"></script>
</head>

<body>
	<jsp:include page="menu.jsp" />
	<table id="table_id" class="display">
		<thead>
			<tr>
				<th>日期</th>
				<th>应用（appkey）</th>
				<th>总额（元）</th>
			</tr>
		</thead>
		<tbody>
			<%
				Daily daily;
				PreparedStatement ps = null;
				Connection con = null;
				ResultSet rs = null;
				try {
					con = ConnectionServiceConfig.getInstance().getConnectionForLocal();
					String sql = "select FROM_UNIXTIME(id/1000/1000000, '%Y-%m-%d') AS date,sum(price) as price,GROUP_CONCAT(DISTINCT appkey) AS appkey from log_success_pays group by date";
					ps = con.prepareStatement(sql);
					rs = ps.executeQuery();
					while (rs.next()) {
						daily = new Daily();
						daily.setId(rs.getString("date"));
						daily.setAppKey(rs.getString("appKey"));
// 						String appKeys[] = daily.getAppKey().split(",");
						daily.setMoney(rs.getFloat("price"));
			%>
			<tr>
				<td><%=daily.getId()%></td>
				<td>
<%-- 				<%for(int i=0;i<appKeys.length;i++){%> --%>
<%-- 				<a href='daily-appkey.jsp?appkey=<%=appKeys[i]%>' class='menus' target="_blank"><%=appKeys[i]%></a> --%>
<%-- 				<% }%></td> --%>
				<%=daily.getAppKey()%></td>
				<td><%=daily.getMoney()/100%></td>
			</tr>
			<%
				}
				} catch (Exception e) {
					// TODO Auto-generated catch block
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
		</tbody>
	</table>
	<script type="text/javascript">
		$(document).ready(function() {
			$('#table_id').DataTable();
		});
	</script>

	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<script src="../js-css/ie10-viewport-bug-workaround.js"></script>
</body>
</html>
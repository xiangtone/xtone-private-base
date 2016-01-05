<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.vanggame.info.Content"%>
<%@page import="org.common.util.ConnectionService"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1">
<meta name="description"
	content="Bootstrap.">
<meta name="keywords"
	content="HTML, CSS, JS, JavaScript, framework, bootstrap, front-end, frontend, web development">
<%
	String idStr = request.getParameter("id");
	String index = request.getParameter("pageindex");
	int pageIndex=Integer.parseInt(index);
	int id = Integer.parseInt(idStr);
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	Content content = new Content();
	try {
		con = ConnectionService.getInstance().getConnectionForLocal();
		String sql = "SELECT `title`,`content`,`lastModifyTime` FROM `tbl_cms_contents` WHERE id=?";
		ps = con.prepareStatement(sql);
		ps.setInt(1, id);
		rs = ps.executeQuery();
		if (rs.next()) {
			content.setTitle(rs.getString("title"));
			content.setContent(rs.getString("content"));
			content.setLastModifyTime(rs.getLong("lastModifyTime"));
		}
	} catch (Exception e) {
		e.printStackTrace();
	}finally {
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
<title>万家游戏-新闻动态</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/docs.min.css" rel="stylesheet">
<!--[if lt IE 9]><script src="assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
<script async="" id="gauges-tracker" data-site-id="4f0dc9fef5a1f55508000013" src="js/bootstrap/track.js"></script>
<script async="" src="js/bootstrap/analytics.js"></script>
<script src="js/bootstrap/ie-emulation-modes-warning.js"></script>
<!--[if lt IE 9]><script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script><![endif]-->
<!-- <script>!function(e,t,a,n,c,o,s){e.GoogleAnalyticsObject=c,e[c]=e[c]||function(){(e[c].q=e[c].q||[]).push(arguments)},e[c].l=1*new Date,o=t.createElement(a),s=t.getElementsByTagName(a)[0],o.async=1,o.src=n,s.parentNode.insertBefore(o,s)}(window,document,"script","//www.google-analytics.com/analytics.js","ga"),ga("create","UA-146052-10","getbootstrap.com"),ga("send","pageview");</script> -->
<style>

.newsli{
	border-bottom: #e1e1e0 solid 1px;
	padding-top: 15px;
	padding-bottom: 16px;
/* 	list-style-image: url("images/index/dian_1.png"); */
	color: #cf1232;
}

@media ( min-width :768px){
.newsli{
	border-bottom: #e1e1e0 solid 1px;
	padding-top: 11px;
	padding-bottom: 11px;
/* 	list-style-image: url("images/index/dian_1.png"); */
color: #cf1232;
}
}
@media ( min-width :1201px){
.newsli{
	border-bottom: #e1e1e0 solid 1px;
	padding-top: 16px;
	padding-bottom: 16px;
/* 	list-style-image: url("images/index/dian_1.png"); */
color: #cf1232;
}
}

.newsli i{
color: #aaaaaa;
}

.newsli em{
color: #aaaaaa;
}

.links{
	color: #aaaaaa;
    text-decoration: none;
}

.title{
	font-size:30px;
	color: #cf1232;
	margin: 0;
}

.content{
	background: #fff;
	margin-bottom: 30px;
	padding: 15px 100px 15px 100px;
}

@media ( min-width :768px){
.content{
	background: #fff;
	margin-bottom: 30px;
	padding: 15px 0px 15px 0px;
}
}

.titleimg{
	float: left;
    padding-top: 10px;
    margin-right: 10px;
}

.content-top{
	background: #2c2b30;
	padding: 15px 0px 15px 20px;
}

.btn{
	float:right;
    background: #cf1232;
    color: #fff;
    font: 14px Verdana, Arial, Helvetica, sans-serif;
    padding-left: 35px;
    padding-right: 35px;
    }
</style>
</head>
<body class="bs-docs-home">
	<header class="navbar navbar-static-top bs-docs-nav" id="top"
		role="banner">
		<div class="container">
			<div class="navbar-header">
				<button class="navbar-toggle collapsed" type="button"
					data-toggle="collapse" data-target="#bs-navbar"
					aria-controls="bs-navbar" aria-expanded="false">
					<span class="sr-only"></span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a href="#"><img alt="logo" src="images/logo.png"></a>
			</div>
			<nav id="bs-navbar" class="collapse navbar-collapse">
				<ul class="nav navbar-nav navbar-right ztgs">
					<li><a id="index" href="index.jsp"><small class="nav-chinese">首页</small><br><small class="nav-english">HOME</small></a></li>
					<li class="active"><a id="news" href="news.jsp"><small class="nav-chinese">新闻动态</small><br><small class="nav-english">NEWS</small></a></li>
					<li><a id="partform" href="porducts-sjyx.html"><small class="nav-chinese">旗下产品</small><br><small class="nav-english">PORDUCTS</small></a></li>					
					<li><a id="abouts" href="about-us-gsjs.html"><small class="nav-chinese">关于我们</small><br><small class="nav-english">VANGGAME</small></a></li>
					<li><a id="cooperation" href="cooperation.html"><small class="nav-chinese">商务合作</small><br><small class="nav-english">COOPERATION</small></a></li>
					<li><a id="join" href="join-us-shzp.html"><small class="nav-chinese">招贤纳士</small><br><small class="nav-english">JOIN&nbsp;US</small></a></li>
					
				</ul>
			</nav>
		</div>
	</header>
	<div class="col-sm-12 col-sm-12 col-xs-12 nopadding"><img src="images/news/banner-news.png" class="bsimg"></div>
	<div class="bs-docs-featurette" style="background: #f1f1f1">
		<div class="container">
			<div class="row ztgs">
				<div class="col-md-12 col-sm-12 col-xs-12 nopadding">
					<a href="new.html"><img src="images/news/news.png"
						class="bsimg tab"></a>
				</div>
				<div class="col-md-12 col-sm-12 col-xs-12 content-top">
				<font size="4" color="#8a8a8a">您所在位置&nbsp;:&nbsp;<a style="color:#8a8a8a" href="news.jsp">新闻中心</a>&nbsp;>&nbsp;<a style="color:#8a8a8a" href="#">综合正文</a></font>
				</div>
				<div class="col-md-12 col-sm-12 col-xs-12 content">
					<div class="col-md-12 col-sm-12 col-xs-12" align="center">
						<strong style="font-size: 30px;"><%=content.getTitle()%></strong>
					</div>
					<div class="col-md-12 col-sm-12 col-xs-12" align="center">
						<p class="" style="color: #8f8f8f;"><%=content.getTimeStr()%>
					</div>
					<div class="col-md-12 col-sm-12 col-xs-12" style="padding-left: 37px;">
						<%=content.getContent()%>
					</div>
<!-- 					<div class="col-md-12 col-sm-12 col-xs-12"> -->
<%-- 						<input type="button" value="返回" onclick="window.location.href='news.jsp?pageIndex=<%=pageIndex%>'" class="btn"> --%>
<!-- 					</div> -->
				</div>
				<div class="col-md-12 col-sm-12 col-xs-12 bottom-hieght"></div>
			</div>
		</div>	
	</div>
	<footer class="bs-docs-footer" role="contentinfo">
		<div class="container nopadding">
			<div class="col-md-3 col-sm-5 col-xs-12 nopadding">
				<a href="#"><img alt="footlogo" src="images/footlogo.png"></a>
			</div>
			<div class="col-md-9 col-sm-7 col-xs-12 nopadding ztgs"
				style="text-align: center;">
				<ul class="bs-docs-footer-links">
					<!-- 			 style="border-right: #fff 1px solid;display: block;width: 80px;" -->
					<li><a href="about-us-gsjs.html">关于我们</a></li>
					<li><a href="cooperation.html">商务合作</a></li>
					<li><a href="join-us-shzp.html">招贤纳士</a></li>
					<li><a href="flsm.html">法律声明</a></li>				
					<li><img alt="" src="images/ico-weixin.png">&nbsp;<a data-toggle="popover">微信 </a></li>
					<li><a href="http://weibo.com/vanggame" target="_blank"><img alt="" src="images/ico-weibo.png"></a>&nbsp;<a href="http://weibo.com/vanggame" target="_blank">微博</a></li>
				</ul>
				<p class="footer-fontsize">
					万家游戏版权所有Copyright2002-2015中国网络游戏版权保护联盟举报中心&nbsp;闽B2-20040096-20&nbsp;&nbsp;&nbsp;&nbsp;
						<img alt="" src="images/index/foot1.png">&nbsp;&nbsp;&nbsp;&nbsp;
						<img alt="" src="images/index/foot2.png">&nbsp;&nbsp;&nbsp;&nbsp;
						<img alt="" src="images/index/foot3.png">&nbsp;&nbsp;&nbsp;&nbsp;
						<img alt="" src="images/index/foot4.png">
				<br>
				健康游戏忠告：抵制不良游戏&nbsp;拒绝盗版游戏&nbsp;注意自我保护&nbsp;谨防上当受骗&nbsp;适度游戏益脑&nbsp;沉迷游戏伤身&nbsp;合理安排时间&nbsp;享受健康生活</p>
			</div>
		</div>
	</footer>
	
	<script	src="js/bootstrap/jquery.min.js"></script>
	<script	src="js/bootstrap/bootstrap.min.js"></script>
	<script	src="js/docs.min.js"></script>
	<script	src="js/bootstrap/ie10-viewport-bug-workaround.js"></script>
	<script>var _gauges=_gauges||[];!function(){var e=document.createElement("script");e.async=!0,e.id="gauges-tracker",e.setAttribute("data-site-id","4f0dc9fef5a1f55508000013"),e.src="//secure.gaug.es/track.js";var t=document.getElementsByTagName("script")[0];t.parentNode.insertBefore(e,t)}();</script>
	<script type="text/javascript">
$(function(){
	$('.hot_list .hot_gamebox').hotlist();
})
</script>
</body>
</html>
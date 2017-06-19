<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
		<meta name="keywords" content="Answer,搜索,语义理解,搜索引擎,领域本体,自然语言搜索,智能搜索,语义网,电影搜索">
		<meta name="description" content="Answer是基于本体库的自动问答系统，同时也会给出全文搜索引擎的搜索结果">
		<meta name="renderer" content="webkit">
		<meta http-equiv="Cache-Control" content="no-siteapp">
		<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=0">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		<title>Answer</title>
		<!-- answer-main -->		
		<link rel="stylesheet" type="text/css" href="./css/answer-main-css/answer-main-css.css">
		<!-- fancy select -->
		<link rel="stylesheet" type="text/css" href="./css/fancy-select-css/normalize.css" />
		<link rel="stylesheet" type="text/css" media="screen, projection" href="./css/fancy-select-css/fancy-select.css">
		
	</head>
	<body>
		<div class="logo-main" onclick="document.location='mobile_index.jsp'">
			<a href="answer_index.jsp"><img id="logo" alt="logo" src="./images/answer-logo.png"></a>
		</div>
		
		<div id="sc" class="f-main">
			<div id="u">
				<div id="main">
					<select id="basic-usage-demo">
						<option value="1">开发者模式</option>
						<option value="2">用户模式</option>
						<option value="3">测试模式</option>
					</select>
					<!-- <div id="dc"></div> -->
				</div>
			</div>
			
			<div id="b" class="s">
				<div id="sb">
					<form action="${pageContext.request.contextPath}/front/developerAction!answer.action" target="_self">
						<input placeholder="人物 / 电影 / 动画 / 漫画 / 音乐 / ..." name="question" id="question" class="question" autocorrect="on" 
							autocomplete="off" maxlength="50"
							list="suggests" oninput="su()" x-webkit-speech="" required="">
						<datalist id="suggests"></datalist>
						<div id="cl">
							<div id="cb"></div>
						</div>
						<input type="submit" id="ask" value="约一下" class="s_btn">
					</form>
				</div>
			</div>
		</div>
		
		<div class="f-main">
			<div id="ec" style="text-align: center; padding-top: 20px">
				<canvas width="738" height="350" id="ex" style="margin-left: auto; margin-right: auto; cursor: pointer;">
					<ul id="exa">
						<li><a id="mes" target="_self" href="${pageContext.request.contextPath}/front/developerAction!answer.action?question=美人鱼的导演是谁？">美人鱼的导演是谁?</a></li>
						<li><a href="${pageContext.request.contextPath}/front/developerAction!answer.action?question=火影忍者的作者是谁?">火影忍者的作者是谁?</a></li>
						<li><a href="${pageContext.request.contextPath}/front/developerAction!answer.action?question=周星驰出演过哪些电影?">周星驰出演过哪些电影?</a></li>
						<li><a href="${pageContext.request.contextPath}/front/developerAction!answer.action?question=周星驰什么时候出生的?">周星驰什么时候出生的?</a></li>
						<li><a href="${pageContext.request.contextPath}/front/developerAction!answer.action?question=星爷的电影?">星爷的电影?</a></li>
						<li><a href="${pageContext.request.contextPath}/front/developerAction!answer.action?question=周杰伦和昆凌?">周杰伦和昆凌?</a></li>
						<li><a href="${pageContext.request.contextPath}/front/developerAction!answer.action?question=美人鱼导演的出生日期?">美人鱼导演的出生日期?</a></li>
						<li><a href="${pageContext.request.contextPath}/front/developerAction!answer.action?question=北京大学?">北京大学?</a></li>
						<li><a href="${pageContext.request.contextPath}/front/developerAction!answer.action?question=习近平的出生日期?">习近平的出生日期?</a></li>
						<li><a href="${pageContext.request.contextPath}/front/developerAction!answer.action?question=周杰伦是哪里的人?">周杰伦是哪里的人?</a></li>
						<li><a href="${pageContext.request.contextPath}/front/developerAction!answer.action?question=死神的作者是谁?">死神的作者是谁?</a></li>
					</ul>
				</canvas>
			</div>
		</div>
	
	<!-- tag canvas -->
	<script src="./js/answer-main-js/tagcanvas.min.js" type="text/javascript"></script>
	<!-- tag canvas -->
	<script src="./js/tag-canvas-js/tag-canvas.js"></script>
	<!-- fancy select -->
		<script src="./js/libs/jquery-2.2.3.min.js"></script>
		<script src="./js/fancy-select-js/fancy-select.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				$('#basic-usage-demo').fancySelect();
			});
		</script>
	</body>
</html>
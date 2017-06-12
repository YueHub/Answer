<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta name="keywords" content="Answer,搜索,语义理解,搜索引擎,领域本体,自然语言搜索,智能搜索,语义网,电影搜索">
<meta name="description" content="Answer是基于电影领域本体的自动问答系统，同时也会给出全文搜索引擎的搜索结果">
<meta name="renderer" content="webkit">
<meta http-equiv="Cache-Control" content="no-siteapp">
<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=0">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<link rel="stylesheet" type="text/css" href="./answer_main/answer_main_css/answer-main-css.css">
<title>Answer</title>
<script src="./answer_main/answer_main_js/tagcanvas.min.js" type="text/javascript"></script>

<!-- LOGO -->
<link rel="stylesheet" type="text/css" href="./answer_logo/css/style.css" /> 
<script src="./answer_logo/js/modernizr.custom.js"></script>
<!-- LOGO -->
<!-- 模式选择 -->
	<link rel="stylesheet" type="text/css" href="./answer_select/answer_select_css/normalize.css" />
	<link rel='stylesheet' type='text/css'>
	<link rel="stylesheet" media="screen, projection" href="./answer_select/answer_select_css/fancySelect.css">
	<style type="text/css">
		*{padding: 0;margin: 0;}
		/* div#main { font-size: 16px; line-height: 26px; color: #fff; } */
		div#main div.fancy-select { margin: 0 auto 70px; width: 110px; }
		div#main code { font-size: 16px; padding: 2px 3px; background: #5A606F; color: #fff; border-radius: 4px; font-family: "Inconsolata", monospace; }
		div#main pre { padding: 27px 27px 30px; background: #5A606F; color: #fff; border-radius: 4px; white-space: pre-wrap; max-width: 540px; line-height: 28px; margin-bottom: 30px; }
		div#main pre code { padding: 0; background: transparent; border-radius: 0; margin-bottom: 18px; }
		div#main section { width: 940px; margin: 90px auto; }
		div#main section h2, div#main section h3 { font-size: 22px; margin-bottom: 18px; font-weight: 600; }
		/*one column*/
		@media only screen and (max-width: 639px) {
			div#main section { width: 300px; }
		}
		/*two columns*/
		@media only screen and (min-width: 640px) and (max-width: 959px) {
			div#main section { width: 620px; }
		}
		/* cyrillic-ext */
		/* 按钮 */
		.s_btn {
		    width: 100px;
		    height: 40px;
		    color: white;
		    font-size: 15px;
		    letter-spacing: 1px;
		    background: #3385ff;
		    border-bottom: 1px solid #2d78f4;
		    outline: medium;
		    -webkit-appearance: none;
		    -webkit-border-radius: 0;
		}
		
		/* 按钮 */
	</style>
<!-- 模式选择 -->
</head>
<body>
<%-- 	<div class="main clearfix" style='width:300px;margin:60px auto;cursor:hand;' onclick="document.location='answer_index.jsp'">
		<div class="circle " style="float:left;" >
			<h1>Answer</h1>
		</div>
		<div style="cursor:hand;">
			<span style='font-size:60px;margin-left:30px;'>Answer</span>
		</div>
	</div> --%>
	
	<div class="logo-main" onclick="document.location='mobile_index.jsp'">
		<a href="answer_index.jsp"><img id="logo" alt="logo" src="./answer_logo/answer-logo.png"></a>
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
	
	<script type="text/javascript">
		var sps = document.getElementById('exa').children, sul = document.getElementById('suggests');
        var mc = parseInt(Math.random()*11), ce = function(){
            if(mc>=sps.length)
                mc = 0;
            var es = document.getElementById('mes');
            var qs = sps[mc].innerText||sps[mc].textContent;
            es.innerHTML = "• "+qs;
            es.setAttribute("href","/i?q="+qs);
            mc++;
        };
        ce();
        setInterval(ce,1000);
        
        // Init samples while empty ...
        for(sl = 0;sl < sps.length;sl++){
            var o = document.createElement('option');
            o.setAttribute('value',sps[sl].innerText||sps[sl].textContent);
            sul.appendChild(o);
        }
        
        window.onload = function () {
            try {
                TagCanvas.Start('ex', null, {
                    textFont: 'Verdana, Geneva, sans-serif',
                    textColour: '#494949',
                    textHeight: 18,
                    wheelZoom: false
                });
            } catch (e) {
                document.getElementById('ec').style.display = 'none';
            }
        };
        document.getElementById("i").focus();
        document.getElementById('rf').id = "f";
        document.getElementById('rfh').id = "fh";
        document.getElementById('rfc').id = "fc";
        document.getElementById('rfi').id = "fi";
       </script>
<!-- 模式选择 -->
	<script src="jquery_js/jquery-2.2.3.min.js"></script>
	<script src="./answer_select/answer_select_js/fancySelect.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			$('#basic-usage-demo').fancySelect();
		});
	</script>
<!-- 模式选择 -->
</body>
</html>
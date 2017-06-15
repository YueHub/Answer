<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
		<meta name="keywords" content="Answer,搜索,下一代搜索,搜索引擎,本体库,自然语言搜索,智能搜索">
		<meta name="description" content="Answer是由阿约奉献的基于领域本体的语义搜索引擎">
		<meta name="renderer" content="webkit">
		<meta http-equiv="Cache-Control" content="no-siteapp">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		<link rel="stylesheet" type="text/css" href="./css/answer-main-css/answer-main-css.css">
	
		<!-- 模式选择 -->
		<link rel="stylesheet" type="text/css" href="./css/answer-select-css/normalize.css" />
		<link rel="stylesheet" type="text/css" media="screen, projection" href="./css/fancy-select-css/fancy-select.css">
		<!-- 模式选择 -->
	
		<!-- arbor.js 结点联系可视化 -->
		<link rel="stylesheet" href="./css/arbor-js/mxh7kqd-e.css">
		<script type="text/javascript">try{Typekit.load();}catch(e){}</script>
		<style type="text/css">.tk-athelas{font-family:"athelas-1","athelas-2",serif;}</style>
		<script type="text/javascript" src="./js/arbor-js/mxh7kqd.js"></script>
		<script src="./js/libs/jquery-2.2.3.min.js"></script>
		<script src="./js/arbor-js/jquery.address-1.4.min.js"></script>
		<script src="./js/arbor-js/arbor.js"></script>
		<script src="./js/arbor-js/arbor-tween.js"></script>
		<script src="./js/arbor-js/arbor-graphics.js"></script>
		<script src="./js/arbor-js/semantic_graph.js"></script>
		<script src="./js/arbor-js/knowledge_graph.js"></script>
		<!-- arbor.js 结点联系可视化 -->
	
		<!-- LTP 结点关联力导图-->
		<link rel="stylesheet" href="./css/ltp-dependency/draw.css"/>
	    <style id="holderjs-style" type="text/css">.holderjs-fluid {font-size:16px;font-weight:bold;text-align:center;font-family:sans-serif;margin:0}</style>
		<script type="text/javascript" src="./js/ltp-dependency/jsobj_extension.js"></script>
		<script type="text/javascript" src="./js/ltp-dependency/draw_obj.js"></script>
		<script type="text/javascript" src="./js/ltp-dependency/draw_sent_view.js"></script>
		<script type="text/javascript" src="./js/ltp-dependency/draw_para_view.js"></script>
		<script type="text/javascript" src="./js/ltp-dependency/draw_load.js"></script>
	    <script type="text/javascript" src="../js/ltp-dependency/holder.js"></script>
		<!-- LTP 结点关联力导图-->
		
		<!-- bootstrap -->
		<script src="./js/libs/bootstrap.min.js"></script>
		<!-- bootstrap -->
		
		<!-- 图标1 -->
		<link rel="stylesheet" href="./css/answer-main-css/kinerloader2.css"/>
		<!-- 图标1 -->
		
		<!-- 图标2 -->
		<link href="./css/libs/font-awesome-4.5.0/css/font-awesome.min.css" rel="stylesheet" />
		<!-- 图标2 -->
		
		<!-- 滚动条 -->
		<!-- Custom scrollbars CSS -->
		<link href="./css/scroll-bar/jquery.mCustomScrollbar.css" rel="stylesheet" />
		<!-- 滚动条 -->
		
		<!-- 传统搜索结果  -->
		<style type="text/css">
		</style>
		<!-- 传统搜索结果 -->
	<title>Answer</title>
	</head>
	<body class="bxg" style="zoom: 1; background-color: #FFF;">
		<div id="sc" class="f">
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
							list="suggests" oninput="su()" x-webkit-speech="" value='<s:property value='#session.question'/>'>
						<datalist id="suggests"></datalist>
						<div id="cl">
							<div id="cb"></div>
						</div>
						<input type="submit" id="ask" value="约一下" class="s_btn">
					</form>
				</div>
			</div>
		</div>
		
		<!-- 右侧弹窗 -->
		<div class="htmleaf-container" style="margin:auto;width:500px;">
			<div class="htmleaf-content bgcolor-3">
				<div class="js-sidebar modal sidebar content"  id="content_1" style='height:98%;width:24%;overflow:auto; border:1px solid #000000;'>
				<s:iterator value='#session.polysemantNamedEntities' id='polysemantNamedEntity'>
					<span class="modal-close-btn"></span>
					<span style='font-style:oblique;margin-top:-20px;'>Answer知识网络</span><br/><br/>
					<!-- 实体名称 -->
					<i class="fa fa-square" style='color:#ffd800;font-size:27px;'></i>
					<span style='font-family:"楷体"; font-size:22px;font-weight:bold;'>&nbsp;<s:property value='#polysemantNamedEntity.entityName'/></span>
					<!-- 所属类 -->
					<span style='font-family:"SimHei";'>属于</span>
					<span class='lk' style='font-family:"SimHei";color:yellow;'><s:property value='#polysemantNamedEntity.ontClass'/></span>
					<div style='height:2px;width:130%;margin-left:-15%;margin-top:0px;overflow:hidden;background:#ffd800;'></div>
					
					<!-- 实体图片 -->
					<div style='text-align:center;margin-top:5px;'>
						<img src="answer_image/<s:property value='#polysemantNamedEntity.ontClass'/>/<s:property value='#polysemantNamedEntity.picSrc'/>" style='width:100px;height:100px;'></img>
						<!-- <img src="answer_image/1462945150148822.jpg" style='width:100px;height:100px;'></img> -->
					</div>
					
					<div style='margin-top:5px;width:330px;height:auto;'>
					<!-- 描述 -->
					<span style='font-family:"SimHei";'>
						&nbsp;&nbsp;&nbsp;&nbsp;<s:property value='#polysemantNamedEntity.lemmaSummary'/>
					</span>
					</div>
									
					<div style='width:320px;height:auto;height-min:50px;overflow:hidden;'>
					<i class="fa fa-thumbs-up" style='color:#ffd800;font-size:18px;'></i>
					<!-- 相关对象 -->
					<span style='font-family:LiSu;font-size:20px;'>相关对象</span><br/>
					<s:iterator value='#polysemantNamedEntity.objectProperties' id='objectProperty'>
						<s:if test="#objectProperty.value != null">
							<div class="myborder" style='text-align:center;width:49px;height:49px;float:left;margin-left:14px;margin-right:8px;margin-top:10px;margin-bottom:5px;'>
								<a class='mylk' target="_blank" style='font-family:"楷体";font-weight:bold;' href='${pageContext.request.contextPath}/front/developerAction!answer.action?question=<s:property value='#objectProperty.value'/>'>
									<s:property value='#objectProperty.value'/>
								</a>
							</div>
						</s:if>
					</s:iterator>
					</div>
					
					<!-- 主要数据属性 -->
					<div style='width:320px;height:auto;height-min:20px;overflow:hidden;'>
					<i class="fa fa-thumbs-up" style='color:#ffd800;font-size:18px;'></i>
					<span style='font-family:LiSu;font-size:20px;'>主要属性</span><br/>
					<s:iterator value='#polysemantNamedEntity.dataProperties' id='dataProperty'>
						<s:if test="#dataProperty.value != null">
						<div class="wb2 bwb" style='text-align:center;margin-top:5px;margin-right:11px;width:80px;height:20px;float:left;'>
							<span style='font-family:LiSu;'><s:property value='#dataProperty.key'/></span>
						</div>
						</s:if>
					</s:iterator>
					</div>
					
					<div style='width:320px;height:auto;height-min:50px;overflow:hidden;'>
					<i class="fa fa-thumbs-up" style='color:#ffd800;font-size:18px;'></i>
					<!-- 歧义理解 -->
					<span style='font-family:LiSu;font-size:20px;'>歧义理解</span><br/>
					<s:iterator value='#session.answerResultVO.words' id='word'>
						<s:if test='#word.name == #polysemantNamedEntity.entityName'>
							<center>
							<span style='font-family:"SimHei";color:yellow;'>
								<s:property value='#polysemantNamedEntity.entityName'/>
							</span>
							<span style='font-family:"SimHei";'>
								至少含有<span style='font-family:"SimHei";color:yellow;'><s:property value='#word.polysemantNamedEntities.size'/></span>种歧义理解
							</span>
							</center>
							<s:iterator value='#word.polysemantNamedEntities' id='polysemantNamedEntity'>
								<div style='margin-left:20px;'>
									<i class="fa fa-circle" style='color:#ffd800;font-size:12px;margin-left:-2%;'></i>
									<span style='font-size:10px;color:rgba(255, 255, 255, 0.65);'><s:property value='#polysemantNamedEntity.polysemantExplain'/></span>
								</div>
							</s:iterator>
						</s:if>
					</s:iterator>
					</div>
					
				</s:iterator>
				</div>
			</div>
		</div>
		<!-- 右侧弹窗 -->
		
		<div></div>
		
		<div class="f">
			<%-- <div class="loader duration-3s-before" style='margin-left:-5%;margin-top:5%;'>
				<a href="javascript:;"></a>
				<span style='margin-left:40px;font-size:30px;margin-bottom:10px;color:#1E90FF;'>Answer</span>
			</div> --%>
			
			<div class="bf" style='text-align:center;'>
				<s:iterator value='#session.answerResultVO.words' id='word'>
				<s:if test="#word.polysemantNamedEntities.size > 1">
					<s:property value='#word.name'/>
					至少含有
					<span style='font-size:12px;font-weight:bold;'>
						<s:property value='#word.polysemantNamedEntities.size'/>
					</span>
					种歧义理解
					Answer会根据问题自动识别
				</s:if>
				</s:iterator>
			</div>
			
			<!-- 答案 -->
			<%-- <s:if test="#session.answerResultVO.polysemantSituationVOs.size > 1"> --%>
				<s:iterator value='#session.answerResultVO.polysemantSituationVOs' id='polysemantSituationVO'>
					<s:if test="#polysemantSituationVO.queryResults.size != 0">
					<div class="bsm">
						<span style='font-style:oblique;font-size:20px;font-family:"SimHei";'>
							<i class="fa fa-cog fa-spin fa-fw" style='font-size:30px;color:#777777;'></i>
							<s:property value='#session.question'/>
						</span>
					</div>
					<div class="k" style="min-height: 54px;">
						<s:iterator value='#polysemantSituationVO.queryResults' id='queryResult' status='st1'>
							<s:iterator value='#queryResult.answers' id='answer'>
								<div class="n gr"></div>
								<div class="in">
									<span style='font-family:"SimHei";'><s:property value='#answer.content'/></span>
									&nbsp;&nbsp;
									<font class="wb bwb">
										<s:iterator value='#polysemantSituationVO.predicateDisambiguationStatements' id='predicateDisambiguationStatement' status='st2'>
											<s:if test="#st1.index == #st2.index">
												<span><s:property value='#predicateDisambiguationStatement.subject.name'/></span>
												<span><s:property value='#predicateDisambiguationStatement.predicate.disambiguationName'/></span>
											</s:if>
										</s:iterator>
									</font>
								</div>
							</s:iterator>
						</s:iterator>
					</div>
					<div class="bf" style='text-align:center;'>
						<s:iterator value='#polysemantSituationVO.activePolysemantNamedEntities' id='activePolysemantNamedEntity'>
							<s:if test="#activePolysemantNamedEntity.entityName != null">
							如果<s:property value='#activePolysemantNamedEntity.entityName'/>指的是<span style='font-size:12px;font-weight:bold;'><s:property value='#activePolysemantNamedEntity.polysemantExplain'/></span>
								<%-- <s:iterator value='#polysemantSituationVO.predicateDisambiguationStatements' id='predicateDisambiguationStatement'>
									,<s:property value='#predicateDisambiguationStatement.predicate.name'/>指的是<span style='font-size:12px;font-weight:bold;'><s:property value='#predicateDisambiguationStatement.predicate.disambiguationName'/></span>
								</s:iterator> --%>
							</s:if>
						</s:iterator>
					</div>
					</s:if>
				</s:iterator>
			<%-- </s:if> --%>
			<!-- 答案 -->
			
			
			<!-- 使用原力 绘制逻辑图谱-->
			<div id='knowledge'>
				<div class='bsm'>
					<i class='fa fa-cog fa-spin fa-fw' style='font-size:30px;color:#777777;'></i>
					<span style='font-size:20px;font-family:SimHei;'>Answer逻辑图谱</span>
				</div>
				<div class='k' style='min-height:54px;'>
					<canvas id='knowledge_graph' width='800' height='448' style='opacity: 1;'></canvas>
				</div>
				<div class='container' style='padding-left: 0;'></div>
			</div>
			<!-- 使用原力 绘制逻辑图谱-->
			
			<!-- 命名实体识别 -->
			<s:set name='flag' value='0' scope='request'></s:set>
			<s:iterator value='#session.answerResultVO.words' id='word'>
				<s:if test="#word.polysemantNamedEntities.size != 0">
					<s:iterator value='#word.polysemantNamedEntities' id='polysemantNamedEntity' status="st">
						<s:set name='flag' value='1' scope='request'></s:set>
					</s:iterator>
				</s:if>
			</s:iterator>
			<s:if test="#request.flag == 1">
			<div class="ner">
				<div class="bsm">
					<i class="fa fa-cog fa-spin fa-fw" style='font-size:30px;color:#777777;'></i>
					<span style='font-size:20px;font-family:"SimHei";'>命名实体识别</span>
				</div>
				<div class="k" style="min-height: 54px;">
					<div class="n tb"></div>
					<div class="tbx">
						<table class="bt" border="1" style='text-align:center;'>
							<tbody>
								<tr class="th">
									<th class="tn tny">实体</th>
									<th width='70%'>歧义理解</th>
									<th width='30%'>所属类</th>
								</tr>
								<s:iterator value='#session.answerResultVO.words' id='word'>
								<s:if test="#word.polysemantNamedEntities.size != 0">
								<s:iterator value='#word.polysemantNamedEntities' id='polysemantNamedEntity' status="st">
								<s:if test="#st.index % 2 == 0">
								<tr class="trh">
									<td class="tn">
										<a href="${pageContext.request.contextPath}/front/developerAction!answer.action?question=<s:property value='#word.name'/>" class="lk" target="_blank">
											<span style='font-family:"SimHei";font-weight:bold;'><s:property value='#word.name'/></span>
										</a>
									</td>
									<td>
										<span style='font-family:"SimHei";'><s:property value='#polysemantNamedEntity.polysemantExplain'/></span>
									</td>
									<td>
										<span style='font-family:"SimHei";'><s:property value='#polysemantNamedEntity.ontClass'/></span>
									</td>
								</tr>
								</s:if>
								<s:else>
								<tr>
									<td class="tn">
										<a href="${pageContext.request.contextPath}/front/developerAction!answer.action?question=<s:property value='#word.name'/>" class="lk" target="_blank">
											<span style='font-family:"SimHei";font-weight:bold;'><s:property value='#word.name'/></span>
										</a>
									</td>
									<td>
										<span style='font-family:"SimHei";'><s:property value='#polysemantNamedEntity.polysemantExplain'/></span>
									</td>
									<td>
										<span style='font-family:"SimHei";'><s:property value='#polysemantNamedEntity.ontClass'/></span>
									</td>
								</tr>
								</s:else>
								</s:iterator>
								</s:if>
								</s:iterator>
							</tbody>
						</table>
					</div>
					<div class="in" style='text-align:center;'>
						<section class="section--white">
							<div class="btn" style='font-size:17px;' data-cta-target=".js-sidebar">知识网络</div>
						</section>
					</div>
				</div>
			</div>
			</s:if>
			<!-- 命名实体识别 -->
			
				
		<div class="bsm">
		<i class="fa fa-cog fa-spin fa-fw" style='font-size:30px;color:#777777;'></i>
		<span style='font-size:20px;font-family:"SimHei";'>传统搜索结果</span>
		</div>
		<!-- 传统搜索结果 -->
		<div class="k" style="min-height: 54px;">
		<div id='b_content'>
			<ol id='b_results'>
				<li class="b_algo" style='padding:10px;'>
					<h2><a style='font-size:18px;color:#464646;' href="http://comic.qq.com/zt2013/hynaruto/index.htm" target="_blank"><strong>火影忍者naruto</strong>_腾讯动漫_腾讯网</a></h2>
					<div class="b_caption"><p style='color:#717171;font-size:14px;'>最新<strong>火影忍者</strong>漫画，最大的<strong>火影忍者</strong>fans聚集地，腾讯
						<strong>火影忍者</strong>专区! ... 分享按钮不再出现？确定取消</p>
						<div class="b_attribution" u="4|5056|4690032837527202|o_oSYauBhz5Ux67nuq8NPrGPYkETsHki">
							<cite style='font-size:13px;color:#AFAFAF;'>comic.qq.com/zt2013/hy<strong>naruto</strong>/index.htm</cite>
							<a href="http://cn.bing.com/search?q=%E7%81%AB%E5%BD%B1%E5%BF%8D%E8%80%85&amp;go=%E6%8F%90%E4%BA%A4&amp;qs=n&amp;form=QBLH&amp;pq=%E7%81%AB%E5%BD%B1%E5%BF%8D%E8%80%85&amp;sc=8-4&amp;sp=-1&amp;sk=&amp;cvid=CB53F4CC4AC24E20BCC6047EEE8CF2B0#" aria-label="此网站的操作" aria-haspopup="true"><a href="#" aria-label="此网站的操作" aria-haspopup="true"><span class="c_tlbxTrg"><span class="c_tlbxTrgIcn sw_ddgn"></span><span class="c_tlbxTrgIcn sw_ddgn"></span><span class="c_tlbxH" h="BASE:CACHEDPAGEDEFAULT" k="SERP,5199.1">
							</span>
							</span>
							</a>
							</a>
						</div>
					</div>
				</li>
			</ol>
		</div>
		</div>
		<div class="bf" style='text-align:center;margin-top:20px;'>传统搜索结果来自必应</div>
		<!-- 传统搜索结果 -->
		
			
			<!-- 使用原力 语义图构建-->
			<div id='semantic'>
				<div class="bsm">
				<i class="fa fa-cog fa-spin fa-fw" style='font-size:30px;color:#777777;'></i>
				<span style='font-size:20px;font-family:"SimHei";'>Answer语义图构建</span>
				</div>
				<div class="k" style="min-height:54px;">
					<canvas id="semantic_graph" width="800" height="348" style="opacity: 1;"></canvas>
				</div>
				<div class="container" style="padding-left: 0; "></div>
			</div>	
			<!-- 使用原力 语义图构建-->
		
			<!-- 分词结果 -->
			<div class="bsm">
			<i class="fa fa-cog fa-spin fa-fw" style='font-size:30px;color:#777777;'></i>
			<span style='font-size:20px;font-family:"SimHei";'>分词结果</span>
			
			</div>
			<div class="k" style="min-height:54px;">
				<div class="in">
					<s:iterator value='#session.answerResultVO.words' id="word">
						<font class="wb bwb"><s:property value='#word.name'/>/<s:property value='#word.cpostag'/></font>&nbsp;&nbsp;&nbsp;&nbsp;
					</s:iterator>
				</div>
			</div>
			<!-- 分词结果 -->
		
			<!-- 依存关系分析画图 -->
			<div class="bsm">
				<i class="fa fa-cog fa-spin fa-fw" style='font-size:30px;color:#777777;'></i>
				<span style='font-size:20px;font-family:"SimHei";'>依存文法分析</span>
			</div>
			<div class=" k">
	        <div class="row-fluid">
	            <div id="mask" class="span12" style="display: none;">
	                <div id="loadingTag" style="display: block; left: 380.5px;"> <!-- At First , it hidden -->
	                    <img width="28px" height="28px" src="./css/ltp-dependency/ajax_loader.gif">
	                    <span class="text-info">分析中...如果长时间无反应，请点击<code>分析</code>重试.</span>
	                </div>
	            </div>
	        </div>
	        <div id="analysisPanel" style="display: block;">
		        <div class="row-fluid">
		            <div class="span8 clearfix" id="disableAttrPanel">
		                <!-- <label class="checkbox inline">
		                    <input type="checkbox" value="postag-enable" checked="">
		                    	词性标注1
		                </label>
		                <label class="checkbox inline">
		                    <input type="checkbox" value="ner-enable" checked="">
		                    	命名实体
		                </label>
		                <label class="checkbox inline">
		                    <input type="checkbox" value="dp-enable" checked="">
		                  		  句法分析
		                </label>
		                <label class="checkbox inline">
		                    <input type="checkbox" value="srl-enable" checked="">
		                		    语义角色标注
		                </label>
		                <label class="checkbox inline">
		                    <input type="checkbox" value="sdp-enable" checked="">
		                   		 语义依存分析
		                </label> -->
		            </div>
		        </div>
		        <div class="row-fluid">
		            <div class="span12">
		                <div class="analysis-wrapper">
		                    <ul id="analysisContent">
		                     <li>
		                      <div class="canvasContainer" style="height: 0px;">
		                      	<canvas id="" width="700" height="300" style="cursor: default;"></canvas>
		                      </div>
		                     </li>
		                    </ul>
		                </div>
		            </div>
		        </div>
	        </div>
	    </div>
		<!-- 依存关系分析画图 -->
			
			<!-- 消岐 -->
			<%-- <s:if test="#session.answerResultVO.polysemantSituationVOs.size > 1"> --%>
			<div id='disambiguation'>
			
			<div class="bsm">
				<i class="fa fa-cog fa-spin fa-fw" style='font-size:30px;color:#777777;'></i>
				<span style='font-size:20px;font-family:"SimHei";'>消岐</span>
			</div>
			
			<s:iterator value='#session.answerResultVO.polysemantSituationVOs' id='polysemantSituationVO'>
				<div class="k" style="min-height: 54px;">
					<s:iterator value='#polysemantSituationVO.individualsDisambiguationStatements' id='individualsDisambiguationStatement'>
						<s:if test="#individualsDisambiguationStatement.subject.disambiguationName != null">
						<div class="n gr"></div>
						<div class="in">
							<font class="wb">
								<s:property value='#individualsDisambiguationStatement.subject.name'/>
							</font>
							&nbsp;&nbsp;&nbsp;消岐为&nbsp;&nbsp;&nbsp;
							<font class="wb bwb">
								<s:property value='#individualsDisambiguationStatement.subject.disambiguationName'/>
							</font>
						</div>
						</s:if>
					</s:iterator>
						
					<s:iterator value='#polysemantSituationVO.predicateDisambiguationStatements' id='predicateDisambiguationStatement'>
						<s:if test="#predicateDisambiguationStatement.predicate.disambiguationName != null">
						<div class="n gr"></div>
						<div class="in">
							<font class="wb">
								<s:property value='#predicateDisambiguationStatement.predicate.name'/>
							</font>
							&nbsp;&nbsp;&nbsp;消岐为&nbsp;&nbsp;&nbsp;
							<font class="wb bwb">
							<s:property value='#predicateDisambiguationStatement.predicate.disambiguationName'/>
							</font>
						</div>
						</s:if>
					</s:iterator>
				</div>
				<div class="bf" style='text-align:center;'>
				<s:iterator value='#polysemantSituationVO.activePolysemantNamedEntities' id='activePolysemantNamedEntity' status='st'> 
					<s:if test="#activePolysemantNamedEntity.entityName != null && #st.index == 0">
					如果<s:property value='#activePolysemantNamedEntity.entityName'/>指的是<span style='font-size:12px;font-weight:bold;'><s:property value='#activePolysemantNamedEntity.polysemantExplain'/></span>
						<%-- <s:iterator value='#polysemantSituationVO.predicateDisambiguationStatements' id='predicateDisambiguationStatement'>
							,<s:property value='#predicateDisambiguationStatement.predicate.name'/>指的是<span style='font-size:12px;font-weight:bold;'><s:property value='#predicateDisambiguationStatement.predicate.disambiguationName'/></span>
						</s:iterator> --%>
					</s:if>
				</s:iterator>
				</div>
			</s:iterator>
			</div>
			<%-- </s:if> --%>
			<!-- 消岐 -->
			
		<div style="height: 10px; display: block;"></div>
		<%-- <script language="javascript" type="text/javascript">
			/* window.parent.postMessage('magibox-answer:true', '*'); */
			var lastHeight = -1;
			function heightReport() {
				if (true) {
					var boxheight = document.querySelector('html').offsetHeight;
					if (lastHeight != boxheight) {
						window.parent.postMessage('magibox-resize:' + boxheight, '*');
						lastHeight = boxheight;
					}
				}
			}
			setInterval('heightReport()', 32);
		</script> --%>
		
		<!-- 模式选择 -->
		<script src="./js/libs/jquery-2.2.3.min.js"></script>
		<script src="./js/fancy-select-js/fancy-select.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				$('#basic-usage-demo').fancySelect();
			});
		</script>
		<!-- 模式选择 -->
		<!-- 右侧弹窗 -->
		<script src="./js/cta/cta.js"></script>
			<script>
			var closeFn;
			function closeShowingModal() {
				var showingModal = document.querySelector('.modal.show');
				if (!showingModal) return;
				showingModal.classList.remove('show');
				document.body.classList.remove('disable-mouse');
				if (closeFn) {
					closeFn();
					closeFn = null;
				}
			}
			document.addEventListener('click', function (e) {
				var target = e.target;
				if (target.dataset.ctaTarget) {
					closeFn = cta(target, document.querySelector(target.dataset.ctaTarget), { relativeToWindow: true }, function showModal(modal) {
						modal.classList.add('show');
						document.body.classList.add('disable-mouse');
					});
				}
				else if (target.classList.contains('modal-close-btn')) {
					closeShowingModal();
				}
			});
			document.addEventListener('keyup', function (e) {
				if (e.which === 27) {
					closeShowingModal();
				}
			})
			</script>
		<!-- 右侧弹窗 -->
		
		<!-- 滚动条 -->
		<!-- custom scrollbars plugin -->
		<script src="./js/scroll-bar/jquery.mCustomScrollbar.concat.min.js"></script>
		<script>
			(function($){
				$(window).load(function(){
					$("#content_1").mCustomScrollbar({
						set_width:false, /*optional element width: boolean, pixels, percentage*/
						set_height:false, /*optional element height: boolean, pixels, percentage*/
						horizontalScroll:false, /*scroll horizontally: boolean*/
						scrollInertia:450, /*scrolling inertia: integer (milliseconds)*/
						mouseWheel:true, /*mousewheel support: boolean*/
						mouseWheelPixels:"auto", /*mousewheel pixels amount: integer, "auto"*/
						autoDraggerLength:true, /*auto-adjust scrollbar dragger length: boolean*/
						autoHideScrollbar:false, /*auto-hide scrollbar when idle*/
						scrollButtons:{ /*scroll buttons*/
							enable:false, /*scroll buttons support: boolean*/
							scrollType:"continuous", /*scroll buttons scrolling type: "continuous", "pixels"*/
							scrollSpeed:"auto", /*scroll buttons continuous scrolling speed: integer, "auto"*/
							scrollAmount:40 /*scroll buttons pixels scroll amount: integer (pixels)*/
						},
						advanced:{
							updateOnBrowserResize:true, /*update scrollbars on browser resize (for layouts based on percentages): boolean*/
							updateOnContentResize:false, /*auto-update scrollbars on content resize (for dynamic content): boolean*/
							autoExpandHorizontalScroll:false, /*auto-expand width for horizontal scrolling: boolean*/
							autoScrollOnFocus:true, /*auto-scroll on focused elements: boolean*/
							normalizeMouseWheelDelta:false /*normalize mouse-wheel delta (-1/1)*/
						},
						contentTouchScroll:true, /*scrolling by touch-swipe content: boolean*/
						callbacks:{
							onScrollStart:function(){}, /*user custom callback function on scroll start event*/
							onScroll:function(){}, /*user custom callback function on scroll event*/
							onTotalScroll:function(){}, /*user custom callback function on scroll end reached event*/
							onTotalScrollBack:function(){}, /*user custom callback function on scroll begin reached event*/
							onTotalScrollOffset:0, /*scroll end reached offset: integer (pixels)*/
							onTotalScrollBackOffset:0, /*scroll begin reached offset: integer (pixels)*/
							whileScrolling:function(){} /*user custom callback function on scrolling event*/
						},
						theme:"light" /*"light", "dark", "light-2", "dark-2", "light-thick", "dark-thick", "light-thin", "dark-thin"*/
					});
				});
			})(jQuery);
		</script>
		<!-- 滚动条 -->
		
		<!-- 传统搜索结果 -->
		<script type="text/javascript">
		$(function() {
	   		var $question = $('#question');
	        var params = {
	            // Request parameters
	            "q": $question.val(),
	            "count": "10",
	            "offset": "0",
	            "mkt": "en-us",
	            "safesearch": "Moderate",
	        };
	        $.ajax({
	            url: "https://bingapis.azure-api.net/api/v5/search/?" + $.param(params),
	            beforeSend: function(xhrObj){
	                // Request headers 767fb82fcabf4c48badea4a21ea4927d
	                xhrObj.setRequestHeader("Ocp-Apim-Subscription-Key","767fb82fcabf4c48badea4a21ea4927d");
	            },
	            type: "GET",
	            // Request body
	            data: "{body}",
	        })
	        .done(function(data) {
	        	var b_results = document.getElementById("b_results"); 
	        	var webPages = data.webPages;
	        	var value = webPages.value;
	        	var sum = "";
	        	for(var i = 0; i < value.length; i++) {
	        		var li = "<li class='b_algo' style='padding:10px;'>"
						+ "<h2>"
	    					+ "<a style='font-size:18px;color:#464646;' href='" + value[i].url + "' target='_blank' h='ID=SERP,5198.1'>"
							+ "<strong>"+value[i].name+"</strong></a>"
						+ "</h2>"
						+ "<div class='b_caption'>"
						+ "<p style='color:#717171;font-size:14px;'>" + value[i].snippet + "</p>"
						+ "<div class='b_attribution' u='4|5056|4690032837527202|o_oSYauBhz5Ux67nuq8NPrGPYkETsHki'>"
						+ "<cite style='font-size:13px;color:#AFAFAF;'>"+value[i].displayUrl+"</cite>"
						+ "<a href='http://cn.bing.com/search?q=%E7%81%AB%E5%BD%B1%E5%BF%8D%E8%80%85&amp;go=%E6%8F%90%E4%BA%A4&amp;qs=n&amp;form=QBLH&amp;pq=%E7%81%AB%E5%BD%B1%E5%BF%8D%E8%80%85&amp;sc=8-4&amp;sp=-1&amp;sk=&amp;cvid=CB53F4CC4AC24E20BCC6047EEE8CF2B0#' aria-label='此网站的操作' aria-haspopup='true'>"
						+ "<a href=''#' aria-label='此网站的操作' aria-haspopup='true'>"
							+ "<span class='c_tlbxTrg'>"
								+ "<span class='c_tlbxTrgIcn sw_ddgn'></span>"
								+ "<span class='c_tlbxTrgIcn sw_ddgn'></span>"
								+ "<span class='c_tlbxH' h='BASE:CACHEDPAGEDEFAULT' k='SERP,5199.1'></span>"
							+ "</span>"
						+ "</a>"
						+ "</a>"
						+ "</div>"
					+ "<div style='height:1px;width:100%;margin-top:20px;overflow:hidden;background:rgba(0, 0, 0, 0.28);'></div>"
					+ "</div>"
					+ "</li>";
	        		sum += li;
	        	}
	        	$(b_results).append(sum);
	        })
	        .fail(function() {
	            alert("必应搜索 API 调用失败");
	        });
		 });
		</script>
		<!-- 传统搜索结果 -->
		
	</body>
</html>

var returnAnalysisRst = null ; // global json object storing the ltp server return value

$(document).ready(function () {
    //-----main view-----
	var analysisBtn, // the button for active analysis
	// analysis = null, // function to start analysis
    readySentView = null , // function to set the sent view 
    readyXmlView = null ,
    readyParaView = null ,
	maskObj = document.getElementById("mask"),
	manualObj = document.getElementById("usingManual"),
	loadingObj = document.getElementById("loadingTag"),
    loadingOriHtml = loadingObj.innerHTML ,
	analysisPanelObj = document.getElementById("analysisPanel"),
	setLoadingPos = null, // function to set loading tag position
    /*SELECT_SENTS = {
        "singleSentence" : "我们即将以昂扬的斗志迎来新的一年。\n国内专家学者40余人参加研讨会。\n在家禽摊位中，有一个摊位专卖乌骨鸡。\n徐先生还具体帮助他确定了把画雄鹰、松鼠和麻雀作为主攻目标。" ,
    } , */
    sentSelectDomObj = document.getElementById("sentSelect") ,
    textareaEnterNum = 0 ,
    getEnterNumFromStr = null ; // function to get Enter number
	var targetURL = "http://localhost:8080/Answer/front/developerAction!getDependencyGraph.action"
        //ajax
        $.ajax({
            url : targetURL,
            type : "POST",
            async : true,
            dataType : "json", 
            //dataType : "jsonp",
            timeout : 10000 ,
            success : function (returnVal) {
				var dependencyNodes = returnVal.dependencyNodes;
				var returnAnalysisRstStr = "[[" + JSON.stringify(dependencyNodes) + "]]"
				returnAnalysisRst =  eval("("+returnAnalysisRstStr+")")
				//returnAnalysisRst = dependencyNodes
                //sent view update
                readySentView() ;
            },
            error : function (errorInfo , errorType) {
                console.log(errorInfo);
                loadingObj.innerHTML = ['<p class="text-error">' ,
                                        '发送分析请求失败！请点击<code>分析</code>重试.' ,
                '</p>'].join('') ;
                loadingObj.innerHTML += '<p>' + errorType ;
                loadingObj.innerHTML += errorInfo.responseText ;
            }
        });
	
    readySentView = function(){
        initDom(DRAW_PARENT_ID, returnAnalysisRst); //init the sent view
        //update UI ! it is necessary to update it before drawing the canvas
        maskObj.style.display = "none";
        analysisPanel.style.display = "block";
        //active the sent view
        /*$("#sent-tab").tab('show');*/
        //! `lastOpendEle` is a global variable ! For switch animating ! 
        //~ first , make the first text item be the lastOpenedEle to init the draw state
        lastOpenedEle = document.getElementById(DRAW_PARENT_ID).getElementsByTagName("DIV")[0];
        //! draw canvas for first line . 
        drawCanvas(lastOpenedEle);
        //! set label explanation panel content . according to current demo draw data .
        /*updateLabelExplanationPanelContent() ;*/
        //! important ! subsequent switching animate rely on this setting
        lastOpenedEle.parentNode.getElementsByTagName("CANVAS")[0].parentNode.style.height = 200 + "px"; 
    }
    
    readyParaView = function(){
        // has build a bad function ! - -
        selectParaPartToDrawByValue($("input[name=paraDisItem]:checked").val());
    }
    /*readyXmlView = function(returnJsonObj){
        var xmlDOM = LTPRstParseJSON2XMLDOM(returnJsonObj) ,
            str = parseXMLDOM2String(xmlDOM) ,
            formatedStr = formatDOMStrForDisplay(str) ;
        $("#xml_area").val(formatedStr) ;
    }*/

	setLoadingPos = function () {
		var parentNode = maskObj,
		pWidth = parentNode.offsetWidth,
		left ;
		if (pWidth != 0) {
			left = (pWidth - loadingObj.offsetWidth) / 2;
			loadingObj.style.left = left + "px";
		}
	}
	//---------------Event Bind----------------
	/*analysisBtn.bind("click", analysis);*/
	$("#viewTab a").click(function (e) {
		e.preventDefault() ;
		$(this).tab('show');
        var targetId = e.target.getAttribute("id") ;
        //! If change to other tab except for sent-tab , we close the panel to restore the main content . 
        if(targetId != "sent-tab") labelExplanationPanel.closePanel() ;
        if(targetId == "para-tab") selectParaPartToDrawByValue($("input[name=paraDisItem]:checked").val());
	});
    
    $(sentSelectDomObj).bind("change" , function(){
        $("#inputText").val(SELECT_SENTS[$(this).val()]) ;
    })
    
   /* $("#load-local-xml-btn").bind("click" , function(e){
        var localXML = parseString2XMLDOM($("#xml_area").val()) ,
            localJson = LTPRstParseXMLDOM2JSON( localXML ) ;
        if(localJson != [] && localJson != undefined){
            returnAnalysisRst = localJson ;
            readySentView() ;
        }        
        return false ;    
    })*/
    $(window).bind("resize" , function(e){
        if(demo != null){
            demo.move(0,0) ;
        }
        setLoadingPos() ;
    }) ;
	//----------------First Call--------------------------
	//analysis() ;
	initNerIntro();
    $("#inputText").val(SELECT_SENTS[sentSelectDomObj.value]) ;
});
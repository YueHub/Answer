function Demo(canvasId) {
    this.canvasId = canvasId ;
	this.canvas = null ;
	this.drawStruct = null;
	this.canvasBufs = null;
	this.imageData = null;
	this.undefined = undefined;
    this.offset = {} ;
    this.eventModule = {} ;
    this.canvasHeight = 200 ;
}
Demo.prototype = {
	
    /*******************************
    * PRE PROCESING JSON DATA to DRAWING DATA
    *******************************/
    processNerNode : function (neVal, idx, drawStruct) {
		// ner 's values set : {O , {B|M|E|S}-{Ni,Nh,Ns}} , where value 'O' stands for None ! {B|M|E|S} stands for the ner node position , while
		//{Ni , Nh , Ns} represent the institution , human , site
		if (neVal == undefined)
			return;
		var neValParts = neVal.toLowerCase().split("-");
		if (neValParts.length == 2) {
			switch (neValParts[0]) {
			case "s":
				drawStruct.ner.push({
					"neName" : neValParts[1],
					"startIdx" : idx,
					"endIdx" : idx
				});
				break;
			case "b":
				drawStruct.ner.push({
					"neName" : neValParts[1],
					"startIdx" : idx
				});
				break;
			case "e":
				drawStruct.ner[drawStruct.ner.length - 1]["endIdx"] = idx;
				break;
			default:
			}
		}
	},
	processSrlNode : function (idx, arg, drawStruct) {
		if (arg.length == 0)
			return; // if node has no arg , the arg should be a empty array
		else{
            for(var i = 0 ; i < arg.length ; i++){
                arg[i]['beg'] = parseInt(arg[i]['beg']) ;
                arg[i]['end'] = parseInt(arg[i]['end']) ; // fix the BUG when the arg.beg and arg.end is the numeric string
			    arg[i]['id'] = parseInt(arg[i]['id']) ;
            }
            drawStruct.srl.push({
				"idx" : idx,
				"arg" : arg
			});
        }
	},
	initDrawStruct : function (sentObj) {
		var canvas = document.createElement("canvas"),
		cxt = canvas.getContext("2d"),
		i,
		preTextWidth,
		curContPos;
		this.drawStruct = {
			posInfo : [],
			texts : [],
			postag : [],
			ner : [],
			srl : [],
			dp : [],
            sdp : [] ,
			WS_INTERVAL : 40,
            WS_Y_START : 0 , // for make the ws line align in the view vertical center .
			width : 0, // init below according to the word nums 
			height : 5000 , // to make it big enough
			NER_DISCONF : {
				"ni" : {
					"bgcolor" : "#99ffff",
					"cnName" : "机构"
				},
				"nh" : {
					"bgcolor" : "#99ff99",
					"cnName" : "人名"
				},
				"ns" : {
					"bgcolor" : "#ccff66",
					"cnName" : "地名"
				}
			},
			SRL_DISCONF : {
				"bgColor" : "#ffec8b",
				"lineColor" : "#eeeeee",
				"textColor" : "#6f8ca0",
				"lineWidth" : 5 ,
                "perHeight" : 16 ,
                "hInterval" : 4 
			},
			DP_DISCONF : {
				"lineColor" : "blue",
				"textColor" : "red"
			} ,
            SDP_DISCONF : {
                "lineColor" : "green" ,
                "textColor" : "red"
            } ,
            ALL_ELEMENTS_HIGHT_CONF : {
                'WS' : 100 ,
                'POSTAG' : 50 , 
                'NER' : 50 ,
                'SRL' : 500 , // to make it big enough when initialization .
                'DP' : 5000 ,
                'SDP' : 5000
            }

		},
		this.offset = {
			x : 0,
			y : 0
		} ,
		this.eventModule = {
			startX : 0,
			startY : 0,
			hasDown : false
		};
		this.drawStruct.texts.push("Root"); // ---- it is not correct ! the "Root" is drawed at DP while not at WS !! but it become difficult to modify it - -
		this.drawStruct.posInfo.push(0);
		this.drawStruct.postag.push(""); // For align the texts and posInfo , push a empty str to the postag array
		cxt.save();
		this.setTextFont(cxt);
		for (i = 0; i < sentObj.length; i++) {
			//process posInfo
			preTextWidth = cxt.measureText(this.drawStruct.texts[i]).width; // word's length , measured under the canvas context 
			curContPos = this.drawStruct.posInfo[i] + preTextWidth + this.drawStruct.WS_INTERVAL; // previous position + word's length + interval
			this.drawStruct.texts.push(sentObj[i].cont);
			this.drawStruct.posInfo.push(curContPos);
			//process postag
			this.drawStruct.postag.push(sentObj[i].pos);
			//process ner
			this.processNerNode(sentObj[i].ne, i + 1, this.drawStruct);
			//process srl
			this.processSrlNode(i, sentObj[i]["arg"], this.drawStruct); // do not (i+1) !!
			//process dp
			this.drawStruct.dp.push({
				"from" : parseInt(sentObj[i].parent),
				"to" : parseInt(i) ,
				"relate" : sentObj[i].relate
			});
            this.drawStruct.sdp.push({
                "from" : parseInt(sentObj[i].semparent) ,
                "to" : parseInt(i) ,
                "relate" : sentObj[i].semrelate
            })
		}
		// we need know the last Word 's length, so add a virtual node at last
        // because the posInfo[-1] stands for the last word drawing starting position ! we should get the end pos ! 
        // so starting position + last word's width ( + interval , it also can be omitted)
		preTextWidth = cxt.measureText(this.drawStruct.texts[i]).width;
		curContPos = this.drawStruct.posInfo[i] + preTextWidth + this.drawStruct.WS_INTERVAL;
		this.drawStruct.posInfo.push(curContPos);

		// set the big canvas ' width : set to the last ws node pos
		this.drawStruct.width = curContPos;
        
        cxt.restore();
		canvas = null; // release the reference
	},
    /*******************************
    * DRAWING TOOLS FUNCTIONS
    ********************************/
	setTextFont : function (cxt) {
		cxt.font = "12px Microsoft YaHei,SimSun,STSong";
	},
	setEnTextFont : function (cxt) {
		cxt.font = "12px Arial,Courier New";
	},
	setTextFillStyle : function (cxt) {
		cxt.fillStyle = "black";
	},
	getFontSizeFromFontstr : function(fontstr) {
		// input : fontstr = "12px SimSun"
		var parts = fontstr.split(""),
		idx;
		for (var i = 0; i < parts.length; i++) {
			if ((idx = parts[i].indexOf("px")) != -1) {
				return parts[i].slice(0, idx);
			}
		}
		return 12;
	},
	getTextLineheightAndPaintHeight : function (fontSize) {
		var verticalMargin,
		lineheight,
		paintHeight;
		verticalMargin = fontSize * 0.25;
		paintHeight = fontSize + 2 * verticalMargin;
		lineheight = fontSize + verticalMargin;
		return {
			"lineheight" : lineheight,
			"paintHeight" : paintHeight
		};
	},
	setCanvasAppropriateHeight : function (canvas, startY, height) {
		var cxt = canvas.getContext("2d"),
		imageData;
		imageData = cxt.getImageData(0, startY, canvas.width, height);
		canvas.height = height;
		cxt.putImageData(imageData, 0, 0);
	},
	setOffset : function (x, y) {
		this.offset.x = x;
		this.offset.y = y;
	},
    updateOffset : function(x , y){
        this.offset.x += x ;
        this.offset.y += y ;
    } , 
	createCanvasBuffer : function (w, h) {
		var canvasBuf = document.createElement("canvas");
		canvasBuf.width = w;
		canvasBuf.height = h;
		return canvasBuf;
	},
    /******************************
    * DRAWING FUNCTIONS FOR LTP RESULT : WS , POS-TAG , NER , DP , SRL , SDP
    *******************************/
	drawWS : function (drawStruct) {
		if (drawStruct.texts.length == 0)
			return null;
		var canvas = this.createCanvasBuffer(drawStruct.width, drawStruct.ALL_ELEMENTS_HIGHT_CONF['WS']),
		cxt,
		i,
		heightObj;
		cxt = canvas.getContext("2d");
		cxt.save();
		this.setTextFont(cxt);
		this.setTextFillStyle(cxt);
		fontSize = this.getFontSizeFromFontstr(cxt.font);
		heightObj = this.getTextLineheightAndPaintHeight(fontSize);
		//draw ws
		for (i = 0; i < drawStruct.texts.length; i++) {
			cxt.fillText(drawStruct.texts[i], drawStruct.posInfo[i], heightObj.lineheight);
		}
		cxt.restore();
		this.setCanvasAppropriateHeight(canvas, 0, heightObj.paintHeight);
        drawStruct.ALL_ELEMENTS_HIGHT_CONF['WS'] = heightObj.paintHeight ;
		return canvas;
	},

	drawPOSTAG : function (drawStruct) {
		if (drawStruct.postag.length == 0)
			return null;
		var canvas = this.createCanvasBuffer(drawStruct.width, drawStruct.ALL_ELEMENTS_HIGHT_CONF['POSTAG']),
		cxt,
		i,
		heightObj,
		fontSize;
		cxt = canvas.getContext("2d");
		cxt.save();
		this.setEnTextFont(cxt);
		this.setTextFillStyle(cxt);
		cxt.textAlign = "center";
		fontSize = this.getFontSizeFromFontstr(cxt.font);

		heightObj = this.getTextLineheightAndPaintHeight(fontSize);

		for (i = 1; i < drawStruct.postag.length; i++) { // skip "Root" 's postag
			//we need to calculate the center of the position which the corresponding WS lay at .
			var centerPos = (drawStruct.posInfo[i] + (drawStruct.posInfo[i + 1] - drawStruct.WS_INTERVAL)) / 2;
			cxt.fillText(drawStruct.postag[i], centerPos, heightObj.lineheight);
		}
		cxt.restore();
		this.setCanvasAppropriateHeight(canvas, 0, heightObj.paintHeight);
        drawStruct.ALL_ELEMENTS_HIGHT_CONF['POSTAG'] = heightObj.paintHeight ;
		return canvas;
	},

	drawNerNode : function (nerNode, drawStruct, cxt) {
		var bgColor,
		cnNeName,
		color = "#000000",
		paintX,
		paintY = 0,
		paintWidth,
        heightObj ,
		paintHeight,
		confSet = drawStruct.NER_DISCONF,
		fontSize,
		fontBaselineHeight,
		fontX;
		//draw bg
		cxt.beginPath();
		bgColor = confSet[nerNode.neName] == undefined ? "#0099cc" : confSet[nerNode.neName].bgcolor;
		cnNeName = confSet[nerNode.neName] == undefined ? "实体名" : confSet[nerNode.neName].cnName;
		cxt.fillStyle = bgColor;
		paintX = drawStruct.posInfo[nerNode.startIdx] - drawStruct.WS_INTERVAL / 2;
		paintWidth = drawStruct.posInfo[nerNode.endIdx + 1] - drawStruct.posInfo[nerNode.startIdx];
		
		fontSize = this.getFontSizeFromFontstr(cxt.font);
        heightObj = this.getTextLineheightAndPaintHeight(fontSize);
		
        paintHeight = heightObj.paintHeight ;
        cxt.fillRect(paintX, paintY, paintWidth, paintHeight);
		
        //draw text
		
        this.setTextFillStyle(cxt);
		this.setTextFont(cxt);
		fontBaselineHeight = paintY + (paintHeight - fontSize)/2 + fontSize  - 1 ; // -1 is tricky
		fontX = paintX + paintWidth / 2;
		cxt.textAlign = "center";
		cxt.fillText(cnNeName, fontX, fontBaselineHeight);

		return paintY + paintHeight;

	},
	drawNER : function (drawStruct) {
		if (drawStruct.ner.length == 0)
			return null;
		var canvas = this.createCanvasBuffer(drawStruct.width, drawStruct.ALL_ELEMENTS_HIGHT_CONF['NER']),
		cxt = canvas.getContext("2d"),
		i,
		height = canvas.height ;
		cxt.save();
		for (i = 0; i < drawStruct.ner.length; i++) {
			height = this.drawNerNode(drawStruct.ner[i], drawStruct, cxt);
		}
		cxt.restore();
        this.setCanvasAppropriateHeight(canvas, 0, height);
        drawStruct.ALL_ELEMENTS_HIGHT_CONF['NER'] = height ;
		return canvas;
	},
	drawSrlNode : function drawSrlNode(drawIdx, srlNode, drawStruct, cxt) {

		var lineStartPos,
		lineEndPos,
        startIdx ,
        endIdx ,
		lineY,
		textX,
		textY,
		paintY,
		paintHeight = drawStruct.SRL_DISCONF['perHeight'],
		paintInterval = drawStruct.SRL_DISCONF['hInterval'],
		roundRectRadius = 6,
		paintX,
		argLen = srlNode.arg.length;
		paintY = drawIdx * (paintHeight + paintInterval);
		//first , draw line
		cxt.save();
		// Atention ! the arg[0].beg may not be the first of the line . Because the `verb` may be the first
        startIdx = srlNode.idx < srlNode.arg[0].beg ? srlNode.idx : srlNode.arg[0].beg ;
        lineStartPos = drawStruct.posInfo[startIdx  + 1] - drawStruct.WS_INTERVAL / 2;
        // Atention! the arg[-1].end may not be the end of the line . because the `verb` may be the last
        endIdx = srlNode.idx > srlNode.arg[argLen -1].end ? srlNode.idx : srlNode.arg[argLen -1].end ;
		lineEndPos = drawStruct.posInfo[endIdx + 1 + 1] - drawStruct.WS_INTERVAL / 2; // first +1 because the posInfo has a more node for "Root" ,
		//second +1 for the right position is in the next node
		lineY = paintY + paintHeight - drawStruct.SRL_DISCONF.lineWidth;
		cxt.strokeStyle = drawStruct.SRL_DISCONF.lineColor;
		cxt.lineWidth = drawStruct.SRL_DISCONF.lineWidth;
		cxt.beginPath();
		cxt.moveTo(lineStartPos, lineY);
		cxt.lineTo(lineEndPos, lineY);
		cxt.closePath();
		cxt.stroke();
		cxt.restore();
		//draw text (verb)
		cxt.fillStyle = drawStruct.SRL_DISCONF.textColor;
		cxt.textAlign = "center";
		this.setTextFont(cxt);
		textX = (drawStruct.posInfo[srlNode.idx + 1] + drawStruct.posInfo[srlNode.idx + 1 + 1] - drawStruct.WS_INTERVAL) / 2;
		textY = paintY + this.getFontSizeFromFontstr(cxt.font);
		cxt.fillText(drawStruct.texts[srlNode.idx + 1], textX, textY);

		//draw semantic role
		// the english 's font should be another
		this.setEnTextFont(cxt);
		for (var i = 0; i < argLen; i++) {
			//draw round rect
			cxt.fillStyle = drawStruct.SRL_DISCONF.bgColor;
			var x = drawStruct.posInfo[srlNode.arg[i].beg + 1] - drawStruct.WS_INTERVAL / 2,
			y = paintY,
			w = drawStruct.posInfo[srlNode.arg[i].end + 1 + 1] - drawStruct.posInfo[srlNode.arg[i].beg + 1],
			h = paintHeight;
			cxt.beginPath();
			cxt.roundRect(x, y, w, h, roundRectRadius);
			cxt.closePath();
			cxt.fill();
			//draw text
			this.setTextFillStyle(cxt);
			cxt.fillText(srlNode.arg[i].type.toUpperCase(), x + w / 2, textY);
		}

		//return the canvas Height
		return paintY + paintHeight;
	},
	drawSRL : function (drawStruct) {
		if (drawStruct.srl.length == 0)
			return null;
		var canvas = this.createCanvasBuffer(drawStruct.width, drawStruct.ALL_ELEMENTS_HIGHT_CONF['SRL']),
		cxt = canvas.getContext("2d"),
		i,
		height = canvas.height ;
		cxt.save();
		for (i = 0; i < drawStruct.srl.length; i++) {
			height = this.drawSrlNode(i, drawStruct.srl[i], drawStruct, cxt);
		}
		cxt.restore();
		this.setCanvasAppropriateHeight(canvas, 0, height);
        drawStruct.ALL_ELEMENTS_HIGHT_CONF['SRL'] = height ;
		return canvas;
	},
	drawDpNode : function (dpNode, drawStruct, cxt , dp_type) {
		var fromCenter,
		fromX,
		toCenter,
		lineInterval = 7,
		//arrow configure
		arrowHeight = 7,
		arrowWidth = 5,
		arrowDownOffset = 2,
		arrowLeftOffset,
		//paint configure
		paintY = cxt.canvas.height,
		controlPointHeight,
		controlPointY,
		lineY = paintY - arrowDownOffset,
		actualY // we should return it to calculate the MAX height
	;
		fromCenter = (drawStruct.posInfo[dpNode.from + 1] + drawStruct.posInfo[dpNode.from + 1 + 1] - drawStruct.WS_INTERVAL) / 2;
		toCenter = (drawStruct.posInfo[dpNode.to + 1] + drawStruct.posInfo[dpNode.to + 1 + 1] - drawStruct.WS_INTERVAL) / 2;
		fromX = fromCenter < toCenter ? fromCenter + lineInterval : fromCenter - lineInterval;
		controlPointHeight = Math.abs(dpNode.to - dpNode.from) * 13 + 10;
		controlPointY = lineY - controlPointHeight;
		actualY = lineY - controlPointHeight * 3 / 4;
		cxt.strokeStyle = dp_type=="dp" ? drawStruct.DP_DISCONF.lineColor : drawStruct.SDP_DISCONF.lineColor ;
		cxt.beginPath(); // !!!! must clear previous path ! or previous path will be repaint again and again , then the line become ugly with jaggies
		cxt.moveTo(fromX, lineY);
		cxt.bezierCurveTo(fromX, controlPointY, toCenter, controlPointY, toCenter, lineY);
		cxt.stroke();
		cxt.closePath();
		//draw arrow
		arrowLeftOffset = dpNode.to > dpNode.from ? (arrowWidth - cxt.lineWidth) / 2 + 1 : (arrowWidth - cxt.lineWidth) / 2 - 1;
		cxt.beginPath();
		cxt.moveTo(toCenter, paintY);
		cxt.lineTo(toCenter - arrowLeftOffset, paintY - arrowHeight);
		cxt.lineTo(toCenter + (arrowWidth - arrowLeftOffset), paintY - arrowHeight);
		cxt.closePath();
		cxt.fillStyle = dp_type=="dp" ? drawStruct.DP_DISCONF.lineColor : drawStruct.SDP_DISCONF.lineColor ;
		cxt.fill();
		//draw relate text
		cxt.fillStyle = dp_type=="dp" ? drawStruct.DP_DISCONF.textColor : drawStruct.SDP_DISCONF.textColor ;
		cxt.textAlign = "center";
		this.setEnTextFont(cxt);
		cxt.fillText(dpNode.relate, (fromX + toCenter) / 2, actualY + this.getFontSizeFromFontstr(cxt.font) / 2);
		return actualY;
	},
	drawDP : function (drawStruct) {
		if (drawStruct.dp.length == 0)
			return null;
		var canvas = this.createCanvasBuffer(drawStruct.width, drawStruct.ALL_ELEMENTS_HIGHT_CONF['DP']),
		cxt = canvas.getContext("2d"),
		i,
		minY = canvas.height,
		newHeight;
		cxt.lineWidth = 0.6;
		for (i = 0; i < drawStruct.dp.length; i++) {
			yPos = this.drawDpNode(drawStruct.dp[i], drawStruct, cxt , "dp");
			if (minY > yPos)
				minY = yPos;
		}
		minY -= 10; //  the up margin
		//move the image to the right position
		//first we should save the image data , then change the canvas height and put the imageData to it
		newHeight = canvas.height - minY;
		this.setCanvasAppropriateHeight(canvas, minY, newHeight);
        drawStruct.ALL_ELEMENTS_HIGHT_CONF['DP'] = newHeight ;
		return canvas;
	},
    drawSDP : function(drawStruct){
        if(drawStruct.sdp.length == 0) return null ;
        // TODO 修改将SDP无效
        return null;
		var sdpCanvas = this.createCanvasBuffer(drawStruct.width, drawStruct.ALL_ELEMENTS_HIGHT_CONF['SDP']),
        wsCanvas = null ,
        wholeCanvas = null ,
		cxt = sdpCanvas.getContext("2d"),
		i,
		minY = sdpCanvas.height,
		newHeight ,
        wholeWidth ,
        wholeHeight;
		cxt.lineWidth = 0.6;
		for (i = 0; i < drawStruct.sdp.length; i++) {
			yPos = this.drawDpNode(drawStruct.sdp[i], drawStruct, cxt , "sdp");
			if (minY > yPos)
				minY = yPos;
		}
		minY -= 10; //  the up margin
		//move the image to the right position
		//first we should save the image data , then change the canvas height and put the imageData to it
		newHeight = sdpCanvas.height - minY;
		this.setCanvasAppropriateHeight(sdpCanvas, minY, newHeight);
        drawStruct.ALL_ELEMENTS_HIGHT_CONF['SDP'] = newHeight ; 
		
        // here we should add the ws result under the sdp lines
        wsCanvas = this.drawWS(drawStruct) ;
        wholeWidth = Math.max(sdpCanvas.width , wsCanvas.width) ;
        wholeHeight = sdpCanvas.height + wsCanvas.height ;
        wholeCanvas = this.createCanvasBuffer(wholeWidth , wholeHeight) ;
        cxt = wholeCanvas.getContext('2d') ;
        cxt.drawImage(sdpCanvas , 0 , 0) ;
        cxt.drawImage(wsCanvas , 0 , sdpCanvas.height) ;
        return wholeCanvas ;
    } ,
	/*
	 **according to the drawStruct , paint the five components
	 * return value : a object contains the five components
	 */
	drawComponent : function (drawStruct) {
		var WSCanvas,
		POSTAGCanvas,
		NERCanvas,
		SRLCanvas,
		DPCanvas ,
        SDPCanvas ;
		this.canvasBufs = {};
		// draw WS at WSCanvas buffer
		WSCanvas = this.drawWS(drawStruct);
		// draw POSTAG
		POSTAGCanvas = this.drawPOSTAG(drawStruct);
		// draw NER
		NERCanvas = this.drawNER(drawStruct);
		// draw SRL
		SRLCanvas = this.drawSRL(drawStruct);
		// draw DP
		DPCanvas = this.drawDP(drawStruct);
        // draw SDP
        SDPCanvas =this.drawSDP(drawStruct) ;
		this.canvasBufs["WS"] = WSCanvas;
		this.canvasBufs["POSTAG"] = POSTAGCanvas;
		this.canvasBufs["NER"] = NERCanvas;
		this.canvasBufs["SRL"] = SRLCanvas;
		this.canvasBufs["DP"] = DPCanvas;
        this.canvasBufs["SDP"] = SDPCanvas ;

		return this.canvasBufs;
	},
	/**
	 *draw containers according to the six components  , and the disable attribute
	 *return value : the container 's imageData
	 */
	drawContainer : function (drawStruct, canvasBufs, disableAttr) {
		var drawBufs = {
			"WS" : null,
			"POSTAG" : null,
			"NER" : null,
			"DP" : null,
			"SRL" : null ,
            "SDP" : null 
		},
		offsetX = 0,
		offsetY = 0,
		intervalY = 10,
		paintY = offsetY,
		drawTarget ,
        WS_Y_START ,
        WS_Y_END ;

		//bulid the drawBufs
		if (typeof disableAttr == "undefined" || disableAttr == null)
			disableAttr = {};
		for (var key in drawBufs) {
			drawBufs[key] = (canvasBufs[key] === undefined || disableAttr[key] == true) ? "disable" : canvasBufs[key];
		}
        // to get a more appropriate height
        var tmpHeight = 0 ;
        for(var key in drawBufs){
            tmpHeight += ( drawBufs[key] == "disable" || drawBufs[key] == null ) ? 0 : drawBufs[key].height ;
            tmpHeight += 3 * intervalY ;
        }
        drawStruct.height = tmpHeight ;
        // build container canvas
		var canvas = this.createCanvasBuffer(drawStruct.width, drawStruct.height) ,
		    cxt = canvas.getContext("2d") ;
		//first clear the canvas
		cxt.clearRect(0, 0, canvas.width, canvas.height);

		//draw canvasBufs to the display canvas
		// drawImage 's first parameter can be HTMLCanvasElement , HTMLImageElement , HTMLVideoElement
		drawTarget = ["DP", "WS", "POSTAG", "NER", "SRL" , "SDP"];
		for (var i = 0; i < drawTarget.length; i++) {
			var curTarget = drawTarget[i] ,
                drawCanvas = drawBufs[curTarget];
			if (typeof drawCanvas == "string" && drawCanvas == "disable")
				continue;
			else if (drawCanvas == null)
				paintY += 3 * intervalY;
			else {
				cxt.drawImage(drawCanvas, offsetX, paintY);
				paintY += intervalY + drawCanvas.height;
                if(curTarget == "WS"){
                    WS_Y_START = paintY - drawCanvas.height - intervalY ;
                    WS_Y_END = paintY ;
                    drawStruct.WS_Y_START = paintY - intervalY - drawCanvas.height / 2  ;
                }
			}
		}
        // fix the 'Root' BUG
        if(drawBufs["DP"] == null || drawBufs["DP"] == "disable"){
            cxt.clearRect(0,WS_Y_START,drawStruct.posInfo[1] - drawStruct.WS_INTERVAL,WS_Y_END - WS_Y_START) ;
        }
		this.setCanvasAppropriateHeight(canvas, 0, paintY);
		// update drawStruct 's height as the  appropriate value .
		this.drawStruct.height = paintY;
		this.imageData = cxt.getImageData(0, 0, canvas.width, canvas.height);
		return this.imageData;
	},
	/**
	 *draw the caontainer 's image to the view port
        the x , y is the position relativing to the (0,0)
	 */
	drawView : function (imageData, canvas, x, y) {
        this.canvas = document.getElementById(this.canvasId) ;
        if(this.canvas == null) return ;
		var cxt = canvas.getContext("2d");
		cxt.clearRect(0, 0, canvas.width, canvas.height);
		cxt.putImageData(imageData, x , y);
	},
	//-------------now define the API for outer ---------------
        // get drawStruct
    getDrawData : function() {
        return this.drawStruct ;
    } ,
        // init the drawStruct , component
	analysis : function (sentObj) {
		this.initDrawStruct(sentObj);
		this.drawComponent(this.drawStruct);
	},
        //get the imageData and move to offset(0,0)
	update : function (disableAttr) {
		this.drawContainer(this.drawStruct, this.canvasBufs, disableAttr);
		this.setOffset( 0 , this.canvasHeight / 2 - this.drawStruct.WS_Y_START) ; 
        this.move(0, 0 ); 
	},
	paint : function () {
		this.update();
	},
        //move to offset(x,y)
	move : function (offsetX, offsetY) {
		//offsetX , offsetY relatives to the position previous offset
        this.drawView(this.imageData, this.canvas, this.offset.x + offsetX, this.offset.y + offsetY);
	},
	addaptWidth : function () {
        this.canvas = document.getElementById(this.canvasId) ;
        if(this.canvas == null) return ;
		var parent = this.canvas.parentNode;
        if(parent.offsetWidth == 0) return ;
		this.canvas.width = parent.offsetWidth;
		this.canvas.height = this.canvasHeight;
	},
	//------------event module--------------
	downAction : function (e) {
		if (e.target.tagName == "CANVAS") {
			this.eventModule.startX = e.pageX;
			this.eventModule.startY = e.pageY;
			this.eventModule.hasDown = true;
            //set mouse 
            //this.style.cursor = "hand" ; --!! error ; here , this pointed to the current instance
            e.target.style.cursor = "move" ;
		}
	},
	upAction : function (e) {
        if(this.eventModule.hasDown == true){
            var x = e.pageX - this.eventModule.startX,
            y = e.pageY - this.eventModule.startY;
            this.updateOffset(x, y);
            this.eventModule.startX = e.pageX;
            this.eventModule.startY = e.pageY;
            this.eventModule.hasDown = false;
            e.target.style.cursor = "default" ;
        }
	},
	moveAction : function (e) {
		if (this.eventModule.hasDown == true) {
			var offsetX = e.pageX - this.eventModule.startX,
			offsetY = e.pageY - this.eventModule.startY;
			this.move(offsetX, offsetY);
		}
	}

};

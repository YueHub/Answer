var paraSelectAction = null, // to response the click action of the paraSelPanel
    drawParaOriginWord = null,
    drawParaWS = null,
    drawParaPostag = null,
    drawParaNer = null,
    paraDrawObj = null, // the dom obj of drawing container
    paraNerIntroDomObj = document.getElementById("paraNerIntro"),
    setNerIntroDis = null, // function to set paraNerIntro 's displaying
    initNerIntro = null, // function to init the ner introduction
    selectParaPartToDrawByValue = null,
    nerMap = null, // the ner map struct
    lastSelectedValue = "para-ws",
    DRAW_PARA_PARENT_ID = "paraContent",
    PARA_SELECT_PANEL_ID = "paraSelPanel";

paraDrawObj = document.getElementById(DRAW_PARA_PARENT_ID);
nerMap = {
    "Ni" : {
        "color" : "#00008b",
        "cnName" : "机构名"
    },
    "Nh" : {
        "color" : "#cda000",
        "cnName" : "人名"
    },
    "Ns" : {
        "color" : "#3c8c71",
        "cnName" : "地名"
    }
};
drawParaOriginWord = function (parentObj, sentsObj) {
    var sentDom = document.createElement("P");

    for (var i = 0; i < sentsObj.length; i++) {
        var paraCont = [],
            sentCopy = sentDom.cloneNode(false);
        for (var j = 0; j < sentsObj[i].length; j++) {
            paraCont.push(getSentent(sentsObj[i][j]));
        }
        sentCopy.innerHTML = paraCont.join("");
        parentObj.appendChild(sentCopy);
    }
}
drawParaWS = function (parentObj, sentsObj) {
    var sentDom = document.createElement("P");

    for (var i = 0; i < sentsObj.length; i++) {
        var paraCont = [],
            sentCopy = sentDom.cloneNode(false);
        for (var j = 0; j < sentsObj[i].length; j++) {
            for (var k = 0; k < sentsObj[i][j].length; k++) {
                paraCont.push(sentsObj[i][j][k].cont);
            }
        }
        sentCopy.innerHTML = paraCont.join("&nbsp;&nbsp;");
        parentObj.appendChild(sentCopy);
    }
}
drawParaPostag = function (parentObj, sentsObj) {
    var POSTAG_COLOR = "#8b00b2",
        postag_style_start = '<span style="color:' + POSTAG_COLOR + ';">',
        postag_style_end = '</span>',
        sentDom = document.createElement("P");
    for (var i = 0; i < sentsObj.length; i++) {
        var paraCont = [],
            sentCopy = sentDom.cloneNode(false);
        for (var j = 0; j < sentsObj[i].length; j++) {
            for (var k = 0; k < sentsObj[i][j].length; k++) {
                paraCont.push([sentsObj[i][j][k].cont, "/", postag_style_start, sentsObj[i][j][k].pos, postag_style_end].join(""));
            }
        }
        sentCopy.innerHTML = paraCont.join("&nbsp;&nbsp;");
        parentObj.appendChild(sentCopy);
    }
}

drawParaNer = function (parentObj, sentsObj) {
    var sentDom = document.createElement("P");
    // draw text
    for (var i = 0; i < sentsObj.length; i++) {
        var paraCont = [],
            sentCopy = sentDom.cloneNode(false);
        for (var j = 0; j < sentsObj[i].length; j++) {
            for (var k = 0; k < sentsObj[i][j].length; k++) {
                var nerVal = sentsObj[i][j][k].ne,
                    nerMatch = nerVal.match(/^(\w+)?-(\w+)$/);
                if (nerMatch != null && nerMatch.length == 3) {
                    var tag = nerMatch[1],
                        nerType = nerMatch[2],
                        style_start = '<span style="color : ' + nerMap[nerType].color + ';">',
                        style_end = '</span>';
                    switch (tag) {
                        case "S":
                            paraCont.push([style_start, sentsObj[i][j][k].cont, style_end].join(""));
                        break;
                        case "B":
                            paraCont.push([style_start, sentsObj[i][j][k].cont].join(""));
                        break;
                        case "M":
                            paraCont.push(sentsObj[i][j][k].cont);
                        break;
                        case "E":
                            paraCont.push([sentsObj[i][j][k].cont, style_end].join(""));
                        break;

                    }
                } else {
                    paraCont.push(sentsObj[i][j][k].cont);
                }
            }
        }
        sentCopy.innerHTML = paraCont.join("");
        parentObj.appendChild(sentCopy);
    }

}
setNerIntroDis = function (isToDis) {
    if (isToDis == true || isToDis === undefined) {
        var wrapper = paraNerIntroDomObj.parentNode,
            wrapperHeight = wrapper.offsetHeight,
            paraContentHeight = paraDrawObj.offsetHeight,
            introHeight = (paraNerIntroDomObj.currentStyle || document.defaultView.getComputedStyle(paraNerIntroDomObj, false))["height"].slice(0, -2), // get height value from class defined value
            whiteHeight = wrapperHeight - paraContentHeight;
        if (whiteHeight > introHeight) {
            var marginTopVal = whiteHeight - introHeight;
            paraNerIntroDomObj.style.marginTop = marginTopVal + "px";
            paraNerIntroDomObj.style.display = "block";
        } else {
            paraNerIntroDomObj.style.marginTop = "0px"; // cancel the margin top
            paraNerIntroDomObj.style.display = "block";
        }
    } else {
        paraNerIntroDomObj.style.display = "none";
    }
}
initNerIntro = function () {
    for (var key in nerMap) {
        var introItem = document.createElement("LABEL"),
            box = document.createElement("SPAN"),
            textNode = document.createTextNode([nerMap[key].cnName, "(", key, ")"].join(""));
        box.style.background = nerMap[key].color;
        box.setAttribute("class", "box");
        introItem.appendChild(box);
        introItem.appendChild(textNode);
        introItem.setAttribute("class" , "inline") ;
        paraNerIntroDomObj.appendChild(introItem);
    }
}
selectParaPartToDrawByValue = function (paraValue) {
    //first clear origin content
    while (paraDrawObj.lastChild != null)
        paraDrawObj.removeChild(paraDrawObj.lastChild);
    switch (paraValue) {
        case "para-originWord":
            drawParaOriginWord(paraDrawObj, returnAnalysisRst);
        setNerIntroDis(false);
        break;
        case "para-ws":
            drawParaWS(paraDrawObj, returnAnalysisRst);
        setNerIntroDis(false);
        break;
        case "para-postag":
            drawParaPostag(paraDrawObj, returnAnalysisRst);
        setNerIntroDis(false);
        break;
        case "para-ner":
            drawParaNer(paraDrawObj, returnAnalysisRst);
        setNerIntroDis(true);
        break;
    }

}
paraSelectAction = function (e) {
    if (e.target.type == "radio") {
        if (lastSelectedValue == e.target.value)
            return false;
        lastSelectedValue = e.target.value;
        selectParaPartToDrawByValue(e.target.value);
    }
}
//------------Event Bind(using JQuery)-------
$(document).ready(function(){
        $("#" + PARA_SELECT_PANEL_ID).bind("click", function (e) {
            paraSelectAction(e);
            });
        }) ;

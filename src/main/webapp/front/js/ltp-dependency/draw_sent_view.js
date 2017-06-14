var lastOpenedEle = null, // global object storing the current selected canvas' parent's sibling item(corresponding to the "text-item")
    intervalId, // for switch animation
    getCanvasContainerFromParentSibling = null,
    getSentent = null, // function to get sentent from the drawStruct's segment words
    initDom = null, // function to init the list dom dynamicly
    drawCanvas = null, // function to draw canvas
    switchCanvas = null, // function to switch the list selected
    changeDisObj = null, // function for switching animate action
    disableAttr = null, // global object for get the disable canvas-drawing element
    setDisableAttr = null, // function to set variable disableAttr
    demo = null, // global draw Demo
    disableAttr = {}, // disabled attr for drawing
    DRAW_CANVAS_ID = "canvasTest", // only this id is active
    DRAW_PARENT_ID = "analysisContent", // parent id
    DISABLE_ATTR_PANEL_ID = "disableAttrPanel",
    CANVAS_HEIGHT = 200 
    ;

demo = new Demo(DRAW_CANVAS_ID);


changeDisObj = function (newObj, previousObj) {
    var stepLen = 50,
        newObjOriHeight = newObj.offsetHeight,
        previousObjOriHeight = previousObj.offsetHeight;
    //console.log(previousObj) ;
    newObj.style.height = (stepLen + newObjOriHeight) + "px";
    previousObj.style.height = (previousObjOriHeight - stepLen) + "px";
    if (previousObj.offsetHeight <= 0) {
        clearInterval(intervalId);
    }
}
switchCanvas = function (clickedItemDivEle, previousClickedItemDivEle) {
    if (clickedItemDivEle != previousClickedItemDivEle) {
        //clickedItemDivEle is the text-item div , while the function changeDisObj 's param is the canvasContainer div
        var clickedCanvasContainer = getCanvasContainerFromParentSibling(clickedItemDivEle),
            previousCanvasContainer = getCanvasContainerFromParentSibling(previousClickedItemDivEle);
        //console.log(clickedCanvasContainer) ;
        //console.log(previousCanvasContainer) ;
        intervalId = setInterval(function () {
                changeDisObj(clickedCanvasContainer, previousCanvasContainer);
                }, 10);
    }
}
getCanvasContainerFromParentSibling = function (parentSibling) {
    return parentSibling.parentNode.getElementsByTagName("CANVAS")[0].parentNode;
}
getSentent = function (sentEle) {
    var wsList = [],
        enReg = /^[A-Za-z']*$/;
    for (var i = 0; i < sentEle.length; i++) {
        wsList.push(sentEle[i].cont);
        // add the logic for English sentence !
        if (enReg.test(sentEle[i].cont) && i < sentEle.length - 1 && enReg.test(sentEle[i + 1].cont)) {
            wsList.push(" ");
        }
    }
    return wsList.join("");
}
initDom = function (parentId, sentsObj) {
    /* in > sentsObj : JSON data by  the ltp server returning */
    var parentContainer = document.getElementById(parentId),
        liEle = document.createElement("LI"),
        itemDivEle = document.createElement("DIV"),
        canvasDivEle = document.createElement("DIV"),
        canvasEle = document.createElement("CANVAS"),
        eleCopy;
    // clear the container
    while (parentContainer.childNodes.length > 0)
        parentContainer.removeChild(parentContainer.lastChild);
    itemDivEle.setAttribute("class", "text-item");
    canvasDivEle.setAttribute("class", "canvasContainer");
    canvasDivEle.appendChild(canvasEle);
    liEle.appendChild(itemDivEle);
    liEle.appendChild(canvasDivEle);
    for (var i = 0; i < sentsObj.length; i++) {
        for (var j = 0; j < sentsObj[i].length; j++) {
            eleCopy = liEle.cloneNode(true);
            eleCopy.firstChild.setAttribute("x", i),
            eleCopy.firstChild.setAttribute("y", j);
            var sentent = getSentent(sentsObj[i][j]);
            var textNode = document.createTextNode([sentent].join(""));
            eleCopy.firstChild.appendChild(textNode);
            parentContainer.appendChild(eleCopy);
        }
    }

}
drawCanvas = function (itemDivEle) {
    var i = itemDivEle.getAttribute("x"),
        j = itemDivEle.getAttribute("y");
    //first set previous canvas 's id = ""
    if (lastOpenedEle != itemDivEle)
        lastOpenedEle.parentNode.getElementsByTagName("CANVAS")[0].setAttribute("id", "");
    itemDivEle.parentNode.getElementsByTagName("CANVAS")[0].setAttribute("id", DRAW_CANVAS_ID);
    demo.addaptWidth();
    demo.analysis(returnAnalysisRst[i][j]);
    setDisableAttr(DISABLE_ATTR_PANEL_ID) ;
    demo.update(disableAttr);
}
/*updateLabelExplanationPanelContent = function(){
    labelExplanationPanel.setLabelPanelContent(demo.getDrawData()) ;
}*/
setDisableAttr = function (panelId) {
    var panel = document.getElementById(panelId),
        checkboxList = panel.getElementsByTagName("INPUT");
    disableAttr = {} ; // empty it !
    for (var i = 0; i < checkboxList.length; i++) {
        if (checkboxList[i].type == "checkbox" && checkboxList[i].checked == false) {
            var value = checkboxList[i].value,
                key = value.match(/[^-]*/)[0].toUpperCase(); // WS , POSTAG , NER , DP , SDP , SRL
            disableAttr[key] = true;
        }
    }
}
//-------------------Event Bind (using JQuery)-----------------
$(document).ready(function(){
        $("#" + DISABLE_ATTR_PANEL_ID).bind("click", function (e) {
            setDisableAttr(DISABLE_ATTR_PANEL_ID);
            demo.update(disableAttr);
            });
        $("#" + DRAW_PARENT_ID).bind({
            "mousedown" : function (e) {
            demo.downAction(e)
            },
            "mouseup" : function (e) {
            demo.upAction(e)
            },
            "mousemove" : function (e) {
            demo.moveAction(e)
            }
        });
        $("#" + DRAW_PARENT_ID).bind("click", function (e) {
            var ele = e.target;
            if (lastOpenedEle != null && ele.getAttribute("class") == "text-item" && ele != lastOpenedEle) {
                //! draw new canvas .
                drawCanvas(ele);
                //! update label explanation panel content .
                /*updateLabelExplanationPanelContent() ;*/
                //! animating .
                (function (previousClicked) {
                 switchCanvas(ele, previousClicked);
                 })(lastOpenedEle);
                lastOpenedEle = ele;
            }
        });
}) ;   


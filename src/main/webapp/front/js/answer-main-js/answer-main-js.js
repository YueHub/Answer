// 传统搜索结果
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
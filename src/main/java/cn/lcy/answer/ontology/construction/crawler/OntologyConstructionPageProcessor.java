package cn.lcy.answer.ontology.construction.crawler;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import cn.lcy.answer.ontology.base.dao.BaseDAOImpl;

public class OntologyConstructionPageProcessor extends BaseDAOImpl implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
  
    
    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
    	// 词条图片
    	String picSrc = page.getHtml().xpath("//div[@class='summary-pic']//img/@src").toString();
    	if(picSrc == null) 
    		picSrc = page.getHtml().xpath("//dl[@class='lemmaWgt-albumList-poster']//img/@src").toString();
    	// 词条图片
    	page.putField("picSrc", picSrc);
    	
    	
    	
    	// 内容一：词条标题
    	String lemmaTitle = page.getHtml().xpath("//dd[@class='lemmaWgt-lemmaTitle-title']/h1/allText()").toString();
    	page.putField("lemmaTitle", lemmaTitle);
    	
    	// 内容二：词条概括（词条描述）
    	String lemmaSummary = page.getHtml().xpath("//div[@class='lemma-summary']/allText()").toString();
    	page.putField("lemmaSummary", lemmaSummary);

    	// 内容三：所有同义词（所有同名实体）
    	List<String> polysemants = page.getHtml().xpath("//ul[@class='polysemantList-wrapper']/allText()").replace("▪", "").all();
    	// 本实体的同义说明（被选中的实体，即当前实体）
    	String polysemantExplain = page.getHtml().xpath("//ul[@class='polysemantList-wrapper']/li[@class='item']/span[@class='selected']/allText()").toString();
    	page.putField("polysemants", polysemants);
    	page.putField("polysemantExplain", polysemantExplain);
    	
    	// 内容四：人物关系
    	List<String> relationNames = page.getHtml().xpath("//div[@class='star-info-block relations']//div[@id='slider_relations']//div[@class='name']/text(1)").all();
    	List<String> relationValues = page.getHtml().xpath("//div[@class='star-info-block relations']//div[@id='slider_relations']//div[@class='name']/em/text(1)").all();
    	List<String> relationUrls = page.getHtml().xpath("//div[@class='star-info-block relations']//div[@id='slider_relations']//a/@href").all();
    	page.putField("relationNames", relationNames);
    	page.putField("relationValues", relationValues);
    	page.putField("relationUrls", relationUrls);
    	
    	// 内容五：词条属性
    	List<String> parameterNames = page.getHtml().xpath("div//dt[@class='basicInfo-item name']/allText()").all();
    	List<String> parameterValues = page.getHtml().xpath("div//dd[@class='basicInfo-item value']/allText()").all();
    	List<String> parameterHasUrlValues = page.getHtml().xpath("div//dd[@class='basicInfo-item value']/a/allText()").all();
    	List<String> parameterHasUrl = page.getHtml().xpath("div//dd[@class='basicInfo-item value']/a/@href").all();
    	page.putField("parameterNames", parameterNames);
    	page.putField("parameterValues", parameterValues);
    	page.putField("parameterHasUrlValues", parameterHasUrlValues);
    	page.putField("parameterHasUrl", parameterHasUrl);
    	// 对象属性URL
    	
    	// 词条主题和词条概括都为空则跳过 避免抓取http://baike.baidu.com/m#download百科客户端下载页之类的无关页面
        if (page.getResultItems().get("lemmaTitle") == null && page.getResultItems().get("lemmaSummary") == null) {
            // skip this page
            page.setSkip(true);
        }
        
        if(page.getResultItems().getRequest().getUrl().split("#").length >= 2) {
        	page.setSkip(true);
        }
        
       /*if(OntologyConstructionLauncher.count < 10) {
            page.addTargetRequests(page.getHtml().links().regex("http://baike\\.baidu\\.com/.*").all());
            ++OntologyConstructionLauncher.count;
        }
       	page.addTargetRequest("http://baike.baidu.com/subview/2321/5786291.htm"); // 火影忍者
        page.addTargetRequest("http://baike.baidu.com/subview/8980/5236815.htm"); // 死神
        page.addTargetRequest("http://baike.baidu.com/subview/9514/15201085.htm"); // 美人鱼
        page.addTargetRequest(" http://baike.baidu.com/subview/82349/6500980.htm"); // 喜剧之王
       
        page.addTargetRequest("http://baike.baidu.com/subview/12085227/12482265.htm"); // 美国队长3
        page.addTargetRequest("http://baike.baidu.com/view/5081.htm");	// 周星驰
        //page.addTargetRequest("http://baike.baidu.com/view/2632.htm");	// 周杰伦
        page.addTargetRequest("http://baike.baidu.com/view/27362.htm");	// 习近平
        page.addTargetRequest("http://baike.baidu.com/view/1471.htm");	// 北京大学
        page.addTargetRequest("http://baike.baidu.com/view/1247049.htm");	// 阿里巴巴集团
        page.addTargetRequest("http://baike.baidu.com/view/1269423.htm");	// 亚信科技
        page.addTargetRequest("http://baike.baidu.com/view/1591.htm");	// 腾讯
        page.addTargetRequest("http://baike.baidu.com/subview/61891/14022133.htm"); // 中国
        page.addTargetRequest("http://baike.baidu.com/view/30969.htm"); // 尾田荣一郎
        page.addTargetRequest("http://baike.baidu.com/subview/9514/13552337.htm"); // 美人鱼歌曲
        page.addTargetRequest("http://baike.baidu.com/view/7599.htm"); // 温州市
*/    }

    @Override
    public Site getSite() {
        return site;
    }
}

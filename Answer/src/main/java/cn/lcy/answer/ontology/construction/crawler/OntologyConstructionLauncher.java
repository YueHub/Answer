package cn.lcy.answer.ontology.construction.crawler;

import org.apache.http.HttpHost;

import us.codecraft.webmagic.Spider;


public class OntologyConstructionLauncher {
	
	public static int count = 0;
	
	public static void main(String args[]) {
		OntologyConstructionPageProcessor constructionPageProcessor = new OntologyConstructionPageProcessor();
		OntologyConstructionPipeline constructionPipeline = new OntologyConstructionPipeline();
		// 设置代理
		HttpHost httpProxy = new HttpHost("proxy.asiainfo.com",8080);
		constructionPageProcessor.getSite().setHttpProxy(httpProxy);
		Spider.create(constructionPageProcessor)
			//从"https://github.com/code4craft"开始抓
			// 周星驰：http://baike.baidu.com/view/5081.htm
			// 周杰伦：http://baike.baidu.com/view/2632.htm
			// 昆凌：http://baike.baidu.com/item/%E6%98%86%E5%87%8C/1545451
			// 侯佩岑：http://baike.baidu.com/item/%E4%BE%AF%E4%BD%A9%E5%B2%91/257126
			// 叶詹阿妹:http://baike.baidu.com/item/%E5%8F%B6%E8%A9%B9%E9%98%BF%E5%A6%B9/926323
			// 叶惠美：http://baike.baidu.com/item/%E5%8F%B6%E6%83%A0%E7%BE%8E/2325933
			// 苍井空：http://baike.baidu.com/view/548227.htm
			.addUrl("http://baike.baidu.com/view/548227.htm")
			.addPipeline(constructionPipeline)
			//开启5个线程抓取
			.thread(1)
			//启动爬虫
			.run();
	}

}

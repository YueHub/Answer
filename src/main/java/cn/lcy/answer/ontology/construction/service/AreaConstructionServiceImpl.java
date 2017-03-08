package cn.lcy.answer.ontology.construction.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;

import cn.lcy.answer.enums.OntologyClassEnum;
import cn.lcy.answer.io.util.FileIOUtil;
import cn.lcy.answer.ontology.construction.dao.ConstructionBaseDAOImpl;
import cn.lcy.answer.ontology.construction.dao.ConstructionDAOI;
import cn.lcy.answer.ontology.construction.dao.ConstructionDAOImpl;
import cn.lcy.answer.ontology.construction.util.PictureDownloader;
import cn.lcy.answer.ontology.construction.util.StringFilter;
import cn.lcy.answer.ontology.model.BaikePage;

import com.hankcs.hanlp.corpus.io.IOUtil;

public class AreaConstructionServiceImpl implements ConstructionServiceI {

	private ConstructionDAOI constructionDAO = new ConstructionDAOImpl();
	
	@Override
	public boolean construction(BaikePage baikePage) throws Exception {
		// 词条标题（实体名）
		String individualName = baikePage.getLemmaTitle();
		String polysemantExplain = baikePage.getPolysemantExplain();
		String url = baikePage.getUrl();
		Individual areaIndividual = null;
		
		// 查询词典中是否有该实体 有则查询返回 没有则创建返回  true表示这是本名
		areaIndividual = this.queryIndividual(individualName, polysemantExplain, url, true, OntologyClassEnum.AREA);
		constructionDAO.addObjectProperty(areaIndividual, "是", areaIndividual);
		
		// 添加数据属性（描述和歧义说明）
		String lemmaSummary =  baikePage.getLemmaSummary();
		String picSrc = baikePage.getPicSrc();
		if(picSrc != null) {
			// 取得当前时间
			long times = System.currentTimeMillis();
			// 生成0-1000的随机数
			int random = (int)(Math.random() * 1000);
			// 扩展名称
			String newPicName = times + "" + random  + ".jpg";
			PictureDownloader.picDownload(picSrc, newPicName, ConstructionBaseDAOImpl.PIC_SAVE_PATH + "//" + OntologyClassEnum.AREA.getName() );
			constructionDAO.addDataProperty(areaIndividual, "picSrc", newPicName);
		}
		constructionDAO.addDataProperty(areaIndividual, "URL信息来源", url);
		constructionDAO.addDataProperty(areaIndividual, "描述", lemmaSummary);
		constructionDAO.addDataProperty(areaIndividual, "歧义说明", polysemantExplain);
		
		// 添加基本信息
		List<String> parameterNamesFilter = baikePage.getParameterNames();
		List<String> parameterValuesFilter = baikePage.getParameterValues();
		constructionDAO.addDataProperties(areaIndividual, parameterNamesFilter, parameterValuesFilter);
		
		// 处理地区-地区对象
		this.dealAreas(areaIndividual, baikePage);
		
		// 处理地区-音乐对象
		this.dealMusics(areaIndividual, baikePage);
		
		return false;
	}
	
	public Individual queryIndividual(String individualName, String polysemantExplain, String url, boolean isAliases, OntologyClassEnum parentClass) {
		// 以生省内存的方式读取Answer_Dict词典
		LinkedList<String> dictIndividualList = IOUtil.readLineListWithLessMemory(INDIVIDUAL_UUID_DICT_FILE);
		Individual areaIndividual = null;
		Long rowNum = 0l;
		// 遍历词典中的实体记录 判断当前实体是否已经存在
		for(String row : dictIndividualList) {
			++rowNum;
			String[] fieldsDict = row.split("_");
			String dictIndividualUUID = fieldsDict[0]; // UUID
			String dictIndividualName = fieldsDict[1]; // 实体名
			String dictPolysemantExplain = fieldsDict[2]; // 歧义说明
			String dictIndividualURL = fieldsDict[3]; // 实体百科页面URL
			String dictIsAliasesWrite = fieldsDict[4]; // 是否是本名
			int dictIndividualClass = Integer.parseInt(fieldsDict[5]);	// 实体所属类型
			// 如果词典中歧义理解字段为待更新
			// 第一种情况：如果找到实体名相同并且明确指出该实体没有歧义则   该实体就是当前迭代到的实体
			if(individualName.equals(dictIndividualName) && dictPolysemantExplain.equals("无")) {
				areaIndividual = constructionDAO.getIndividual(dictIndividualUUID);
				// 找到完全相同的实体了 使用#去除所有框架定位网页
			} else if(individualName.equals(dictIndividualName) && url.split("#")[0].equals(dictIndividualURL) && dictIndividualClass == parentClass.getIndex()) {
				// 如果此时抓到的实体歧义不为空 则表示该实体有同名实体 则更新词典 TODO 应该把 != null 去掉
				if(dictPolysemantExplain.equals("待更新")) {
					if(polysemantExplain == null) {
						polysemantExplain = "无";
					}
					// 更新词典 修改歧义说明字段
					row = dictIndividualUUID + "_" + dictIndividualName + "_" + polysemantExplain + "_" + dictIndividualURL + "_" + dictIsAliasesWrite + "_" + parentClass.getIndex();
					// 更新Answer_Dict
					FileIOUtil.updateContent(INDIVIDUAL_UUID_DICT_FILE, rowNum, row);
				}
				// 获取该实体
				areaIndividual = constructionDAO.getIndividual(dictIndividualUUID);
			}
		}
		
		// 如果词典中不存在该实体，则插入词典并且创建一个实体
		if(areaIndividual == null) {
			String areaIndividualUUID = UUID.randomUUID().toString().replace("-", "");
			String isAliasesWrite = null;
			if(isAliases == true) {
				isAliasesWrite = "1";
			} else {
				isAliasesWrite = "0";
			}
			if(polysemantExplain == null) {
				polysemantExplain = "无";
			}
			String row_add_individual = areaIndividualUUID + "_" + individualName + "_" + polysemantExplain + "_" + url.split("#")[0] + "_" + isAliasesWrite + "_" + parentClass.getIndex();
			FileIOUtil.appendContent(INDIVIDUAL_UUID_DICT_FILE, row_add_individual);
			OntClass areaClass = constructionDAO.getOntClass(parentClass.getName());		// 获取电影类型
			areaIndividual = constructionDAO.createIndividual(areaIndividualUUID, areaClass);
		}
		
		constructionDAO.addComment(areaIndividual, individualName); // 创建comment
		return areaIndividual;
	}
	
	public boolean dealAreas(Individual areaIndividual, BaikePage baikePage) {
		List<String> areas = new ArrayList<String>();
		int index = 0;
		for(String parameterName : baikePage.getParameterNames()) {
			if(parameterName.equals("所属州")) {
				areas = StringFilter.parameterValueSeparates(baikePage.getParameterValues().get(index));
				for(String area : areas) {
					// 创建二级类
					OntClass stateAreaClass = constructionDAO.getOntClass(area);
					// 如果本体中此时没有该二级类
					if(stateAreaClass == null) {
						stateAreaClass = constructionDAO.createOntClass(area);
					}
					// 获取一级类 地区类
					OntClass areaClass = constructionDAO.getOntClass(OntologyClassEnum.AREA.getName());
					// 创建子类（二级类）
					constructionDAO.addSubClass(areaClass, stateAreaClass);
					constructionDAO.addSubClass(areaIndividual, stateAreaClass);
				}
			}
			
			if(parameterName.equals("首都")) {
				areas = StringFilter.parameterValueSeparates(baikePage.getParameterValues().get(index));
				for(String area : areas) {
					String url = null;
					int i = 0;
					for(String parameterHasUrlValue : baikePage.getParameterHasUrlValues()) {
						if(area.equals(parameterHasUrlValue)) {
							url = baikePage.getParameterHasUrl().get(i);
						}
						++i;
					}
					
					if(url != null) {
						String polysemantExplain = "待更新";
						Individual capitalIndividual = this.queryIndividual(area, polysemantExplain, url, true, OntologyClassEnum.AREA);
						constructionDAO.addObjectProperty(capitalIndividual, "属于", areaIndividual);
						constructionDAO.addObjectProperty(areaIndividual, "首都", capitalIndividual);
					}
				}
				
			}
			++index;
		}
		return false;
	}
	
	public boolean dealMusics(Individual areaIndividual, BaikePage baikePage) {
		List<String> musics = new ArrayList<String>();
		int index = 0;
		for(String parameterName : baikePage.getParameterNames()) {
			if(parameterName.equals("国歌")) {
				musics = StringFilter.parameterValueSeparates(baikePage.getParameterValues().get(index));
				for(String music : musics) {
					String url = null;
					int i = 0;
					for(String parameterHasUrlValue : baikePage.getParameterHasUrlValues()) {
						if(music.equals(parameterHasUrlValue)) {
							url = baikePage.getParameterHasUrl().get(i);
						}
						++i;
					}
					
					if(url != null) {
						String polysemantExplain = "待更新";
						Individual musicIndividual = this.queryIndividual(music, polysemantExplain, url, true, OntologyClassEnum.MUSIC);
						constructionDAO.addObjectProperty(areaIndividual, "国歌", musicIndividual);
					}
				}
				
			}
			++index;
		}
		return false;
	}

}

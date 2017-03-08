package cn.lcy.answer.ontology.construction.service;

import java.util.ArrayList;
import java.util.Arrays;
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

public class AnimationConstructionServiceImpl implements ConstructionServiceI {

	private ConstructionDAOI constructionDAO = new ConstructionDAOImpl();
	
	@Override
	public boolean construction(BaikePage baikePage) throws Exception {
		// 词条标题（实体名）
		String individualName = baikePage.getLemmaTitle();
		String polysemantExplain = baikePage.getPolysemantExplain();
		String url = baikePage.getUrl();
		Individual animationIndividual = null;
		
		// 查询词典中是否有该实体 有则查询返回 没有则创建返回  true表示这是本名
		animationIndividual = this.queryIndividual(individualName, polysemantExplain, url, true, OntologyClassEnum.ANIMATION);
		constructionDAO.addObjectProperty(animationIndividual, "是", animationIndividual);
		
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
			PictureDownloader.picDownload(picSrc, newPicName, ConstructionBaseDAOImpl.PIC_SAVE_PATH + "//" + OntologyClassEnum.ANIMATION.getName() );
			constructionDAO.addDataProperty(animationIndividual, "picSrc", newPicName);
		}
		constructionDAO.addDataProperty(animationIndividual, "URL信息来源", url);
		constructionDAO.addDataProperty(animationIndividual, "描述", lemmaSummary);
		constructionDAO.addDataProperty(animationIndividual, "歧义说明", polysemantExplain);
		
		// 添加基本信息
		List<String> parameterNamesFilter = baikePage.getParameterNames();
		List<String> parameterValuesFilter = baikePage.getParameterValues();
		constructionDAO.addDataProperties(animationIndividual, parameterNamesFilter, parameterValuesFilter);
		
		// 处理动画-公司对象（动画制作公司）
		this.dealCompanys(animationIndividual, baikePage);
		
		// 处理动画-公司对象（制片公司）
		//this.dealCompanys(animationIndividual, baikePage);
		
		// 处理动画-地区对象（地区）
		this.dealAreas(animationIndividual, baikePage);
		
		// 处理动画-人物对象（主演）
		this.dealCharacters(animationIndividual, baikePage);
		
		
		return false;
	}
	
	public Individual queryIndividual(String individualName, String polysemantExplain, String url, boolean isAliases, OntologyClassEnum parentClass) {
		// 以生省内存的方式读取Answer_Dict词典
		LinkedList<String> dictIndividualList = IOUtil.readLineListWithLessMemory(INDIVIDUAL_UUID_DICT_FILE);
		Individual movieIndividual = null;
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
				movieIndividual = constructionDAO.getIndividual(dictIndividualUUID);
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
				movieIndividual = constructionDAO.getIndividual(dictIndividualUUID);
			}
		}
		
		// 如果词典中不存在该实体，则插入词典并且创建一个实体
		if(movieIndividual == null) {
			String movieIndividualUUID = UUID.randomUUID().toString().replace("-", "");
			String isAliasesWrite = null;
			if(isAliases == true) {
				isAliasesWrite = "1";
			} else {
				isAliasesWrite = "0";
			}
			if(polysemantExplain == null) {
				polysemantExplain = "无";
			}
			String row_add_individual = movieIndividualUUID + "_" + individualName + "_" + polysemantExplain + "_" + url.split("#")[0] + "_" + isAliasesWrite + "_" + parentClass.getIndex();
			FileIOUtil.appendContent(INDIVIDUAL_UUID_DICT_FILE, row_add_individual);
			OntClass movieClass = constructionDAO.getOntClass(parentClass.getName());		// 获取电影类型
			movieIndividual = constructionDAO.createIndividual(movieIndividualUUID, movieClass);
		}
		constructionDAO.addComment(movieIndividual, individualName); // 创建comment
		return movieIndividual;
	}
	
	public boolean dealCompanys(Individual animationIndividual, BaikePage baikePage) {
		List<String> companys = new ArrayList<String>();
		int index = 0;
		for(String parameterName : baikePage.getParameterNames()) {
			if(parameterName.equals("动画制作")) {
				companys = Arrays.asList(baikePage.getParameterValues().get(index).split("、"));
				if(companys.size() == 1) {
					companys = Arrays.asList(baikePage.getParameterValues().get(index).split("，"));
				}
				for(String company : companys) {
					String url = null;
					int i = 0;
					for(String parameterHasUrlValue : baikePage.getParameterHasUrlValues()) {
						if(company.equals(parameterHasUrlValue)) {
							url = baikePage.getParameterHasUrl().get(i);
						}
						++i;
					}
					if(url != null) {
						String polysemantExplain = "待更新";
						Individual companyIndividual = this.queryIndividual(company, polysemantExplain, url, true, OntologyClassEnum.COMPANY);
						constructionDAO.addObjectProperty(companyIndividual, "制作", animationIndividual);
					}
				}
			}
			if(parameterName.equals("制片")) {
				companys = Arrays.asList(baikePage.getParameterValues().get(index).split("、"));
				if(companys.size() == 1) {
					companys = Arrays.asList(baikePage.getParameterValues().get(index).split("，"));
				}
				for(String company : companys) {
					String url = null;
					int i = 0;
					for(String parameterHasUrlValue : baikePage.getParameterHasUrlValues()) {
						if(company.equals(parameterHasUrlValue)) {
							url = baikePage.getParameterHasUrl().get(i);
						}
						++i;
					}
					
					if(url != null) {
						String polysemantExplain = "待更新";
						Individual companyIndividual = this.queryIndividual(company, polysemantExplain, url, true, OntologyClassEnum.COMPANY);
						constructionDAO.addObjectProperty(companyIndividual, "制片", animationIndividual);
						constructionDAO.addObjectProperty(companyIndividual, "制作", animationIndividual);
					}
				}
			}
			++index;
		}
		return true;
	}
	
	public boolean dealAreas(Individual animationIndividual, BaikePage baikePage) {
		List<String> areas = new ArrayList<String>();
		int index = 0;
		for(String parameterName : baikePage.getParameterNames()) {
			if(parameterName.equals("地区")) {
				areas = Arrays.asList(baikePage.getParameterValues().get(index).split("、"));
				if(areas.size() == 1) {
					areas = Arrays.asList(baikePage.getParameterValues().get(index).split("，"));
				}
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
						Individual areaIndividual = this.queryIndividual(area, polysemantExplain, url, true, OntologyClassEnum.AREA);
						constructionDAO.addObjectProperty(animationIndividual, "属于", areaIndividual);
					}
					
					// 创建二级类
					OntClass areaClass = constructionDAO.getOntClass(area);
					// 如果本体中此时没有该二级类
					if(areaClass == null) {
						areaClass = constructionDAO.createOntClass(area);
					}
					// 获取一级类 动画类
					OntClass animationClass = constructionDAO.getOntClass(OntologyClassEnum.ANIMATION.getName());
					// 创建子类（二级类）
					constructionDAO.addSubClass(animationClass, areaClass);
					constructionDAO.addSubClass(animationIndividual, areaClass);
				}
			}
			++index;
		}
		return false;
	}
	
	public boolean dealCharacters(Individual animationIndividual, BaikePage baikePage) {
		List<String> characters = new ArrayList<String>();
		int index = 0;
		for(String parameterName : baikePage.getParameterNames()) {
			if(parameterName.equals("原作")) {
				characters = Arrays.asList(baikePage.getParameterValues().get(index).split("、"));
				if(characters.size() == 1) {
					characters = Arrays.asList(baikePage.getParameterValues().get(index).split("，"));
				}
				for(String character : characters) {
					String url = null;
					int i = 0;
					for(String parameterHasUrlValue : baikePage.getParameterHasUrlValues()) {
						if(character.equals(parameterHasUrlValue)) {
							url = baikePage.getParameterHasUrl().get(i);
						}
						++i;
					}
					
					if(url != null) {
						String polysemantExplain = "待更新";
						Individual characterIndividual = this.queryIndividual(character, polysemantExplain, url, true, OntologyClassEnum.CHARACTER);
						constructionDAO.addObjectProperty(characterIndividual, "创作", animationIndividual);
						constructionDAO.addObjectProperty(animationIndividual, "原作", characterIndividual);
						constructionDAO.addObjectProperty(characterIndividual, "有动画", animationIndividual);
					}
				}
			}
			if(parameterName.equals("导演")) {
				characters = StringFilter.parameterValueSeparates(baikePage.getParameterValues().get(index));
				for(String character : characters) {
					String url = null;
					int i = 0;
					for(String parameterHasUrlValue : baikePage.getParameterHasUrlValues()) {
						if(character.equals(parameterHasUrlValue)) {
							url = baikePage.getParameterHasUrl().get(i);
						}
						++i;
					}
					
					if(url != null) {
						String polysemantExplain = "待更新";
						Individual characterIndividual = this.queryIndividual(character, polysemantExplain, url, true, OntologyClassEnum.CHARACTER);
						constructionDAO.addObjectProperty(characterIndividual, "导演", animationIndividual);
						constructionDAO.addObjectProperty(characterIndividual, "执导", animationIndividual);
						constructionDAO.addObjectProperty(characterIndividual, "拍", animationIndividual);
					}
				}
			}
			if(parameterName.equals("剧本")) {
				characters = StringFilter.parameterValueSeparates(baikePage.getParameterValues().get(index));
				for(String character : characters) {
					String url = null;
					int i = 0;
					for(String parameterHasUrlValue : baikePage.getParameterHasUrlValues()) {
						if(character.equals(parameterHasUrlValue)) {
							url = baikePage.getParameterHasUrl().get(i);
						}
						++i;
					}
					
					if(url != null) {
						String polysemantExplain = "待更新";
						Individual characterIndividual = this.queryIndividual(character, polysemantExplain, url, true, OntologyClassEnum.CHARACTER);
						constructionDAO.addObjectProperty(characterIndividual, "写剧本", animationIndividual);
					}
				}
			}
			if(parameterName.equals("角色设计")) {
				characters = StringFilter.parameterValueSeparates(baikePage.getParameterValues().get(index));
				for(String character : characters) {
					String url = null;
					int i = 0;
					for(String parameterHasUrlValue : baikePage.getParameterHasUrlValues()) {
						if(character.equals(parameterHasUrlValue)) {
							url = baikePage.getParameterHasUrl().get(i);
						}
						++i;
					}
					
					if(url != null) {
						String polysemantExplain = "待更新";
						Individual characterIndividual = this.queryIndividual(character, polysemantExplain, url, true, OntologyClassEnum.CHARACTER);
						constructionDAO.addObjectProperty(characterIndividual, "设计角色", animationIndividual);
						constructionDAO.addObjectProperty(characterIndividual, "设计", animationIndividual);
					}
				}
			}
			if(parameterName.equals("作画监督")) {
				characters = StringFilter.parameterValueSeparates(baikePage.getParameterValues().get(index));
				for(String character : characters) {
					String url = null;
					int i = 0;
					for(String parameterHasUrlValue : baikePage.getParameterHasUrlValues()) {
						if(character.equals(parameterHasUrlValue)) {
							url = baikePage.getParameterHasUrl().get(i);
						}
						++i;
					}
					
					if(url != null) {
						String polysemantExplain = "待更新";
						Individual characterIndividual = this.queryIndividual(character, polysemantExplain, url, true, OntologyClassEnum.CHARACTER);
						constructionDAO.addObjectProperty(characterIndividual, "监督作画", animationIndividual);
						constructionDAO.addObjectProperty(characterIndividual, "监督", animationIndividual);
					}
				}
			}
			if(parameterName.equals("音乐")) {
				characters = StringFilter.parameterValueSeparates(baikePage.getParameterValues().get(index));
				for(String character : characters) {
					String url = null;
					int i = 0;
					for(String parameterHasUrlValue : baikePage.getParameterHasUrlValues()) {
						if(character.equals(parameterHasUrlValue)) {
							url = baikePage.getParameterHasUrl().get(i);
						}
						++i;
					}
					
					if(url != null) {
						String polysemantExplain = "待更新";
						Individual characterIndividual = this.queryIndividual(character, polysemantExplain, url, true, OntologyClassEnum.CHARACTER);
						constructionDAO.addObjectProperty(characterIndividual, "创作音乐", animationIndividual);
						constructionDAO.addObjectProperty(characterIndividual, "创作", animationIndividual);
					}
				}
			}
			if(parameterName.equals("主要配音")) {
				characters = StringFilter.parameterValueSeparates(baikePage.getParameterValues().get(index));
				for(String character : characters) {
					String url = null;
					int i = 0;
					for(String parameterHasUrlValue : baikePage.getParameterHasUrlValues()) {
						if(character.equals(parameterHasUrlValue)) {
							url = baikePage.getParameterHasUrl().get(i);
						}
						++i;
					}
					
					if(url != null) {
						String polysemantExplain = "待更新";
						Individual characterIndividual = this.queryIndividual(character, polysemantExplain, url, true, OntologyClassEnum.CHARACTER);
						constructionDAO.addObjectProperty(characterIndividual, "配音", animationIndividual);
					}
				}
			}
			++index;
		}
		return true;
	}

}

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

public class MovieConstructionServiceImpl implements ConstructionServiceI {

	private ConstructionDAOI constructionDAO = new ConstructionDAOImpl();
	
	@Override
	public boolean construction(BaikePage baikePage) throws Exception {
		// 词条标题（实体名）
		String individualName = baikePage.getLemmaTitle();
		String polysemantExplain = baikePage.getPolysemantExplain();
		String url = baikePage.getUrl();
		Individual movieIndividual = null;
		
		// 查询词典中是否有该实体 有则查询返回 没有则创建返回  true表示这是本名
		movieIndividual = this.queryIndividual(individualName, polysemantExplain, url, true, OntologyClassEnum.MOVIE);
		constructionDAO.addObjectProperty(movieIndividual, "是", movieIndividual);
		
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
			PictureDownloader.picDownload(picSrc, newPicName, ConstructionBaseDAOImpl.PIC_SAVE_PATH + "//" + OntologyClassEnum.MOVIE.getName() );
			constructionDAO.addDataProperty(movieIndividual, "picSrc", newPicName);
		}
		
		constructionDAO.addDataProperty(movieIndividual, "URL信息来源", url);
		constructionDAO.addDataProperty(movieIndividual, "描述", lemmaSummary);
		constructionDAO.addDataProperty(movieIndividual, "歧义说明", polysemantExplain);
		
		// 添加基本信息
		List<String> parameterNamesFilter = baikePage.getParameterNames();
		List<String> parameterValuesFilter = baikePage.getParameterValues();
		constructionDAO.addDataProperties(movieIndividual, parameterNamesFilter, parameterValuesFilter);
		
		// 处理电影-公司对象
		this.dealCompanys(movieIndividual, baikePage);
		
		// 处理电影-公司对象（发行公司）
		//this.dealCompanys(movieIndividual, baikePage);
		
		// 处理电影-人物对象（导演）
		this.dealCharacters(movieIndividual, baikePage);
		
		// 处理电影-人物对象（主演）
		//this.dealCharacters(movieIndividual, baikePage);
		
		// 处理电影-人物对象（编剧）
		//this.dealCharacters(movieIndividual, baikePage);
		
		// 处理电影-人物对象（制片人）
		//this.dealCharacters(movieIndividual, baikePage);
		
		// 处理类型对象（类型-二级类）
		this.dealMoiveType(movieIndividual, baikePage);
		
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
	
	public boolean dealCompanys(Individual movieIndividual, BaikePage baikePage) {
		List<String> companys = new ArrayList<String>();
		int index = 0;
		for(String parameterName : baikePage.getParameterNames()) {
			if(parameterName.equals("出品公司")) {
				companys = StringFilter.parameterValueSeparates(baikePage.getParameterValues().get(index));
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
						constructionDAO.addObjectProperty(movieIndividual, "出品公司", companyIndividual);
						constructionDAO.addObjectProperty(companyIndividual, "出品", movieIndividual);
					}
				}
			}
			if(parameterName.equals("发行公司")) {
				companys = StringFilter.parameterValueSeparates(baikePage.getParameterValues().get(index));
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
						constructionDAO.addObjectProperty(movieIndividual, "发行公司", companyIndividual);
						constructionDAO.addObjectProperty(companyIndividual, "发行", movieIndividual);
					}
				}
			}
			++index;
		}
		return true;
	}
	
	public boolean dealCharacters(Individual movieIndividual, BaikePage baikePage) {
		List<String> characters = new ArrayList<String>();
		int index = 0;
		for(String parameterName : baikePage.getParameterNames()) {
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
						constructionDAO.addObjectProperty(characterIndividual, "有电影", movieIndividual);
						constructionDAO.addObjectProperty(characterIndividual, "导演", movieIndividual);
						constructionDAO.addObjectProperty(characterIndividual, "拍", movieIndividual);
						constructionDAO.addObjectProperty(characterIndividual, "执导", movieIndividual);
						constructionDAO.addObjectProperty(movieIndividual, "有导演", characterIndividual);
					}
				}
			}
			if(parameterName.equals("主演")) {
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
						constructionDAO.addObjectProperty(characterIndividual, "有电影", movieIndividual);
						constructionDAO.addObjectProperty(characterIndividual, "主演", movieIndividual);
						constructionDAO.addObjectProperty(characterIndividual, "出演", movieIndividual);
						constructionDAO.addObjectProperty(characterIndividual, "拍", movieIndividual);
						constructionDAO.addObjectProperty(characterIndividual, "参演", movieIndividual);
						constructionDAO.addObjectProperty(movieIndividual, "有主演", characterIndividual);
						constructionDAO.addObjectProperty(movieIndividual, "有演员", characterIndividual);
					}
				}
			}
			if(parameterName.equals("编剧")) {
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
						constructionDAO.addObjectProperty(characterIndividual, "编写", movieIndividual);
						constructionDAO.addObjectProperty(characterIndividual, "编剧", movieIndividual);
						constructionDAO.addObjectProperty(movieIndividual, "有编剧", characterIndividual);
					}
				}
			}
			if(parameterName.equals("制片人")) {
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
						constructionDAO.addObjectProperty(characterIndividual, "制作", movieIndividual);
						constructionDAO.addObjectProperty(movieIndividual, "有制片", characterIndividual);
						constructionDAO.addObjectProperty(movieIndividual, "有制片人", characterIndividual);
					}
				}
			}
			
			++index;
		}
		return true;
	}
	
	public boolean dealMoiveType(Individual movieIndividual, BaikePage baikePage) {
		int index = 0;
		List<String> movieTypeList = new ArrayList<String>();
		for(String parameterName : baikePage.getParameterNames()) {
			if(parameterName.equals("类型")) {
				movieTypeList = StringFilter.parameterValueSeparates(baikePage.getParameterValues().get(index));
				for(String movieType : movieTypeList) {
					OntClass movieTypeClass = constructionDAO.getOntClass(movieType);
					// 如果本体中此时没有该二级类
					if(movieTypeClass == null) {
						movieTypeClass = constructionDAO.createOntClass(movieType);
					}
					// 获取一级类 电影类
					OntClass movieClass = constructionDAO.getOntClass(OntologyClassEnum.MOVIE.getName());
					// 创建子类（二级类）
					constructionDAO.addSubClass(movieClass, movieTypeClass);
					constructionDAO.addSubClass(movieIndividual, movieTypeClass);
				}
			}
			++index;
		}
		return true;
	}

}

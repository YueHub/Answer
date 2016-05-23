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

/**
 * 拟人本体构建
 * @author NarutoKu
 *
 */
public class CharacterConstructionServiceImpl implements ConstructionServiceI {

	private ConstructionDAOI constructionDAO = new ConstructionDAOImpl();

	@Override
	public boolean construction(BaikePage baikePage) throws Exception {
		// 词条标题（实体名）
		String individualName = baikePage.getLemmaTitle();
		String polysemantExplain = baikePage.getPolysemantExplain();
		String url = baikePage.getUrl();
		Individual characterIndividual = null;
		
		// 查询词典中是否有该实体 有则查询返回 没有则创建返回  true表示这是本名
		characterIndividual = this.queryIndividual(individualName, polysemantExplain, url, true, OntologyClassEnum.CHARACTER);
		constructionDAO.addObjectProperty(characterIndividual, "是", characterIndividual);
		
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
			PictureDownloader.picDownload(picSrc, newPicName, ConstructionBaseDAOImpl.PIC_SAVE_PATH + "//" + OntologyClassEnum.CHARACTER.getName() );
			constructionDAO.addDataProperty(characterIndividual, "picSrc", newPicName);
		}
		constructionDAO.addDataProperty(characterIndividual, "URL信息来源", url);
		constructionDAO.addDataProperty(characterIndividual, "描述", lemmaSummary);
		constructionDAO.addDataProperty(characterIndividual, "歧义说明", polysemantExplain);
		
		// 添加人物关系
		/*List<String> relationNames = baikePage.getRelationNames();
		List<String> relationValues = baikePage.getParameterValues();
		List<String> relationUrls = baikePage.getRelationUrls();*/
		if(baikePage.getRelationNames().size() != 0 && baikePage.getRelationValues().size() !=0) {
			this.dealCharacterRelations(characterIndividual, baikePage);
		}
		
		// 添加基本信息
		List<String> parameterNamesFilter = baikePage.getParameterNames();
		List<String> parameterValuesFilter = baikePage.getParameterValues();
		constructionDAO.addDataProperties(characterIndividual, parameterNamesFilter, parameterValuesFilter);
		
		// 处理别名
		this.dealAliases(characterIndividual, baikePage);
		
		// 处理职业
		this.dealProfession(characterIndividual, baikePage);
		
		// 处理人物-地区对象（国籍对象属性） TODO 超链接情况
		this.dealNationality(characterIndividual, baikePage);
		
		// 处理人物-院校对象（毕业院校对象属性）
		this.dealGraduateSchools(characterIndividual, baikePage);
		
		// 处理人物-公司对象（毕业院校公司属性）
		this.dealCompanys(characterIndividual, baikePage);
		
		return false;
	}
	
	/**
	 * 查询实体是否在词典中存在
	 * @param dictIndividualList
	 * @param individualName
	 * @param polysemantExplain
	 * @param url
	 * @param isAliases
	 * @return
	 */
	public Individual queryIndividual(String individualName, String polysemantExplain, String url, boolean isAliases, OntologyClassEnum parentClass) {
		// 以生省内存的方式读取Answer_Dict词典
		LinkedList<String> dictIndividualList = IOUtil.readLineListWithLessMemory(INDIVIDUAL_UUID_DICT_FILE);
		Individual characterIndividual = null;
		// 遍历词典中的实体记录 判断当前实体是否已经存在
		Long rowNum = 0l;
		for(String row : dictIndividualList) {
			rowNum++;
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
				characterIndividual = constructionDAO.getIndividual(dictIndividualUUID);
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
				characterIndividual = constructionDAO.getIndividual(dictIndividualUUID);
			}
		}
		
		
		// 如果词典中不存在该实体，则插入词典并且创建一个实体
		if(characterIndividual == null) {
			String characterIndividualUUID = UUID.randomUUID().toString().replace("-", "");
			String isAliasesWrite = null;
			if(isAliases == true) {
				isAliasesWrite = "1";
			} else {
				isAliasesWrite = "0";
			}
			if(polysemantExplain == null) {
				polysemantExplain = "无";
			}
			String row_add_individual = characterIndividualUUID + "_" + individualName + "_" + polysemantExplain + "_" + url.split("#")[0] + "_" + isAliasesWrite + "_" + parentClass.getIndex();
			FileIOUtil.appendContent(INDIVIDUAL_UUID_DICT_FILE, row_add_individual);
			OntClass characterClass = constructionDAO.getOntClass(parentClass.getName());		// 获取拟人类型
			characterIndividual = constructionDAO.createIndividual(characterIndividualUUID, characterClass);
		}
		
		constructionDAO.addComment(characterIndividual, individualName); // 创建comment
		return characterIndividual;
	}
	
	/**
	 * 处理人物关系
	 * @param characterIndividual
	 * @param dictIndividualList
	 * @param baikePage
	 * @return
	 */
	public boolean dealCharacterRelations(Individual characterIndividual, BaikePage baikePage) {
		int index = 0;
		List<Individual> relationCharacterIndividuals = new ArrayList<Individual>();
		for(String relationValue : baikePage.getRelationValues()) {
			// 歧义理解暂时为空
			String polysemantExplain = "待更新";
			String relationUrl = baikePage.getRelationUrls().get(index);
			if(relationUrl != null) {
				// true 表示此为本名
				Individual relationCharacterIndividual = this.queryIndividual(relationValue, polysemantExplain, relationUrl, true, OntologyClassEnum.CHARACTER);
				relationCharacterIndividuals.add(relationCharacterIndividual);
			}
			++index;
		}
		// 将人物实体关系添加到本体库中
		constructionDAO.addObjectProperties(characterIndividual, baikePage.getRelationNames(), relationCharacterIndividuals);
		List<String> newRelationNames = new ArrayList<String>();
		for(String relationName : baikePage.getRelationNames()) {
			newRelationNames.add("有" + relationName);
		}
		constructionDAO.addObjectProperties(characterIndividual, newRelationNames, relationCharacterIndividuals);
		return true;
	}
	
	/**
	 * 处理别名（人物实体别名）
	 * @param characterIndividual
	 * @param dictIndividualList
	 * @param individualName
	 * @param polysemantExplain
	 * @param url
	 * @return
	 */
	public boolean dealAliases(Individual characterIndividual, BaikePage baikePage) {
		// 获取实体别名
		List<String> aliasList = new ArrayList<String>();
		int index = 0;
		for(String parameterNameFilter : baikePage.getParameterNames()) {
			if(parameterNameFilter.equals("别名")) {
				aliasList = StringFilter.parameterValueSeparates(baikePage.getParameterValues().get(index));
				for(String alias : aliasList) {
					// false表示不是本名 而是别名（即等价类）
					// 因为别名作为该百科页面实体的等价类 ，所以URL应该相同
					Individual aliasIndividual = this.queryIndividual(alias, baikePage.getLemmaTitle() + "的别名", baikePage.getUrl(), false, OntologyClassEnum.CHARACTER);
					// 添加等价类
					constructionDAO.addSameAs(characterIndividual, aliasIndividual);
				}
			}
			++index;
		}
		return true;
	}
	
	/**
	 * 处理职业
	 * @param baikePage
	 * @return
	 */
	public boolean dealProfession(Individual characterIndividual, BaikePage baikePage) {
		int index = 0;
		List<String> professionList = new ArrayList<String>();
		for(String parameterName : baikePage.getParameterNames()) {
			if(parameterName.equals("职业")) {
				professionList = StringFilter.parameterValueSeparates(baikePage.getParameterValues().get(index));
				for(String profession : professionList) {
					OntClass professionClass = constructionDAO.getOntClass(profession);
					// 如果本体中此时没有该二级类
					if(professionClass == null) {
						professionClass = constructionDAO.createOntClass(profession);
					}
					// 获取一级类 人物类
					OntClass characterClass = constructionDAO.getOntClass(OntologyClassEnum.CHARACTER.getName());
					// 创建子类（二级类）
					constructionDAO.addSubClass(characterClass, professionClass);
					constructionDAO.addSubClass(characterIndividual, professionClass);
				}
			}
			++index;
		}
		return true;
	}
	
	/**
	 * TODO 处理国籍不应该直接判断为地区类处理国籍属性
	 * @param characterIndividual
	 * @param baikePage
	 * @return
	 */
	public boolean dealNationality(Individual characterIndividual, BaikePage baikePage) {
		List<String> nationalities = new ArrayList<String>();
		int index = 0;
		for(String parameterName : baikePage.getParameterNames()) {
			if(parameterName.equals("国籍")) {
				nationalities = StringFilter.parameterValueSeparates(baikePage.getParameterValues().get(index));
				for(String nationality : nationalities) {
					String url = null;
					int i = 0;
					for(String parameterHasUrlValue : baikePage.getParameterHasUrlValues()) {
						if(nationality.equals(parameterHasUrlValue)) {
							url = baikePage.getParameterHasUrl().get(i);
						}
						++i;
					}
					if(url != null) {
						String polysemantExplain = "待更新";
						Individual nationalityIndividual = this.queryIndividual(nationality, polysemantExplain, url, true, OntologyClassEnum.AREA);
						constructionDAO.addObjectProperty(nationalityIndividual, "公民", characterIndividual);
					}
				}
				
			}
			++index;
		}
		return false;
	}
	
	/**
	 * TODO 处理毕业院校 
	 * @param characterIndividual
	 * @param dictIndividualList
	 * @param baikePage
	 * @return
	 */
	public boolean dealGraduateSchools(Individual characterIndividual, BaikePage baikePage) {
		List<String> graduateSchools = new ArrayList<String>();
		int index = 0;
		for(String parameterName : baikePage.getParameterNames()) {
			if(parameterName.equals("毕业院校")) {
				graduateSchools = StringFilter.parameterValueSeparates(baikePage.getParameterValues().get(index));
				for(String graduateSchool : graduateSchools) {
					String url = null;
					int i = 0;
					for(String parameterHasUrlValue : baikePage.getParameterHasUrlValues()) {
						if(graduateSchool.equals(parameterHasUrlValue)) {
							url = baikePage.getParameterHasUrl().get(i);
						}
						++i;
					}
					if(url != null) {
						String polysemantExplain = "待更新";
						Individual academyIndividual = this.queryIndividual(graduateSchool, polysemantExplain, url, true, OntologyClassEnum.ACADEMY);
						constructionDAO.addObjectProperty(academyIndividual, "学生", characterIndividual);
						constructionDAO.addObjectProperty(characterIndividual, "毕业于", academyIndividual);
					}
				}
			}
			++index;
		}
		return false;
	}
	
	/**
	 * 处理经纪公司
	 * @param characterIndividual
	 * @param dictIndividualList
	 * @param baikePage
	 * @return
	 */
	public boolean dealCompanys(Individual characterIndividual, BaikePage baikePage) {
		List<String> companys = new ArrayList<String>();
		int index = 0;
		for(String parameterName : baikePage.getParameterNames()) {
			if(parameterName.equals("经纪公司")) {
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
						constructionDAO.addObjectProperty(characterIndividual, "经纪公司", companyIndividual);
					}
				}
			}
			++index;
		}
		return true;
	}
}
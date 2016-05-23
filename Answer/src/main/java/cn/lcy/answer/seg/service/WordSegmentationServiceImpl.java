package cn.lcy.answer.seg.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import cn.lcy.answer.enums.OntologyClassEnum;
import cn.lcy.answer.ontology.construction.service.ConstructionServiceI;
import cn.lcy.answer.sem.model.PolysemantNamedEntity;
import cn.lcy.answer.sem.model.Word;
import cn.lcy.answer.sem.model.WordSegmentResult;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.common.Term;

@Service("wordSegmentationService")
public class WordSegmentationServiceImpl implements WordSegmentationServiceI {
	
	// 以生省内存的方式读取Answer_Dict词典
	LinkedList<String> dictIndividualList = IOUtil.readLineListWithLessMemory(ConstructionServiceI.INDIVIDUAL_UUID_DICT_FILE);
	
	/**
	 * HanLP分词以及命名实体识别
	 */
	@Override
	public WordSegmentResult wordSegmentation(String question) {
		// 命名实体
		List<PolysemantNamedEntity> polysemantNamedEntities = new ArrayList<PolysemantNamedEntity>();
		// 本次分词主要为命名实体识别
		List<Term> terms = HanLP.segment(question);
		
		for(String dictRow : dictIndividualList) {
			String[] fieldsDict = dictRow.split("_");
			String dictIndividualUUID = fieldsDict[0];	// UUID
			String dictIndividualName = fieldsDict[1];	// 实体名 
			String dictPolysemantExplain = fieldsDict[2];	// 歧义说明
			String dictIndividualURL = fieldsDict[3]; // 实体百科页面URL
			String dictIsAliasesWrite = fieldsDict[4]; // 是否是本名
			int dictIndividualClass = Integer.parseInt(fieldsDict[5]);	// 实体所属类型
			int id = 1;
			for(Term term : terms) {
				if(term.word.equals(dictIndividualName)) {
					PolysemantNamedEntity polysemantNamedEntitiy = new PolysemantNamedEntity();
					polysemantNamedEntitiy.setUUID(dictIndividualUUID);
					polysemantNamedEntitiy.setEntityName(dictIndividualName);
					polysemantNamedEntitiy.setPolysemantExplain(dictPolysemantExplain);
					polysemantNamedEntitiy.setEntityUrl(dictIndividualURL);
					polysemantNamedEntitiy.setIsAliases(dictIsAliasesWrite);
					// 默认均为未激活状态
					polysemantNamedEntitiy.setActive(false);
					if(dictIndividualClass == OntologyClassEnum.CHARACTER.getIndex()) {
						polysemantNamedEntitiy.setOntClass(OntologyClassEnum.CHARACTER.getName());
					} else if (dictIndividualClass == OntologyClassEnum.MOVIE.getIndex()) {
						polysemantNamedEntitiy.setOntClass(OntologyClassEnum.MOVIE.getName());
					} else if(dictIndividualClass == OntologyClassEnum.MUSIC.getIndex()) {
						polysemantNamedEntitiy.setOntClass(OntologyClassEnum.MUSIC.getName());
					} else if(dictIndividualClass == OntologyClassEnum.ANIMATION.getIndex()) {
						polysemantNamedEntitiy.setOntClass(OntologyClassEnum.ANIMATION.getName());
					} else if(dictIndividualClass == OntologyClassEnum.CARICATURE.getIndex()) {
						polysemantNamedEntitiy.setOntClass(OntologyClassEnum.CARICATURE.getName());
					} else if(dictIndividualClass == OntologyClassEnum.AREA.getIndex()) {
						polysemantNamedEntitiy.setOntClass(OntologyClassEnum.AREA.getName());
					} else if(dictIndividualClass == OntologyClassEnum.ACADEMY.getIndex()) {
						polysemantNamedEntitiy.setOntClass(OntologyClassEnum.ACADEMY.getName());
					} else if(dictIndividualClass == OntologyClassEnum.COMPANY.getIndex()) {
						polysemantNamedEntitiy.setOntClass(OntologyClassEnum.COMPANY.getName());
					} else if(dictIndividualClass == OntologyClassEnum.OTHERS.getIndex()) {
						polysemantNamedEntitiy.setOntClass(OntologyClassEnum.OTHERS.getName());
					}
					polysemantNamedEntitiy.setPosition(id);
					polysemantNamedEntities.add(polysemantNamedEntitiy);
					// 覆盖模式插入
					CustomDictionary.insert(dictIndividualName, "n 1024");
				}
				++id;
			}
		}
		
		// 加载用户词典后的分词
		List<Term> termList = HanLP.segment(question);
		
		int index = 1;
		List<Word> words = new ArrayList<Word>();
		for(Term term : terms) {
			Word word = new Word();
			word.setPosition(index);
			word.setName(term.word);
			word.setCpostag(term.nature.toString());
			word.setPostag(term.nature.toString());
			List<PolysemantNamedEntity> wordPolysemantNamedEntities = new ArrayList<PolysemantNamedEntity>();
			for(PolysemantNamedEntity polysemantNamedEntity : polysemantNamedEntities) {
				if(polysemantNamedEntity.getPosition() == index) {
					wordPolysemantNamedEntities.add(polysemantNamedEntity);
				}
			}
			word.setPolysemantNamedEntities(wordPolysemantNamedEntities);
			words.add(word);
			++index;
		}
		// 分词结果
		WordSegmentResult wordSegmentResult = new WordSegmentResult();
		wordSegmentResult.setTerms(termList);
		wordSegmentResult.setPolysemantEntities(polysemantNamedEntities);
		wordSegmentResult.setWords(words);
		return wordSegmentResult;
	}
}

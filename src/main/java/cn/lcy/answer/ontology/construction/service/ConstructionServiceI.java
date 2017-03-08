package cn.lcy.answer.ontology.construction.service;

import cn.lcy.answer.ontology.model.BaikePage;

public interface ConstructionServiceI {

	// G:/GraduationProject/HanLP/DataAndModel/data/dictionary/custom/Answer_Dict.txt
	public static final String INDIVIDUAL_UUID_DICT_FILE = "D:/AnswerDict/Answer_Dict.txt";
	
	public boolean construction(BaikePage baikePage) throws Exception;
	
}

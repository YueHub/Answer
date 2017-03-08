package cn.lcy.answer.dictionary.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.lcy.answer.dictionary.dao.DictionaryDAOI;
import cn.lcy.answer.dictionary.dao.DictionaryDAOImpl;
import cn.lcy.answer.enums.OntologyClassEnum;
import cn.lcy.answer.io.util.FileIOUtil;
import cn.lcy.answer.ontology.base.dao.BaseDAOImpl;

public class IndividualsDictUtil extends BaseDAOImpl {
	
	@SuppressWarnings( value = "unused" )
    private static final Logger log = LoggerFactory.getLogger( IndividualsDictUtil.class );
	
    public static String ontologySource = SOURCE + "GraduationProject_MO.owl";
    
    public static final String CUSTOMDICTDIR = "I:/IT/GraduationProject/HanLP/DataAndModel/data/dictionary/custom/";
    
    public static final Model loadModel = FileManager.get().readModel(model, ontologySource);
    
    /**
     * 生成用户词典的主程序
     * @param args
     */
    public static void main(String[] args) {
    	
    	DictionaryDAOI jenaUtil = new DictionaryDAOImpl();
    	// 人物集合
    	List<String> people = new ArrayList<String>();
    	// 电影集合
    	List<String> movies = new ArrayList<String>();
    	int size = 0;
    	for(Iterator<Individual> iter = model.listIndividuals(); iter.hasNext();) {
    		// 实体
    		Individual individual = iter.next();
    		// 实体的最终最高级父类
    		OntClass superClass = individual.getOntClass();
    		if(superClass != null) {
    			while(superClass.getSuperClass() != null) {
        			superClass = superClass.getSuperClass();
        		}
    		}
    		if(superClass.getLocalName().equals("人")) {
    			String name = individual.getLocalName();
    			String att = " nr 100";
    			people.add(name + att);
    			List<String> sameIndividuals = jenaUtil.querySameIndividuals(individual.getLocalName());
        		for(String sameIndividual : sameIndividuals) {
        			people.add(sameIndividual + att);
        			++size;
        		}
    		} else if (superClass.getLocalName().equals("电影")) {
    			String name = individual.getLocalName();
    			String att = " n 100";
    			movies.add(name + att);
    			List<String> sameIndividuals = jenaUtil.querySameIndividuals(individual.getLocalName());
        		for(String sameIndividual : sameIndividuals) {
        			movies.add(sameIndividual + att);
        			++size;
        		}
    		}
    		System.out.println("实体:"+individual.getLocalName()+" 所属类:" + superClass.getLocalName());
    		++size;
    	}
    	System.out.println("实体的个数:"+size);
    	
    	// 将人物添加到用户词典中
    	IndividualsDictUtil.createIndividualDict(OntologyClassEnum.CHARACTER, people);
    	// 建电影添加到用户词典中
    	IndividualsDictUtil.createIndividualDict(OntologyClassEnum.MOVIE, movies);
    	
    }
    
    /**
     * 创建用户词典
     * @return
     */
    public static boolean createIndividualDict(OntologyClassEnum ontologyClass, List<String> contents) {
    	// 如果是属于人的话 则添加到人物词典中
    	if(ontologyClass == OntologyClassEnum.CHARACTER) {
    		String fileName = "人物词典.txt";
    		String filePath = CUSTOMDICTDIR + fileName;
    		return FileIOUtil.appendContent(filePath, contents);
    	}
    	if(ontologyClass == OntologyClassEnum.MOVIE) {
    		String fileName = "电影词典.txt";
    		String filePath = CUSTOMDICTDIR + fileName;
    		return FileIOUtil.appendContent(filePath, contents);
    	} 
    	return true;
    }
}
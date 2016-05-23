package cn.lcy.answer.ontology.construction.util;

import java.util.List;

import cn.lcy.answer.enums.OntologyClassEnum;
import cn.lcy.answer.ontology.construction.service.AcademyConstructionServiceImpl;
import cn.lcy.answer.ontology.construction.service.AnimationConstructionServiceImpl;
import cn.lcy.answer.ontology.construction.service.AreaConstructionServiceImpl;
import cn.lcy.answer.ontology.construction.service.CaricatureConstructionServiceImpl;
import cn.lcy.answer.ontology.construction.service.CharacterConstructionServiceImpl;
import cn.lcy.answer.ontology.construction.service.CompanyConstructionServiceImpl;
import cn.lcy.answer.ontology.construction.service.ConstructionServiceI;
import cn.lcy.answer.ontology.construction.service.MovieConstructionServiceImpl;
import cn.lcy.answer.ontology.construction.service.MusicConstructionServiceImpl;
import cn.lcy.answer.ontology.construction.service.OthersConstructionServiceImpl;

public class LemmaClassify {


	/**
	 * 对词条进行分类 返回为类型枚举
	 * TODO 对关键属性的是否应该使用枚举
	 * @return
	 */
	public static OntologyClassEnum classify(List<String> parameterNamesFilter) {
		// 如果该页面没有实体属性 则将该页面分类到其他类别中
		if(parameterNamesFilter == null) {
			return OntologyClassEnum.OTHERS;
		}
		for(String parameterName : parameterNamesFilter) {
			if(parameterName.equals("出生地点") || parameterName.equals("出生日期") || parameterName.equals("性别") || parameterName.equals("出生地") || parameterName.equals("职业") || parameterName.equals("国籍")) {
				return OntologyClassEnum.CHARACTER;
			}
			if(parameterName.equals("票房") || parameterName.equals("片长") || parameterName.equals("上映时间")) {
				return OntologyClassEnum.MOVIE;
			}
			if(parameterName.equals("谱曲") || parameterName.equals("填词") || parameterName.equals("谱曲") || parameterName.equals("歌曲时长")) {
				return OntologyClassEnum.MUSIC;
			}
			if(parameterName.equals("动画制作")) {
				return OntologyClassEnum.ANIMATION;
			}
			if(parameterName.equals("出版期间")) {
				return OntologyClassEnum.CARICATURE;
			}
			if(parameterName.equals("首都") || parameterName.equals("国歌") || parameterName.equals("国旗") || parameterName.equals("地区生产总值") || parameterName.equals("人均生产总值")) {
				return OntologyClassEnum.AREA;
			}
			if(parameterName.equals("学校类型") || parameterName.equals("学校代码") || parameterName.equals("学校地址") || parameterName.equals("现任校长") || parameterName.equals("知名校友")) {
				return OntologyClassEnum.ACADEMY;
			}
			if(parameterName.equals("公司名称") || parameterName.equals("公司性质") || parameterName.equals("公司使命") || parameterName.equals("公司口号") || parameterName.equals("公司地址")) {
				return OntologyClassEnum.COMPANY;
			}
		}
		return OntologyClassEnum.OTHERS;
	}
	
	/**
	 * 对词条进行分类 返回为类型枚举
	 * @return
	 */
	public static ConstructionServiceI classify(OntologyClassEnum ontologyClassEnum) {
		switch(ontologyClassEnum) {
			case CHARACTER : 
				return new CharacterConstructionServiceImpl();
			case MOVIE : 
				return new MovieConstructionServiceImpl();
			case MUSIC : 
				return new MusicConstructionServiceImpl();
			case ANIMATION : 
				return new AnimationConstructionServiceImpl();
			case CARICATURE : 
				return new CaricatureConstructionServiceImpl();
			case AREA : 
				return new AreaConstructionServiceImpl();
			case ACADEMY : 
				return new AcademyConstructionServiceImpl();
			case COMPANY : 
				return new CompanyConstructionServiceImpl();
			default : 
				return new OthersConstructionServiceImpl();
		}
	}
}
